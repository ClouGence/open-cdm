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
package com.clougence.clouddm.ds.goldendb.execute.dsfactory;

import java.sql.*;
import java.util.Properties;

import com.clougence.clouddm.ds.goldendb.dsconf.GoldenDBCompatibilityMode;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoldenDBDsFactory implements DsFactory<Connection> {

    private static final String DRIVER_CLASS = "com.goldendb.jdbc.Driver";

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties properties = driverProperties(dsConfig);
        String instanceId = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String jdbcUrl = buildJdbcUrl(dsConfig);
        GoldenDBCompatibilityMode expectedMode = expectedMode(dsConfig);

        Connection connection = null;
        try {
            Class<?> driverClass = GoldenDBDsFactory.class.getClassLoader().loadClass(DRIVER_CLASS);
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            connection = driver.connect(jdbcUrl, properties);
            if (connection == null) {
                throw new SQLException("GoldenDB JDBC driver rejected the connection URL.");
            }
            validateCompatibilityMode(connection, expectedMode);

            String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
            if (StringUtils.isNotBlank(autoCommit)) {
                connection.setAutoCommit(StringUtils.equalsIgnoreCase("true", autoCommit));
            }
            return new DsObject<>(dsConfig, connection, this);
        } catch (SQLException e) {
            closeConnection(connection);
            log.error("Create GoldenDB connection failed, instanceId={}", instanceId, e);
            throw e;
        } catch (Exception e) {
            closeConnection(connection);
            String msg = "Load GoldenDB JDBC driver failed.";
            log.error(msg + " instanceId={}", instanceId, e);
            throw new SQLException(msg, e);
        }
    }

    private GoldenDBCompatibilityMode expectedMode(Properties dsConfig) throws SQLException {
        String mode = dsConfig.getProperty(GoldenDBCompatibilityMode.EXPECTED_MODE_PROPERTY, GoldenDBCompatibilityMode.MYSQL.getServerMode());
        try {
            return GoldenDBCompatibilityMode.fromServerMode(mode);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    private void validateCompatibilityMode(Connection connection, GoldenDBCompatibilityMode expectedMode) throws SQLException {
        String sqlMode;
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT @@SESSION.sql_mode")) {
            sqlMode = resultSet.next() ? resultSet.getString(1) : "";
        }
        GoldenDBCompatibilityMode actualMode = GoldenDBCompatibilityMode.MYSQL;
        for (String mode : StringUtils.defaultString(sqlMode).split(",")) {
            if (StringUtils.equalsIgnoreCase("ORA_COMPATIBLE_MODE", mode.trim())) {
                actualMode = GoldenDBCompatibilityMode.ORACLE;
                break;
            }
        }
        if (actualMode != expectedMode) {
            throw new SQLException("GoldenDB compatibility mode mismatch, expected: " + expectedMode.getServerMode() + ", actual: " + actualMode.getServerMode());
        }
    }

    private void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("Close failed GoldenDB connection: {}", e.getMessage());
        }
    }

    private Properties driverProperties(Properties dsConfig) {
        Properties properties = new Properties();
        properties.putAll(dsConfig);
        for (DsConfigKeys configKey : DsConfigKeys.values()) {
            properties.remove(configKey.getConfigKey());
        }
        properties.remove(GoldenDBCompatibilityMode.EXPECTED_MODE_PROPERTY);
        properties.entrySet().removeIf(entry -> entry.getValue() == null || StringUtils.isBlank(String.valueOf(entry.getValue())));

        putIfNotBlank(properties, "user", dsConfig.getProperty(DsConfigKeys.USER.getConfigKey()));
        putIfNotBlank(properties, "password", dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey()));
        putIfNotBlank(properties, "connectTimeout", dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey()));
        putSocketTimeout(properties, dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey()));
        putIfNotBlank(properties, "characterEncoding", dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey()));
        return properties;
    }

    private void putIfNotBlank(Properties properties, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            properties.setProperty(key, value);
        }
    }

    private void putSocketTimeout(Properties properties, String soTimeoutSec) {
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            properties.setProperty("socketTimeout", Long.toString(Long.parseLong(soTimeoutSec) * 1000));
        }
    }

    private String buildJdbcUrl(Properties dsConfig) {
        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String schema = StringUtils.defaultIfBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey()), "");
        String[] hostPort = StringUtils.defaultIfBlank(host, "").split(":");
        if (hostPort.length != 2 || StringUtils.isBlank(hostPort[0]) || StringUtils.isBlank(hostPort[1])) {
            throw new IllegalArgumentException("GoldenDB host must include an explicit port.");
        }
        return String.format("jdbc:goldendb://%s:%s/%s", hostPort[0], hostPort[1], schema);
    }
}
