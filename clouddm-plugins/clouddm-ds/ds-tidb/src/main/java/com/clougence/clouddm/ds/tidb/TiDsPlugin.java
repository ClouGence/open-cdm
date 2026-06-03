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
package com.clougence.clouddm.ds.tidb;

import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.tidb.analysis.*;
import com.clougence.clouddm.ds.tidb.analysis.rewrite.TiRewriteSpi;
import com.clougence.clouddm.ds.tidb.definition.TiDefService;
import com.clougence.clouddm.ds.tidb.definition.ui.browser.TiDsBrowseSpi;
import com.clougence.clouddm.ds.tidb.definition.ui.ddl.TiConvertTableDDLSpi;
import com.clougence.clouddm.ds.tidb.definition.ui.editor.data.TiDBDataEditorSpi;
import com.clougence.clouddm.ds.tidb.definition.ui.editor.table.TiEditorProvider;
import com.clougence.clouddm.ds.tidb.definition.ui.editor.table.TiTableEditorUiDataSpi;
import com.clougence.clouddm.ds.tidb.definition.ui.exception.TiDetermineExceptionSpi;
import com.clougence.clouddm.ds.tidb.dialect.TiDBDialect;
import com.clougence.clouddm.ds.tidb.dsconf.TiConfigSpi;
import com.clougence.clouddm.ds.tidb.dsconf.TiSqlSerializationSpi;
import com.clougence.clouddm.ds.tidb.execute.TiSessionFactory;
import com.clougence.clouddm.ds.tidb.execute.TiSupportSpi;
import com.clougence.clouddm.ds.tidb.i18n.TiDsI18nKeys;
import com.clougence.clouddm.ds.tidb.language.TiLanguageSpi;
import com.clougence.clouddm.ds.tidb.resource.TiEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.mysql.execute.MySessionSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",      //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*",//
                            "com.clougence.clouddm.ds.tidb.execute.*"        //
}, dsProduct = DataSourceType.TiDB)
public class TiDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.TiDB);
        binder.bindTypes(DsType.TiDB, TiDBTypes.values(), TiDBTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.TiDB, TiDBTypes.values());
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
        dsPlugin.addPluginSpi(new TiConfigSpi());
        dsPlugin.addPluginSpi(new TiSqlSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(TiSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new TiSupportSpi());
        dsPlugin.addPluginSpi(new TiRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(TiDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(TiEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(TiDBDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new TiDsBrowseSpi());
        dsPlugin.addPluginSpi(new TiDefService());
        dsPlugin.addPluginSpi(new TiTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new TiDBDataEditorSpi());
        dsPlugin.addPluginSpi(new TiConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new TiDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new TiLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new TiEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new TiResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new TiSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new TiSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new TiSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new TiSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
