/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.clougence.clouddm.init.component.fixtasks.*;
import com.clougence.clouddm.init.component.flyway.DmFlywayInit;
import com.clougence.clouddm.init.component.log.InstallUpgradeLogBus;
import com.clougence.clouddm.init.constant.I18nInitFieldKeys;
import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.clouddm.init.model.InitFieldDef;
import com.clougence.clouddm.init.model.TestDbResult;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * Core service for the system initialization workflow.
 * - Merges the init-fields.json schema with runtime property defaults.
 * - Evaluates database connectivity and installation state.
 * - Persists configuration and runs Flyway-based initialization or upgrade tasks.
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

    // Database connectivity and installation-state checks.
    // ========================================================================

    /**
     * Tests database connectivity with the temporary parameters submitted by the user.
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
            result.setMessage(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_CONNECTION_FAILED.name(), e.getMessage()));
        }
        return result;
    }

    public List<String> previewExecutionScripts(Map<String, String> userConfig) {
        if (shouldRunAllScripts(userConfig)) {
            return DmFlywayInit.listAllScriptNames();
        }

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
    // Configuration loading and persistence.
    // ========================================================================

    /**
     * Applies the full initialization flow: persist configuration, run Flyway, and update the administrator account.
     */
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
                runFixTasks(jdbcUrl, dbUser, dbPass, true);
            }

            InstallUpgradeLogBus.complete("Initialization completed successfully.");
            log.info("[SysInitService] Initialization apply flow completed successfully for jdbcUrl={}", jdbcUrl);
        } catch (Exception e) {
            InstallUpgradeLogBus.fail("Initialization failed.", e);
            throw toDetailedRuntimeException(e);
        }
    }

    /**
     * Updates only the database configuration.
     * For a new, empty, or explicitly rebuilt database, it also runs initialization and writes the administrator account.
     * For an existing non-empty database, it only updates connectivity and runs migration or fix tasks without resetting the administrator account.
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
            runFixTasks(jdbcUrl, dbUser, dbPass, true);
            InstallUpgradeLogBus.complete("Installation flow completed successfully.");
        } catch (Exception e) {
            InstallUpgradeLogBus.fail("Installation failed.", e);
            throw toDetailedRuntimeException(e);
        }
    }

    public void upgradeSystem(Map<String, String> userConfig) throws Exception {
        if (userConfig != null && !userConfig.isEmpty()) {
            replaceConfigLines(userConfig);
        }

        Properties props = this.defService.loadSystemProperties();
        String jdbcUrl = resolveConfigValue(userConfig, props, "spring.datasource.jdbcurl");
        String dbUser = resolveConfigValue(userConfig, props, "spring.datasource.username");
        String dbPass = resolveConfigValue(userConfig, props, "spring.datasource.password");
        boolean createIfMissing = userConfig != null && userConfig.containsKey(INIT_DB_CREATE_IF_MISSING) && Boolean.parseBoolean(userConfig.get(INIT_DB_CREATE_IF_MISSING));
        boolean rebuildIfNotEmpty = userConfig != null && userConfig.containsKey(INIT_DB_REBUILD_IF_NOT_EMPTY)
                                    && Boolean.parseBoolean(userConfig.get(INIT_DB_REBUILD_IF_NOT_EMPTY));

        InstallUpgradeLogBus.start("upgrade", jdbcUrl);
        try {
            if (StringUtils.isBlank(jdbcUrl) || StringUtils.isBlank(dbUser)) {
                throw new IllegalStateException("Database configuration is missing.");
            }

            if (createIfMissing || rebuildIfNotEmpty) {
                InstallUpgradeLogBus.info("Preparing database before upgrade.");
                prepareDatabase(jdbcUrl, dbUser, dbPass, createIfMissing, rebuildIfNotEmpty);
            }

            runUpgradeMigration(jdbcUrl, dbUser, dbPass);
            if (rebuildIfNotEmpty || createIfMissing) {
                runFixTasks(jdbcUrl, dbUser, dbPass, false);
            }
            InstallUpgradeLogBus.complete("Upgrade completed successfully.");
        } catch (Exception e) {
            InstallUpgradeLogBus.fail("Upgrade failed.", e);
            throw toDetailedRuntimeException(e);
        }
    }

    // ========================================================================
    // Internal helpers.
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
            InstallUpgradeLogBus.notice("DB_INIT", "info");
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
            InstallUpgradeLogBus.notice("DB_INIT", "info");
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
            // Encrypt the password using the same format as the Flyway seed scripts.
            com.clougence.clouddm.api.common.crypt.PasswordInfo cryptResult = com.clougence.clouddm.api.common.crypt.CryptService.INSTANCE.encryptForOneWay(adminPassword);
            String encodedPassword = cryptResult.getEncryptPassword();

            // Check whether the user table already exists before attempting any update.
            String dbName = InitDBStatusDetector.getDatabaseName(jdbcUrl);
            try (Statement checkStmt = conn.createStatement();
                    ResultSet crs = checkStmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='" + dbName + "' AND table_name='rdp_user'")) {
                if (!crs.next() || crs.getInt(1) == 0) {
                    log.warn("[SysInitService] rdp_user table not found, admin user will be created by Flyway with default values.");
                    return;
                }
            }

            // Look up the administrator account so it can be updated or inserted.
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
    // Post-migration fix tasks.
    // ========================================================================

    /**
     * Starts a temporary non-web Spring container to run fix tasks such as internal user, role, and security rule initialization.
     */
    private void runFixTasks(String jdbcUrl, String dbUser, String dbPass, boolean includeDefaultClusterWorker) {
        log.info("[SysInitService] Running fix tasks with temporary Spring context...");
        InstallUpgradeLogBus.info("Running post-migration fix tasks.");
        InstallUpgradeLogBus.notice("FIX_RUNNING", "info");
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
                if (includeDefaultClusterWorker) {
                    ctx.getBean(DmFixDefaultClusterWorker.class).init();
                }
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
            result.setMessage(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_SUCCESS.name()));
            return;
        }

        if (!inspection.charsetValid) {
            result.setSuccess(false);
            result.setMessageType("error");
            result.setMessage(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_CHARSET_INVALID.name(), StringUtils.defaultIfBlank(inspection.databaseCharset, "unknown")));
            return;
        }

        result.setSuccess(true);
        if (inspection.empty) {
            result.setCanProceed(true);
            result.setMessageType("success");
            result.setMessage(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_SUCCESS.name()));
            return;
        }

        result.setShowRebuildChoice(true);
        result.setRebuildPrompt(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_REBUILD_PROMPT.name()));

        if (!"true".equals(rebuildIfNotEmpty) && !"false".equals(rebuildIfNotEmpty)) {
            return;
        }

        result.setMessageType("warning");
        if ("false".equals(rebuildIfNotEmpty)) {
            result.setCanProceed(true);
            result.setMessage(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_USE_EXISTING_WARNING.name()));
            return;
        }

        result.setMessage(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_REBUILD_WARNING.name()));
        result.setRequireConfirmInput(true);
        result.setConfirmInputLabel(DmI18nUtils.getMessage(I18nInitFieldKeys.INIT_TEST_DB_REBUILD_CONFIRM_LABEL.name()));
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
            InstallUpgradeLogBus.notice("DB_REBUILD", "warning");
            recreateDatabase(inspection.serverJdbcUrl, username, password, inspection.databaseName);
            return;
        }

        log.info("[SysInitService] Target database {} exists with data, keeping existing schema and proceeding with migration/fix tasks", inspection.databaseName);
    }

    private boolean shouldRunAllScripts(Map<String, String> userConfig) {
        return Boolean.parseBoolean(resolveConfigValue(userConfig, null, INIT_DB_REBUILD_IF_NOT_EMPTY));
    }

    private String resolveConfigValue(Map<String, String> userConfig, Properties props, String key) {
        if (userConfig != null && userConfig.containsKey(key)) {
            return userConfig.get(key);
        }
        return props == null ? null : props.getProperty(key);
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
                // It is sufficient to verify that the target schema can be reached.
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
    // Restart handling.
    // ========================================================================

    /**
     * Triggers a system restart by writing the restart marker file and then exiting the current process.
     */
    public void scheduleRestart() {
        try {
            Path restartFlag = resolveRestartFlagPath();
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

    private Path resolveRestartFlagPath() throws IOException {
        Path restartFlag = Paths.get(GlobalConfUtils.getAppHome(), ".restarting");
        Files.createDirectories(restartFlag.getParent());
        return restartFlag;
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
