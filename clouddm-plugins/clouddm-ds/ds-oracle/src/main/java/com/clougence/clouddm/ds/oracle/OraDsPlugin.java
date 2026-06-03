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
package com.clougence.clouddm.ds.oracle;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.oracle.analysis.*;
import com.clougence.clouddm.ds.oracle.definition.ui.OraDefService;
import com.clougence.clouddm.ds.oracle.definition.ui.browser.OraDsBrowseSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.ddl.OraConvertTableDDLSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.data.OraDataEditorSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraEditorProvider;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraTableEditorUiDataSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.exception.OraDetermineExceptionSpi;
import com.clougence.clouddm.ds.oracle.definition.ui.template.OraCmdTemplateSpi;
import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.clouddm.ds.oracle.dsconf.OraConfigSpi;
import com.clougence.clouddm.ds.oracle.dsconf.OraSerializationSpi;
import com.clougence.clouddm.ds.oracle.execute.OraSessionFactory;
import com.clougence.clouddm.ds.oracle.execute.OraSessionSpi;
import com.clougence.clouddm.ds.oracle.execute.OraSupportSpi;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.ds.oracle.language.OraLanguageSpi;
import com.clougence.clouddm.ds.oracle.resource.OraEditorResourceSpi;
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
                            "com.clougence.clouddm.ds.oracle.execute.*" //
}, dsProduct = DataSourceType.Oracle)
public class OraDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Oracle);
        binder.bindTypes(DsType.Oracle, OracleSqlTypes.values(), OracleSqlTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Oracle, OracleSqlTypes.values());
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
        dsPlugin.addPluginSpi(new OraConfigSpi());
        dsPlugin.addPluginSpi(new OraSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(OraSessionFactory.class);
        dsPlugin.bindDsDriverFamily("Oracle JDBC Driver");
        dsPlugin.addPluginSpi(new OraSessionSpi());
        dsPlugin.addPluginSpi(new OraSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(Ora18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(OraEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(OracleDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new OraDsBrowseSpi());
        dsPlugin.addPluginSpi(new OraDefService());
        dsPlugin.addPluginSpi(new OraTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new OraCmdTemplateSpi());
        dsPlugin.addPluginSpi(new OraDataEditorSpi());
        dsPlugin.addPluginSpi(new OraConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new OraDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new OraLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new OraEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new OraResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new OraSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new OraSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new OraSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new OraSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
