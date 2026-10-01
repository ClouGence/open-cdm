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
package com.clougence.clouddm.ds.hana.execute.dsfactory;

import java.net.URI;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.hana.i18n.HanaConfigI18nKeys;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;
import com.sap.db.jdbc.ConnectionProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HanaDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws Exception {
        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        Connection connection = null;
        try {
            String jdbcUrl = buildJdbcUrl(dsConfig);
            Properties props = connectionProperties(dsConfig);
            HanaSslProperties.apply(dsConfig, props);
            log.info("Create HANA connection, instanceId={}", id);
            Class<?> driverClass = HanaDsFactory.class.getClassLoader().loadClass("com.sap.db.jdbc.Driver");
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            connection = driver.connect(jdbcUrl, props);
            if (connection == null) {
                throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_URL_ERROR);
            }

            String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
            if (StringUtils.isNotBlank(autoCommit)) {
                connection.setAutoCommit(Boolean.parseBoolean(autoCommit));
            }

            return new DsObject<>(dsConfig, connection, this);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception closeError) {
                    e.addSuppressed(closeError);
                }
            }

            log.error("Create HANA connection failed, instanceId=" + id, e);
            if (e instanceof ThirdPartyApiException apiException) {
                throw apiException;
            }

            throw ThirdPartyApiException.as().with(e);
        }
    }

    private Properties connectionProperties(Properties dsConfig) {
        Properties props = new Properties();
        props.putAll(dsConfig);
        props.remove(DataSourceConfig.Fields.sshProxyEnabled);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String database = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String schema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.setProperty(ConnectionProperty.USER.getName(), username);
        }

        if (password != null) {
            props.setProperty(ConnectionProperty.PASSWD.getName(), password);
        }

        if (StringUtils.isNotBlank(database)) {
            props.setProperty(ConnectionProperty.DATABASE_NAME.getName(), database.trim());
        }

        if (StringUtils.isNotBlank(schema)) {
            props.setProperty(ConnectionProperty.CURRENT_SCHEMA.getName(), HanaDialect.INSTANCE.fmtName(true, schema));
        }

        if (StringUtils.isNotBlank(autoCommit)) {
            props.setProperty(ConnectionProperty.AUTO_COMMIT.getName(), autoCommit);
        }

        if (StringUtils.isNotBlank(clientName)) {
            props.setProperty(ConnectionProperty.APPLICATION.getName(), clientName);
        }

        // ngdbc 2.22/2.28 consume connectTimeout in milliseconds, like Socket.connect.
        props.setProperty(ConnectionProperty.CONNECT_TIMEOUT.getName(), timeoutMillis(dsConfig, DsConfigKeys.CONNECT_TIMEOUT_MS, 5000, 1));
        props.setProperty(ConnectionProperty.COMMUNICATION_TIMEOUT.getName(), timeoutMillis(dsConfig, DsConfigKeys.SO_TIMEOUT_SEC, 10, 1000));
        return props;
    }

    private String timeoutMillis(Properties dsConfig, DsConfigKeys key, long defaultValue, int multiplier) {
        String value = dsConfig.getProperty(key.getConfigKey());
        long timeout = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            try {
                timeout = Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TIMEOUT_ERROR);
            }
        }

        if (timeout < 0 || timeout > Integer.MAX_VALUE / multiplier) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_TIMEOUT_ERROR);
        }

        return Long.toString(timeout * multiplier);
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcUrl = StringUtils.trimToNull(dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey()));
        if (jdbcUrl != null && Boolean.parseBoolean(dsConfig.getProperty(DataSourceConfig.Fields.sshProxyEnabled))) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_URL_SSH_ERROR);
        }

        if (jdbcUrl == null) {
            String host = StringUtils.trimToNull(dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey()));
            if (host == null) {
                throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_URL_ERROR);
            }
            jdbcUrl = "jdbc:sap://" + host;
        }

        // Keep credentials and options in Properties so errors cannot echo a credential-bearing URL.
        URI endpoint;
        try {
            if (!jdbcUrl.startsWith("jdbc:sap://")) {
                throw new IllegalArgumentException();
            }
            endpoint = URI.create(jdbcUrl.substring(5));
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_URL_ERROR);
        }

        if (endpoint.getHost() == null || endpoint.getUserInfo() != null || endpoint.getRawQuery() != null || endpoint.getRawFragment() != null
            || !(endpoint.getPath().isEmpty() || endpoint.getPath().equals("/")) || endpoint.getPort() == 0 || endpoint.getPort() > 65535) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_URL_ERROR);
        }

        int port = endpoint.getPort();
        if (port == -1) {
            port = 30015;
        }

        return "jdbc:sap://" + endpoint.getHost() + ":" + port + "/";
    }
}
