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
package com.clougence.clouddm.ds.hologres.dsconf;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigMap;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;

public class HgConfigSpi implements DsConfigSpi, ConfigKeys {

    @Override
    public DataSourceConfig newConfig(Map<String, String> configMap) {
        return new HgConfig();
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, DsConfigMap dsConfigMap) {
        // dsConfig
        ((HgConfig) dsConfig).setUserName((String) dsConfigMap.getRdpDsBean().get(ConfigKeys.RDP_DS_KEY_ACCESS_KEY));
        ((HgConfig) dsConfig).setPassword((String) dsConfigMap.getRdpDsBean().get(ConfigKeys.RDP_DS_KEY_SECRET_KEY));
        ((HgConfig) dsConfig).setDefaultDataBase((String) dsConfigMap.getRdpDsBean().get(ConfigKeys.RDP_DS_KEY_DB_NAME));

        return dsConfig;
    }}
