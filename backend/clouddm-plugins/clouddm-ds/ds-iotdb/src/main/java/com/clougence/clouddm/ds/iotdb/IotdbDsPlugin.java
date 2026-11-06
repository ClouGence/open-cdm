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

package com.clougence.clouddm.ds.iotdb;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbConfigSpi;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSerializationSpi;
import com.clougence.clouddm.ds.iotdb.execute.IotdbSessionFactory;
import com.clougence.clouddm.ds.iotdb.execute.IotdbSessionSpi;
import com.clougence.clouddm.ds.iotdb.execute.IotdbSupportSpi;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.ds.iotdb.metadata.*;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;
import com.clougence.sql.iotdb.IotdbLanguageSpi;
import com.clougence.sql.iotdb.IotdbSqlEngineSpi;

@Plugin(name = "i18n::" + IotdbI18nKeys.PLUGIN_NAME_IOTDB, display = true, dsProduct = DataSourceType.IoTDB, includePackages = { "com.clougence.clouddm.ds.iotdb.execute.*",
                                                                                                                                 "org.apache.iotdb.*", "org.apache.thrift.*",
                                                                                                                                 "org.apache.tsfile.*", "org.antlr.v4.runtime.*" })
public class IotdbDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.IoTDB);
        binder.bindTypes(DsType.IoTDB, IotdbFieldType.values(), IotdbFieldType::valueOfCode);
    }

    @Override
    public void loadPlugin(DsPluginBinder binder) {
        SchemaFramework.install(this);
        binder.bindPluginI18n(IotdbI18nKeys.class);
        binder.bindDsDriverFamily("IoTDB JDBC");
        binder.bindSqlEngine(IotdbSqlEngineSpi.NAME);
        binder.bindDsDialect(new IotdbDialect());
        binder.bindDsSessionFactory(IotdbSessionFactory.class);
        binder.addPluginSpi(new IotdbConfigSpi());
        binder.addPluginSpi(new IotdbSerializationSpi(binder.getPluginClassLoader()));
        binder.addPluginSpi(new IotdbSessionSpi());
        binder.addPluginSpi(new IotdbSupportSpi());
        binder.addPluginSpi(new IotdbBrowseSpi());
        binder.addGlobalSpi(new IotdbAuthInfoSpi());
        binder.addPluginSpi(new IotdbCmdTemplateSpi());
        binder.addPluginSpi(new IotdbLanguageSpi(binder.findGlobalService(MetaService.class)));
    }
}
