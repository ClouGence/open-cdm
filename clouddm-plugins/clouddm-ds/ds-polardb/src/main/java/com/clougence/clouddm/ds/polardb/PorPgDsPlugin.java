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
package com.clougence.clouddm.ds.polardb;

import com.clougence.adapter.polar.porpg.PolarDBPgTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.polardb.analysis.porpg.*;
import com.clougence.clouddm.ds.polardb.definition.porpg.PorPgDefService;
import com.clougence.clouddm.ds.polardb.definition.porpg.ui.ddl.PorPgConvertTableDDLSpi;
import com.clougence.clouddm.ds.polardb.dsconf.porpg.PorPgConfigSpi;
import com.clougence.clouddm.ds.polardb.dsconf.porpg.PorPgSerializationSpi;
import com.clougence.clouddm.ds.polardb.execute.porpg.PorPgSessionFactory;
import com.clougence.clouddm.ds.polardb.language.porpg.PorPgLanguageSpi;
import com.clougence.clouddm.ds.polardb.resource.PorPgEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.analysis.rewrite.PgRewriteSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.browser.PgDsBrowseSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data.PgDataEditorSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgEditorProvider;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSessionSpi;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSupportSpi;
import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;
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
                            "com.clougence.clouddm.ds.polardb.execute.porpg.*"  //
}, dsProduct = DataSourceType.PolarDBPg)
public class PorPgDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PolarDBPostgre);
        binder.bindTypes(DsType.PolarDBPostgre, PolarDBPgTypes.values(), PolarDBPgTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.PolarDBPg, PolarDBPgTypes.values());
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
        dsPlugin.addPluginSpi(new PorPgConfigSpi());
        dsPlugin.addPluginSpi(new PorPgSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(PorPgSessionFactory.class);
        dsPlugin.bindDsDriverFamily("PostgreSQL JDBC");
        dsPlugin.addPluginSpi(new PgSessionSpi());
        dsPlugin.addPluginSpi(new PgSupportSpi());
        dsPlugin.addPluginSpi(new PgRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(PgDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(PgEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PostgreDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new PgDsBrowseSpi());
        dsPlugin.addPluginSpi(new PorPgDefService());
        dsPlugin.addPluginSpi(new PgTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new PgDataEditorSpi());
        dsPlugin.addPluginSpi(new PorPgConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new PgDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PorPgLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorPgEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PorPgResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorPgSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new PorPgSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorPgSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new PorPgSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
