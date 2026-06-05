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
package com.clougence.clouddm.ds.polardb.dsconf.pormy;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/5 20:29
 */
@Getter
@Setter
@Serialization(provider = PorMySerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PorMyConfig extends DataSourceConfig {

    @ConfigDef(name = "connectionCharset", defaultValue = "utf8", descKey = ConfigI18nKey.CONFIG_POLARDBMYSQL_CONN_CHARSET_DESCRIPTION, readOnly = false)
    private String  connectionCharset;

    @ConfigDef(name = "useCursorFetch", valueRequire = false, descKey = ConfigI18nKey.CONFIG_POLARDBMYSQL_CONN_USE_CURSOR_FETCH, readOnly = false, valueAdvance = "true - false", group = DsConfigGroup.OPTIONS)
    private Boolean useCursorFetch;

    public PorMyConfig(){
        setDataSourceType(DataSourceType.PolarDbMySQL);
    }

    @Override
    public void deserialize() {
        super.deserialize();
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(this.getDefaultSchema()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        properties.setProperty("use_cursor_fetch", safeStr(StringUtils.toString(this.getUseCursorFetch())));
        properties.setProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey(), safeStr(this.getConnectionCharset()));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), "Asia/Shanghai");
        return properties;
    }
}
