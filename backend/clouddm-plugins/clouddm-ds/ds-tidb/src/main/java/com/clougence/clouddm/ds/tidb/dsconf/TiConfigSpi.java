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
package com.clougence.clouddm.ds.tidb.dsconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.clouddm.sdk.execute.dsconf.capability.ClientTimeZoneExtProperties;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class TiConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return "4000";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return TiConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        TiConfig config = (TiConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(TiConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(TiConfig.Fields.soTimeoutSec), false);
        config.setDefaultSchema(defaultConfig.get(TiConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(StringUtils.defaultIfBlank(defaultConfig.get(ClientTimeZoneExtProperties.CLIENT_TIME_ZONE_FIELD), "Asia/Shanghai"));
        config.setConnectionCharset(StringUtils.defaultIfBlank(defaultConfig.get(TiConfig.Fields.connectionCharset), "utf8"));
        return dsConfig;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.NONE);
        options.add(SecurityType.ONLY_USER);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }

    @Override
    public boolean supportSSL() {
        return false;
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
