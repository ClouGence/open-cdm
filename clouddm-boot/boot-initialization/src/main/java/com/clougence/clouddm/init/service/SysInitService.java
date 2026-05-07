package com.clougence.clouddm.init.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.init.InitTaskApplication;
import com.clougence.clouddm.init.component.fixtasks.DmFixDmDsConfig;
import com.clougence.clouddm.init.component.fixtasks.DmFixSecRules;
import com.clougence.clouddm.init.component.fixtasks.InitConsolePluginLoader;
import com.clougence.clouddm.init.component.fixtasks.RdpFixUserRole;
import com.clougence.clouddm.init.component.flyway.DmFlywayInit;
import com.clougence.clouddm.init.component.log.InstallUpgradeLogBus;
import com.clougence.clouddm.init.constant.I18nInitFieldKeys;
import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.clouddm.init.model.InitFieldDef;
import com.clougence.clouddm.init.model.TestDbResult;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统初始化核心服务。
 * - 合并 init-fields.json schema + classpath 运行时配置值
 * - DB 连接自检判断系统状态
 * - 应用配置（写入 classpath properties / 执行 Flyway 迁移）
 */
@Slf4j
@Service
public class SysInitService {

    private static final String INIT_DB_CREATE_IF_MISSING    = "clougence.init.db.createIfMissing";
    private static final String INIT_DB_REBUILD_IF_NOT_EMPTY = "clougence.init.db.rebuildIfNotEmpty";
    private static final String REQUIRED_DB_CHARSET          = "utf8mb4";
    private static final String REQUIRED_DB_COLLATION        = "utf8mb4_general_ci";
    private static final String SCHEMA_EXISTS_SQL            = "SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name = ?";
    private static final String SCHEMA_CHARSET_SQL           = "SELECT default_character_set_name FROM information_schema.schemata WHERE schema_name = ?";
    private static final String TABLE_COUNT_SQL              = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ?";

    @Resource
    private SysInitDefService   defService;
    private static final String ALONE_CONFIG                 = "alone.properties";
    private static final String CONSOLE_CONFIG               = "console.properties";

    // 数据库测试
    // ========================================================================

