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
package com.clougence.clouddm.ds.clickhouse;

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.clickhouse.analysis.*;
import com.clougence.clouddm.ds.clickhouse.analysis.rewrite.ChRewriteSpi;
import com.clougence.clouddm.ds.clickhouse.definition.ChDefService;
import com.clougence.clouddm.ds.clickhouse.definition.ui.browser.ChDsBrowseSpi;
import com.clougence.clouddm.ds.clickhouse.definition.ui.ddl.ChConvertTableDDLSpi;
import com.clougence.clouddm.ds.clickhouse.definition.ui.editor.data.ChDataEditorSpi;
import com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table.ChEditorProvider;
import com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table.ChTableEditorUiDataSpi;
import com.clougence.clouddm.ds.clickhouse.definition.ui.exception.ChDetermineExceptionSpi;
import com.clougence.clouddm.ds.clickhouse.definition.ui.template.ChCmdTemplateSpi;
import com.clougence.clouddm.ds.clickhouse.dialect.ClickHouseDialect;
import com.clougence.clouddm.ds.clickhouse.dsconf.ChConfigSpi;
import com.clougence.clouddm.ds.clickhouse.dsconf.ChSerializationSpi;
import com.clougence.clouddm.ds.clickhouse.execute.ChSessionFactory;
import com.clougence.clouddm.ds.clickhouse.execute.ChSupportSpi;
import com.clougence.clouddm.ds.clickhouse.i18n.ChDsI18nKeys;
import com.clougence.clouddm.ds.clickhouse.language.ChLanguageSpi;
import com.clougence.clouddm.ds.clickhouse.resource.ChEditorResourceSpi;
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
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",     //
                            "com.clougence.clouddm.ds.clickhouse.execute.*" //
}, dsProduct = DataSourceType.ClickHouse)
public class ChPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.ClickHouse);
        binder.bindTypes(DsType.ClickHouse, ClickHouseTypes.values(), ClickHouseTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.ClickHouse, ClickHouseTypes.values());
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
        dsPlugin.addPluginSpi(new ChConfigSpi());
        dsPlugin.addPluginSpi(new ChSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(ChSessionFactory.class);
        dsPlugin.bindDsDriverFamily("ClickHouse JDBC", "Yandex JDBC", "Native JDBC");
        dsPlugin.addPluginSpi(new RdbSessionSpi());
        dsPlugin.addPluginSpi(new ChSupportSpi());
        dsPlugin.addPluginSpi(new ChRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(ChDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(ChEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(ClickHouseDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new ChDsBrowseSpi());
        dsPlugin.addPluginSpi(new ChDefService());
        dsPlugin.addPluginSpi(new ChTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new ChCmdTemplateSpi());
        dsPlugin.addPluginSpi(new ChDataEditorSpi());
        dsPlugin.addPluginSpi(new ChConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new ChDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ChLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ChEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ChResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ChSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new ChSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ChSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ChSecRulesSupportSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        //dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
