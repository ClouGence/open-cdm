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
package com.clougence.clouddm.ds.postgres.dsconf;

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
 * @author bucketli 2020/11/6 10:23
 */
@Getter
@Setter
@FieldNameConstants
@Serialization(provider = PgSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PgConfig extends DataSourceConfig {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.defaultCatalog, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_DESCRIPTION)
    private String  defaultCatalog;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.defaultSchema, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_DESCRIPTION)
    private String  defaultSchema;
    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.autoCommit, defaultValue = "true", descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESCRIPTION)
    private Boolean autoCommit;
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.clientTimeZone, descKey = ConfigI18nKey.CONFIG_RDB_CLIENT_TIME_ZONE_DESCRIPTION)
    private String  clientTimeZone;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.connectTimeoutMs, defaultValue = "5000", descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESCRIPTION)
    private Long    connectTimeoutMs;
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.soTimeoutSec, defaultValue = "10", descKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_DESCRIPTION)
    private Integer soTimeoutSec;

    public PgConfig(){
        setDataSourceType(DataSourceType.PostgreSQL);
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
        return properties;
    }
}
