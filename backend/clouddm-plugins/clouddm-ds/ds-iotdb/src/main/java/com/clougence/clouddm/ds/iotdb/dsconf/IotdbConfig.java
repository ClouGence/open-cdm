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

package com.clougence.clouddm.ds.iotdb.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Serialization(provider = IotdbSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IotdbConfig extends DataSourceConfig implements Cloneable {

    static final String               NETWORK_TIMEOUT_FIELD      = "networkTimeoutMs";
    private static final int          DEFAULT_NETWORK_TIMEOUT_MS = 5000;

    @Getter
    @Setter
    @ConfigDef(name = NETWORK_TIMEOUT_FIELD, defaultValue = ""
                                                            + DEFAULT_NETWORK_TIMEOUT_MS, group = DsConfigGroup.ADVANCED, labelKey = IotdbI18nKeys.IOTDB_NETWORK_TIMEOUT_LABEL, descKey = IotdbI18nKeys.IOTDB_NETWORK_TIMEOUT_DESC, readOnly = false)
    private int                       networkTimeoutMs           = DEFAULT_NETWORK_TIMEOUT_MS;
    private transient IotdbSqlDialect sessionDialect             = IotdbSqlDialect.TREE;
    private transient String          sessionDatabase;

    public IotdbConfig(){
        setDataSourceType(DataSourceType.IoTDB);
    }

    @Override
    public Properties asDriverProperties() {
        return asDriverProperties(sessionDialect, sessionDatabase);
    }

    public IotdbConfig forSession(IotdbSqlDialect dialect, String database) {
        try {
            IotdbConfig config = (IotdbConfig) super.clone();
            config.sessionDialect = dialect;
            config.sessionDatabase = database;
            return config;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Properties asDriverProperties(IotdbSqlDialect dialect, String database) {
        requireDirectConnection();
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(getPassword()));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), Integer.toString(networkTimeoutMs));
        properties.setProperty(IotdbSqlDialect.DRIVER_PROPERTY, dialect.name());
        if (StringUtils.isNotBlank(database)) {
            properties.setProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), database);
        }
        return properties;
    }

    void requireDirectConnection() {
        if (Boolean.TRUE.equals(getSshProxyEnabled()) || (getSslMode() != null && getSslMode() != SslMode.DISABLED)) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_DIRECT_CONNECTION_REQUIRED);
        }
    }

    public static int resolveNetworkTimeoutMs(String value) {
        if (StringUtils.isBlank(value)) {
            return DEFAULT_NETWORK_TIMEOUT_MS;
        }
        try {
            int timeout = Integer.parseInt(value.trim());
            if (timeout > 0) {
                return timeout;
            }
        } catch (NumberFormatException ignored) {
            // Return the same validation error for malformed and out-of-range values.
        }
        throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_NETWORK_TIMEOUT_INVALID);
    }
}
