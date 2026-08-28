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

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.kingbasees.dsconf.oracle.KingbaseESOracleConfigSpi;
import com.clougence.clouddm.ds.kingbasees.dsconf.oracle.KingbaseESOracleSerializationSpi;
import com.clougence.clouddm.ds.kingbasees.execute.KingbaseESSessionFactory;
import com.clougence.clouddm.ds.kingbasees.i18n.KingbaseESDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.oracle.definition.OraDefService;
import com.clougence.clouddm.dsfamily.oracle.definition.secrules.OraSecRulesSupportSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.browser.OraDsBrowseSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.ddl.OraConvertTableDDLSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.editor.data.OraDataEditorSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.editor.table.OraEditorProvider;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.editor.table.OraTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.exception.OraDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.oracle.definition.ui.template.OraCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.oracle.dialect.OracleDialect;
import com.clougence.clouddm.dsfamily.oracle.execute.OraSessionSpi;
import com.clougence.clouddm.dsfamily.oracle.execute.OraSupportSpi;
import com.clougence.clouddm.dsfamily.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.oracle.i18n.OraConfigI18nKeys;
import com.clougence.clouddm.dsfamily.oracle.language.OraLanguageSpi;
import com.clougence.clouddm.dsfamily.oracle.resource.OraEditorResourceSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(name = "i18n::" + KingbaseESDsI18nKeys.PLUGIN_NAME_KINGBASE_ES_ORACLE,             //
        includePackages = { "com.clougence.clouddm.dsfamily.execute.*",                   //
                            "com.clougence.clouddm.dsfamily.oracle.execute.*",            //
                            "com.clougence.clouddm.ds.kingbasees.execute.*",              //
                            "com.clougence.clouddm.ds.kingbasees.execute.dsfactory.*"     //
        }, dsProduct = DataSourceType.KingbaseESOracle)
public class KingbaseESOracleDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Oracle);
        binder.bindTypes(DsType.Oracle, OracleSqlTypes.values(), OracleSqlTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.KingbaseESOracle, OracleSqlTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        dsPlugin.addPluginSpi(new KingbaseESOracleConfigSpi());
        dsPlugin.addPluginSpi(new KingbaseESOracleSerializationSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.bindDsSessionFactory(KingbaseESSessionFactory.class);
        dsPlugin.bindDsDriverFamily("KingbaseES JDBC Driver");
        dsPlugin.bindSqlEngine("Oracle SQL");
        dsPlugin.addPluginSpi(new OraSessionSpi());
        dsPlugin.addPluginSpi(new OraSupportSpi());

        dsPlugin.bindPluginI18n(KingbaseESDsI18nKeys.class);
        dsPlugin.bindPluginI18n(Ora18nKeys.class);
        dsPlugin.bindPluginI18n(OraConfigI18nKeys.class);
        dsPlugin.bindDsSqlBuilder(OraEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(OracleDialect.INSTANCE);
        dsPlugin.addPluginSpi(new OraDsBrowseSpi());
        dsPlugin.addPluginSpi(new OraDefService());
        dsPlugin.addPluginSpi(new OraTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new OraCmdTemplateSpi());
        dsPlugin.addPluginSpi(new OraDataEditorSpi());
        dsPlugin.addPluginSpi(new OraConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new OraDetermineExceptionSpi());
        dsPlugin.addPluginSpi(new OraLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new OraEditorResourceSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.addPluginSpi(new OraSecRulesSupportSpi());
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
