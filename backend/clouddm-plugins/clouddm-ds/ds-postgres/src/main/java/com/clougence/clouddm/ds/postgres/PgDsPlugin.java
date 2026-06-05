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
package com.clougence.clouddm.ds.postgres;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.postgres.definition.PgDefService;
import com.clougence.clouddm.ds.postgres.definition.ui.ddl.PgConvertTableDDLSpi;
import com.clougence.clouddm.ds.postgres.dsconf.PgConfigSpi;
import com.clougence.clouddm.ds.postgres.dsconf.PgSerializationSpi;
import com.clougence.clouddm.ds.postgres.execute.PgSessionFactory;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.analysis.*;
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
import com.clougence.clouddm.dsfamily.postgres.language.PgLanguageSpi;
import com.clougence.clouddm.dsfamily.postgres.resource.PgEditorResourceSpi;
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
                            "com.clougence.clouddm.ds.postgres.execute.*"       //
}, dsProduct = DataSourceType.PostgreSQL)
public class PgDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PostgreSQL);
        binder.bindTypes(DsType.PostgreSQL, PostgresTypes.values(), PostgresTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.PostgreSQL, PostgresTypes.values());
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
        dsPlugin.addPluginSpi(new PgConfigSpi());
        dsPlugin.addPluginSpi(new PgSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(PgSessionFactory.class);
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
        dsPlugin.addPluginSpi(new PgDefService());
        dsPlugin.addPluginSpi(new PgTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new PgDataEditorSpi());
        dsPlugin.addPluginSpi(new PgConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new PgDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PgLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PgEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PgResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PgSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new PgSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PgSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new PgSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
