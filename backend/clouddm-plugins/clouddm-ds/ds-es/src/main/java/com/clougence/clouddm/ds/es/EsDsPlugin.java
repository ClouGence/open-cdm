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
package com.clougence.clouddm.ds.es;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.es.definition.EsSecRulesSupportSpi;
import com.clougence.clouddm.ds.es.definition.ui.EsCmdTemplateSpi;
import com.clougence.clouddm.ds.es.definition.ui.EsDsBrowseSpi;
import com.clougence.clouddm.ds.es.dsconf.EsConfigSpi;
import com.clougence.clouddm.ds.es.dsconf.EsSerializationSpi;
import com.clougence.clouddm.ds.es.execute.EsSessionFactory;
import com.clougence.clouddm.ds.es.execute.EsSessionSpi;
import com.clougence.clouddm.ds.es.execute.EsSupportSpi;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.ds.es.language.EsLanguageSpi;
import com.clougence.clouddm.ds.es.metadata.EsFieldType;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;
import com.clougence.sql.es.EsSqlEngineSpi;

@Plugin(name = "i18n::" + EsDsI18nKeys.PLUGIN_NAME_ES, includePackages = { "com.clougence.clouddm.ds.es.execute.*" }, dsProduct = DataSourceType.ElasticSearch)
public class EsDsPlugin implements DsPlugin, SchemaPlugin {
    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.ElasticSearch);
        binder.bindTypes(DsType.ElasticSearch, EsFieldType.values(), EsFieldType::valueOfCode);
    }

    @Override
    public void loadPlugin(DsPluginBinder binder) {
        SchemaFramework.install(this);
        binder.bindDsDriverFamily("ES REST Client");
        binder.bindDsSessionFactory(EsSessionFactory.class);
        binder.addPluginSpi(new EsSessionSpi());
        binder.addPluginSpi(new EsSupportSpi());
        binder.addPluginSpi(new EsSecRulesSupportSpi());
        binder.addPluginSpi(new EsDsBrowseSpi());
        binder.addPluginSpi(new EsCmdTemplateSpi());
        binder.addPluginSpi(new EsLanguageSpi(binder.findGlobalService(MetaService.class)));
        binder.bindSqlEngine(EsSqlEngineSpi.NAME);
        binder.bindPluginI18n(EsDsI18nKeys.class);
        binder.addPluginSpi(new EsConfigSpi());
        binder.addPluginSpi(new EsSerializationSpi(binder.getPluginClassLoader()));
    }
}
