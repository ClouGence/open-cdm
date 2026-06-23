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
package com.clougence.clouddm.ds.polardb.dsconf.porx;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2020/11/5 20:29
 */
@Getter
@Setter
@FieldNameConstants
@Serialization(provider = PorXSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PorXConfig extends DataSourceConfig {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.defaultSchema, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_DESCRIPTION)
    private String  defaultSchema;
    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.autoCommit, defaultValue = "true", descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESCRIPTION)
    private Boolean autoCommit;
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.clientTimeZone, defaultValue = "Asia/Shanghai", descKey = ConfigI18nKey.CONFIG_RDB_CLIENT_TIME_ZONE_DESCRIPTION)
    private String  clientTimeZone;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.connectTimeoutMs, defaultValue = "5000", descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESCRIPTION)
    private Long    connectTimeoutMs;
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.soTimeoutSec, defaultValue = "10", descKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_DESCRIPTION)
    private Integer soTimeoutSec;
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.connectionCharset, defaultValue = "utf8", descKey = ConfigI18nKey.CONFIG_POLARDBX_CONN_CHARSET_DESCRIPTION)
    private String  connectionCharset;
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.useCursorFetch, descKey = ConfigI18nKey.CONFIG_POLARDBX_CONN_USE_CURSOR_FETCH)
    private Boolean useCursorFetch;

    public PorXConfig(){
        setDataSourceType(DataSourceType.PolarDbX);
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
        properties.setProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey(), safeStr(this.getConnectionCharset()));
        properties.setProperty("useCursorFetch", safeStr(StringUtils.toString(this.getUseCursorFetch())));
        properties.setProperty("allowPublicKeyRetrieval", "true");
        properties.setProperty("allowMultiQueries", "true");
        properties.setProperty("rewriteBatchedStatements", "true");
        properties.setProperty("useServerPrepStmts", "true");
        properties.setProperty("useOldAliasMetadataBehavior", "true");
        return properties;
    }
}
