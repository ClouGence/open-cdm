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
package com.clougence.clouddm.ds.gauss;

import com.clougence.adapter.gauss.GaussDBTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.gauss.analysis.gs.*;
import com.clougence.clouddm.ds.gauss.analysis.gs.rewrite.GsRewriteSpi;
import com.clougence.clouddm.ds.gauss.broswer.gs.GsDsBrowseSpi;
import com.clougence.clouddm.ds.gauss.definition.gs.GsDefService;
import com.clougence.clouddm.ds.gauss.definition.gs.ui.ddl.GsConvertTableDDLSpi;
import com.clougence.clouddm.ds.gauss.definition.gs.ui.editor.table.GsEditorProvider;
import com.clougence.clouddm.ds.gauss.definition.gs.ui.editor.table.GsTableEditorUiDataSpi;
import com.clougence.clouddm.ds.gauss.dialect.GaussDBDialect;
import com.clougence.clouddm.ds.gauss.dsconf.gs.GsConfigSpi;
import com.clougence.clouddm.ds.gauss.dsconf.gs.GsSerializationSpi;
import com.clougence.clouddm.ds.gauss.execute.gs.GsSessionFactory;
import com.clougence.clouddm.ds.gauss.execute.gs.GsSessionSpi;
import com.clougence.clouddm.ds.gauss.execute.gs.GsSupportSpi;
import com.clougence.clouddm.ds.gauss.i18n.gs.GsDsI18nKeys;
import com.clougence.clouddm.ds.gauss.language.gs.GsLanguageSpi;
import com.clougence.clouddm.ds.gauss.resource.GsEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data.PgDataEditorSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",         //
                            "com.clougence.clouddm.dsfamily.postgres.execute.*",//
                            "com.clougence.clouddm.ds.gauss.execute.gs.*"       //
}, dsProduct = DataSourceType.GaussDB)
public class GsDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.GaussDB);
        binder.bindTypes(DsType.GaussDB, GaussDBTypes.values(), GaussDBTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.GaussDB, GaussDBTypes.values());
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
        dsPlugin.addPluginSpi(new GsConfigSpi());
        dsPlugin.addPluginSpi(new GsSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(GsSessionFactory.class);
        dsPlugin.bindDsDriverFamily("GaussDB JDBC Driver", "PostgreSQL JDBC");
        dsPlugin.addPluginSpi(new GsSessionSpi());
        dsPlugin.addPluginSpi(new GsSupportSpi());
        dsPlugin.addPluginSpi(new GsRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(GsDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(GsEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(GaussDBDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new GsDsBrowseSpi());
        dsPlugin.addPluginSpi(new GsDefService());
        dsPlugin.addPluginSpi(new GsTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new PgDataEditorSpi());
        dsPlugin.addPluginSpi(new GsConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new PgDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new GsLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GsEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new GsResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GsSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new GsSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GsSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new GsSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
