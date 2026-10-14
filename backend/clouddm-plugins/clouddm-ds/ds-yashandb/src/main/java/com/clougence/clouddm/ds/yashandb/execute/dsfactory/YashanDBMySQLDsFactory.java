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
package com.clougence.clouddm.ds.yashandb.execute.dsfactory;

import java.sql.*;
import java.util.Properties;

import com.clougence.clouddm.ds.yashandb.dsconf.YashanDBCompatibilityMode;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Connection factory for the YashanDB MySQL compatibility mode.
 * <p>
 * The YashanDB MySQL compatibility mode serves the standard MySQL protocol port ({@code 1690} on the tested
 * single-node instance 23.4.1.109); the official documentation states that MySQL clients/drivers can connect
 * to it directly, so this variant uses {@code com.mysql.cj.jdbc.Driver} and a connection URL of the form
 * {@code jdbc:mysql://host:1690/database}.
 * </p>
 * <p>
 * Why not use the YashanDB driver with {@code ?compatVector=mysql}: that parameter only provides <b>SQL
 * dialect</b> compatibility, while the driver's JDBC metadata semantics remain Oracle-oriented. Measured on
 * the same instance: {@code getCatalogs()} returns 0 rows, {@code getSchemas()} returns all-uppercase names
 * mixed with a ghost schema {@code $MY_test}, {@code identifierQuoteString} is a double quote (MySQL uses a
 * backtick), and {@code DatabaseMetaData.getColumns()} directly raises
 * {@code YAS-04323 arguments count must be 2}. The metadata implementations in {@code dsc-common-mysql}, which
 * as a whole assume "MySQL Connector/J semantics", therefore cannot be reused.
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBMySQLDsFactory implements DsFactory<Connection> {

    private static final String                DRIVER_CLASS     = "com.mysql.cj.jdbc.Driver";

    /** Engine name YashanDB registers in {@code information_schema.ENGINES}, used to confirm the target instance is really YashanDB. */
    private static final String                YASHANDB_ENGINE  = "YashanDB";

    /**
     * This variant always corresponds to the MySQL compatibility mode.
     * <p>
     * Unlike GoldenDB, whose two variants share one factory and pass the expected mode through a driver
     * property, the two YashanDB variants use completely different drivers and connection protocols and each
     * binds its own driver family and factory. The expected mode is therefore held by the factory itself; no
     * custom property needs to be pushed into the driver properties (which also avoids passing invalid
     * properties through to the yasdb driver).
     * </p>
     */
    private static final YashanDBCompatibilityMode EXPECTED_MODE = YashanDBCompatibilityMode.MYSQL;

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties properties = driverProperties(dsConfig);
        String instanceId = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String jdbcUrl = buildJdbcUrl(dsConfig);

        Connection connection = null;
        try {
            Class<?> driverClass = YashanDBMySQLDsFactory.class.getClassLoader().loadClass(DRIVER_CLASS);
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            connection = driver.connect(jdbcUrl, properties);
            if (connection == null) {
                throw new SQLException("YashanDB MySQL JDBC driver rejected the connection URL.");
            }
            validateCompatibilityMode(connection);

            String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
            if (StringUtils.isNotBlank(autoCommit)) {
                connection.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }
            log.info("Create YashanDB(MySQL mode) connection instanceId={}, jdbcUrl={}", instanceId, jdbcUrl);
            return new DsObject<>(dsConfig, connection, this);
        } catch (SQLException e) {
            closeConnection(connection);
            log.error("Create YashanDB(MySQL mode) connection failed, instanceId={}", instanceId, e);
            throw e;
        } catch (Exception e) {
            closeConnection(connection);
            String msg = "Load YashanDB MySQL JDBC driver failed.";
            log.error(msg + " instanceId={}", instanceId, e);
            throw new SQLException(msg, e);
        }
    }

    /**
     * Confirms after connecting that the target instance really runs in the YashanDB MySQL compatibility mode.
     * <p>
     * The YashanDB MySQL compatibility mode has no system variable such as {@code COMPAT_VECTOR} to check, but
     * it registers itself as the only engine {@code YashanDB} in {@code information_schema.ENGINES}, whereas
     * standard MySQL returns {@code InnoDB} and others. That is therefore used as the fingerprint.
     * </p>
     */
    private void validateCompatibilityMode(Connection connection) throws SQLException {
        boolean matched = false;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ENGINE FROM information_schema.ENGINES")) {
            while (resultSet.next()) {
                if (StringUtils.equalsIgnoreCase(YASHANDB_ENGINE, resultSet.getString(1))) {
                    matched = true;
                    break;
                }
            }
        }
        if (!matched) {
            throw new SQLException("YashanDB compatibility mode mismatch, expected: " + EXPECTED_MODE.getServerMode()
                                   + ", but the target instance does not expose the " + YASHANDB_ENGINE + " engine in information_schema.ENGINES.");
        }
    }

    private void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("Close failed YashanDB(MySQL mode) connection: {}", e.getMessage());
        }
    }

    private Properties driverProperties(Properties dsConfig) {
        Properties properties = new Properties();
        properties.putAll(dsConfig);
        for (DsConfigKeys configKey : DsConfigKeys.values()) {
            properties.remove(configKey.getConfigKey());
        }
        properties.entrySet().removeIf(entry -> entry.getValue() == null || StringUtils.isBlank(String.valueOf(entry.getValue())));

        putIfNotBlank(properties, "user", dsConfig.getProperty(DsConfigKeys.USER.getConfigKey()));
        putIfNotBlank(properties, "password", dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey()));
        putIfNotBlank(properties, "connectTimeout", dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey()));
        putSocketTimeout(properties, dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey()));
        putIfNotBlank(properties, "characterEncoding", dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey()));
        putIfNotBlank(properties, "connectionTimeZone", dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey()));
        // Keep consistent with the dsc-common-mysql default: YashanDB bit(1) must not be treated as Boolean.
        properties.putIfAbsent("tinyInt1isBit", "false");
        return properties;
    }

    private void putIfNotBlank(Properties properties, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            properties.setProperty(key, value);
        }
    }

    private void putSocketTimeout(Properties properties, String soTimeoutSec) {
        if (StringUtils.isNotBlank(soTimeoutSec) && StringUtils.isNumeric(soTimeoutSec.trim())) {
            properties.setProperty("socketTimeout", Long.toString(Long.parseLong(soTimeoutSec.trim()) * 1000));
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String customUrl = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(customUrl)) {
            return customUrl;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        // The database name may be filled in either the "default schema" or the "default database" field in the
        // UI; both are accepted. Measured: the YashanDB MySQL compatibility mode allows connecting without a
        // database name, so the URL is built the same way when both are empty.
        String database = StringUtils.defaultIfBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey()),
            dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey()));

        String[] hostPort = StringUtils.defaultIfBlank(host, "").split(":");
        if (hostPort.length == 1) {
            return String.format("jdbc:mysql://%s:%s/%s", hostPort[0], EXPECTED_MODE.getDefaultPort(), StringUtils.defaultString(database));
        }
        if (hostPort.length == 2) {
            return String.format("jdbc:mysql://%s:%s/%s", hostPort[0], hostPort[1], StringUtils.defaultString(database));
        }
        throw new IllegalArgumentException("Unsupported YashanDB host format: " + host);
    }
}
