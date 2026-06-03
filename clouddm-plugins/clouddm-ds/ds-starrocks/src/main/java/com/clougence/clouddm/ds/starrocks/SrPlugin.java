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
package com.clougence.clouddm.ds.starrocks;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.starrocks.analysis.*;
import com.clougence.clouddm.ds.starrocks.analysis.rewrite.SrRewriteSpi;
import com.clougence.clouddm.ds.starrocks.definition.SrDefService;
import com.clougence.clouddm.ds.starrocks.definition.ui.SrDetermineExceptionSpi;
import com.clougence.clouddm.ds.starrocks.definition.ui.browser.SrDsBrowseSpi;
import com.clougence.clouddm.ds.starrocks.definition.ui.ddl.SrConvertTableDDLSpi;
import com.clougence.clouddm.ds.starrocks.definition.ui.editor.data.SrDataEditorSpi;
import com.clougence.clouddm.ds.starrocks.definition.ui.editor.table.SrEditorProvider;
import com.clougence.clouddm.ds.starrocks.definition.ui.editor.table.SrTableEditorUiDataSpi;
import com.clougence.clouddm.ds.starrocks.definition.ui.template.SrCmdTemplateSpi;
import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
import com.clougence.clouddm.ds.starrocks.dsconf.SrConfigSpi;
import com.clougence.clouddm.ds.starrocks.dsconf.SrSqlSerializationSpi;
import com.clougence.clouddm.ds.starrocks.execute.SrSessionFactory;
import com.clougence.clouddm.ds.starrocks.execute.SrSupportSpi;
import com.clougence.clouddm.ds.starrocks.i18n.SrDsI18nKeys;
import com.clougence.clouddm.ds.starrocks.language.SrLanguageSpi;
import com.clougence.clouddm.ds.starrocks.resource.SrEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
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
                            "com.clougence.clouddm.ds.starrocks.execute.*"   //
}, dsProduct = DataSourceType.StarRocks)
public class SrPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.StarRocks);
        binder.bindTypes(DsType.StarRocks, StarRocksTypes.values(), StarRocksTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.StarRocks, StarRocksTypes.values());
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
        dsPlugin.addPluginSpi(new SrConfigSpi());
        dsPlugin.addPluginSpi(new SrSqlSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(SrSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new RdbSessionSpi());
        dsPlugin.addPluginSpi(new SrSupportSpi());
        dsPlugin.addPluginSpi(new SrRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(SrDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(SrEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(StarRocksDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new SrDsBrowseSpi());
        dsPlugin.addPluginSpi(new SrDefService());
        dsPlugin.addPluginSpi(new SrTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new SrCmdTemplateSpi());
        dsPlugin.addPluginSpi(new SrDataEditorSpi());
        dsPlugin.addPluginSpi(new SrConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new SrDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new SrLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new SrEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new SrResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new SrSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new SrSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new SrSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new SrSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
