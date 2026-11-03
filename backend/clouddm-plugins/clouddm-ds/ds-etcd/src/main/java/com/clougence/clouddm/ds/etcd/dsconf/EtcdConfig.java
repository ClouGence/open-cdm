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
package com.clougence.clouddm.ds.etcd.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.etcd.i18n.EtcdConfigI18nKeys;
import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@Serialization(provider = EtcdSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtcdConfig extends DataSourceConfig {

    public static final String ENDPOINTS = "endpoints";

    @ConfigDef(name = Fields.apiTimeoutMs, defaultValue = "10000", group = DsConfigGroup.ADVANCED, labelKey = EtcdConfigI18nKeys.CONFIG_ETCD_TIMEOUT_LABEL, descKey = EtcdConfigI18nKeys.CONFIG_ETCD_TIMEOUT_DESC, readOnly = false)
    private int apiTimeoutMs = 10000;

    public EtcdConfig(){
        setDataSourceType(DataSourceType.Etcd);
    }

    @Override
    public Properties asDriverProperties() {
        SecurityType securityType = getSecurityType();
        if (securityType == null) {
            securityType = SecurityType.NONE;
        }
        if (securityType != SecurityType.NONE && securityType != SecurityType.USER_PASSWD) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SESSION_UNSUPPORTED);
        }
        SslMode sslMode = getSslMode();
        if (sslMode == null) {
            sslMode = SslMode.DISABLED;
        }
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty(ENDPOINTS, safeStr(getHost()));
        properties.setProperty("api.timeout.ms", String.valueOf(apiTimeoutMs));
        properties.setProperty(DsConfigKeys.SSL_MODE.getConfigKey(), sslMode.name());
        if (sslMode == SslMode.CA && StringUtils.isNotBlank(getSslCaFilePath())) {
            properties.setProperty(DsConfigKeys.SSL_CA_FILE.getConfigKey(), getSslCaFilePath());
        }
        if (securityType == SecurityType.USER_PASSWD) {
            properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(getUserName()));
            properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(getPassword()));
        }
        return properties;
    }
}
