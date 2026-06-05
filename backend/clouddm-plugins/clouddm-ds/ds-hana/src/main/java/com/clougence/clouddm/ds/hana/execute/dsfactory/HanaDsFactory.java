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

import java.sql.Connection;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;
import com.sap.db.jdbc.ConnectionProperty;
import com.sap.db.jdbc.Driver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HanaDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws Exception {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put(ConnectionProperty.USER.getName(), username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put(ConnectionProperty.PASSWD.getName(), password);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put(ConnectionProperty.CONNECT_TIMEOUT.getName(), Long.parseLong(connTimeoutMs));
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put(ConnectionProperty.COMMUNICATION_TIMEOUT.getName(), Long.parseLong(soTimeoutSec) * 1000);
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.put(ConnectionProperty.CURRENT_SCHEMA.getName(), defaultSchema);
        }
        if (StringUtils.isNotBlank(autoCommit)) {
            props.put(ConnectionProperty.AUTO_COMMIT.getName(), autoCommit);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            log.info("[HanaDsFactory] Create new connection, jdbcUrl:{}, username:{}, connTimeoutMs:{}, soTimeoutSec:{}, defaultSchema:{}, autoCommit:{}", jdbcUrl, username, connTimeoutMs, soTimeoutSec, defaultSchema, autoCommit);
            Class<?> driverClass = HanaDsFactory.class.getClassLoader().loadClass("com.sap.db.jdbc.Driver");
            Connection myConnect = ((Driver) driverClass.newInstance()).connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                myConnect.setAutoCommit(StringUtils.equalsIgnoreCase("true", autoCommit));
            }

            return new DsObject<>(dsConfig, myConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(Hana)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String defaultDb = "";
        if (StringUtils.isNotBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey()))) {
            defaultDb = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        }

        String[] ipPort = host.split(":");
        String url;
        if (ipPort.length == 1) {
            url = String.format("jdbc:sap://%s:30015/?", ipPort[0]);
        } else if (ipPort.length == 2) {
            url = String.format("jdbc:sap://%s:%s/?", ipPort[0], ipPort[1]);
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
        if (StringUtils.isBlank(defaultDb)) {
            return url;
        }
        return url + "databaseName=" + StringUtils.trim(defaultDb);
    }
}
