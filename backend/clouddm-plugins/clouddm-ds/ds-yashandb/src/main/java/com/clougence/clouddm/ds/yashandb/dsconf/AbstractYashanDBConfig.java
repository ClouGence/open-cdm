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
package com.clougence.clouddm.ds.yashandb.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.sdk.execute.dsconf.capability.ClientTimeZoneExtProperties;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * Base class for the YashanDB data source connection configuration.
 * <p>
 * The server-side compatibility mode of YashanDB is fixed at installation time (Oracle compatibility mode is
 * the default; MySQL compatibility mode is installed with {@code --mode mysql}). The <b>connection
 * configuration items</b> of the two modes are identical; the differences are concentrated in the
 * {@code DataSourceType}, the default port, the JDBC driver and the connection URL form (see
 * {@link YashanDBCompatibilityMode}). Therefore the common fields and value handling are lifted into this class.
 * </p>
 *
 * @author open-cdm
 */
@Getter
@Setter
@FieldNameConstants
public abstract class AbstractYashanDBConfig extends DataSourceConfig implements ClientTimeZoneExtProperties {

    @ConfigDef(name = Fields.defaultCatalog, group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_DESC, readOnly = false)
    private String  defaultCatalog;

    @ConfigDef(name = Fields.defaultSchema, group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_DESC, readOnly = false)
    private String  defaultSchema;

    @ConfigDef(name = ClientTimeZoneExtProperties.CLIENT_TIME_ZONE_FIELD, group = DsConfigGroup.OPTIONS, labelKey = ConfigI18nKey.CONFIG_RDB_CLIENT_TIME_ZONE_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_CLIENT_TIME_ZONE_DESC, readOnly = false)
    private String  clientTimeZone;

    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", group = DsConfigGroup.ADVANCED, labelKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private Long    connectTimeoutMs;

    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", group = DsConfigGroup.ADVANCED, labelKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer soTimeoutSec;

    protected AbstractYashanDBConfig(DataSourceType dataSourceType){
        setDataSourceType(dataSourceType);
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(getDefaultCatalog()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(getDefaultSchema()));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), safeStr(StringUtils.toString(getAutoCommit())));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), safeStr(getClientTimeZone()));
        return properties;
    }
}
