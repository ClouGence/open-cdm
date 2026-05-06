package com.clougence.clouddm.init.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.init.InitTaskApplication;
import com.clougence.clouddm.init.component.fixtasks.DmFixDmDsConfig;
import com.clougence.clouddm.init.component.fixtasks.DmFixSecRules;
import com.clougence.clouddm.init.component.fixtasks.RdpFixInnerUser;
import com.clougence.clouddm.init.component.fixtasks.RdpFixUserRole;
import com.clougence.clouddm.init.component.flyway.DmFlywayInit;
import com.clougence.clouddm.init.model.InitFieldDef;
import com.clougence.clouddm.init.model.TestDbResult;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

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

    private static final String INIT_DB_CREATE_IF_MISSING = "clougence.init.db.createIfMissing";
    private static final String INIT_DB_REBUILD_IF_NOT_EMPTY = "clougence.init.db.rebuildIfNotEmpty";
    private static final String REQUIRED_DB_CHARSET = "utf8mb4";
    private static final String REQUIRED_DB_COLLATION = "utf8mb4_general_ci";
    private static final String SCHEMA_EXISTS_SQL = "SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name = ?";
    private static final String SCHEMA_CHARSET_SQL = "SELECT default_character_set_name FROM information_schema.schemata WHERE schema_name = ?";
    private static final String TABLE_COUNT_SQL = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ?";

    @Resource
    private SysInitDefService   defService;
    private static final String ALONE_CONFIG   = "alone.properties";
    private static final String CONSOLE_CONFIG = "console.properties";
    private static final String ADMIN_UID      = "6258151610403310";

    // 数据库测试
    // ========================================================================

    /**
     * 测试数据库连接（使用用户提交的临时参数）。
     */
    public TestDbResult testDbConnection(String jdbcUrl, String username, String password) {
        TestDbResult result = new TestDbResult();
        try {
            DatabaseInspection inspection = inspectDatabase(jdbcUrl, username, password, true);
            applyInspectionResult(result, inspection);
        } catch (Exception e) {
            result.setInstalled(false);
            result.setEmpty(false);
            result.setSuccess(false);
            result.setDatabaseExists(false);
            result.setCharsetValid(false);
            result.setCreateDatabase(false);
            result.setMessage("连接失败: " + e.getMessage());
        }
        return result;
    }

    // ========================================================================
    // 配置读写
    // ========================================================================

    /**  应用初始化配置（完整模式：写配置 + Flyway 迁移 + 更新管理员） */
    public void applyInitConfig(Map<String, String> userConfig) throws Exception {
        // 1. 处理 rawContent（高级模式：整体替换）
        if (userConfig.containsKey("rawContent")) {
            writeRawConfigFile(userConfig.get("rawContent"));
        } else {
            // 2. 普通模式：按 schema 逐行替换
            replaceConfigLines(userConfig);
        }

        // 3. 提取 DB 配置和 admin 配置
        Properties props = this.defService.loadSystemProperties();
        String jdbcUrl = userConfig.getOrDefault("spring.datasource.jdbcurl", props.getProperty("spring.datasource.jdbcurl"));
        String dbUser = userConfig.getOrDefault("spring.datasource.username", props.getProperty("spring.datasource.username"));
        String dbPass = userConfig.getOrDefault("spring.datasource.password", props.getProperty("spring.datasource.password"));
        String adminEmail = userConfig.get("clougence.init.admin.email");
        String adminPassword = userConfig.get("clougence.init.admin.password");
        boolean createIfMissing = Boolean.parseBoolean(userConfig.getOrDefault(INIT_DB_CREATE_IF_MISSING, "false"));
        boolean rebuildIfNotEmpty = Boolean.parseBoolean(userConfig.getOrDefault(INIT_DB_REBUILD_IF_NOT_EMPTY, "false"));

        if (StringUtils.isNotBlank(jdbcUrl) && StringUtils.isNotBlank(dbUser)) {
            prepareDatabase(jdbcUrl, dbUser, dbPass, createIfMissing, rebuildIfNotEmpty);
        }

        // 4. 执行 Flyway 迁移（通过 DmFlywayInit）
        if (StringUtils.isNotBlank(jdbcUrl) && StringUtils.isNotBlank(dbUser)) {
            runFlywayMigration(jdbcUrl, dbUser, dbPass);
        }

        // 5. 更新管理员账号
        if (StringUtils.isNotBlank(adminEmail) && StringUtils.isNotBlank(adminPassword)) {
            updateAdminUser(jdbcUrl, dbUser, dbPass, adminEmail, adminPassword);
        }

        // 6. 执行 fix 初始化（内部用户、角色、安全规则等）
        if (StringUtils.isNotBlank(jdbcUrl) && StringUtils.isNotBlank(dbUser)) {
            runFixTasks(jdbcUrl, dbUser, dbPass);
        }
    }

    /**
     * 仅更新数据库配置（dbOnly 模式，不执行 Flyway 迁移和账号初始化）。
     */
    public void updateDbConfig(Map<String, String> userConfig) throws Exception {
        replaceConfigLines(userConfig);
    }

    // ========================================================================
    // 内部方法
    // ========================================================================

    private boolean isAloneMode() { return "embedded".equals(System.getProperty("app.mode")); }

    private void replaceConfigLines(Map<String, String> userConfig) throws Exception {
        String configName = isAloneMode() ? ALONE_CONFIG : CONSOLE_CONFIG;
        URL resource = ResourcesUtils.getResource(configName);
        if (resource == null) {
            log.warn("[SysInitService] Config file not found: {}", configName);
            return;
        }

        List<String> lines;
        Path filePath = null;
        if ("file".equals(resource.getProtocol())) {
            filePath = Paths.get(resource.toURI());
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } else {
            // classpath 资源可能来自 JAR
            String content = IOUtils.toString(ResourcesUtils.getResourceAsStream(configName), StandardCharsets.UTF_8);
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

        if (filePath != null) {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } else {
            log.warn("[SysInitService] Cannot write to non-file resource: {}", configName);
        }
    }

    private void writeRawConfigFile(String rawContent) throws Exception {
        String configName = isAloneMode() ? ALONE_CONFIG : CONSOLE_CONFIG;
        URL resource = ResourcesUtils.getResource(configName);
        if (resource != null && "file".equals(resource.getProtocol())) {
            Path filePath = Paths.get(resource.toURI());
            Files.write(filePath, rawContent.getBytes(StandardCharsets.UTF_8));
        } else {
            log.warn("[SysInitService] Cannot write raw config to non-file resource: {}", configName);
        }
    }

    private void runFlywayMigration(String jdbcUrl, String dbUser, String dbPass) {
        log.info("[SysInitService] Running Flyway migration with: {}", jdbcUrl);
        try {
            SpringApplication app = new SpringApplication(InitTaskApplication.class);
            app.setWebApplicationType(WebApplicationType.NONE);

            Properties props = buildTaskProperties(jdbcUrl, dbUser, dbPass);
            props.setProperty("spring.flyway.enabled", "true");
            app.setDefaultProperties(props);

            try (ConfigurableApplicationContext ctx = app.run()) {
                ctx.getBean(DmFlywayInit.class).doUpgrade();
                log.info("[SysInitService] Flyway migration done.");
            }
        } catch (Exception e) {
            log.error("[SysInitService] Flyway migration failed", e);
            throw new RuntimeException("Flyway migration failed: " + e.getMessage(), e);
        }
    }

    private void updateAdminUser(String jdbcUrl, String dbUser, String dbPass, String adminEmail, String adminPassword) {
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
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT id, email FROM rdp_user WHERE uid='" + ADMIN_UID + "'")) {
                if (rs.next()) {
                    long existingId = rs.getLong(1);
                    String existingEmail = rs.getString(2);
                    log.info("[SysInitService] Admin user found (id={}, email={}), updating...", existingId, existingEmail);
                    // 更新邮箱和密码
                    String updateSql = String.format("UPDATE rdp_user SET email='%s', password='%s' WHERE uid='%s'", adminEmail, encodedPassword, ADMIN_UID);
                    try (Statement updateStmt = conn.createStatement()) {
                        int affected = updateStmt.executeUpdate(updateSql);
                        log.info("[SysInitService] Admin user updated, affected rows: {}", affected);
                    }
                } else {
                    log.warn("[SysInitService] Admin user not found by uid={}, inserting new admin user...", ADMIN_UID);
                    // Flyway 没有创建默认管理员，手动插入
                    String insertSql = String
                        .format("INSERT INTO rdp_user (uid, email, password, username, account_type, user_domain, gmt_create, gmt_modified) "
                                + "VALUES ('%s', '%s', '%s', 'Trial', 'PRIMARY_ACCOUNT', '%s.clougence.com', now(), now())", ADMIN_UID, adminEmail, encodedPassword, ADMIN_UID);
                    try (Statement insertStmt = conn.createStatement()) {
                        insertStmt.executeUpdate(insertSql);
                    }
                    log.info("[SysInitService] New admin user inserted: {}", adminEmail);
                }
            }
        } catch (SQLException e) {
            log.error("[SysInitService] Failed to update admin user", e);
        }
    }

    // ========================================================================
    // Fix 任务
    // ========================================================================

    /**
     * 启动临时非 Web Spring 容器执行 fix 初始化（内部用户、角色、安全规则等）。
     */
    private void runFixTasks(String jdbcUrl, String dbUser, String dbPass) {
        log.info("[SysInitService] Running fix tasks with temporary Spring context...");
        try {
            SpringApplication app = new SpringApplication(InitTaskApplication.class);
            app.setWebApplicationType(WebApplicationType.NONE);

            Properties props = buildTaskProperties(jdbcUrl, dbUser, dbPass);
            props.setProperty("spring.flyway.enabled", "false");

            app.setDefaultProperties(props);

            try (ConfigurableApplicationContext ctx = app.run()) {
                ctx.getBean(RdpFixInnerUser.class).init();
                ctx.getBean(RdpFixUserRole.class).init();
                ctx.getBean(DmFixSecRules.class).init();
                ctx.getBean(DmFixDmDsConfig.class).init();
                log.info("[SysInitService] Fix tasks completed successfully.");
            }
        } catch (Exception e) {
            log.error("[SysInitService] Fix tasks failed", e);
            throw new RuntimeException("Fix tasks failed: " + e.getMessage(), e);
        }
    }

    private void applyInspectionResult(TestDbResult result, DatabaseInspection inspection) {
        result.setDatabaseExists(inspection.databaseExists);
        result.setCharsetValid(inspection.charsetValid);
        result.setDatabaseCharset(inspection.databaseCharset);
        result.setCreateDatabase(!inspection.databaseExists);
        result.setEmpty(!inspection.databaseExists || inspection.tableCount == 0);
        result.setInstalled(inspection.databaseExists && inspection.tableCount > 0);

        if (!inspection.databaseExists) {
            result.setSuccess(true);
            result.setMessage("测试成功：数据库不存在，后续将自动创建并使用 utf8mb4 编码");
            return;
        }

        if (!inspection.charsetValid) {
            result.setSuccess(false);
            result.setMessage("测试失败：数据库默认编码必须为 utf8mb4，当前为 " + StringUtils.defaultIfBlank(inspection.databaseCharset, "未知"));
            return;
        }

        result.setSuccess(true);
        if (inspection.tableCount > 0) {
            result.setMessage("连接成功：检测到数据库已有数据");
        } else {
            result.setMessage("连接成功");
        }
    }

    private void prepareDatabase(String jdbcUrl, String username, String password, boolean createIfMissing, boolean rebuildIfNotEmpty) throws SQLException {
        DatabaseInspection inspection = inspectDatabase(jdbcUrl, username, password, false);

        if (!inspection.databaseExists) {
            if (!createIfMissing) {
                throw new IllegalStateException("目标数据库不存在，请先测试连接并确认自动创建数据库");
            }
            createDatabase(inspection.serverJdbcUrl, username, password, inspection.databaseName);
            return;
        }

        if (!inspection.charsetValid) {
            throw new IllegalStateException("目标数据库默认编码必须为 utf8mb4，当前为 " + StringUtils.defaultIfBlank(inspection.databaseCharset, "未知"));
        }

        if (!inspection.empty && rebuildIfNotEmpty) {
            recreateDatabase(inspection.serverJdbcUrl, username, password, inspection.databaseName);
        }
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
        executeDatabaseStatement(serverJdbcUrl, username, password,
                "CREATE DATABASE `" + escapeMysqlIdentifier(databaseName) + "` DEFAULT CHARACTER SET " + REQUIRED_DB_CHARSET + " COLLATE " + REQUIRED_DB_COLLATION);
    }

    private void recreateDatabase(String serverJdbcUrl, String username, String password, String databaseName) throws SQLException {
        String quotedName = "`" + escapeMysqlIdentifier(databaseName) + "`";
        executeDatabaseStatement(serverJdbcUrl, username, password, "DROP DATABASE " + quotedName);
        executeDatabaseStatement(serverJdbcUrl, username, password,
                "CREATE DATABASE " + quotedName + " DEFAULT CHARACTER SET " + REQUIRED_DB_CHARSET + " COLLATE " + REQUIRED_DB_COLLATION);
    }

    private void executeDatabaseStatement(String serverJdbcUrl, String username, String password, String sql) throws SQLException {
        try (Connection conn = DriverManager.getConnection(serverJdbcUrl, username, password); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
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

    // ========================================================================
    // 重启
    // ========================================================================

    /**
     * 触发系统重启（方案 B：写标记文件 + 退出进程）。
     */
    public void scheduleRestart() {
        try {
            Path restartFlag = Paths.get(System.getProperty("user.dir"), "config", ".restarting");
            Files.createDirectories(restartFlag.getParent());
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
