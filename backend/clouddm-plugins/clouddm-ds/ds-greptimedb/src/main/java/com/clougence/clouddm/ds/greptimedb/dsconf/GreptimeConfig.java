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
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;

import lombok.Getter;
import lombok.Setter;

@Serialization(provider = GreptimeSerializationSpi.PROVIDER_NAME)
public class GreptimeConfig extends DataSourceConfig {

    static final String DEFAULT_SCHEMA_FIELD = "defaultSchema";
    static final String DEFAULT_SCHEMA       = "public";

    @Getter
    @Setter
    @ConfigDef(name = DEFAULT_SCHEMA_FIELD, defaultValue = DEFAULT_SCHEMA, group = DsConfigGroup.GENERAL, labelKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_LABEL, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_DESC, readOnly = false)
    private String      defaultSchema        = DEFAULT_SCHEMA;

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
        return properties;
    }
}
