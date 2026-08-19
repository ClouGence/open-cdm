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
package com.clougence.clouddm.ds.kingbasees;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.kingbasees.dsconf.mysql.KingbaseESMySQLConfigSpi;
import com.clougence.clouddm.ds.kingbasees.dsconf.mysql.KingbaseESMySQLSerializationSpi;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESSessionFactory;
import com.clougence.clouddm.ds.kingbasees.i18n.KingbaseESDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.definition.MyDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.secrules.MySecRulesSupportSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.ddl.MyConvertTableDDLSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MySessionSpi;
import com.clougence.clouddm.dsfamily.mysql.execute.MySupportSpi;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyConfigI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.language.MyLanguageSpi;
import com.clougence.clouddm.dsfamily.mysql.resource.MyEditorResourceSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + KingbaseESDsI18nKeys.PLUGIN_NAME_KINGBASE_ES_MYSQL,              //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*",                   //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*",             //
                            "com.clougence.clouddm.ds.kingbasees.execute.*",              //
                            "com.clougence.clouddm.ds.kingbasees.execute.dsfactory.*"     //
        }, dsProduct = DataSourceType.KingbaseESMySQL)
public class KingbaseESMySQLDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.MySQL);
        binder.bindTypes(DsType.MySQL, MySQLTypes.values(), MySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.KingbaseESMySQL, MySQLTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        dsPlugin.addPluginSpi(new KingbaseESMySQLConfigSpi());
        dsPlugin.addPluginSpi(new KingbaseESMySQLSerializationSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.bindDsSessionFactory(KingbaseESSessionFactory.class);
        dsPlugin.bindDsDriverFamily("KingbaseES JDBC Driver");
        dsPlugin.bindSqlEngine("MySQL");
        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new MySupportSpi());

        dsPlugin.bindPluginI18n(KingbaseESDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MyDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MyConfigI18nKeys.class);
        dsPlugin.bindDsSqlBuilder(MyEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(MySqlDialect.INSTANCE);
        dsPlugin.addPluginSpi(new MyDsBrowseSpi());
        dsPlugin.addPluginSpi(new MyDefService());
        dsPlugin.addPluginSpi(new MyTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new MyConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MyDetermineExceptionSpi());
        dsPlugin.addPluginSpi(new MyLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MyEditorResourceSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.addPluginSpi(new MySecRulesSupportSpi());
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
