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
public class DataSourceConfig implements DeserializeAble {

    // ------------------------------------------------------------------------------------------------------------------------ basic
    @ConfigDef(name = Fields.instanceId, descKey = ConfigI18nKey.CONFIG_DS_INSTANCE_ID_DESCRIPTION)
    private String         instanceId;
    @ConfigDef(name = Fields.dataSourceType, descKey = ConfigI18nKey.CONFIG_DS_TYPE_DESCRIPTION)
    private DataSourceType dataSourceType;
    @ConfigDef(name = Fields.version, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION)
    private String         version;
    @ConfigDef(name = Fields.driverVersion, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION, readOnly = false)
    private String         driverVersion;
    @ConfigDef(name = Fields.securityType, descKey = ConfigI18nKey.CONFIG_DS_SECURITY_TYPE_DESCRIPTION, readOnly = false, valueAdvance = "NONE / ONLY_USER / ONLY_PASSWD / USER_PASSWD / API_KEY / AK_SK")
    private SecurityType   securityType;

    // ------------------------------------------------------------------------------------------------------------------------ SSH/SSL
    @ConfigDef(name = Fields.sshProxyEnabled, defaultValue = "false", valueAdvance = "true or false", descKey = ConfigI18nKey.CONFIG_DS_SSH_PROXY_ENABLED, readOnly = false, group = DsConfigGroup.OPTIONS)
    private Boolean        sshProxyEnabled;
    @ConfigDef(name = Fields.sshConfigId, valueRequire = false, descKey = ConfigI18nKey.CONFIG_DS_SSH_CONFIG_ID, readOnly = false, group = DsConfigGroup.OPTIONS)
    private Long           sshConfigId;
    @ConfigDef(name = Fields.sslMode, defaultValue = "DISABLED", descKey = ConfigI18nKey.CONFIG_DS_SSL_MODE, readOnly = false, group = DsConfigGroup.OPTIONS)
    private SslMode        sslMode;
    @ConfigDef(name = Fields.sslCaData, valueRequire = false, readOnly = false, isSecret = true, lazy = true, group = DsConfigGroup.OPTIONS)
    private String         sslCaData;
    @JsonIgnore
    private String         sslCaFilePath;
    @ConfigDef(name = Fields.sslClientCertData, valueRequire = false, readOnly = false, isSecret = true, lazy = true, group = DsConfigGroup.OPTIONS)
    private String         sslClientCertData;
    @JsonIgnore
    private String         sslClientCertFilePath;
    @ConfigDef(name = Fields.sslClientKeyData, valueRequire = false, readOnly = false, isSecret = true, lazy = true, group = DsConfigGroup.OPTIONS)
    private String         sslClientKeyData;
    @JsonIgnore
    private String         sslClientKeyFilePath;
    @ConfigDef(name = Fields.sslClientKeyPassword, valueRequire = false, descKey = ConfigI18nKey.CONFIG_DS_SSL_CLIENT_KEY_PASSWORD, isSecret = true, readOnly = false, group = DsConfigGroup.OPTIONS)
    private String         sslClientKeyPassword;

    // ------------------------------------------------------------------------------------------------------------------------ default session config
    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", valueRequire = false, descKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_DESCRIPTION, readOnly = false, valueAdvance = "10 - 60", group = DsConfigGroup.OPTIONS)
    private Integer        soTimeoutSec;
    @ConfigDef(name = Fields.maxIdleTimeSec, defaultValue = "300", descKey = ConfigI18nKey.CONFIG_DS_MAX_IDLE_TIME_SEC_DESCRIPTION, readOnly = false, valueAdvance = "value is second.", group = DsConfigGroup.OPTIONS)
    private Integer        maxIdleTimeSec;
    @ConfigDef(name = Fields.readOnly, defaultValue = "false", valueAdvance = "true or false", descKey = ConfigI18nKey.CONFIG_DS_READONLY_DESCRIPTION, readOnly = false)
    private Boolean        readOnly;

    // console
    @ConfigDef(name = Fields.onlineMaxConnections, defaultValue = "100", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONNECTIONS_DESCRIPTION, readOnly = false)
    private Integer        onlineMaxConnections;
    @ConfigDef(name = Fields.onlineMaxQueryTimeoutSec, defaultValue = "30", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION, readOnly = false)
    private Integer        onlineMaxQueryTimeoutSec;
    @ConfigDef(name = Fields.exportMaxConnections, defaultValue = "50", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_DESCRIPTION, readOnly = false)
    private Integer        exportMaxConnections;
    @ConfigDef(name = Fields.exportMaxQueryTimeoutSec, defaultValue = "300", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION, readOnly = false)
    private Integer        exportMaxQueryTimeoutSec;

    // jdbc config
    // ---------------------------------------------------------------------------------------------------

    @ConfigDef(name = Fields.userName, descKey = ConfigI18nKey.CONFIG_RDB_USERNAME_DESCRIPTION, readOnly = false)
    private String         userName;
    @ConfigDef(name = Fields.password, descKey = ConfigI18nKey.CONFIG_RDB_PASSWORD_DESCRIPTION, isSecret = true, readOnly = false)
    private String         password;
    @ConfigDef(name = Fields.host, descKey = ConfigI18nKey.CONFIG_RDB_CONN_HOST_DESCRIPTION, readOnly = false)
    private String         host;
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", valueRequire = false, descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESCRIPTION, readOnly = false, valueAdvance = "2000 - 100000", group = DsConfigGroup.OPTIONS)
    private Long           connectTimeoutMs;
    @ConfigDef(name = Fields.isolation, defaultValue = "DEFAULT", valueAdvance = "DEFAULT/READ_UNCOMMITTED/READ_COMMITTED/REPEATABLE_READ/SERIALIZABLE", descKey = ConfigI18nKey.CONFIG_RDB_ISOLATION_DESCRIPTION, readOnly = false)
    private String         isolation;
    @ConfigDef(name = Fields.autoCommit, defaultValue = "true", valueAdvance = "true or false", descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESCRIPTION, readOnly = false)
    private Boolean        autoCommit;
    @ConfigDef(name = Fields.storePassword, descKey = ConfigI18nKey.CONFIG_RDB_STORE_PASSWORD_DESCRIPTION, isSecret = true, readOnly = false)
    private String         storePassword;

    // ----------------------------------------------------------------------------------------------- config version  UUID,for ssl file update
    @ConfigDef(name = Fields.configVersion, descKey = ConfigI18nKey.CONFIG_RDB_CONFIG_VERSION_DESCRIPTION, defaultValue = "1")
    private Long           configVersion;

    public void setDataSourceType(DataSourceType dataSourceType) {
        if (this.dataSourceType == null) {
            this.dataSourceType = Objects.requireNonNull(dataSourceType, "dataSourceType can not be null.");
            return;
        }

        if (dataSourceType != this.dataSourceType) {
            throw new UnsupportedOperationException("different values can only be initialized once.");
        }
    }

    @Override
    public void deserialize() {
    }

    protected String safeStr(String value) {
        return value == null ? "" : value;
    }

    public Properties asDriverProperties() {
        return new Properties();
    }
}
