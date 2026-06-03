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
package com.clougence.clouddm.ds.mariadb;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.mariadb.analysis.*;
import com.clougence.clouddm.ds.mariadb.definition.MarDefService;
import com.clougence.clouddm.ds.mariadb.definition.ui.browser.MarDsBrowseSpi;
import com.clougence.clouddm.ds.mariadb.definition.ui.ddl.MarConvertTableDDLSpi;
import com.clougence.clouddm.ds.mariadb.dsconf.MarConfigSpi;
import com.clougence.clouddm.ds.mariadb.dsconf.MarSqlSerializationSpi;
import com.clougence.clouddm.ds.mariadb.execute.MarSessionFactory;
import com.clougence.clouddm.ds.mariadb.i18n.MarDsI18nKeys;
import com.clougence.clouddm.ds.mariadb.resource.MarEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.analysis.rewrite.MyRewriteSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MySessionSpi;
import com.clougence.clouddm.dsfamily.mysql.execute.MySupportSpi;
import com.clougence.clouddm.dsfamily.mysql.language.MyLanguageSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",      //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*",//
                            "com.clougence.clouddm.ds.mariadb.execute.*"     //
}, dsProduct = DataSourceType.MariaDB)
public class MarDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.MySQL);
        binder.bindTypes(DsType.MySQL, MySQLTypes.values(), MySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.MariaDB, MySQLTypes.values());
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
        dsPlugin.addPluginSpi(new MarConfigSpi());
        dsPlugin.addPluginSpi(new MarSqlSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(MarSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MariaDB Java Client", "MySQL Connector/J");
        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new MySupportSpi());
        dsPlugin.addPluginSpi(new MyRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(MarDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(MyEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(MySqlDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new MarDsBrowseSpi());
        dsPlugin.addPluginSpi(new MarDefService());
        dsPlugin.addPluginSpi(new MyTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new MarConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MyDetermineExceptionSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new MyLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MarEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new MarResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MarSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new MarSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MarSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new MarSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
