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
package com.clougence.clouddm.ds.kafka.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaKeys;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaJdbcDsFactory implements DsFactory<Connection> {

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
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String driverVersion = dsConfig.getProperty(DsConfigKeys.DRIVER_VERSION.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put(KafkaKeys.USERNAME, username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put(KafkaKeys.PASSWORD, password);
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.put(KafkaKeys.CLIENT_NAME, clientName.replace(" ", "-"));
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.put(KafkaKeys.DATABASE, defaultSchema);
        } else {
            props.put(KafkaKeys.DATABASE, KafkaKeys.DEFAULT_SCHEMA);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put(KafkaKeys.CONN_TIMEOUT, connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put(KafkaKeys.SO_TIMEOUT, String.valueOf(Long.parseLong(soTimeoutSec) * 1000));
        }
        if (StringUtils.isNotBlank(driverVersion)) {
            props.put(KafkaKeys.DRIVER_VERSION, driverVersion);
        }

        String bootstrap = buildBootstrap(dsConfig);
        props.put(KafkaKeys.SERVER, bootstrap);
        props.put(KafkaKeys.BOOTSTRAP_SERVERS, bootstrap);

        String jdbcUrl = KafkaKeys.START_URL + "//" + bootstrap;
        try {
            Connection connection = new JdbcDriver().connect(jdbcUrl, props);
            return new DsObject<>(dsConfig, connection, this);
        } catch (Exception e) {
            log.error("create Kafka connection failed, instanceId=" + id + ", bootstrap=" + bootstrap, e);
            throw e;
        }
    }

    private static String buildBootstrap(Properties dsConfig) {
        String customUrl = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(customUrl)) {
            if (customUrl.startsWith(KafkaKeys.START_URL)) {
                String rest = customUrl.substring(KafkaKeys.START_URL.length());
                if (rest.startsWith("//")) {
                    rest = rest.substring(2);
                }
                return rest;
            }
            return customUrl;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("Kafka host/bootstrap is required.");
        }

        String[] parts = host.split(",");
        List<String> servers = new ArrayList<>();
        for (String part : parts) {
            String item = part.trim();
            if (StringUtils.isBlank(item)) {
                continue;
            }
            if (item.contains(":")) {
                servers.add(item);
            } else {
                servers.add(item + ":" + KafkaKeys.DEFAULT_PORT);
            }
        }
        if (servers.isEmpty()) {
            throw new IllegalArgumentException("unsupported Kafka host format:" + host);
        }
        return StringUtils.join(servers, ",");
    }
}
