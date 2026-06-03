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
package com.clougence.clouddm.ds.sqlserver;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.sqlserver.analysis.*;
import com.clougence.clouddm.ds.sqlserver.definition.ui.MsSqlDefService;
import com.clougence.clouddm.ds.sqlserver.definition.ui.browser.SqlServerDsBrowseSpi;
import com.clougence.clouddm.ds.sqlserver.definition.ui.ddl.MsConvertTableDDLSpi;
import com.clougence.clouddm.ds.sqlserver.definition.ui.editor.data.SqlServerDataEditorSpi;
import com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table.MsSqlEditorProvider;
import com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table.MsSqlTableEditorUiDataSpi;
import com.clougence.clouddm.ds.sqlserver.definition.ui.exception.MsDetermineExceptionSpi;
import com.clougence.clouddm.ds.sqlserver.definition.ui.template.MsSqlCmdTemplateSpi;
import com.clougence.clouddm.ds.sqlserver.dialect.SqlServerDialect;
import com.clougence.clouddm.ds.sqlserver.dsconf.MsSqlConfigSpi;
import com.clougence.clouddm.ds.sqlserver.dsconf.MsSqlSerializationSpi;
import com.clougence.clouddm.ds.sqlserver.execute.MsSqlSessionFactory;
import com.clougence.clouddm.ds.sqlserver.execute.MsSqlSessionSpi;
import com.clougence.clouddm.ds.sqlserver.execute.MsSqlSupportSpi;
import com.clougence.clouddm.ds.sqlserver.i18n.MsSqlI18nKeys;
import com.clougence.clouddm.ds.sqlserver.language.MsSqlLanguageSpi;
import com.clougence.clouddm.ds.sqlserver.resource.MsSqlEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",   //
                            "com.clougence.clouddm.ds.sqlserver.execute.*"//
}, dsProduct = DataSourceType.SQLServer)
public class MsSqlPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.SqlServer);
        binder.bindTypes(DsType.SqlServer, SqlServerTypes.values(), SqlServerTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.SQLServer, SqlServerTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        // init schema plugin
        SchemaFramework.install(this);

        this.configBasic(dsPlugin);
        this.configExecute(dsPlugin);
        this.configUi(dsPlugin);
        this.configEditor(dsPlugin);
        this.configTeam(dsPlugin);
        this.configFeature(dsPlugin);
    }

    private void configBasic(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new MsSqlConfigSpi());
        dsPlugin.addPluginSpi(new MsSqlSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(MsSqlSessionFactory.class);
        dsPlugin.bindDsDriverFamily("SQL Server JDBC Driver", "jTDS");
        dsPlugin.addPluginSpi(new MsSqlSessionSpi());
        dsPlugin.addPluginSpi(new MsSqlSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(MsSqlI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(MsSqlEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(SqlServerDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new SqlServerDsBrowseSpi());
        dsPlugin.addPluginSpi(new MsSqlDefService());
        dsPlugin.addPluginSpi(new MsSqlTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MsSqlCmdTemplateSpi());
        dsPlugin.addPluginSpi(new SqlServerDataEditorSpi());
        dsPlugin.addPluginSpi(new MsConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MsDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new MsSqlLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MsSqlEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new MsSqlResAnalysisSpi());
        dsPlugin.addPluginSpi(new MsSqlSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new MsSqlSecDomainResolveSpi());
        dsPlugin.addPluginSpi(new MsSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new MsSelectColumnAnalysisSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
