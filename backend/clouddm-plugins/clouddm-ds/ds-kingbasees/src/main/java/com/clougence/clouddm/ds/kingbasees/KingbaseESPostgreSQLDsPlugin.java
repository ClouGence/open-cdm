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
package com.clougence.clouddm.ds.kingbasees;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.kingbasees.definition.KingbaseESPostgreSQLDefService;
import com.clougence.clouddm.ds.kingbasees.definition.secrules.KingbaseESPostgreSQLSecRulesSupportSpi;
import com.clougence.clouddm.ds.kingbasees.definition.ui.ddl.KingbaseESPostgreSQLConvertTableDDLSpi;
import com.clougence.clouddm.ds.kingbasees.dsconf.postgresql.KingbaseESPostgreSQLConfigSpi;
import com.clougence.clouddm.ds.kingbasees.dsconf.postgresql.KingbaseESPostgreSQLSerializationSpi;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESSessionFactory;
import com.clougence.clouddm.ds.kingbasees.i18n.KingbaseESDsI18nKeys;
import com.clougence.clouddm.ds.kingbasees.resource.KingbaseESPostgreSQLEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.browser.PgDsBrowseSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data.PgDataEditorSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgEditorProvider;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSessionSpi;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSupportSpi;
import com.clougence.clouddm.dsfamily.postgres.language.PgLanguageSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + KingbaseESDsI18nKeys.PLUGIN_NAME_KINGBASE_ES_POSTGRESQL,         //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*",                   //
                            "com.clougence.clouddm.dsfamily.postgres.execute.*",          //
                            "com.clougence.clouddm.ds.kingbasees.execute.*",              //
                            "com.clougence.clouddm.ds.kingbasees.execute.dsfactory.*"     //
        }, dsProduct = DataSourceType.KingbaseESPostgreSQL)
public class KingbaseESPostgreSQLDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PostgreSQL);
        binder.bindTypes(DsType.PostgreSQL, PostgresTypes.values(), PostgresTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.KingbaseESPostgreSQL, PostgresTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        dsPlugin.addPluginSpi(new KingbaseESPostgreSQLConfigSpi());
        dsPlugin.addPluginSpi(new KingbaseESPostgreSQLSerializationSpi(dsPlugin.getPluginClassLoader()));

        dsPlugin.bindDsSessionFactory(KingbaseESSessionFactory.class);
        dsPlugin.bindDsDriverFamily("KingbaseES JDBC Driver");
        dsPlugin.bindSqlEngine("PG SQL", "ISO-SQL-92", "ISO-SQL-99");
        dsPlugin.addPluginSpi(new PgSessionSpi());
        dsPlugin.addPluginSpi(new PgSupportSpi());

        dsPlugin.bindPluginI18n(KingbaseESDsI18nKeys.class);
        dsPlugin.bindDsSqlBuilder(PgEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PostgreDialect.INSTANCE);
        dsPlugin.addPluginSpi(new PgDsBrowseSpi());
        dsPlugin.addPluginSpi(new KingbaseESPostgreSQLDefService());
        dsPlugin.addPluginSpi(new PgTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new PgDataEditorSpi());
        dsPlugin.addPluginSpi(new KingbaseESPostgreSQLConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new PgDetermineExceptionSpi());

        dsPlugin.addPluginSpi(new PgLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new KingbaseESPostgreSQLEditorResourceSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.addPluginSpi(new KingbaseESPostgreSQLSecRulesSupportSpi());
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
