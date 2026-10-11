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
package com.clougence.clouddm.ds.es.dsconf;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;

import java.util.List;
import java.util.Map;

public class EsConfigSpi extends AbstractDsConfigSpi {
    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return EsConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        return dsConfig;
    }

    @Override
    public String defaultPort() {
        return "9200";
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public boolean supportSSH() {
        return false;
    }

    @Override
    public boolean supportTx() {
        return false;
    }

    @Override
    public List<SecurityType> securityTypes() {
        return List.of();
    }

    @Override
    public List<SslMode> sslModeSet() {
        return List.of();
    }
}
