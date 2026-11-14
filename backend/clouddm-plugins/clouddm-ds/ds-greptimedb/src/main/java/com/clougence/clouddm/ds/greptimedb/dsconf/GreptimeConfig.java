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

package com.clougence.clouddm.ds.greptimedb.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;

import lombok.Getter;
import lombok.Setter;

@Serialization(provider = GreptimeSerializationSpi.PROVIDER_NAME)
public class GreptimeConfig extends DataSourceConfig {

    static final String DEFAULT_SCHEMA_FIELD       = "defaultSchema";
    static final String DEFAULT_SCHEMA             = "public";
    static final String CONNECT_TIMEOUT_MS_FIELD   = "connectTimeoutMs";
    static final String SOCKET_TIMEOUT_SEC_FIELD   = "soTimeoutSec";
    static final int    DEFAULT_CONNECT_TIMEOUT_MS = 5000;
    static final int    DEFAULT_SOCKET_TIMEOUT_SEC = 10;

    @Getter
    @Setter
    @ConfigDef(name = DEFAULT_SCHEMA_FIELD, defaultValue = DEFAULT_SCHEMA, group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_DESC, readOnly = false)
    private String      defaultSchema              = DEFAULT_SCHEMA;

    @Getter
    @Setter
    @ConfigDef(name = CONNECT_TIMEOUT_MS_FIELD, defaultValue = ""
                                                               + DEFAULT_CONNECT_TIMEOUT_MS, group = DsConfigGroup.ADVANCED, labelKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private int         connectTimeoutMs           = DEFAULT_CONNECT_TIMEOUT_MS;

    @Getter
    @Setter
    @ConfigDef(name = SOCKET_TIMEOUT_SEC_FIELD, defaultValue = ""
                                                               + DEFAULT_SOCKET_TIMEOUT_SEC, group = DsConfigGroup.ADVANCED, labelKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = GreptimeConfigI18nKeys.GREPTIMEDB_SOCKET_TIMEOUT_DESC, readOnly = false)
    private int         soTimeoutSec               = DEFAULT_SOCKET_TIMEOUT_SEC;

    public GreptimeConfig(){
        setDataSourceType(DataSourceType.GreptimeDB);
    }

    @Override
    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(getDefaultSchema()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), Integer.toString(connectTimeoutMs));
        // Connector/J reads socketTimeout as a string in milliseconds.
        properties.setProperty("socketTimeout", Long.toString(soTimeoutSec * 1000L));
        properties.setProperty("autoReconnect", "false");
        properties.setProperty("autoReconnectForPools", "false");
        // The session enforces the deadline through result fetching. Connector/J's timer uses MySQL KILL QUERY.
        properties.setProperty("enableQueryTimeouts", "false");
        return properties;
    }
}
