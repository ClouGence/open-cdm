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
package com.clougence.clouddm.ds.sqlserver.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import lombok.extern.slf4j.Slf4j;

/**
 * https://learn.microsoft.com/en-us/sql/connect/jdbc/setting-the-connection-properties?view=sql-server-ver16
 * https://docs.microsoft.com/en-us/answers/questions/714335/how-to-resolve-34unable-to-find-valid-certificatio.html
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class JdbcSqlServerDsFactory implements DsFactory<Connection> {

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

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        if (StringUtils.isNotBlank(loginTimeoutMs)) {
            int loginTimeoutSec = (int) (Long.parseLong(loginTimeoutMs) / 1000);
            props.put("loginTimeout", String.valueOf(loginTimeoutSec));
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("socketTimeout", String.valueOf(Long.parseLong(soTimeoutSec) * 1000));
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.put("applicationName", clientName);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection msConnect = createConnection(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    msConnect.setAutoCommit(false);
                } else {
                    msConnect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, msConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(SqlServer)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage(), e);
            throw e;
        }
    }

    protected String safeString(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String defaultDataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());

        if (StringUtils.isBlank(defaultDataBase)) {
            defaultDataBase = "master";
        }

        String dbName = "databaseName=" + safeString(defaultDataBase);

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return String.format("jdbc:sqlserver://%s:1433;" + dbName, ipPort[0]);
        } else if (ipPort.length == 2) {
            return String.format("jdbc:sqlserver://%s:%s;" + dbName, ipPort[0], ipPort[1]);
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

    protected Connection createConnection(String jdbcUrl, Properties dsConfig) throws SQLServerException {
        return new com.microsoft.sqlserver.jdbc.SQLServerDriver().connect(jdbcUrl, dsConfig);
    }
}
