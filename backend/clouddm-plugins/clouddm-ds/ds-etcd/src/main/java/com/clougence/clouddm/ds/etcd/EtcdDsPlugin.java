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
package com.clougence.clouddm.ds.etcd;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.etcd.definition.auth.EtcdAuthInfoSpi;
import com.clougence.clouddm.ds.etcd.definition.ui.EtcdDsBrowseSpi;
import com.clougence.clouddm.ds.etcd.definition.ui.template.EtcdCmdTemplateSpi;
import com.clougence.clouddm.ds.etcd.dsconf.EtcdConfigSpi;
import com.clougence.clouddm.ds.etcd.dsconf.EtcdSerializationSpi;
import com.clougence.clouddm.ds.etcd.execute.EtcdSessionFactory;
import com.clougence.clouddm.ds.etcd.execute.EtcdSessionSpi;
import com.clougence.clouddm.ds.etcd.execute.EtcdSupportSpi;
import com.clougence.clouddm.ds.etcd.language.EtcdLanguageSpi;
import com.clougence.clouddm.ds.etcd.sql.EtcdSqlEngineSpi;
import com.clougence.clouddm.ds.etcd.i18n.EtcdConfigI18nKeys;
import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + EtcdDsI18nKeys.PLUGIN_NAME_ETCD, includePackages = { "com.clougence.clouddm.ds.etcd.execute.*" }, dsProduct = DataSourceType.Etcd)
public class EtcdDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Etcd);
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);
        dsPlugin.bindDsDriverFamily("Etcd");
        dsPlugin.bindSqlEngine(EtcdSqlEngineSpi.NAME);
        dsPlugin.bindDsSessionFactory(EtcdSessionFactory.class);
        dsPlugin.addPluginSpi(new EtcdSessionSpi());
        dsPlugin.addPluginSpi(new EtcdSupportSpi());
        dsPlugin.addPluginSpi(new EtcdLanguageSpi());
        dsPlugin.addGlobalSpi(new EtcdSqlEngineSpi());
        dsPlugin.addPluginSpi(new EtcdDsBrowseSpi());
        dsPlugin.addPluginSpi(new EtcdCmdTemplateSpi());
        dsPlugin.addGlobalSpi(new EtcdAuthInfoSpi());
        dsPlugin.bindGlobalI18n(EtcdDsI18nKeys.class);
        dsPlugin.bindPluginI18n(EtcdDsI18nKeys.class, EtcdConfigI18nKeys.class);
        dsPlugin.addPluginSpi(new EtcdConfigSpi());
        dsPlugin.addPluginSpi(new EtcdSerializationSpi(dsPlugin.getPluginClassLoader()));
    }
}
