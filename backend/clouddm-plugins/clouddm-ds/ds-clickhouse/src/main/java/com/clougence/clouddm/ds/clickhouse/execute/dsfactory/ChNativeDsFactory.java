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
package com.clougence.clouddm.ds.clickhouse.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;
import com.github.housepower.exception.ClickHouseSQLException;

import lombok.extern.slf4j.Slf4j;

/**
 * https://github.com/housepower/ClickHouse-Native-JDBC/blob/master/clickhouse-native-jdbc/src/main/java/com/github/housepower/settings/ClickHouseConfig.java
 * @author Ekko
 * @date 2023/4/6 19:23
*/
@Slf4j
public class ChNativeDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String loginTimeoutMs = dsConfig.getProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey());
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        String chSessionTimeoutMs = dsConfig.getProperty(DsConfigKeys.CH_SESSION_TIMEOUT_MS.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.setProperty("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.setProperty("password", password);
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.setProperty("database", defaultSchema);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.setProperty("connectTimeout", connTimeoutMs);
        }
        if (StringUtils.isNotBlank(clientEncoding)) {
            props.setProperty("charset", clientEncoding);
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.setProperty("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection connect = new com.github.housepower.jdbc.ClickHouseDriver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    connect.setAutoCommit(false);
                } else {
                    connect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, connect, this);
        } catch (SQLException e) {
            SQLException ex = e;
            if (ex.getErrorCode() == 516) {
                ex = new ClickHouseSQLException(516, "Authentication failed: password is incorrect or there is no user with such name.");
            }
            log.error("create connection instanceID(ClickHouse)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + ex.getMessage());
            throw ex;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String defaultDataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return String.format("jdbc:clickhouse://%s:9000/%s", ipPort[0], safeString(defaultDataBase));
        } else if (ipPort.length == 2) {
            if (StringUtils.isBlank(defaultDataBase)) {
                return String.format("jdbc:clickhouse://%s:%s", ipPort[0], ipPort[1]);
            } else {
                return String.format("jdbc:clickhouse://%s:%s/%s", ipPort[0], ipPort[1], safeString(defaultDataBase));
            }
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

    private String safeString(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }
}
