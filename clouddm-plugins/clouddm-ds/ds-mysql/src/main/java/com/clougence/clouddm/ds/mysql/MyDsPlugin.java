package com.clougence.clouddm.ds.mysql;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.mysql.definition.MyDefService;
import com.clougence.clouddm.ds.mysql.dsconf.MyConfigSpi;
import com.clougence.clouddm.ds.mysql.dsconf.MySerializationSpi;
import com.clougence.clouddm.ds.mysql.execute.MySessionFactory;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.analysis.*;
import com.clougence.clouddm.dsfamily.mysql.analysis.rewrite.MyRewriteSpi;
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
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
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
                            "com.clougence.clouddm.ds.mysql.execute.*"       //
}, dsProduct = DataSourceType.MySQL)
public class MyDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.MySQL);
        binder.bindTypes(DsType.MySQL, MySQLTypes.values(), MySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.MySQL, MySQLTypes.values());
    }

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        // init schema plugin
        SchemaFramework.install(this);

        this.configBasic(dsPlugin);
        this.configExecute(dsPlugin);
        this.configUi(dsPlugin);
        this.configTeam(dsPlugin);
        this.configFeature(dsPlugin);
    }

    private void configBasic(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginSpi(new MyConfigSpi());
        dsPlugin.addPluginSpi(new MySerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(MySessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new MySupportSpi());
        dsPlugin.addPluginSpi(new MyRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(MyDsI18nKeys.class);
        //sqlBuilder
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

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new MyResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MySplitAnalysisSpi());
        dsPlugin.addPluginSpi(new MySecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MySecRulesSupportSpi());
        dsPlugin.addPluginSpi(new MySelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
