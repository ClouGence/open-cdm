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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.kafka.execute.KafkaClients;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.dsfamily.execute.ssl.DsSslSupport;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;

@Slf4j
public class KafkaDsFactory implements DsFactory<KafkaClients> {

    @Override
    public DsObject<KafkaClients> create(Properties dsConfig) {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys key : DsConfigKeys.values()) {
            props.remove(key.getConfigKey());
        }
        props.remove("sslCaFileFormat");
        props.remove("sslCaPassword");
        props.remove("sslClientCertFileFormat");
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        if (StringUtils.isNotBlank(clientName)) {
            props.setProperty("client.id", clientName);
        }
        try {
            if (props.getProperty("security.protocol", "PLAINTEXT").startsWith("SASL_")) {
                configureSasl(dsConfig, props);
            }
            configureSsl(dsConfig, props);
            return new DsObject<>(dsConfig, new KafkaClients(props), this);
        } catch (IOException | IllegalArgumentException | KafkaException e) {
            String msg = "Create Kafka clients failed, instanceID=" + dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    private void configureSasl(Properties dsConfig, Properties props) {
        String mechanism = dsConfig.getProperty("sasl.mechanism", "PLAIN");
        if (!List.of("PLAIN", "SCRAM-SHA-256", "SCRAM-SHA-512").contains(mechanism)) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SASL_ERROR);
        }
        String user = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        if (StringUtils.isBlank(user) || password == null || password.isEmpty()) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_CREDENTIALS_ERROR);
        }
        String module = "org.apache.kafka.common.security.scram.ScramLoginModule";
        if ("PLAIN".equals(mechanism)) {
            module = "org.apache.kafka.common.security.plain.PlainLoginModule";
        }
        StringBuilder jaas = new StringBuilder(module).append(" required");
        for (Map.Entry<String, String> option : Map.of("username", user, "password", password).entrySet()) {
            String value = option.getValue().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
            jaas.append(' ').append(option.getKey()).append("=\"").append(value).append('"');
        }
        props.setProperty("sasl.jaas.config", jaas.append(';').toString());
    }

    private void configureSsl(Properties dsConfig, Properties props) throws IOException {
        SslMode mode = SslMode.valueOf(dsConfig.getProperty(DsConfigKeys.SSL_MODE.getConfigKey(), "DISABLED"));
        if (mode == SslMode.DISABLED) {
            return;
        }
        if (mode == SslMode.TRUST) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSL_ERROR);
        }
        String caPath = dsConfig.getProperty(DsConfigKeys.SSL_CA_FILE.getConfigKey());
        if (StringUtils.isBlank(caPath)) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSL_FILES_ERROR);
        }
        props.setProperty("ssl.endpoint.identification.algorithm", "https");
        props.setProperty("ssl.truststore.location", caPath);
        if (mode == SslMode.CA || mode == SslMode.CLIENT_CERT) {
            props.setProperty("ssl.truststore.type", "PEM");
        } else {
            props.setProperty("ssl.truststore.type", DsSslSupport.keyStoreType(dsConfig.getProperty("sslCaFileFormat"), new File(caPath), "TrustStore"));
            props.setProperty("ssl.truststore.password", dsConfig.getProperty("sslCaPassword", ""));
        }
        if (mode == SslMode.CA || mode == SslMode.TRUSTSTORE) {
            return;
        }
        String certPath = dsConfig.getProperty(DsConfigKeys.SSL_CLIENT_CERT_FILE.getConfigKey());
        if (StringUtils.isBlank(certPath)) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSL_FILES_ERROR);
        }
        String password = dsConfig.getProperty(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey(), "");
        if (mode == SslMode.KEYSTORE_TRUSTSTORE) {
            props.setProperty("ssl.keystore.type", DsSslSupport.keyStoreType(dsConfig.getProperty("sslClientCertFileFormat"), new File(certPath), "KeyStore"));
            props.setProperty("ssl.keystore.location", certPath);
            props.setProperty("ssl.keystore.password", password);
            props.setProperty("ssl.key.password", password);
        } else {
            String keyPath = dsConfig.getProperty(DsConfigKeys.SSL_CLIENT_KEY_FILE.getConfigKey());
            if (StringUtils.isBlank(keyPath)) {
                throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSL_FILES_ERROR);
            }
            props.setProperty("ssl.keystore.type", "PEM");
            props.setProperty("ssl.keystore.certificate.chain", Files.readString(Path.of(certPath)));
            props.setProperty("ssl.keystore.key", Files.readString(Path.of(keyPath)));
            if (!password.isEmpty()) {
                props.setProperty("ssl.key.password", password);
            }
        }
    }
}
