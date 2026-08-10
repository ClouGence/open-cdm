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
package com.clougence.clouddm.ds.cloudberry;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.cloudberry.broswer.CbDsBrowseSpi;
import com.clougence.clouddm.ds.cloudberry.definition.CbDefService;
import com.clougence.clouddm.ds.cloudberry.definition.secrules.CbSecRulesSupportSpi;
import com.clougence.clouddm.ds.cloudberry.definition.ui.ddl.CbConvertTableDDLSpi;
import com.clougence.clouddm.ds.cloudberry.definition.ui.editor.data.CbDataEditorSpi;
import com.clougence.clouddm.ds.cloudberry.definition.ui.editor.table.CbEditorProvider;
import com.clougence.clouddm.ds.cloudberry.definition.ui.editor.table.CbTableEditorUiDataSpi;
import com.clougence.clouddm.ds.cloudberry.definition.ui.exception.CbDetermineExceptionSpi;
import com.clougence.clouddm.ds.cloudberry.dsconf.CbConfigSpi;
import com.clougence.clouddm.ds.cloudberry.dsconf.CbSerializationSpi;
import com.clougence.clouddm.ds.cloudberry.execute.CbSessionFactory;
import com.clougence.clouddm.ds.cloudberry.execute.CbSessionSpi;
import com.clougence.clouddm.ds.cloudberry.execute.CbSupportSpi;
import com.clougence.clouddm.ds.cloudberry.i18n.CbDsI18nKeys;
import com.clougence.clouddm.ds.cloudberry.resource.CbEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.dsfamily.postgres.language.PgLanguageSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + CbDsI18nKeys.PLUGIN_NAME_CLOUDBERRY,                   //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*",         //
                            "com.clougence.clouddm.dsfamily.postgres.execute.*",//
                            "com.clougence.clouddm.ds.cloudberry.execute.*"      //
        }, dsProduct = DataSourceType.Cloudberry)
public class CbDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PostgreSQL);
        binder.bindTypes(DsType.PostgreSQL, PostgresTypes.values(), PostgresTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Cloudberry, PostgresTypes.values());
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
        dsPlugin.addPluginSpi(new CbConfigSpi());
        dsPlugin.addPluginSpi(new CbSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(CbSessionFactory.class);
        dsPlugin.bindDsDriverFamily("PostgreSQL JDBC");
        dsPlugin.bindSqlEngine("PG SQL", "ISO-SQL-92", "ISO-SQL-99");

        dsPlugin.addPluginSpi(new CbSessionSpi());
        dsPlugin.addPluginSpi(new CbSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(CbDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(CbEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PostgreDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new CbDsBrowseSpi());
        dsPlugin.addPluginSpi(new CbDefService());
        dsPlugin.addPluginSpi(new CbTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new CbDataEditorSpi());
        dsPlugin.addPluginSpi(new CbConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new CbDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PgLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new CbEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new CbSecRulesSupportSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
