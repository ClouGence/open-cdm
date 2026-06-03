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
package com.clougence.clouddm.ds.doris;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.doris.analysis.*;
import com.clougence.clouddm.ds.doris.analysis.rewrite.DrRewriteSpi;
import com.clougence.clouddm.ds.doris.definition.DrDefService;
import com.clougence.clouddm.ds.doris.definition.ui.browser.DrDsBrowseSpi;
import com.clougence.clouddm.ds.doris.definition.ui.ddl.DrConvertTableDDLSpi;
import com.clougence.clouddm.ds.doris.definition.ui.editor.data.DrDataEditorSpi;
import com.clougence.clouddm.ds.doris.definition.ui.editor.table.DrEditorProvider;
import com.clougence.clouddm.ds.doris.definition.ui.editor.table.DrTableEditorUiDataSpi;
import com.clougence.clouddm.ds.doris.definition.ui.exception.DrDetermineExceptionSpi;
import com.clougence.clouddm.ds.doris.definition.ui.template.DrCmdTemplateSpi;
import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.ds.doris.dsconf.DrConfigSpi;
import com.clougence.clouddm.ds.doris.dsconf.DrSerializationSpi;
import com.clougence.clouddm.ds.doris.execute.DrSessionFactory;
import com.clougence.clouddm.ds.doris.execute.DrSupportSpi;
import com.clougence.clouddm.ds.doris.i18n.DrDsI18nKeys;
import com.clougence.clouddm.ds.doris.language.DrLanguageSpi;
import com.clougence.clouddm.ds.doris.resource.DrEditorResourceSpi;
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
                            "com.clougence.clouddm.ds.doris.execute.*"       //
}, dsProduct = DataSourceType.Doris)
public class DrPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Doris);
        binder.bindTypes(DsType.Doris, DorisTypes.values(), DorisTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Doris, DorisTypes.values());
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
        dsPlugin.addPluginSpi(new DrConfigSpi());
        dsPlugin.addPluginSpi(new DrSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(DrSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new RdbSessionSpi());
        dsPlugin.addPluginSpi(new DrSupportSpi());
        dsPlugin.addPluginSpi(new DrRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(DrDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(DrEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(DorisDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new DrDsBrowseSpi());
        dsPlugin.addPluginSpi(new DrDefService());
        dsPlugin.addPluginSpi(new DrTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new DrCmdTemplateSpi());
        dsPlugin.addPluginSpi(new DrDataEditorSpi());
        dsPlugin.addPluginSpi(new DrConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new DrDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new DrLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new DrEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new DrResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new DrSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new DrSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new DrSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new DrSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
