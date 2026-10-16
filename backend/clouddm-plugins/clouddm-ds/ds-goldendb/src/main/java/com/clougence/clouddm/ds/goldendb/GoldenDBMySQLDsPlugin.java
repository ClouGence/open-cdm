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
package com.clougence.clouddm.ds.goldendb;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.goldendb.definition.mysql.GoldenDBMySQLDefService;
import com.clougence.clouddm.ds.goldendb.definition.mysql.ui.browser.GoldenDBMySQLDsBrowseSpi;
import com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table.GoldenDBMySQLEditorProvider;
import com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table.GoldenDBMySQLTableEditorUiDataSpi;
import com.clougence.clouddm.ds.goldendb.dialect.mysql.GoldenDBMySQLDialect;
import com.clougence.clouddm.ds.goldendb.dsconf.mysql.GoldenDBMySQLConfigSpi;
import com.clougence.clouddm.ds.goldendb.dsconf.mysql.GoldenDBMySQLSerializationSpi;
import com.clougence.clouddm.ds.goldendb.execute.GoldenDBSessionFactory;
import com.clougence.clouddm.ds.goldendb.execute.mysql.GoldenDBMySQLSessionSpi;
import com.clougence.clouddm.ds.goldendb.execute.mysql.GoldenDBMySQLSupportSpi;
import com.clougence.clouddm.ds.goldendb.i18n.GoldenDBDsI18nKeys;
import com.clougence.clouddm.ds.goldendb.resource.mysql.GoldenDBMySQLEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.definition.secrules.MySecRulesSupportSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.ddl.MyConvertTableDDLSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyConfigI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.language.MyLanguageSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;
import com.clougence.sql.mysql.MySqlEngineSpi;

@Plugin(name = "i18n::" + GoldenDBDsI18nKeys.PLUGIN_NAME_GOLDENDB_MYSQL,              //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*",              //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*",        //
                            "com.clougence.clouddm.ds.goldendb.execute.*",           //
                            "com.clougence.clouddm.ds.goldendb.execute.dsfactory.*"  //
        }, dsProduct = DataSourceType.GoldenDBMySQL)
public class GoldenDBMySQLDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.GoldenDB);
        binder.bindTypes(DsType.GoldenDB, MySQLTypes.values(), MySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.GoldenDBMySQL, MySQLTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        dsPlugin.addPluginSpi(new GoldenDBMySQLConfigSpi());
        dsPlugin.addPluginSpi(new GoldenDBMySQLSerializationSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.bindDsSessionFactory(GoldenDBSessionFactory.class);
        dsPlugin.bindDsDriverFamily("GoldenDB MySQL JDBC Driver");
        dsPlugin.bindSqlEngine(MySqlEngineSpi.NAME);
        dsPlugin.addPluginSpi(new GoldenDBMySQLSessionSpi());
        dsPlugin.addPluginSpi(new GoldenDBMySQLSupportSpi());

        dsPlugin.bindPluginI18n(GoldenDBDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MyDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MyConfigI18nKeys.class);
        dsPlugin.bindDsSqlBuilder(GoldenDBMySQLEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(GoldenDBMySQLDialect.INSTANCE);
        dsPlugin.addPluginSpi(DsBrowseSpi.class, DsBrowseSpi.class.getName(), new GoldenDBMySQLDsBrowseSpi());
        dsPlugin.addPluginSpi(new GoldenDBMySQLDefService());
        dsPlugin.addPluginSpi(new GoldenDBMySQLTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new MyConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MyDetermineExceptionSpi());
        dsPlugin.addPluginSpi(new MyLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GoldenDBMySQLEditorResourceSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.addPluginSpi(new MySecRulesSupportSpi());
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
