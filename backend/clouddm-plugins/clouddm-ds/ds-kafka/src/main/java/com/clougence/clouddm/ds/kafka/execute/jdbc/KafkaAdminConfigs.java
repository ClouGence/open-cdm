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
package com.clougence.clouddm.ds.kafka.execute.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.security.plain.PlainLoginModule;

import com.clougence.clouddm.ds.kafka.dsconf.KafkaConfig;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;

public final class KafkaAdminConfigs {

    private KafkaAdminConfigs(){
    }

    public static Properties fromKafkaConfig(KafkaConfig kafkaConfig) {
        Properties dsConfig = kafkaConfig.asDriverProperties();
        Properties props = new Properties();

        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String securityProtocol = dsConfig.getProperty(KafkaKeys.SECURITY_PROTOCOL);

        if (StringUtils.isNotBlank(username)) {
            props.put(KafkaKeys.USERNAME, username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put(KafkaKeys.PASSWORD, password);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put(KafkaKeys.CONN_TIMEOUT, connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put(KafkaKeys.SO_TIMEOUT, String.valueOf(Long.parseLong(soTimeoutSec) * 1000));
        }
        if (StringUtils.isNotBlank(securityProtocol)) {
            props.put(KafkaKeys.SECURITY_PROTOCOL, securityProtocol);
        }

        String bootstrap = buildBootstrap(dsConfig);
        props.put(KafkaKeys.SERVER, bootstrap);
        props.put(KafkaKeys.BOOTSTRAP_SERVERS, bootstrap);
        return build(props);
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

    public static Properties build(Properties props) {
        String bootstrap = props.getProperty(KafkaKeys.BOOTSTRAP_SERVERS);
        if (StringUtils.isBlank(bootstrap)) {
            bootstrap = props.getProperty(KafkaKeys.SERVER);
        }
        if (StringUtils.isBlank(bootstrap)) {
            throw new IllegalArgumentException("Kafka bootstrap servers is required.");
        }

        String clientName = props.getProperty(KafkaKeys.CLIENT_NAME);
        if (StringUtils.isBlank(clientName)) {
            clientName = KafkaKeys.DEFAULT_CLIENT_NAME;
        }

        int connTimeoutMs = parseInt(props.getProperty(KafkaKeys.CONN_TIMEOUT), 5000);
        int soTimeoutMs = parseInt(props.getProperty(KafkaKeys.SO_TIMEOUT), 30000);

        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        adminProps.put(AdminClientConfig.CLIENT_ID_CONFIG, clientName + "-" + UUID.randomUUID());
        adminProps.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, connTimeoutMs);
        adminProps.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, soTimeoutMs);
        adminProps.put(AdminClientConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, Math.max(soTimeoutMs, 60000));

        String protocol = props.getProperty(KafkaKeys.SECURITY_PROTOCOL);
        if (StringUtils.isBlank(protocol)) {
            protocol = KafkaKeys.DEFAULT_SECURITY_PROTOCOL;
        }
        adminProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, protocol);

        String username = props.getProperty(KafkaKeys.USERNAME);
        String password = props.getProperty(KafkaKeys.PASSWORD);
        if (SecurityProtocol.SASL_PLAINTEXT.name().equalsIgnoreCase(protocol) && StringUtils.isNotBlank(username)) {
            adminProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            adminProps.put(SaslConfigs.SASL_JAAS_CONFIG, PlainLoginModule.class.getName() + " required username=\"" + escapeJaas(username) + "\" password=\"" + escapeJaas(password) + "\";");
        }

        return adminProps;
    }

    public static Properties fromCaseMap(Map<String, String> caseProps) {
        Properties props = new Properties();
        caseProps.forEach((k, v) -> {
            if (v != null) {
                props.setProperty(k, v);
            }
        });
        return build(props);
    }

    private static int parseInt(String value, int defaultValue) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    private static String escapeJaas(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
