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

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.yashandb.dsconf.mysql.YashanDBMySQLConfigSpi;
import com.clougence.clouddm.ds.yashandb.dsconf.mysql.YashanDBMySQLSerializationSpi;
import com.clougence.clouddm.ds.yashandb.execute.YashanDBSessionFactory;
import com.clougence.clouddm.ds.yashandb.i18n.YashanDBDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.definition.MyDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.secrules.MySecRulesSupportSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.ddl.MyConvertTableDDLSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MySessionSpi;
import com.clougence.clouddm.dsfamily.mysql.execute.MySupportSpi;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyConfigI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.language.MyLanguageSpi;
import com.clougence.clouddm.dsfamily.mysql.resource.MyEditorResourceSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;
import com.clougence.sql.mysql.MySqlEngineSpi;

/**
 * YashanDB data source plugin (MySQL compatibility mode).
 * <p>
 * The MySQL compatibility mode of YashanDB reuses the dialect, DDL, object editing and SQL analysis
 * capabilities provided by {@code dsfamily.mysql}, overriding only the connection, session and metadata
 * behaviour where YashanDB differs.
 * </p>
 * <p>
 * It shares the same Gradle module and the same jar with the Oracle compatibility mode
 * ({@link YashanDBOracleDsPlugin}); each is discovered independently by {@code @Plugin} annotation scanning,
 * and the two bind different driver families and session factory branches.
 * </p>
 *
 * @author open-cdm
 */
@Plugin(name = "i18n::" + YashanDBDsI18nKeys.PLUGIN_NAME_YASHANDB_MYSQL,              //
        includePackages = { "com.clougence.clouddm.ds.yashandb.execute.*",            //
                            "com.clougence.clouddm.ds.yashandb.execute.dsfactory.*",  //
                            "com.clougence.clouddm.dsfamily.execute.*",               //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*"          //
        }, dsProduct = DataSourceType.YashanDBMySQL)
public class YashanDBMySQLDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.MySQL);
        binder.bindTypes(DsType.MySQL, MySQLTypes.values(), MySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.YashanDBMySQL, MySQLTypes.values());
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
        dsPlugin.addPluginSpi(new YashanDBMySQLConfigSpi());
        dsPlugin.addPluginSpi(new YashanDBMySQLSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(YashanDBSessionFactory.class);
        dsPlugin.bindDsDriverFamily("YashanDB MySQL JDBC Driver");
        dsPlugin.bindSqlEngine(MySqlEngineSpi.NAME);

        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new MySupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        // i18n
        dsPlugin.bindPluginI18n(YashanDBDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MyDsI18nKeys.class);
        dsPlugin.bindPluginI18n(MyConfigI18nKeys.class);
        // sqlBuilder
        dsPlugin.bindDsSqlBuilder(MyEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(MySqlDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new MyDsBrowseSpi());
        dsPlugin.addPluginSpi(new MyDefService());
        dsPlugin.addPluginSpi(new MyTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new MyConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MyDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new MyLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MyEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new MySecRulesSupportSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
