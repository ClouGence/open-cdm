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
package com.clougence.clouddm.ds.goldendb.dsconf;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public abstract class AbstractGoldenDBConfigSpi extends AbstractDsConfigSpi {

    private final Class<? extends AbstractGoldenDBConfig> configClass;

    protected AbstractGoldenDBConfigSpi(Class<? extends AbstractGoldenDBConfig> configClass){
        this.configClass = configClass;
    }

    @Override
    public String defaultPort() {
        return "5502";
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return configClass;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        AbstractGoldenDBConfig config = (AbstractGoldenDBConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(AbstractGoldenDBConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(AbstractGoldenDBConfig.Fields.soTimeoutSec), false);
        config.setDefaultSchema(defaultConfig.get(AbstractGoldenDBConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(StringUtils.defaultIfBlank(defaultConfig.get(AbstractGoldenDBConfig.Fields.clientTimeZone), "Asia/Shanghai"));
        config.setConnectionCharset(StringUtils.defaultIfBlank(defaultConfig.get(AbstractGoldenDBConfig.Fields.connectionCharset), "utf8"));
        return config;
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of(SecurityType.NONE, SecurityType.ONLY_USER, SecurityType.USER_PASSWD);
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
        return true;
    }

    @Override
    public boolean supportTx() {
        return true;
    }
}
