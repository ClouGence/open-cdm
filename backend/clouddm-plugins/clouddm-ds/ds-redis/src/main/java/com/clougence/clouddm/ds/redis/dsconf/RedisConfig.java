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
package com.clougence.clouddm.ds.redis.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/6 10:23
 */
@Getter
@Setter
@Serialization(provider = RedisSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisConfig extends DataSourceConfig {

    @ConfigDef(name = "connAndSoTimeoutMs", defaultValue = "5000", descKey = ConfigI18nKey.CONFIG_REDIS_CON_AND_SO_TIMEOUT_MS_DESCRIPTION, readOnly = false)
    private Integer connAndSoTimeoutMs;

    @JsonIgnore
    private String  ip;

    @JsonIgnore
    private Integer port;

    @JsonIgnore
    private String  database;

    public RedisConfig(){
        setDataSourceType(DataSourceType.Redis);
    }

    @Override
    public void deserialize() {
        super.deserialize();

        if (StringUtils.isNotBlank(this.getHost())) {
            String[] ipPort = this.getHost().split(":");
            if (ipPort.length == 2) {
                this.ip = ipPort[0];
                if (StringUtils.isNotBlank(ipPort[1])) {
                    this.port = Integer.parseInt(ipPort[1]);
                } else {
                    this.port = 6379;
                }
            } else if (ipPort.length == 1) {
                this.ip = ipPort[0];
                this.port = 6379;
            } else {
                throw new IllegalArgumentException("unsupported Redis host format:" + this.getHost());
            }
        }
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnAndSoTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(this.getDefaultSchema()));
        return properties;
    }
}
