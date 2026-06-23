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
    @ConfigDef(group = DsConfigGroup.SHADOW, readOnly = true, name = Fields.instanceId, descKey = ConfigI18nKey.CONFIG_DS_INSTANCE_ID_DESCRIPTION)
    private String         instanceId;
    @ConfigDef(group = DsConfigGroup.SHADOW, readOnly = true, name = Fields.dataSourceType, descKey = ConfigI18nKey.CONFIG_DS_TYPE_DESCRIPTION)
    private DataSourceType dataSourceType;
    @ConfigDef(group = DsConfigGroup.SHADOW, readOnly = true, name = Fields.configVersion, descKey = ConfigI18nKey.CONFIG_RDB_CONFIG_VERSION_DESCRIPTION, defaultValue = "1")
    private Long           configVersion;
    @ConfigDef(group = DsConfigGroup.SHADOW, readOnly = true, name = Fields.version, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION)
    private String         version;

    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.driverVersion, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION)
    private String         driverVersion;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.securityType, descKey = ConfigI18nKey.CONFIG_DS_SECURITY_TYPE_DESCRIPTION)
    private SecurityType   securityType;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.userName, descKey = ConfigI18nKey.CONFIG_RDB_USERNAME_DESCRIPTION)
    private String         userName;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.password, descKey = ConfigI18nKey.CONFIG_RDB_PASSWORD_DESCRIPTION, isSecret = true)
    private String         password;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.host, descKey = ConfigI18nKey.CONFIG_RDB_CONN_HOST_DESCRIPTION)
    private String         host;

    // ------------------------------------------------------------------------------------------------------------------------ SSH/SSL
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sshProxyEnabled, defaultValue = "false", descKey = ConfigI18nKey.CONFIG_DS_SSH_PROXY_ENABLED)
    private Boolean        sshProxyEnabled;
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sshConfigId, descKey = ConfigI18nKey.CONFIG_DS_SSH_CONFIG_ID)
    private Long           sshConfigId;
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sslMode, defaultValue = "DISABLED", descKey = ConfigI18nKey.CONFIG_DS_SSL_MODE)
    private SslMode        sslMode;
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sslCaData, isSecret = true, lazy = true)
    private String         sslCaData;
    @JsonIgnore
    private String         sslCaFilePath;
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sslClientCertData, isSecret = true, lazy = true)
    private String         sslClientCertData;
    @JsonIgnore
    private String         sslClientCertFilePath;
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sslClientKeyData, isSecret = true, lazy = true)
    private String         sslClientKeyData;
    @JsonIgnore
    private String         sslClientKeyFilePath;
    @ConfigDef(group = DsConfigGroup.SSH_SSL, readOnly = false, name = Fields.sslClientKeyPassword, descKey = ConfigI18nKey.CONFIG_DS_SSL_CLIENT_KEY_PASSWORD, isSecret = true)
    private String         sslClientKeyPassword;

    // ------------------------------------------------------------------------------------------------------------------------ CONNECT
    @ConfigDef(group = DsConfigGroup.CONNECT, readOnly = false, name = Fields.maxIdleTimeSec, defaultValue = "300", descKey = ConfigI18nKey.CONFIG_DS_MAX_IDLE_TIME_SEC_DESCRIPTION)
    private Integer        maxIdleTimeSec;
    @ConfigDef(group = DsConfigGroup.CONNECT, readOnly = false, name = Fields.readOnly, defaultValue = "false", descKey = ConfigI18nKey.CONFIG_DS_READONLY_DESCRIPTION)
    private Boolean        readOnly;
    @ConfigDef(group = DsConfigGroup.CONNECT, readOnly = false, name = Fields.isolation, defaultValue = "DEFAULT", descKey = ConfigI18nKey.CONFIG_RDB_ISOLATION_DESCRIPTION)
    private String         isolation;

    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.onlineMaxConnections, defaultValue = "100", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONNECTIONS_DESCRIPTION)
    private Integer        onlineMaxConnections;
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.onlineMaxQueryTimeoutSec, defaultValue = "30", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION)
    private Integer        onlineMaxQueryTimeoutSec;
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.exportMaxConnections, defaultValue = "50", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_DESCRIPTION)
    private Integer        exportMaxConnections;
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.exportMaxQueryTimeoutSec, defaultValue = "300", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION)
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