    /**
     * 测试数据库连接（使用用户提交的临时参数）。
     */
    public TestDbResult testDbConnection(String jdbcUrl, String username, String password, String rebuildIfNotEmpty, String confirmDatabaseName) {
        TestDbResult result = new TestDbResult();
        try {
            DatabaseInspection inspection = inspectDatabase(jdbcUrl, username, password, true);
            applyInspectionResult(result, inspection, rebuildIfNotEmpty, confirmDatabaseName);
        } catch (Exception e) {
            result.setInstalled(false);
            result.setEmpty(false);
            result.setSuccess(false);
            result.setCanProceed(false);
            result.setDatabaseExists(false);
            result.setCharsetValid(false);
            result.setCreateDatabase(false);
            result.setMessageType("error");
            result.setMessage(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_CONNECTION_FAILED.name(), e.getMessage()));
        }
        return result;
    }

    public List<String> previewExecutionScripts(Map<String, String> userConfig) {
        String jdbcUrl = userConfig == null ? null : userConfig.get("spring.datasource.jdbcurl");
        String username = userConfig == null ? null : userConfig.get("spring.datasource.username");
        String password = userConfig == null ? null : userConfig.get("spring.datasource.password");

        if (StringUtils.isBlank(jdbcUrl) || StringUtils.isBlank(username)) {
            return DmFlywayInit.listAllScriptNames();
        }

        try {
            String databaseName = InitDBStatusDetector.getDatabaseName(jdbcUrl);
            if (StringUtils.isBlank(databaseName)) {
                return DmFlywayInit.listAllScriptNames();
            }
            return DmFlywayInit.listUpgradeRequiredScriptNames(jdbcUrl, username, password, databaseName);
        } catch (Exception e) {
            log.warn("[SysInitService] Preview execution scripts failed, fallback to full list. msg={}", e.getMessage());
            return DmFlywayInit.listAllScriptNames();
        }
    }

    // ========================================================================
    // 配置读写
    // ========================================================================

    /**  应用初始化配置（完整模式：写配置 + Flyway 迁移 + 更新管理员） */
    public void applyInitConfig(Map<String, String> userConfig) throws Exception {
        String jdbcUrl = userConfig.get("spring.datasource.jdbcurl");
        InstallUpgradeLogBus.start("install", jdbcUrl);
        try {
            log.info("[SysInitService] Applying initialization config, createIfMissing={}, rebuildIfNotEmpty={}, adminEmail={}", userConfig
                .getOrDefault(INIT_DB_CREATE_IF_MISSING, "false"), userConfig
                    .getOrDefault(INIT_DB_REBUILD_IF_NOT_EMPTY, "false"), userConfig.get(InitSeedConstants.RUNTIME_ADMIN_EMAIL_KEY));
            InstallUpgradeLogBus.info("Applying initialization configuration.");

            replaceConfigLines(userConfig);

            Properties props = this.defService.loadSystemProperties();
            jdbcUrl = userConfig.getOrDefault("spring.datasource.jdbcurl", props.getProperty("spring.datasource.jdbcurl"));
            String dbUser = userConfig.getOrDefault("spring.datasource.username", props.getProperty("spring.datasource.username"));
            String dbPass = userConfig.getOrDefault("spring.datasource.password", props.getProperty("spring.datasource.password"));
            String adminEmail = userConfig.get(InitSeedConstants.RUNTIME_ADMIN_EMAIL_KEY);
            String adminPassword = userConfig.get(InitSeedConstants.RUNTIME_ADMIN_PASSWORD_KEY);
            boolean createIfMissing = Boolean.parseBoolean(userConfig.getOrDefault(INIT_DB_CREATE_IF_MISSING, "false"));
            boolean rebuildIfNotEmpty = Boolean.parseBoolean(userConfig.getOrDefault(INIT_DB_REBUILD_IF_NOT_EMPTY, "false"));

            if (StringUtils.isNotBlank(jdbcUrl) && StringUtils.isNotBlank(dbUser)) {
                InstallUpgradeLogBus.info("Preparing database.");
                prepareDatabase(jdbcUrl, dbUser, dbPass, createIfMissing, rebuildIfNotEmpty);
            }

            if (StringUtils.isNotBlank(jdbcUrl) && StringUtils.isNotBlank(dbUser)) {
                runFlywayMigration(jdbcUrl, dbUser, dbPass, adminEmail, adminPassword);
            }

            if (StringUtils.isNotBlank(adminEmail) && StringUtils.isNotBlank(adminPassword)) {
                InstallUpgradeLogBus.info("Updating administrator account.");
                updateAdminUser(jdbcUrl, dbUser, dbPass, adminEmail, adminPassword);
            }

            if (StringUtils.isNotBlank(jdbcUrl) && StringUtils.isNotBlank(dbUser)) {
                runFixTasks(jdbcUrl, dbUser, dbPass);
            }

            InstallUpgradeLogBus.complete("Initialization completed successfully.");
            log.info("[SysInitService] Initialization apply flow completed successfully for jdbcUrl={}", jdbcUrl);
        } catch (Exception e) {
            InstallUpgradeLogBus.fail("Initialization failed.", e);
            throw toDetailedRuntimeException(e);
        }
    }

    /**
     * 仅更新数据库配置。
     * 对于新库、空库或明确要求重建的场景，同步执行初始化并写入管理员账号；
     * 对于已有非空库，仅更新连接并执行迁移/修复，不重置管理员账号。
     */
    public void updateDbConfig(Map<String, String> userConfig) throws Exception {
        InstallUpgradeLogBus.start("install", userConfig.get("spring.datasource.jdbcurl"));
        try {
            replaceConfigLines(userConfig);

            Properties props = this.defService.loadSystemProperties();
            String jdbcUrl = userConfig.getOrDefault("spring.datasource.jdbcurl", props.getProperty("spring.datasource.jdbcurl"));
            String dbUser = userConfig.getOrDefault("spring.datasource.username", props.getProperty("spring.datasource.username"));
            String dbPass = userConfig.getOrDefault("spring.datasource.password", props.getProperty("spring.datasource.password"));
            String adminEmail = userConfig.get(InitSeedConstants.RUNTIME_ADMIN_EMAIL_KEY);
            String adminPassword = userConfig.get(InitSeedConstants.RUNTIME_ADMIN_PASSWORD_KEY);
            boolean createIfMissing = Boolean.parseBoolean(userConfig.getOrDefault(INIT_DB_CREATE_IF_MISSING, "false"));
            boolean rebuildIfNotEmpty = Boolean.parseBoolean(userConfig.getOrDefault(INIT_DB_REBUILD_IF_NOT_EMPTY, "false"));

            if (StringUtils.isBlank(jdbcUrl) || StringUtils.isBlank(dbUser)) {
                InstallUpgradeLogBus.warn("Database configuration is incomplete, skip update.");
                return;
            }

            DatabaseInspection inspection = inspectDatabase(jdbcUrl, dbUser, dbPass, false);
            boolean bootstrapAdmin = !inspection.databaseExists || inspection.empty || rebuildIfNotEmpty;

            log.info("[SysInitService] Updating DB config, bootstrapAdmin={}, databaseExists={}, empty={}, rebuildIfNotEmpty={}, adminEmail={}", bootstrapAdmin, inspection.databaseExists, inspection.empty, rebuildIfNotEmpty, adminEmail);

            InstallUpgradeLogBus.info("Preparing database.");
            prepareDatabase(jdbcUrl, dbUser, dbPass, createIfMissing, rebuildIfNotEmpty);
            runFlywayMigration(jdbcUrl, dbUser, dbPass, bootstrapAdmin ? adminEmail : null, bootstrapAdmin ? adminPassword : null);
            if (bootstrapAdmin && StringUtils.isNotBlank(adminEmail) && StringUtils.isNotBlank(adminPassword)) {
                InstallUpgradeLogBus.info("Updating administrator account.");
                updateAdminUser(jdbcUrl, dbUser, dbPass, adminEmail, adminPassword);
            }
            runFixTasks(jdbcUrl, dbUser, dbPass);
            InstallUpgradeLogBus.complete("Installation flow completed successfully.");
        } catch (Exception e) {
            InstallUpgradeLogBus.fail("Installation failed.", e);
            throw toDetailedRuntimeException(e);
        }
    }

    public void upgradeSystem() throws Exception {
        Properties props = this.defService.loadSystemProperties();
        String jdbcUrl = props.getProperty("spring.datasource.jdbcurl");
        String dbUser = props.getProperty("spring.datasource.username");
        String dbPass = props.getProperty("spring.datasource.password");

        InstallUpgradeLogBus.start("upgrade", jdbcUrl);
        try {
            if (StringUtils.isBlank(jdbcUrl) || StringUtils.isBlank(dbUser)) {
                throw new IllegalStateException("Database configuration is missing.");
            }

            runUpgradeMigration(jdbcUrl, dbUser, dbPass);
            InstallUpgradeLogBus.complete("Upgrade completed successfully.");
        } catch (Exception e) {
            InstallUpgradeLogBus.fail("Upgrade failed.", e);
            throw toDetailedRuntimeException(e);
        }
    }

    // ========================================================================
    // 内部方法
    // ========================================================================

    private boolean isAloneMode() { return "embedded".equals(System.getProperty("app.mode")); }

    private void replaceConfigLines(Map<String, String> userConfig) throws Exception {
        String configName = isAloneMode() ? ALONE_CONFIG : CONSOLE_CONFIG;
        Path filePath = ensureAppHomeConfigPath(configName);

        List<String> lines;
        if (Files.exists(filePath) && Files.size(filePath) > 0) {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } else {
            String content = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(configName), StandardCharsets.UTF_8);
            lines = new ArrayList<>(java.util.Arrays.asList(content.split("\\r?\\n", -1)));
        }

        List<InitFieldDef> schema = this.defService.getFieldDefsSchema();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("!")) {
                continue;
            }
            int eqIdx = line.indexOf('=');
            if (eqIdx < 0) {
                continue;
            }
            String key = line.substring(0, eqIdx).trim();
            for (InitFieldDef def : schema) {
                if (key.equals(def.getPropertyKey()) && userConfig.containsKey(key)) {
                    String newValue = userConfig.get(key);
                    if (newValue == null) {
                        newValue = "";
                    }
                    lines.set(i, key + "=" + newValue);
                    break;
                }
            }
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    private void runFlywayMigration(String jdbcUrl, String dbUser, String dbPass, String adminEmail, String adminPassword) {
        log.info("[SysInitService] Running Flyway migration with: {}", jdbcUrl);
        String previousAdminEmail = System.getProperty(InitSeedConstants.RUNTIME_ADMIN_EMAIL_KEY);
        String previousAdminPassword = System.getProperty(InitSeedConstants.RUNTIME_ADMIN_PASSWORD_KEY);
        try {
            SpringApplication app = new SpringApplication(InitTaskApplication.class);
            app.setWebApplicationType(WebApplicationType.NONE);
            app.setLazyInitialization(true);

            Properties props = buildTaskProperties(jdbcUrl, dbUser, dbPass);
            app.setDefaultProperties(props);

            setRuntimeAdminProperty(InitSeedConstants.RUNTIME_ADMIN_EMAIL_KEY, adminEmail);
            setRuntimeAdminProperty(InitSeedConstants.RUNTIME_ADMIN_PASSWORD_KEY, adminPassword);
            InstallUpgradeLogBus.info("Starting Flyway migration task.");

            try (ConfigurableApplicationContext ctx = app.run()) {
                ctx.getBean(DmFlywayInit.class).doUpgrade();
                log.info("[SysInitService] Flyway migration done.");
            }
        } catch (Exception e) {
            log.error("[SysInitService] Flyway migration failed", e);
            throw new RuntimeException("Flyway migration failed: " + e.getMessage(), e);
        } finally {
            restoreRuntimeAdminProperty(InitSeedConstants.RUNTIME_ADMIN_EMAIL_KEY, previousAdminEmail);
            restoreRuntimeAdminProperty(InitSeedConstants.RUNTIME_ADMIN_PASSWORD_KEY, previousAdminPassword);
        }
    }

    private void runUpgradeMigration(String jdbcUrl, String dbUser, String dbPass) {
        log.info("[SysInitService] Running upgrade migration with: {}", jdbcUrl);
        try {
            SpringApplication app = new SpringApplication(InitTaskApplication.class);
            app.setWebApplicationType(WebApplicationType.NONE);
            app.setLazyInitialization(true);
            Properties props = buildTaskProperties(jdbcUrl, dbUser, dbPass);
            app.setDefaultProperties(props);
            InstallUpgradeLogBus.info("Starting upgrade migration task.");

            try (ConfigurableApplicationContext ctx = app.run()) {
                ctx.getBean(DmFlywayInit.class).doUpgradeAndValidate();
                log.info("[SysInitService] Upgrade migration done.");
            }
        } catch (Exception e) {
            log.error("[SysInitService] Upgrade migration failed", e);
            throw new RuntimeException(buildDetailedErrorMessage(e), e);
        }
    }

    public String buildDetailedErrorMessage(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
        }
        return stringWriter.toString();
    }

    private void updateAdminUser(String jdbcUrl, String dbUser, String dbPass, String adminEmail, String adminPassword) throws SQLException {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
            // 加密密码（与 Flyway 迁移脚本保持一致）
            com.clougence.clouddm.api.common.crypt.PasswordInfo cryptResult = com.clougence.clouddm.api.common.crypt.CryptService.INSTANCE.encryptForOneWay(adminPassword);
            String encodedPassword = cryptResult.getEncryptPassword();

            // 先检查表是否存在
            String dbName = InitDBStatusDetector.getDatabaseName(jdbcUrl);
            try (Statement checkStmt = conn.createStatement();
                    ResultSet crs = checkStmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='" + dbName + "' AND table_name='rdp_user'")) {
                if (!crs.next() || crs.getInt(1) == 0) {
                    log.warn("[SysInitService] rdp_user table not found, admin user will be created by Flyway with default values.");
                    return;
                }
            }

            // 查询管理员是否存在
            try (PreparedStatement stmt = conn.prepareStatement("SELECT id, email FROM rdp_user WHERE uid = ?")) {
                stmt.setString(1, InitSeedConstants.ADMIN_UID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        long existingId = rs.getLong(1);
                        String existingEmail = rs.getString(2);
                        log.info("[SysInitService] Admin user found (id={}, email={}), updating...", existingId, existingEmail);
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE rdp_user SET email = ?, password = ?, user_domain = ? WHERE uid = ?")) {
                            updateStmt.setString(1, adminEmail);
                            updateStmt.setString(2, encodedPassword);
                            updateStmt.setString(3, InitSeedConstants.ADMIN_UID + ".clougence.com");
                            updateStmt.setString(4, InitSeedConstants.ADMIN_UID);
                            int affected = updateStmt.executeUpdate();
                            log.info("[SysInitService] Admin user updated, affected rows: {}", affected);
                        }
                    } else {
                        log.warn("[SysInitService] Admin user not found by uid={}, inserting new admin user...", InitSeedConstants.ADMIN_UID);
                        try (PreparedStatement insertStmt = conn
                            .prepareStatement("INSERT INTO rdp_user (uid, email, password, username, account_type, user_domain, gmt_create, gmt_modified) VALUES (?, ?, ?, 'Trial', 'PRIMARY_ACCOUNT', ?, now(), now())")) {
                            insertStmt.setString(1, InitSeedConstants.ADMIN_UID);
                            insertStmt.setString(2, adminEmail);
                            insertStmt.setString(3, encodedPassword);
                            insertStmt.setString(4, InitSeedConstants.ADMIN_UID + ".clougence.com");
                            insertStmt.executeUpdate();
                        }
                        log.info("[SysInitService] New admin user inserted: {}", adminEmail);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("[SysInitService] Failed to update admin user", e);
            throw e;
        }
    }

    private void setRuntimeAdminProperty(String key, String value) {
        if (StringUtils.isBlank(value)) {
            System.clearProperty(key);
            return;
        }
        System.setProperty(key, value);
    }

    private void restoreRuntimeAdminProperty(String key, String previousValue) {
        if (previousValue == null) {
            System.clearProperty(key);
            return;
        }
        System.setProperty(key, previousValue);
    }

    // ========================================================================
    // Fix 任务
    // ========================================================================

    /**
     * 启动临时非 Web Spring 容器执行 fix 初始化（内部用户、角色、安全规则等）。
     */
    private void runFixTasks(String jdbcUrl, String dbUser, String dbPass) {
        log.info("[SysInitService] Running fix tasks with temporary Spring context...");
        InstallUpgradeLogBus.info("Running post-migration fix tasks.");
        try {
            SpringApplication app = new SpringApplication(InitTaskApplication.class);
            app.setWebApplicationType(WebApplicationType.NONE);
            app.setLazyInitialization(true);

            Properties props = buildTaskProperties(jdbcUrl, dbUser, dbPass);

            app.setDefaultProperties(props);

            try (ConfigurableApplicationContext ctx = app.run()) {
                ctx.getBean(InitConsolePluginLoader.class).loadPlugin(InitTaskApplication.class.getClassLoader());
                ctx.getBean(RdpFixUserRole.class).init();
                ctx.getBean(DmFixSecRules.class).init();
                ctx.getBean(DmFixDmDsConfig.class).init();
                InstallUpgradeLogBus.info("Post-migration fix tasks completed.");
                log.info("[SysInitService] Fix tasks completed successfully.");
            }
        } catch (Exception e) {
            log.error("[SysInitService] Fix tasks failed", e);
            throw new RuntimeException("Fix tasks failed: " + e.getMessage(), e);
        }
    }

    private void applyInspectionResult(TestDbResult result, DatabaseInspection inspection, String rebuildIfNotEmpty, String confirmDatabaseName) {
        result.setDatabaseExists(inspection.databaseExists);
        result.setCharsetValid(inspection.charsetValid);
        result.setDatabaseCharset(inspection.databaseCharset);
        result.setCreateDatabase(!inspection.databaseExists);
        result.setEmpty(!inspection.databaseExists || inspection.tableCount == 0);
        result.setInstalled(inspection.databaseExists && inspection.tableCount > 0);
        result.setCanProceed(false);

        if (!inspection.databaseExists) {
            result.setSuccess(true);
            result.setCanProceed(true);
            result.setMessageType("success");
            result.setMessage(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_SUCCESS.name()));
            return;
        }

        if (!inspection.charsetValid) {
            result.setSuccess(false);
            result.setMessageType("error");
            result.setMessage(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_CHARSET_INVALID.name(), StringUtils.defaultIfBlank(inspection.databaseCharset, "unknown")));
            return;
        }

        result.setSuccess(true);
        if (inspection.empty) {
            result.setCanProceed(true);
            result.setMessageType("success");
            result.setMessage(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_SUCCESS.name()));
            return;
        }

        result.setShowRebuildChoice(true);
        result.setRebuildPrompt(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_REBUILD_PROMPT.name()));

        if (!"true".equals(rebuildIfNotEmpty) && !"false".equals(rebuildIfNotEmpty)) {
            return;
        }

        result.setMessageType("warning");
        if ("false".equals(rebuildIfNotEmpty)) {
            result.setCanProceed(true);
            result.setMessage(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_USE_EXISTING_WARNING.name()));
            return;
        }

        result.setMessage(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_REBUILD_WARNING.name()));
        result.setRequireConfirmInput(true);
        result.setConfirmInputLabel(RdpI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_REBUILD_CONFIRM_LABEL.name()));
        result.setConfirmInputExpectedValue(inspection.databaseName);
        result.setCanProceed(inspection.databaseName.equals(confirmDatabaseName == null ? "" : confirmDatabaseName.trim()));
    }

    private void prepareDatabase(String jdbcUrl, String username, String password, boolean createIfMissing, boolean rebuildIfNotEmpty) throws SQLException {
        DatabaseInspection inspection = inspectDatabase(jdbcUrl, username, password, false);

        if (!inspection.databaseExists) {
            if (!createIfMissing) {
                throw new IllegalStateException("目标数据库不存在，请先测试连接并确认自动创建数据库");
            }
            log.info("[SysInitService] Target database does not exist, creating database {}", inspection.databaseName);
            createDatabase(inspection.serverJdbcUrl, username, password, inspection.databaseName);
            return;
        }

        if (!inspection.charsetValid) {
            throw new IllegalStateException("目标数据库默认编码必须为 utf8mb4，当前为 " + StringUtils.defaultIfBlank(inspection.databaseCharset, "未知"));
        }

        if (inspection.empty) {
            log.info("[SysInitService] Target database {} exists and is empty, proceeding with Flyway initialization", inspection.databaseName);
            return;
        }

        if (!inspection.empty && rebuildIfNotEmpty) {
            log.info("[SysInitService] Target database {} exists and will be rebuilt before Flyway initialization", inspection.databaseName);
            recreateDatabase(inspection.serverJdbcUrl, username, password, inspection.databaseName);
            return;
        }

        log.info("[SysInitService] Target database {} exists with data, keeping existing schema and proceeding with migration/fix tasks", inspection.databaseName);
    }

    private DatabaseInspection inspectDatabase(String jdbcUrl, String username, String password, boolean verifyTargetConnection) throws SQLException {
        String databaseName = InitDBStatusDetector.getDatabaseName(jdbcUrl);
        if (StringUtils.isBlank(databaseName)) {
            throw new SQLException("JDBC URL 缺少数据库名");
        }

        DatabaseInspection inspection = new DatabaseInspection();
        inspection.databaseName = databaseName;
        inspection.serverJdbcUrl = buildServerJdbcUrl(jdbcUrl);

        try (Connection serverConn = DriverManager.getConnection(inspection.serverJdbcUrl, username, password)) {
            inspection.databaseExists = querySchemaExists(serverConn, databaseName);
            if (!inspection.databaseExists) {
                inspection.empty = true;
                inspection.charsetValid = true;
                inspection.databaseCharset = REQUIRED_DB_CHARSET;
                return inspection;
            }

            inspection.databaseCharset = querySchemaCharset(serverConn, databaseName);
            inspection.charsetValid = REQUIRED_DB_CHARSET.equalsIgnoreCase(inspection.databaseCharset);
            if (!inspection.charsetValid) {
                return inspection;
            }

            inspection.tableCount = queryTableCount(serverConn, databaseName);
            inspection.empty = inspection.tableCount == 0;
        }

        if (verifyTargetConnection && inspection.databaseExists && inspection.charsetValid) {
            try (Connection ignored = DriverManager.getConnection(jdbcUrl, username, password)) {
                // 目标库连接可用即可。
            }
        }

        return inspection;
    }

    private boolean querySchemaExists(Connection conn, String databaseName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SCHEMA_EXISTS_SQL)) {
            stmt.setString(1, databaseName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private String querySchemaCharset(Connection conn, String databaseName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SCHEMA_CHARSET_SQL)) {
            stmt.setString(1, databaseName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    private int queryTableCount(Connection conn, String databaseName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(TABLE_COUNT_SQL)) {
            stmt.setString(1, databaseName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private void createDatabase(String serverJdbcUrl, String username, String password, String databaseName) throws SQLException {
        executeDatabaseStatement(serverJdbcUrl, username, password, "CREATE DATABASE `" + escapeMysqlIdentifier(databaseName) + "` DEFAULT CHARACTER SET " + REQUIRED_DB_CHARSET
                                                                    + " COLLATE " + REQUIRED_DB_COLLATION);
    }

    private void recreateDatabase(String serverJdbcUrl, String username, String password, String databaseName) throws SQLException {
        String quotedName = "`" + escapeMysqlIdentifier(databaseName) + "`";
        executeDatabaseStatement(serverJdbcUrl, username, password, "DROP DATABASE " + quotedName);
        executeDatabaseStatement(serverJdbcUrl, username, password, "CREATE DATABASE " + quotedName + " DEFAULT CHARACTER SET " + REQUIRED_DB_CHARSET + " COLLATE "
                                                                    + REQUIRED_DB_COLLATION);
    }

    private void executeDatabaseStatement(String serverJdbcUrl, String username, String password, String sql) throws SQLException {
        InstallUpgradeLogBus.info("[SQL] " + sql);
        try (Connection conn = DriverManager.getConnection(serverJdbcUrl, username, password); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            InstallUpgradeLogBus.error("[SQL FAILED] " + sql, e);
            throw e;
        }
    }

    private String buildServerJdbcUrl(String jdbcUrl) throws SQLException {
        if (StringUtils.isBlank(jdbcUrl)) {
            throw new SQLException("JDBC URL 不能为空");
        }

        int queryIndex = jdbcUrl.indexOf('?');
        String base = queryIndex >= 0 ? jdbcUrl.substring(0, queryIndex) : jdbcUrl;
        String query = queryIndex >= 0 ? jdbcUrl.substring(queryIndex) : "";
        int slashIndex = base.lastIndexOf('/');
        if (slashIndex < 0 || slashIndex == base.length() - 1) {
            throw new SQLException("JDBC URL 缺少数据库名");
        }
        return base.substring(0, slashIndex + 1) + query;
    }

    private String escapeMysqlIdentifier(String identifier) {
        return identifier == null ? "" : identifier.replace("`", "``");
    }

    private Properties buildTaskProperties(String jdbcUrl, String dbUser, String dbPass) {
        Properties props = this.defService.loadSystemProperties();
        props.setProperty("spring.datasource.url", jdbcUrl);
        props.setProperty("spring.datasource.jdbcurl", jdbcUrl);
        props.setProperty("spring.datasource.username", dbUser);
        props.setProperty("spring.datasource.password", dbPass == null ? "" : dbPass);
        props.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
        props.setProperty("server.port", "-1");
        return props;
    }

    private RuntimeException toDetailedRuntimeException(Exception exception) {
        if (exception instanceof RuntimeException && StringUtils.contains(exception.getMessage(), "\n")) {
            return (RuntimeException) exception;
        }
        return new RuntimeException(buildDetailedErrorMessage(exception), exception);
    }

    // ========================================================================
    // 重启
    // ========================================================================

    /**
     * 触发系统重启（方案 B：写标记文件 + 退出进程）。
     */
    public void scheduleRestart() {
        try {
            Path restartFlag = ensureAppHomeConfigPath(".restarting");
            Files.write(restartFlag, String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
            log.info("[SysInitService] Restart flag written: {}", restartFlag);
        } catch (IOException e) {
            log.error("[SysInitService] Failed to write restart flag", e);
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            System.exit(0);
        }, "restart-thread").start();
    }

    private Path ensureAppHomeConfigPath(String configName) throws IOException {
        Path configPath = Paths.get(GlobalConfUtils.getAppHome(), "conf", configName);
        Files.createDirectories(configPath.getParent());
        return configPath;
    }

    private static class DatabaseInspection {

        private String  databaseName;
        private String  serverJdbcUrl;
        private String  databaseCharset;
        private boolean databaseExists;
        private boolean charsetValid;
        private boolean empty;
        private int     tableCount;
    }
}
