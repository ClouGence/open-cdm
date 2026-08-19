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

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.kingbasees.dsconf.sqlserver.KingbaseESSQLServerConfigSpi;
import com.clougence.clouddm.ds.kingbasees.dsconf.sqlserver.KingbaseESSQLServerSerializationSpi;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESSessionFactory;
import com.clougence.clouddm.ds.kingbasees.execute.sqlserver.KingbaseESSQLServerSessionSpi;
import com.clougence.clouddm.ds.kingbasees.i18n.KingbaseESDsI18nKeys;
import com.clougence.clouddm.ds.kingbasees.resource.KingbaseESSQLServerEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.sqlserver.definition.secrules.MsSecRulesSupportSpi;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.MsSqlDefService;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.browser.SqlServerDsBrowseSpi;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.ddl.MsConvertTableDDLSpi;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.editor.data.SqlServerDataEditorSpi;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.editor.table.MsSqlEditorProvider;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.editor.table.MsSqlTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.exception.MsDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.sqlserver.definition.ui.template.MsSqlCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.sqlserver.dialect.SqlServerDialect;
import com.clougence.clouddm.dsfamily.sqlserver.execute.MsSqlSupportSpi;
import com.clougence.clouddm.dsfamily.sqlserver.i18n.MsSqlConfigI18nKeys;
import com.clougence.clouddm.dsfamily.sqlserver.i18n.MsSqlI18nKeys;
import com.clougence.clouddm.dsfamily.sqlserver.language.MsSqlLanguageSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + KingbaseESDsI18nKeys.PLUGIN_NAME_KINGBASE_ES_SQLSERVER,          //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*",                   //
                            "com.clougence.clouddm.dsfamily.sqlserver.execute.*",         //
                            "com.clougence.clouddm.ds.kingbasees.execute.*",              //
                            "com.clougence.clouddm.ds.kingbasees.execute.dsfactory.*"     //
        }, dsProduct = DataSourceType.KingbaseESSQLServer)
public class KingbaseESSQLServerDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.SqlServer);
        binder.bindTypes(DsType.SqlServer, SqlServerTypes.values(), SqlServerTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.KingbaseESSQLServer, SqlServerTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        dsPlugin.addPluginSpi(new KingbaseESSQLServerConfigSpi());
        dsPlugin.addPluginSpi(new KingbaseESSQLServerSerializationSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.bindDsSessionFactory(KingbaseESSessionFactory.class);
        dsPlugin.bindDsDriverFamily("KingbaseES JDBC Driver");
        dsPlugin.bindSqlEngine("MS T-SQL");
        dsPlugin.addPluginSpi(new KingbaseESSQLServerSessionSpi());
        dsPlugin.addPluginSpi(new MsSqlSupportSpi());

        dsPlugin.bindPluginI18n(KingbaseESDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MsSqlI18nKeys.class);
        dsPlugin.bindPluginI18n(MsSqlConfigI18nKeys.class);
        dsPlugin.bindDsSqlBuilder(MsSqlEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(SqlServerDialect.INSTANCE);
        dsPlugin.addPluginSpi(new SqlServerDsBrowseSpi());
        dsPlugin.addPluginSpi(new MsSqlDefService());
        dsPlugin.addPluginSpi(new MsSqlTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MsSqlCmdTemplateSpi());
        dsPlugin.addPluginSpi(new SqlServerDataEditorSpi());
        dsPlugin.addPluginSpi(new MsConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MsDetermineExceptionSpi());
        dsPlugin.addPluginSpi(new MsSqlLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new KingbaseESSQLServerEditorResourceSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.addPluginSpi(new MsSecRulesSupportSpi());
    }
}
