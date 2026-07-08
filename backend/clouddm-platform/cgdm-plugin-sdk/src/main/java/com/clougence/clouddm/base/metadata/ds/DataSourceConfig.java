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
package com.clougence.clouddm.base.metadata.ds;

import java.util.Objects;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2020/11/6 18:52
 */
@Getter
@Setter
@FieldNameConstants
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSourceConfig {

    // ------------------------------------------------------------------------------------------------------------------------ SHADOW
    @ConfigDef(name = Fields.instanceId, //
            group = DsConfigGroup.SHADOW, labelKey = ConfigI18nKey.CONFIG_DS_INSTANCE_ID_LABEL, descKey = ConfigI18nKey.CONFIG_DS_INSTANCE_ID_DESC, readOnly = true)
    private String         instanceId;
    @ConfigDef(name = Fields.dataSourceType, //
            group = DsConfigGroup.SHADOW, labelKey = ConfigI18nKey.CONFIG_DS_TYPE_LABEL, descKey = ConfigI18nKey.CONFIG_DS_TYPE_DESC, readOnly = true)
    private DataSourceType dataSourceType;
    @ConfigDef(name = Fields.configVersion, defaultValue = "1", //
            group = DsConfigGroup.SHADOW, labelKey = ConfigI18nKey.CONFIG_RDB_CONFIG_VERSION_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_CONFIG_VERSION_DESC, readOnly = true)
    private Long           configVersion;
    @ConfigDef(name = Fields.version, //
            group = DsConfigGroup.SHADOW, labelKey = ConfigI18nKey.CONFIG_RDB_VERSION_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESC, readOnly = true)
    private String         version;

    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(name = Fields.driverVersion, //
            group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_DRIVER_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_DRIVER_DESC, readOnly = false)
    private String         driverVersion;
    @ConfigDef(name = Fields.host, //
            group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_CONN_HOST_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_CONN_HOST_DESC, readOnly = false)
    private String         host;
    @ConfigDef(name = Fields.securityType, //
            group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_DS_SECURITY_TYPE_LABEL, descKey = ConfigI18nKey.CONFIG_DS_SECURITY_TYPE_DESC, readOnly = false)
    private SecurityType   securityType;
    @ConfigDef(name = Fields.userName, //
            group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_USERNAME_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_USERNAME_DESC, readOnly = false)
    private String         userName;
    @ConfigDef(name = Fields.password, //
            group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_PASSWORD_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_PASSWORD_DESC, isSecret = true, readOnly = false)
    private String         password;

    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(name = Fields.maxIdleTimeSec, defaultValue = "300", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_DS_MAX_IDLE_TIME_SEC_LABEL, descKey = ConfigI18nKey.CONFIG_DS_MAX_IDLE_TIME_SEC_DESC, readOnly = false)
    private Integer        maxIdleTimeSec;
    @ConfigDef(name = Fields.readOnly, defaultValue = "false", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_DS_READONLY_LABEL, descKey = ConfigI18nKey.CONFIG_DS_READONLY_DESC, readOnly = false)
    private Boolean        readOnly;
    @ConfigDef(name = Fields.autoCommit, defaultValue = "true", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESC, readOnly = false)
    private Boolean        autoCommit;
    @ConfigDef(name = Fields.isolation, defaultValue = "DEFAULT", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_RDB_ISOLATION_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_ISOLATION_DESC, readOnly = false)
    private String         isolation;
    @ConfigDef(name = Fields.sqlEngine, defaultValue = "", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_DS_DEFAULT_SQL_ENGINE_LABEL, descKey = ConfigI18nKey.CONFIG_DS_DEFAULT_SQL_ENGINE_DESC, readOnly = false)
    private String         sqlEngine;
    @ConfigDef(name = Fields.onlineMaxConnections, defaultValue = "100", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONNECTIONS_LABEL, descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONNECTIONS_DESC, readOnly = false)
    private Integer        onlineMaxConnections;
    @ConfigDef(name = Fields.exportMaxConnections, defaultValue = "50", //
            group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_LABEL, descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_DESC, readOnly = false)
    private Integer        exportMaxConnections;

    // ------------------------------------------------------------------------------------------------------------------------ SSH/SSL
    @ConfigDef(name = Fields.sshProxyEnabled, defaultValue = "false", //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSH_PROXY_ENABLED_LABEL, descKey = ConfigI18nKey.CONFIG_DS_SSH_PROXY_ENABLED_DESC, readOnly = false)
    private Boolean        sshProxyEnabled;
    @ConfigDef(name = Fields.sshConfigId, //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSH_CONFIG_ID_LABEL, descKey = ConfigI18nKey.CONFIG_DS_SSH_CONFIG_ID_DESC, readOnly = false)
    private Long           sshConfigId;
    @ConfigDef(name = Fields.sslMode, defaultValue = "DISABLED", //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSL_MODE_LABEL, descKey = ConfigI18nKey.CONFIG_DS_SSL_MODE_DESC, readOnly = false)
    private SslMode        sslMode;
    @ConfigDef(name = Fields.sslCaData, //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSL_CA_DATA_LABEL, descKey = ConfigI18nKey.CONFIG_ADD_DS_SSL_CA_DATA_DESC, isSecret = true, lazy = true, readOnly = false)
    private String         sslCaData;
    @JsonIgnore
    private String         sslCaFilePath;
    @JsonIgnore
    private String         sslCaFileFormat;
    @ConfigDef(name = Fields.sslCaPassword, //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSL_CA_PASSWORD_LABEL, descKey = ConfigI18nKey.CONFIG_ADD_DS_SSL_CA_PASSWORD_DESC, isSecret = true, readOnly = false)
    private String         sslCaPassword;
    @ConfigDef(name = Fields.sslClientCertData, //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSL_CLIENT_CERT_DATA_LABEL, descKey = ConfigI18nKey.CONFIG_ADD_DS_SSL_CLIENT_CERT_DATA_DESC, isSecret = true, lazy = true, readOnly = false)
    private String         sslClientCertData;
    @JsonIgnore
    private String         sslClientCertFilePath;
    @JsonIgnore
    private String         sslClientCertFileFormat;
    @ConfigDef(name = Fields.sslClientKeyData, //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSL_CLIENT_KEY_DATA_LABEL, descKey = ConfigI18nKey.CONFIG_ADD_DS_SSL_CLIENT_KEY_DATA_DESC, isSecret = true, lazy = true, readOnly = false)
    private String         sslClientKeyData;
    @JsonIgnore
    private String         sslClientKeyFilePath;
    @JsonIgnore
    private String         sslClientKeyFileFormat;
    @ConfigDef(name = Fields.sslClientKeyPassword, //
            group = DsConfigGroup.SSH_SSL, labelKey = ConfigI18nKey.CONFIG_DS_SSL_CLIENT_KEY_PASSWORD_LABEL, descKey = ConfigI18nKey.CONFIG_ADD_DS_SSL_CLIENT_KEY_PASSWORD_DESC, isSecret = true, readOnly = false)
    private String         sslClientKeyPassword;

    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(name = Fields.onlineMaxQueryTimeoutSec, defaultValue = "30", //
            group = DsConfigGroup.ADVANCED, labelKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_LABEL, descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_DESC, readOnly = false)
    private Integer        onlineMaxQueryTimeoutSec;
    @ConfigDef(name = Fields.exportMaxQueryTimeoutSec, defaultValue = "300", //
            group = DsConfigGroup.ADVANCED, labelKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_LABEL, descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_DESC, readOnly = false)
    private Integer        exportMaxQueryTimeoutSec;

    public void setDataSourceType(DataSourceType dataSourceType) {
        if (this.dataSourceType == null) {
            this.dataSourceType = Objects.requireNonNull(dataSourceType, "dataSourceType can not be null.");
            return;
        }

        if (dataSourceType != this.dataSourceType) {
            throw new UnsupportedOperationException("different values can only be initialized once.");
        }
    }

    protected String safeStr(String value) {
        return value == null ? "" : value;
    }

    public Properties asDriverProperties() {
        return new Properties();
    }
}
