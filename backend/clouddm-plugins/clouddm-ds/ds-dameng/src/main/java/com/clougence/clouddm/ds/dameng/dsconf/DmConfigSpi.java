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
package com.clougence.clouddm.ds.dameng.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.common.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;

public class DmConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "5236";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return DmConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        DmConfig config = (DmConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(DmConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(DmConfig.Fields.soTimeoutSec), false);
        config.setDefaultSchema(defaultConfig.get(DmConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(defaultConfig.get(DmConfig.Fields.clientTimeZone));
        return dsConfig;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.USER_PASSWD);
        return options;
    }

    @Override
    public boolean supportSSL() {
        return true;
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of(SslMode.TRUST, SslMode.CA, SslMode.CLIENT_CERT);
    }

    @Override
    public boolean supportSSH() {
        return true;
    }

    @Override
    public boolean supportTx() {
        return true;
    }

}
