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
package com.clougence.clouddm.ds.dameng;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.dameng.analysis.DmResAnalysisSpi;
import com.clougence.clouddm.ds.dameng.analysis.DmSecDomainResolveSpi;
import com.clougence.clouddm.ds.dameng.analysis.DmSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.dameng.analysis.DmSplitAnalysisSpi;
import com.clougence.clouddm.ds.dameng.definition.ui.DmDefService;
import com.clougence.clouddm.ds.dameng.definition.ui.browser.DmDsBrowseSpi;
import com.clougence.clouddm.ds.dameng.definition.ui.ddl.DmConvertTableDDLSpi;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.data.DmDataEditorSpi;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmEditorProvider;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTableEditorUiDataSpi;
import com.clougence.clouddm.ds.dameng.definition.ui.exception.DmDetermineExceptionSpi;
import com.clougence.clouddm.ds.dameng.definition.ui.template.DmCmdTemplateSpi;
import com.clougence.clouddm.ds.dameng.dialect.DmDialect;
import com.clougence.clouddm.ds.dameng.dsconf.DmConfigSpi;
import com.clougence.clouddm.ds.dameng.dsconf.DmSerializationSpi;
import com.clougence.clouddm.ds.dameng.execute.DmSessionFactory;
import com.clougence.clouddm.ds.dameng.execute.DmSessionSpi;
import com.clougence.clouddm.ds.dameng.execute.DmSupportSpi;
import com.clougence.clouddm.ds.dameng.i18n.DmDsI18nKeys;
import com.clougence.clouddm.ds.dameng.language.DmLanguageSpi;
import com.clougence.clouddm.ds.dameng.resource.DmEditorResourceSpi;
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
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.dameng.execute.*" //
}, dsProduct = DataSourceType.Dameng)
public class DmDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Dameng);
        binder.bindTypes(DsType.Dameng, DmSqlTypes.values(), DmSqlTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Dameng, DmSqlTypes.values());
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
        dsPlugin.addPluginSpi(new DmConfigSpi());
        dsPlugin.addPluginSpi(new DmSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(DmSessionFactory.class);
        dsPlugin.bindDsDriverFamily("Dameng JDBC Driver");
        dsPlugin.addPluginSpi(new DmSessionSpi());
        dsPlugin.addPluginSpi(new DmSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(DmDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(DmEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(DmDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new DmDsBrowseSpi());
        dsPlugin.addPluginSpi(new DmDefService());
        dsPlugin.addPluginSpi(new DmTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new DmCmdTemplateSpi());
        dsPlugin.addPluginSpi(new DmDataEditorSpi());
        dsPlugin.addPluginSpi(new DmConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new DmDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new DmLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new DmEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new DmResAnalysisSpi());
        dsPlugin.addPluginSpi(new DmSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new DmSecDomainResolveSpi());
        dsPlugin.addPluginSpi(new DmSelectColumnAnalysisSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
