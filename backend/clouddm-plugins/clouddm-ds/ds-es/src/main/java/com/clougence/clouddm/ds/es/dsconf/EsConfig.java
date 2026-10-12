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
package com.clougence.clouddm.ds.es.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
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
@Serialization(provider = EsSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsConfig extends DataSourceConfig {
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", group = DsConfigGroup.ADVANCED, labelKey = EsDsI18nKeys.ES_CONNECT_TIMEOUT_LABEL, descKey = EsDsI18nKeys.ES_CONNECT_TIMEOUT_DESC, readOnly = false)
    private int connectTimeoutMs           = 5000;
    @ConfigDef(name = Fields.socketTimeoutMs, defaultValue = "30000", group = DsConfigGroup.ADVANCED, labelKey = EsDsI18nKeys.ES_SOCKET_TIMEOUT_LABEL, descKey = EsDsI18nKeys.ES_SOCKET_TIMEOUT_DESC, readOnly = false)
    private int socketTimeoutMs            = 30000;
    @ConfigDef(name = Fields.requestTimeoutMs, defaultValue = "30000", group = DsConfigGroup.ADVANCED, labelKey = EsDsI18nKeys.ES_REQUEST_TIMEOUT_LABEL, descKey = EsDsI18nKeys.ES_REQUEST_TIMEOUT_DESC, readOnly = false)
    private int requestTimeoutMs           = 30000;
    @ConfigDef(name = Fields.connectionRequestTimeoutMs, defaultValue = "5000", group = DsConfigGroup.ADVANCED, labelKey = EsDsI18nKeys.ES_POOL_TIMEOUT_LABEL, descKey = EsDsI18nKeys.ES_POOL_TIMEOUT_DESC, readOnly = false)
    private int connectionRequestTimeoutMs = 5000;
    @ConfigDef(name = Fields.maxConnections, defaultValue = "20", group = DsConfigGroup.ADVANCED, labelKey = EsDsI18nKeys.ES_MAX_CONNECTIONS_LABEL, descKey = EsDsI18nKeys.ES_MAX_CONNECTIONS_DESC, readOnly = false)
    private int maxConnections             = 20;
    @ConfigDef(name = Fields.maxConnectionsPerRoute, defaultValue = "10", group = DsConfigGroup.ADVANCED, labelKey = EsDsI18nKeys.ES_MAX_PER_ROUTE_LABEL, descKey = EsDsI18nKeys.ES_MAX_PER_ROUTE_DESC, readOnly = false)
    private int maxConnectionsPerRoute     = 10;

    public EsConfig(){
        setDataSourceType(DataSourceType.ElasticSearch);
    }

    public void validate() {
        if (Boolean.TRUE.equals(getSshProxyEnabled())) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_SSH_UNSUPPORTED);
        }
        var endpoints = EsEndpoints.parse(getHost());
        validateLimits();
        if (getSslMode() == SslMode.TRUST) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_TLS_INVALID);
        }
        if (getSslMode() != null && getSslMode() != SslMode.DISABLED && !"https".equals(endpoints.get(0).getScheme())) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_TLS_HTTPS_REQUIRED);
        }
        SecurityType security = getSecurityType();
        if (security == null || security == SecurityType.NONE) {
            return;
        }
        if (security != SecurityType.USER_PASSWD && security != SecurityType.API_KEY) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_AUTH_INVALID);
        }
        if (getPassword() == null || getPassword().isEmpty()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_AUTH_INVALID);
        }
        if (security == SecurityType.USER_PASSWD && (getUserName() == null || getUserName().isBlank() || getUserName().contains(":"))) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_AUTH_INVALID);
        }
        // API Key uses the encoded value returned by ES, stored in the existing secret password field.
        if (security == SecurityType.API_KEY && !getPassword().matches("[A-Za-z0-9+/]+={0,2}")) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_AUTH_INVALID);
        }
    }

    public void validateLimits() {
        if (connectTimeoutMs <= 0 || socketTimeoutMs <= 0 || requestTimeoutMs <= 0 || connectionRequestTimeoutMs <= 0 || maxConnections <= 0 || maxConnectionsPerRoute <= 0
            || maxConnectionsPerRoute > maxConnections) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_LIMITS_INVALID);
        }
    }

    @Override
    public Properties asDriverProperties() {
        validate();
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), getHost());
        SecurityType security = getSecurityType();
        if (security == null) {
            security = SecurityType.NONE;
        }
        properties.setProperty(DataSourceConfig.Fields.securityType, security.name());
        if (security == SecurityType.USER_PASSWD) {
            properties.setProperty(DsConfigKeys.USER.getConfigKey(), getUserName());
        }
        if (security != SecurityType.NONE) {
            properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), getPassword());
        }
        SslMode ssl = getSslMode();
        if (ssl == null) {
            ssl = SslMode.DISABLED;
        }
        properties.setProperty(DsConfigKeys.SSL_MODE.getConfigKey(), ssl.name());
        properties.setProperty(DsConfigKeys.SSL_CA_FILE.getConfigKey(), safeStr(getSslCaFilePath()));
        properties.setProperty(DataSourceConfig.Fields.sslCaFileFormat, safeStr(getSslCaFileFormat()));
        properties.setProperty(DataSourceConfig.Fields.sslCaPassword, safeStr(getSslCaPassword()));
        properties.setProperty(DsConfigKeys.SSL_CLIENT_CERT_FILE.getConfigKey(), safeStr(getSslClientCertFilePath()));
        properties.setProperty(DataSourceConfig.Fields.sslClientCertFileFormat, safeStr(getSslClientCertFileFormat()));
        properties.setProperty(DsConfigKeys.SSL_CLIENT_KEY_FILE.getConfigKey(), safeStr(getSslClientKeyFilePath()));
        properties.setProperty(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey(), safeStr(getSslClientKeyPassword()));
        properties.setProperty(Fields.connectTimeoutMs, String.valueOf(connectTimeoutMs));
        properties.setProperty(Fields.socketTimeoutMs, String.valueOf(socketTimeoutMs));
        properties.setProperty(Fields.requestTimeoutMs, String.valueOf(requestTimeoutMs));
        properties.setProperty(Fields.connectionRequestTimeoutMs, String.valueOf(connectionRequestTimeoutMs));
        properties.setProperty(Fields.maxConnections, String.valueOf(maxConnections));
        properties.setProperty(Fields.maxConnectionsPerRoute, String.valueOf(maxConnectionsPerRoute));
        return properties;
    }
}
