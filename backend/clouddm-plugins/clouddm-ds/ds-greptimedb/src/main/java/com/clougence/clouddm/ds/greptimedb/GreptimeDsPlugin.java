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

package com.clougence.clouddm.ds.greptimedb;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.greptimedb.dsconf.GreptimeConfigSpi;
import com.clougence.clouddm.ds.greptimedb.dsconf.GreptimeSerializationSpi;
import com.clougence.clouddm.ds.greptimedb.execute.GreptimeSessionFactory;
import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;
import com.clougence.sql.greptimedb.GreptimeSqlEngineSpi;

@Plugin(name = "GreptimeDB", display = false, dsProduct = DataSourceType.GreptimeDB, includePackages = { "com.clougence.clouddm.dsfamily.execute.*",
                                                                                                         "com.clougence.clouddm.dsfamily.mysql.execute.*",
                                                                                                         "com.clougence.clouddm.ds.greptimedb.execute.*" })
public class GreptimeDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.GreptimeDB);
    }

    @Override
    public void loadPlugin(DsPluginBinder binder) {
        SchemaFramework.install(this);
        binder.bindDsDriverFamily("MySQL Connector/J");
        binder.bindDsSessionFactory(GreptimeSessionFactory.class);
        binder.bindSqlEngine(GreptimeSqlEngineSpi.NAME);
        binder.bindPluginI18n(GreptimeConfigI18nKeys.class);
        binder.addPluginSpi(new GreptimeConfigSpi());
        binder.addPluginSpi(new GreptimeSerializationSpi(binder.getPluginClassLoader()));
        binder.addPluginSpi(new RdbSessionSpi());
    }
}
