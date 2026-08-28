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
package com.clougence.clouddm.ds.goldendb.dsconf.mysql;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.goldendb.dsconf.AbstractGoldenDBConfigSpi;
import com.clougence.sql.mysql.MySqlEngineSpi;

public class GoldenDBMySQLConfigSpi extends AbstractGoldenDBConfigSpi {

    public GoldenDBMySQLConfigSpi(){
        super(GoldenDBMySQLConfig.class);
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        DataSourceConfig config = super.fillConfig(dsConfig, defaultConfig);
        config.setSqlEngine(MySqlEngineSpi.NAME);
        return config;
    }

    @Override
    public void customizeUiMap(Map<String, String> uiMap, Map<String, String> configMap) {
        super.customizeUiMap(uiMap, configMap);
        uiMap.put(DataSourceConfig.Fields.sqlEngine, MySqlEngineSpi.NAME);
    }
}
