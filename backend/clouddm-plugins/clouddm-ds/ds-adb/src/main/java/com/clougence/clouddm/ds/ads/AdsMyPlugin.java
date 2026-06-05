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
package com.clougence.clouddm.ds.ads;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.ads.analysis.ads4my.*;
import com.clougence.clouddm.ds.ads.analysis.ads4my.rewrite.AdbRewriteSpi;
import com.clougence.clouddm.ds.ads.definition.AdsMyDefService;
import com.clougence.clouddm.ds.ads.definition.ui.browser.AdsMyDsBrowseSpi;
import com.clougence.clouddm.ds.ads.definition.ui.ddl.AdsMyConvertTableDDLSpi;
import com.clougence.clouddm.ds.ads.definition.ui.editor.table.AdbMyEditorProvider;
import com.clougence.clouddm.ds.ads.definition.ui.template.AdsMyCmdTemplateSpi;
import com.clougence.clouddm.ds.ads.dialect.ads4my.AdbMySqlDialect;
import com.clougence.clouddm.ds.ads.dsconf.ads4my.AdsMyConfigSpi;
import com.clougence.clouddm.ds.ads.dsconf.ads4my.AdsMySerializationSpi;
import com.clougence.clouddm.ds.ads.execute.ads4my.AdsMySessionFactory;
import com.clougence.clouddm.ds.ads.execute.ads4my.AdsSupportSpi;
import com.clougence.clouddm.ds.ads.i18n.AdsMyDsI18nKeys;
import com.clougence.clouddm.ds.ads.language.ads4my.AdsMyLanguageSpi;
import com.clougence.clouddm.ds.ads.resource.ads4my.AdsMyEditorResourceSpi;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.mysql.execute.MySessionSpi;
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
                            "com.clougence.clouddm.ds.ads.execute.*"         //
}, dsProduct = DataSourceType.AdbForMySQL)
public class AdsMyPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.AdbForMySQL);
        binder.bindTypes(DsType.AdbForMySQL, AdbMySQLTypes.values(), AdbMySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.AdbForMySQL, AdbMySQLTypes.values());
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
        dsPlugin.addPluginSpi(new AdsMyConfigSpi());
        dsPlugin.addPluginSpi(new AdsMySerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(AdsMySessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new AdsSupportSpi());
        dsPlugin.addPluginSpi(new AdbRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(AdsMyDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(AdbMyEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(AdbMySqlDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new AdsMyDsBrowseSpi());
        dsPlugin.addPluginSpi(new AdsMyDefService());
        dsPlugin.addPluginSpi(new MyTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new AdsMyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new AdsMyConvertTableDDLSpi());
    }

    private void configEditor(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new AdsMyLanguageSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new AdsMyEditorResourceSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new AdsMyResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new AdsMySplitAnalysisSpi());
        dsPlugin.addPluginSpi(new AdsMySecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new AdsMySecRulesSupportSpi());
        dsPlugin.addPluginSpi(new AdsMySelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
