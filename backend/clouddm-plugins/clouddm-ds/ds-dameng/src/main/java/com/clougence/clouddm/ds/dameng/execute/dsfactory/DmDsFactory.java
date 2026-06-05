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
package com.clougence.clouddm.ds.dameng.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://eco.dameng.com/document/dm/zh-cn/pm/jdbc-rogramming-guide.html
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class DmDsFactory implements DsFactory<Connection> {

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
        String defaultDatabase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
        String sslFilePath = dsConfig.getProperty(DsConfigKeys.DM_SSL_FILE_PATH.getConfigKey());
        String sslPassword = dsConfig.getProperty(DsConfigKeys.DM_SSL_PASSWORD.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put("connectTimeout", connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("socketTimeout", Long.parseLong(soTimeoutSec) * 1000);
        }
        String useSchema = null;
        if (StringUtils.isNotBlank(defaultDatabase)) {
            useSchema = defaultDatabase;
        } else if (StringUtils.isNotBlank(defaultSchema)) {
            useSchema = defaultSchema;
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.put("appName", clientName);
        }
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put("localTimezone", clientTimeZone);
        }
        if (StringUtils.isNotBlank(sslFilePath)) {
            props.put("sslFilesPath", sslFilePath);
            if (StringUtils.isNotBlank(sslPassword)) {
                props.put("sslPassword", sslPassword);
            }
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection connect = new dm.jdbc.driver.DmDriver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(useSchema)) {
                try (Statement s = connect.createStatement()) {
                    s.executeUpdate("SET SCHEMA \"" + useSchema.replace("\"", "\"\"") + "\"");
                }
            }

            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    connect.setAutoCommit(false);
                } else {
                    connect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, connect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(DM DataBase)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return String.format("jdbc:dm://%s:5236", ipPort[0]);
        } else if (ipPort.length == 2) {
            return String.format("jdbc:dm://%s:%s", ipPort[0], ipPort[1]);
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }
}
