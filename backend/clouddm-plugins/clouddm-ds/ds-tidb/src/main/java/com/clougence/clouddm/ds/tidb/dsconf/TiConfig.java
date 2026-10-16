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
package com.clougence.clouddm.ds.tidb.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.ds.tidb.i18n.TiConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.clouddm.sdk.execute.dsconf.capability.ClientTimeZoneExtProperties;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author mode 2020/11/5 20:29
 */
@Getter
@Setter
@FieldNameConstants
@Serialization(provider = TiSqlSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiConfig extends DataSourceConfig implements//
        ClientTimeZoneExtProperties {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(name = Fields.defaultSchema, //
            group = DsConfigGroup.GENERAL, labelKey = TiConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_LABEL, descKey = TiConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_DESC, readOnly = false)
    private String  defaultSchema;
    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(name = ClientTimeZoneExtProperties.CLIENT_TIME_ZONE_FIELD, defaultValue = "Asia/Shanghai", //
            group = DsConfigGroup.OPTIONS, labelKey = TiConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_LABEL, descKey = TiConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_DESC, readOnly = false)
    private String  clientTimeZone;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", //
            group = DsConfigGroup.ADVANCED, labelKey = TiConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = TiConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private Long    connectTimeoutMs;
    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", //
            group = DsConfigGroup.ADVANCED, labelKey = TiConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = TiConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer soTimeoutSec;
    @ConfigDef(name = Fields.connectionCharset, defaultValue = "utf8", //
            group = DsConfigGroup.ADVANCED, labelKey = TiConfigI18nKeys.CONFIG_TIDB_CONN_CHARSET_LABEL, descKey = TiConfigI18nKeys.CONFIG_TIDB_CONN_CHARSET_DESC, readOnly = false)
    private String  connectionCharset;

    public TiConfig(){
        setDataSourceType(DataSourceType.TiDB);
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(this.getDefaultSchema()));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), safeStr(StringUtils.toString(this.getAutoCommit())));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), safeStr(this.getClientTimeZone()));
        properties.setProperty("sslMode", this.tiSslMode());
        properties.setProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey(), safeStr(this.getConnectionCharset()));
        properties.setProperty("useCursorFetch", "false");
        properties.setProperty("allowPublicKeyRetrieval", "true");
        properties.setProperty("allowMultiQueries", "true");
        properties.setProperty("rewriteBatchedStatements", "true");
        properties.setProperty("useServerPrepStmts", "true");
        properties.setProperty("useOldAliasMetadataBehavior", "true");
        return properties;
    }

    private String tiSslMode() {
        if (getSslMode() == null) {
            return "DISABLED";
        }
        return switch (getSslMode()) {
            case TRUST -> "REQUIRED";
            case CA -> "VERIFY_CA";
            case CLIENT_CERT -> "VERIFY_IDENTITY";
            default -> "DISABLED";
        };
    }
}
