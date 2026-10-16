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
package com.clougence.clouddm.ds.kingbasees.execute.dsfactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.clougence.clouddm.ds.kingbasees.dsconf.KingbaseESCompatibilityMode;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KingbaseESDsFactory implements DsFactory<Connection> {

    private static final char[] TIME_ZONE_INJECT_CHAR = new char[] { ' ', ';', '\'' };

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws Exception {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String database = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String loginTimeoutMs = dsConfig.getProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey());
        String connectTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
        String expectedModeValue = dsConfig.getProperty(KingbaseESCompatibilityMode.EXPECTED_MODE_PROPERTY, KingbaseESCompatibilityMode.POSTGRESQL.getServerMode());
        KingbaseESCompatibilityMode expectedMode = KingbaseESCompatibilityMode.fromServerMode(expectedModeValue);
        props.remove(KingbaseESCompatibilityMode.EXPECTED_MODE_PROPERTY);

        putIfNotBlank(props, "user", username);
        putIfNotBlank(props, "password", password);
        putTimeoutMillisAsSeconds(props, "loginTimeout", loginTimeoutMs);
        putTimeoutMillisAsSeconds(props, "connectTimeout", connectTimeoutMs);
        putIfNotBlank(props, "socketTimeout", soTimeoutSec);
        putIfNotBlank(props, "ApplicationName", clientName);
        putIfNotBlank(props, "currentSchema", defaultSchema);
        putIfNotBlank(props, "tcpKeepAlive", tcpKeepAlive);
        props.putIfAbsent("stringtype", "unspecified");
        props.putIfAbsent("useFetchSizeInAutoCommit", "true");

        String jdbcUrl = buildJdbcUrl(dsConfig);
        log.info("Create KingbaseES connection instanceId={}, host={}, database={}, sslmode={}", id, host, database, props.getProperty("sslmode"));

        Connection connection = null;
        try {
            connection = new com.kingbase8.Driver().connect(jdbcUrl, props);
            if (connection == null) {
                throw new SQLException("KingbaseES JDBC driver rejected the configured URL.");
            }

            KingbaseESCompatibility compatibility = probeCompatibility(connection, expectedMode);
            applyClientTimeZone(connection, clientTimeZone);
            if (StringUtils.isNotBlank(autoCommit)) {
                connection.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }
            return new KingbaseESDsObject(dsConfig, connection, this, compatibility);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeError) {
                    log.warn("Close failed KingbaseES connection: {}", closeError.getMessage());
                }
            }
            String msg = "Create KingbaseES connection failed: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw e;
        }
    }

    private static void putIfNotBlank(Properties props, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            props.put(key, value);
        }
    }

    private static void putTimeoutMillisAsSeconds(Properties props, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            props.put(key, Long.toString(Long.parseLong(value) / 1000));
        }
    }

    private static void applyClientTimeZone(Connection connection, String clientTimeZone) throws SQLException {
        if (StringUtils.isBlank(clientTimeZone) || StringUtils.containsAny(clientTimeZone, TIME_ZONE_INJECT_CHAR)) {
            return;
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET TIME ZONE '" + clientTimeZone + "'");
        }
    }

    private static KingbaseESCompatibility probeCompatibility(Connection connection, KingbaseESCompatibilityMode expectedMode) throws SQLException {
        if (!connection.getAutoCommit()) {
            throw new SQLException("KingbaseES compatibility probe requires an auto-commit connection.");
        }

        String databaseMode = querySingleValue(connection, "SHOW database_mode");
        KingbaseESCompatibilityMode actualMode;
        try {
            actualMode = KingbaseESCompatibilityMode.fromServerMode(databaseMode);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Unsupported KingbaseES compatibility mode: " + databaseMode, e);
        }
        if (actualMode != expectedMode) {
            throw new SQLException("KingbaseES compatibility mode mismatch, expected: " + expectedMode.getServerMode() + ", actual: " + databaseMode);
        }

        String emptyStringIsNull = querySingleValue(connection, "SHOW ora_input_emptystr_isnull");
        boolean actualEmptyStringIsNull = isOn(emptyStringIsNull);
        if (actualEmptyStringIsNull != expectedMode.isEmptyStringIsNull()) {
            throw new SQLException("KingbaseES ora_input_emptystr_isnull mismatch for " + expectedMode.getServerMode() + " mode, expected: "
                                   + (expectedMode.isEmptyStringIsNull() ? "on" : "off") + ", actual: " + emptyStringIsNull);
        }

        String grammarVersion = null;
        if (actualMode == KingbaseESCompatibilityMode.POSTGRESQL) {
            String serverVersionNum = querySingleValue(connection, "SELECT current_setting('server_version_num')");
            grammarVersion = resolveGrammarVersion(serverVersionNum);
        }
        return new KingbaseESCompatibility(actualMode, grammarVersion);
    }

    private static String querySingleValue(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new SQLException("KingbaseES compatibility query returned no rows: " + sql);
            }
            return resultSet.getString(1);
        }
    }

    private static boolean isOn(String value) {
        String normalized = StringUtils.trimToEmpty(value);
        return "on".equalsIgnoreCase(normalized) || "true".equalsIgnoreCase(normalized) || "1".equals(normalized);
    }

    private static String resolveGrammarVersion(String serverVersionNum) throws SQLException {
        String value = StringUtils.trimToEmpty(serverVersionNum);
        try {
            int majorVersion = Integer.parseInt(value) / 10000;
            if (majorVersion < 12 || majorVersion > 18) {
                throw new SQLException("Unsupported KingbaseES PostgreSQL grammar version: " + majorVersion);
            }
            return Integer.toString(majorVersion);
        } catch (NumberFormatException e) {
            throw new SQLException("Invalid KingbaseES server_version_num: " + value, e);
        }
    }

    private static String safeString(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String customUrl = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(customUrl)) {
            return customUrl;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String defaultCatalog = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        if (StringUtils.isBlank(defaultCatalog)) {
            defaultCatalog = "test";
        }

        String[] hostPort = host.split(":");
        if (hostPort.length == 1) {
            return String.format("jdbc:kingbase8://%s:54321/%s", hostPort[0], safeString(defaultCatalog));
        }
        if (hostPort.length == 2) {
            return String.format("jdbc:kingbase8://%s:%s/%s", hostPort[0], hostPort[1], safeString(defaultCatalog));
        }
        throw new IllegalArgumentException("Unsupported KingbaseES host format: " + host);
    }
}
