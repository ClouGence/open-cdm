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
package com.clougence.clouddm.ds.oceanbase;

import com.clougence.adapter.ob.obfororacle.ObForOracleTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4ora.*;
import com.clougence.clouddm.ds.oceanbase.definition.ob4my.ui.ddl.ObDDLSpiConvert;
import com.clougence.clouddm.ds.oceanbase.definition.ob4ora.ui.ObForOraDefService;
import com.clougence.clouddm.ds.oceanbase.definition.ob4ora.ui.browser.ObForOraDsBrowseSpi;
import com.clougence.clouddm.ds.oceanbase.definition.ob4ora.ui.editor.data.ObForOraDataEditorSpi;
import com.clougence.clouddm.ds.oceanbase.dsconf.ob4ora.ObForOraConfigSpi;
import com.clougence.clouddm.ds.oceanbase.dsconf.ob4ora.ObForOraSerializationSpi;
import com.clougence.clouddm.ds.oceanbase.execute.ob4ora.ObForOraSessionFactory;
import com.clougence.clouddm.ds.oceanbase.execute.ob4ora.ObForOraSessionSpi;
import com.clougence.clouddm.ds.oceanbase.execute.ob4ora.ObForOraSupportSpi;
import com.clougence.clouddm.ds.oceanbase.i18n.ObDsI18nKeys;
import com.clougence.clouddm.ds.oceanbase.language.ob4ora.ObOraLanguageSpi;
import com.clougence.clouddm.ds.oceanbase.resource.ObOraEditorResourceSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraEditorProvider;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraTableEditorUiDataSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.exception.OraDetermineExceptionSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.template.OraCmdTemplateSpi;
import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",             //
                            "com.clougence.clouddm.ds.oracle.execute.*",            //
                            "com.clougence.clouddm.ds.oceanbase.execute.obforora.*" //
}, dsProduct = DataSourceType.ObForOracle)
public class ObOraDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.ObForOracle);
        binder.bindTypes(DsType.ObForOracle, ObForOracleTypes.values(), ObForOracleTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.ObForOracle, ObForOracleTypes.values());

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
        dsPlugin.addPluginSpi(new ObForOraConfigSpi());
        dsPlugin.addPluginSpi(new ObForOraSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(ObForOraSessionFactory.class);
        dsPlugin.bindDsDriverFamily("OceanBase Client", "MySQL Connector/J");
        dsPlugin.addPluginSpi(new ObForOraSessionSpi());
        dsPlugin.addPluginSpi(new ObForOraSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(ObDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(OraEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(OracleDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new ObForOraDsBrowseSpi());
        dsPlugin.addPluginSpi(new ObForOraDefService());
        dsPlugin.addPluginSpi(new OraTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new OraCmdTemplateSpi());
        dsPlugin.addPluginSpi(new ObForOraDataEditorSpi());
        dsPlugin.addPluginSpi(new ObDDLSpiConvert());
        dsPlugin.addPluginSpi(new OraDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ObOraLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ObOraEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ObForOraResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ObForOraSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new ObForOraSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ObForOraSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new ObForOraSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
