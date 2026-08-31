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
package com.clougence.clouddm.ds.cloudberry.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.ds.cloudberry.i18n.CbConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.clouddm.sdk.execute.dsconf.capability.ClientTimeZoneExtProperties;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@Serialization(provider = CbSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CbConfig extends DataSourceConfig implements//
        ClientTimeZoneExtProperties {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(name = Fields.defaultCatalog, //
            group = DsConfigGroup.GENERAL, labelKey = CbConfigI18nKeys.CONFIG_RDB_DEFAULT_DB_LABEL, descKey = CbConfigI18nKeys.CONFIG_RDB_DEFAULT_DB_DESC, readOnly = false)
    private String  defaultCatalog;
    @ConfigDef(name = Fields.defaultSchema, //
            group = DsConfigGroup.GENERAL, labelKey = CbConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_LABEL, descKey = CbConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_DESC, readOnly = false)
    private String  defaultSchema;
    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(name = ClientTimeZoneExtProperties.CLIENT_TIME_ZONE_FIELD, //
            group = DsConfigGroup.OPTIONS, labelKey = CbConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_LABEL, descKey = CbConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_DESC, readOnly = false)
    private String  clientTimeZone;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", //
            group = DsConfigGroup.ADVANCED, labelKey = CbConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = CbConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private Long    connectTimeoutMs;
    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", //
            group = DsConfigGroup.ADVANCED, labelKey = CbConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = CbConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer soTimeoutSec;

    public CbConfig(){
        setDataSourceType(DataSourceType.Cloudberry);
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(this.getDefaultCatalog()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(this.getDefaultSchema()));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), safeStr(StringUtils.toString(this.getAutoCommit())));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), safeStr(this.getClientTimeZone()));
        properties.setProperty("sslmode", this.pgSslMode());
        if (StringUtils.isNotBlank(this.getSslCaFilePath())) {
            properties.setProperty("sslrootcert", this.getSslCaFilePath());
        }
        if (StringUtils.isNotBlank(this.getSslClientCertFilePath())) {
            properties.setProperty("sslcert", this.getSslClientCertFilePath());
        }
        if (StringUtils.isNotBlank(this.getSslClientKeyFilePath())) {
            properties.setProperty("sslkey", this.getSslClientKeyFilePath());
        }
        if (StringUtils.isNotBlank(this.getSslClientKeyPassword())) {
            properties.setProperty("sslpassword", this.getSslClientKeyPassword());
        }
        return properties;
    }

    private String pgSslMode() {
        if (this.getSslMode() == null) {
            return "disable";
        }
        switch (this.getSslMode()) {
            case TRUST:
                return "require";
            case CA:
                return "verify-ca";
            case CLIENT_CERT:
                return "verify-full";
            default:
                return "disable";
        }
    }
}
