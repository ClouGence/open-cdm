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

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;

public class IotdbConfigSpi extends AbstractDsConfigSpi {

    public static final String DEFAULT_PORT = "6667";

    @Override
    public String defaultPort() {
        return DEFAULT_PORT;
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return IotdbConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        IotdbConfig config = (IotdbConfig) dsConfig;
        config.requireDirectConnection();
        config.setNetworkTimeoutMs(IotdbConfig.resolveNetworkTimeoutMs(defaultConfig.get(IotdbConfig.NETWORK_TIMEOUT_FIELD)));
        return config;
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of(SecurityType.USER_PASSWD);
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
