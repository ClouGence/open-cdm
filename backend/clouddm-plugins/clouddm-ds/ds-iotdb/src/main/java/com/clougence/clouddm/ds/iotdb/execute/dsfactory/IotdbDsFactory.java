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

package com.clougence.clouddm.ds.iotdb.execute.dsfactory;

import java.net.URI;
import java.sql.*;
import java.util.Properties;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbConfig;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbConfigSpi;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IotdbDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) {
        String jdbcUrl = buildJdbcUrl(dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey(), ""));
        IotdbSqlDialect dialect = IotdbSqlDialect.parse(dsConfig.getProperty(IotdbSqlDialect.DRIVER_PROPERTY));
        String database = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        if (dialect == IotdbSqlDialect.TREE && StringUtils.isNotBlank(database)) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_TABLE_DATABASE_ONLY);
        }

        Properties properties = new Properties();
        properties.setProperty("user", dsConfig.getProperty(DsConfigKeys.USER.getConfigKey(), ""));
        properties.setProperty("password", dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey(), ""));
        int networkTimeout = IotdbConfig.resolveNetworkTimeoutMs(dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey()));
        properties.setProperty("network_timeout", Integer.toString(networkTimeout));
        if (dialect == IotdbSqlDialect.TABLE) {
            properties.setProperty(IotdbSqlDialect.DRIVER_PROPERTY, "table");
        }

        Connection connection = null;
        try {
            Class<?> driverClass = IotdbDsFactory.class.getClassLoader().loadClass("org.apache.iotdb.jdbc.IoTDBDriver");
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            connection = driver.connect(jdbcUrl, properties);
            if (connection == null) {
                throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_ENDPOINT_INVALID);
            }
            if (dialect == IotdbSqlDialect.TABLE) {
                initTableSession(connection, database);
            }
            return new DsObject<>(dsConfig, connection, this);
        } catch (ReflectiveOperationException | SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeError) {
                    e.addSuppressed(closeError);
                }
            }
            String msg = "Open IoTDB JDBC connection failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, IotdbI18nKeys.IOTDB_CONNECTION_FAILED);
        }
    }

    private void initTableSession(Connection connection, String database) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Older drivers can ignore the dialect property and open a Tree session.
            try (ResultSet result = statement.executeQuery("SHOW CURRENT_SQL_DIALECT")) {
                if (!result.next() || !IotdbSqlDialect.TABLE.name().equalsIgnoreCase(result.getString(1))) {
                    throw new SQLException("IoTDB did not open a Table session");
                }
            }
            if (StringUtils.isNotBlank(database)) {
                statement.execute("USE \"" + database.replace("\"", "\"\"") + "\"");
            }
        }
    }

    private String buildJdbcUrl(String host) {
        URI endpoint;
        try {
            endpoint = URI.create("iotdb://" + host.trim());
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_ENDPOINT_INVALID);
        }
        if (endpoint.getHost() == null || endpoint.getRawUserInfo() != null || !endpoint.getRawPath().isEmpty() || endpoint.getRawQuery() != null
            || endpoint.getRawFragment() != null || endpoint.getPort() == 0 || endpoint.getPort() > 65535) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_ENDPOINT_INVALID);
        }

        String port = IotdbConfigSpi.DEFAULT_PORT;
        if (endpoint.getPort() != -1) {
            port = Integer.toString(endpoint.getPort());
        }
        return "jdbc:iotdb://" + endpoint.getHost() + ":" + port + "/";
    }
}
