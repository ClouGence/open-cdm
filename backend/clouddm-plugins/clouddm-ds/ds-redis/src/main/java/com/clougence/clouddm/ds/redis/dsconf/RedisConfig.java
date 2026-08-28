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
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.redis.execute.jdbc.JedisKeys;
import com.clougence.clouddm.ds.redis.i18n.RedisConfigI18nKeys;
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
@Serialization(provider = RedisSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisConfig extends DataSourceConfig {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(name = Fields.defaultSchema, //
            group = DsConfigGroup.GENERAL, labelKey = RedisConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_LABEL, descKey = RedisConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_DESC, readOnly = false)
    private String  defaultSchema;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", //
            group = DsConfigGroup.ADVANCED, labelKey = RedisConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = RedisConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer soTimeoutSec;
    @ConfigDef(name = Fields.connAndSoTimeoutMs, defaultValue = "5000", //
            group = DsConfigGroup.ADVANCED, labelKey = RedisConfigI18nKeys.CONFIG_REDIS_CON_AND_SO_TIMEOUT_MS_LABEL, descKey = RedisConfigI18nKeys.CONFIG_REDIS_CON_AND_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer connAndSoTimeoutMs;

    public RedisConfig(){
        setDataSourceType(DataSourceType.Redis);
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(this.getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), safeStr(this.getDefaultSchema()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnAndSoTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        SslMode sslMode = this.getSslMode();
        if (sslMode == null) {
            sslMode = SslMode.DISABLED;
        }
        properties.setProperty(JedisKeys.SSL_MODE, sslMode.name());
        properties.setProperty(JedisKeys.SSL_CA_FILE, safeStr(this.getSslCaFilePath()));
        properties.setProperty(JedisKeys.SSL_CA_FORMAT, safeStr(this.getSslCaFileFormat()));
        properties.setProperty(JedisKeys.SSL_CA_PASSWORD, safeStr(this.getSslCaPassword()));
        properties.setProperty(JedisKeys.SSL_CLIENT_CERT_FILE, safeStr(this.getSslClientCertFilePath()));
        properties.setProperty(JedisKeys.SSL_CLIENT_CERT_FORMAT, safeStr(this.getSslClientCertFileFormat()));
        properties.setProperty(JedisKeys.SSL_CLIENT_KEY_FILE, safeStr(this.getSslClientKeyFilePath()));
        properties.setProperty(JedisKeys.SSL_CLIENT_KEY_PASSWORD, safeStr(this.getSslClientKeyPassword()));
        return properties;
    }
}
