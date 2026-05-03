package com.clougence.clouddm.ds.polardb;

import com.clougence.adapter.polar.pormy.PolarDBMyTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.polardb.analysis.pormy.*;
import com.clougence.clouddm.ds.polardb.definition.pormy.PorMyDefService;
import com.clougence.clouddm.ds.polardb.definition.pormy.ui.ddl.PorMyConvertTableDDLSpi;
import com.clougence.clouddm.ds.polardb.dialect.pormy.PolarDBMyDialect;
import com.clougence.clouddm.ds.polardb.dsconf.pormy.PorMyConfigSpi;
import com.clougence.clouddm.ds.polardb.dsconf.pormy.PorMySerializationSpi;
import com.clougence.clouddm.ds.polardb.execute.pormy.PorMySessionFactory;
import com.clougence.clouddm.ds.polardb.execute.pormy.PorMySupportSpi;
import com.clougence.clouddm.ds.polardb.i18n.PorMyDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.analysis.rewrite.MyRewriteSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.browser.MyDsBrowseSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
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
                            "com.clougence.clouddm.ds.polardb.execute.pormy.*"//
}, dsProduct = DataSourceType.PolarDbMySQL)
public class PorMyDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PolarDBMySQL);
        binder.bindTypes(DsType.PolarDBMySQL, PolarDBMyTypes.values(), PolarDBMyTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.PolarDbMySQL, PolarDBMyTypes.values());

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
        dsPlugin.addPluginSpi(new PorMyConfigSpi());
        dsPlugin.addPluginSpi(new PorMySerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(PorMySessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new MySessionSpi());
        dsPlugin.addPluginSpi(new PorMySupportSpi());
        dsPlugin.addPluginSpi(new MyRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(PorMyDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(MyEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PolarDBMyDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new MyDsBrowseSpi());
        dsPlugin.addPluginSpi(new PorMyDefService());
        dsPlugin.addPluginSpi(new MyTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new PorMyConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MyDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PorMyResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorMySplitAnalysisSpi());
        dsPlugin.addPluginSpi(new PorMySecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorMySecRulesSupportSpi());
        dsPlugin.addPluginSpi(new PorSelectColumnAnalysisSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
