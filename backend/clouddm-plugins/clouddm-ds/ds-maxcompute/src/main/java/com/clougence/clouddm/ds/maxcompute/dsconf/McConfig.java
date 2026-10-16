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
package com.clougence.clouddm.ds.maxcompute.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.ds.maxcompute.i18n.McConfigI18nKeys;
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
@Serialization(provider = McSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class McConfig extends DataSourceConfig implements//
        ClientTimeZoneExtProperties {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(name = Fields.sdkEndpoint, //
            group = DsConfigGroup.GENERAL, labelKey = McConfigI18nKeys.CONFIG_MC_SDK_ENDPOINT_LABEL, descKey = McConfigI18nKeys.CONFIG_MC_SDK_ENDPOINT_DESC, readOnly = true)
    private String  sdkEndpoint;
    @ConfigDef(name = Fields.defaultCatalog, //
            group = DsConfigGroup.GENERAL, labelKey = McConfigI18nKeys.CONFIG_MC_PROJECT_LABEL, descKey = McConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private String  defaultCatalog;
    @ConfigDef(name = Fields.defaultSchema, //
            group = DsConfigGroup.GENERAL, labelKey = McConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_LABEL, descKey = McConfigI18nKeys.CONFIG_MC_DEFAULT_SCHEMA_DESC, readOnly = false)
    private String  defaultSchema;
    @ConfigDef(name = Fields.interactiveMode, //
            group = DsConfigGroup.GENERAL, labelKey = McConfigI18nKeys.CONFIG_MC_INTERACTIVE_MODE_LABEL, descKey = McConfigI18nKeys.CONFIG_MC_INTERACTIVE_MODE_DESC, readOnly = false)
    private Boolean interactiveMode;
    @ConfigDef(name = Fields.schemaStyle, defaultValue = "false", //
            group = DsConfigGroup.GENERAL, labelKey = McConfigI18nKeys.CONFIG_MC_SCHEMA_STYLE_LABEL, descKey = McConfigI18nKeys.CONFIG_MC_SCHEMA_STYLE_DESC, readOnly = false)
    private Boolean schemaStyle;
    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(name = ClientTimeZoneExtProperties.CLIENT_TIME_ZONE_FIELD, //
            group = DsConfigGroup.OPTIONS, labelKey = McConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_LABEL, descKey = McConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_DESC, readOnly = false)
    private String  clientTimeZone;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", //
            group = DsConfigGroup.ADVANCED, labelKey = McConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = McConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private Long    connectTimeoutMs;
    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", //
            group = DsConfigGroup.ADVANCED, labelKey = McConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = McConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer soTimeoutSec;

    public McConfig(){
        setDataSourceType(DataSourceType.MaxCompute);
    }

    public Properties asDriverProperties() {

        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(getDefaultCatalog()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(getDefaultSchema()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), safeStr(this.getClientTimeZone()));
        properties.setProperty(DsConfigKeys.ODPS_INTERACTIVE.getConfigKey(), safeStr(StringUtils.toString(getInteractiveMode())));
        properties.setProperty(DsConfigKeys.ODPS_SCHEMA_STYLE.getConfigKey(), safeStr(StringUtils.toString(getSchemaStyle())));
        return properties;
    }
}
