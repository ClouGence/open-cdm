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
package com.clougence.clouddm.ds.yashandb;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.yashandb.dsconf.YashanDBConfigSpi;
import com.clougence.clouddm.ds.yashandb.dsconf.YashanDBSerializationSpi;
import com.clougence.clouddm.ds.yashandb.execute.YashanDBSessionFactory;
import com.clougence.clouddm.ds.yashandb.i18n.YashanDBDsI18nKeys;
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

/**
 * 崖山数据库（YashanDB）数据源插件。
 * <p>
 * 崖山数据库为 Oracle 兼容数据库，因此本插件复用 {@code dsfamily.oracle} 提供的方言、DDL、
 * 对象编辑与 SQL 分析能力，仅在连接、元数据等崖山存在差异处做覆写。
 *
 * @author open-cdm
 */
@Plugin(name = "i18n::" + YashanDBDsI18nKeys.PLUGIN_NAME_YASHANDB,          //
        includePackages = { "com.clougence.clouddm.ds.yashandb.execute.*",          //
                            "com.clougence.clouddm.ds.yashandb.execute.dsfactory.*", //
                            "com.clougence.clouddm.dsfamily.execute.*",             //
                            "com.clougence.clouddm.dsfamily.oracle.execute.*"       //
        }, dsProduct = DataSourceType.YashanDBOracle)
public class YashanDBDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Oracle);
        binder.bindTypes(DsType.Oracle, OracleSqlTypes.values(), OracleSqlTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.YashanDBOracle, OracleSqlTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SchemaFramework.install(this);

        this.configBasic(dsPlugin);
        this.configExecute(dsPlugin);
        this.configUi(dsPlugin);
        this.configEditor(dsPlugin);
        this.configTeam(dsPlugin);
        this.configFeature(dsPlugin);
    }

    private void configBasic(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new YashanDBConfigSpi());
        dsPlugin.addPluginSpi(new YashanDBSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(YashanDBSessionFactory.class);
        dsPlugin.bindDsDriverFamily("YashanDB JDBC Driver");
        dsPlugin.bindSqlEngine("Oracle SQL");

        dsPlugin.addPluginSpi(new OraSessionSpi());
        dsPlugin.addPluginSpi(new OraSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        // i18n
        dsPlugin.bindPluginI18n(YashanDBDsI18nKeys.class);
        dsPlugin.bindPluginI18n(Ora18nKeys.class);
        dsPlugin.bindPluginI18n(OraConfigI18nKeys.class);
        // sqlBuilder
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
        dsPlugin.addPluginSpi(new OraLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new OraEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new OraSecRulesSupportSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
