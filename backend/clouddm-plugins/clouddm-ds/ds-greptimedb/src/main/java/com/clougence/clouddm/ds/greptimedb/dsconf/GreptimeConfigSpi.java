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

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.StringUtils;

public class GreptimeConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "4002";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return GreptimeConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        GreptimeConfig config = (GreptimeConfig) dsConfig;
        config.setDefaultSchema(StringUtils.defaultIfBlank(defaultConfig.get(GreptimeConfig.DEFAULT_SCHEMA_FIELD), GreptimeConfig.DEFAULT_SCHEMA));
        config.setConnectTimeoutMs(readTimeout(defaultConfig
            .get(GreptimeConfig.CONNECT_TIMEOUT_MS_FIELD), GreptimeConfig.DEFAULT_CONNECT_TIMEOUT_MS, Integer.MAX_VALUE, GreptimeConfigI18nKeys.GREPTIMEDB_CONNECT_TIMEOUT_INVALID));
        config.setSoTimeoutSec(readTimeout(defaultConfig
            .get(GreptimeConfig.SOCKET_TIMEOUT_SEC_FIELD), GreptimeConfig.DEFAULT_SOCKET_TIMEOUT_SEC, Integer.MAX_VALUE /
                                                                                                      1000, GreptimeConfigI18nKeys.GREPTIMEDB_SOCKET_TIMEOUT_INVALID));
        return config;
    }

    private int readTimeout(String value, int defaultValue, int maximum, String errorKey) {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            int timeout = Integer.parseInt(value.trim());
            if (timeout > 0 && timeout <= maximum) {
                return timeout;
            }
        } catch (NumberFormatException ignored) {
            // Malformed values and out-of-range values use the same validation error.
        }
        throw ThirdPartyApiException.as().with(errorKey);
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of(SecurityType.USER_PASSWD, SecurityType.NONE);
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of();
    }

    @Override
    public boolean supportSSH() {
        return false;
    }

    @Override
    public boolean supportTx() {
        return false;
    }
}
