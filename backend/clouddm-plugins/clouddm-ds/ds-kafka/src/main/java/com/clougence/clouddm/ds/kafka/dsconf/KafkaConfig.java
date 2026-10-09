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
package com.clougence.clouddm.ds.kafka.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@Serialization(provider = KafkaSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaConfig extends DataSourceConfig {

    @ConfigDef(name = Fields.saslMechanism, defaultValue = "PLAIN", group = DsConfigGroup.GENERAL, labelKey = KafkaConfigI18nKeys.CONFIG_KAFKA_SASL_LABEL, descKey = KafkaConfigI18nKeys.CONFIG_KAFKA_SASL_DESC, activeField = DataSourceConfig.Fields.securityType, activeEquals = "USER_PASSWD", readOnly = false)
    private String saslMechanism    = "PLAIN";
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", group = DsConfigGroup.ADVANCED, labelKey = KafkaConfigI18nKeys.CONFIG_KAFKA_CONNECT_TIMEOUT_LABEL, descKey = KafkaConfigI18nKeys.CONFIG_KAFKA_CONNECT_TIMEOUT_DESC, readOnly = false)
    private int    connectTimeoutMs = 5000;
    @ConfigDef(name = Fields.requestTimeoutMs, defaultValue = "10000", group = DsConfigGroup.ADVANCED, labelKey = KafkaConfigI18nKeys.CONFIG_KAFKA_REQUEST_TIMEOUT_LABEL, descKey = KafkaConfigI18nKeys.CONFIG_KAFKA_REQUEST_TIMEOUT_DESC, readOnly = false)
    private int    requestTimeoutMs = 10000;
    @ConfigDef(name = Fields.apiTimeoutMs, defaultValue = "30000", group = DsConfigGroup.ADVANCED, labelKey = KafkaConfigI18nKeys.CONFIG_KAFKA_API_TIMEOUT_LABEL, descKey = KafkaConfigI18nKeys.CONFIG_KAFKA_API_TIMEOUT_DESC, readOnly = false)
    private int    apiTimeoutMs     = 30000;

    public KafkaConfig(){
        setDataSourceType(DataSourceType.Kafka);
    }

    @Override
    public Properties asDriverProperties() {
        if (Boolean.TRUE.equals(getSshProxyEnabled())) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSH_UNSUPPORTED);
        }
        SecurityType securityType = getSecurityType();
        if (securityType == null) {
            securityType = SecurityType.NONE;
        }
        if (securityType != SecurityType.NONE && securityType != SecurityType.USER_PASSWD) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SECURITY_ERROR);
        }
        SslMode sslMode = getSslMode();
        if (sslMode == null) {
            sslMode = SslMode.DISABLED;
        }
        if (sslMode == SslMode.TRUST) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSL_ERROR);
        }
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty("bootstrap.servers", safeStr(getHost()));
        properties.setProperty("socket.connection.setup.timeout.ms", String.valueOf(connectTimeoutMs));
        properties.setProperty("socket.connection.setup.timeout.max.ms", String.valueOf(connectTimeoutMs));
        properties.setProperty("request.timeout.ms", String.valueOf(requestTimeoutMs));
        properties.setProperty("default.api.timeout.ms", String.valueOf(apiTimeoutMs));
        String protocol = "PLAINTEXT";
        if (sslMode != SslMode.DISABLED) {
            protocol = "SSL";
            properties.setProperty(DsConfigKeys.SSL_CA_FILE.getConfigKey(), safeStr(getSslCaFilePath()));
            properties.setProperty("sslCaFileFormat", safeStr(getSslCaFileFormat()));
            properties.setProperty("sslCaPassword", safeStr(getSslCaPassword()));
            properties.setProperty(DsConfigKeys.SSL_CLIENT_CERT_FILE.getConfigKey(), safeStr(getSslClientCertFilePath()));
            properties.setProperty("sslClientCertFileFormat", safeStr(getSslClientCertFileFormat()));
            properties.setProperty(DsConfigKeys.SSL_CLIENT_KEY_FILE.getConfigKey(), safeStr(getSslClientKeyFilePath()));
            properties.setProperty(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey(), safeStr(getSslClientKeyPassword()));
        }
        if (securityType == SecurityType.USER_PASSWD) {
            protocol = "SASL_" + protocol;
            properties.setProperty("sasl.mechanism", saslMechanism);
            properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(getUserName()));
            properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(getPassword()));
        }
        properties.setProperty("security.protocol", protocol);
        properties.setProperty(DsConfigKeys.SSL_MODE.getConfigKey(), sslMode.name());
        return properties;
    }
}
