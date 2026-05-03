package com.clougence.clouddm.ds.polardb;

import com.clougence.adapter.polar.porx.PolarDbXTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.polardb.analysis.porx.*;
import com.clougence.clouddm.ds.polardb.analysis.porx.rewrite.PorXRewriteSpi;
import com.clougence.clouddm.ds.polardb.definition.porx.PorXDefService;
import com.clougence.clouddm.ds.polardb.definition.porx.browser.PorXDsBrowseSpi;
import com.clougence.clouddm.ds.polardb.definition.porx.editor.table.PorXEditorProvider;
import com.clougence.clouddm.ds.polardb.definition.porx.editor.table.PorXTableEditorUiDataSpi;
import com.clougence.clouddm.ds.polardb.definition.porx.ui.ddl.PorXConvertTableDDLSpi;
import com.clougence.clouddm.ds.polardb.dialect.porx.PolarDbXDialect;
import com.clougence.clouddm.ds.polardb.dsconf.porx.PorXConfigSpi;
import com.clougence.clouddm.ds.polardb.dsconf.porx.PorXSerializationSpi;
import com.clougence.clouddm.ds.polardb.execute.porx.PorXSessionFactory;
import com.clougence.clouddm.ds.polardb.execute.porx.PorXSessionSpi;
import com.clougence.clouddm.ds.polardb.execute.porx.PorXSupportSpi;
import com.clougence.clouddm.ds.polardb.i18n.PorXDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.data.MyDataEditorSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.exception.MyDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",      //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*",//
                            "com.clougence.clouddm.ds.polardb.execute.porx.*"//
}, dsProduct = DataSourceType.PolarDbX)
public class PorXDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PolarDbX);
        binder.bindTypes(DsType.PolarDbX, PolarDbXTypes.values(), PolarDbXTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.PolarDbX, PolarDbXTypes.values());
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
        dsPlugin.addPluginSpi(new PorXConfigSpi());
        dsPlugin.addPluginSpi(new PorXSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(PorXSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new PorXSessionSpi());
        dsPlugin.addPluginSpi(new PorXSupportSpi());
        dsPlugin.addPluginSpi(new PorXRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(PorXDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(PorXEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PolarDbXDialect.INSTANCE);
        // table Editor
        dsPlugin.addPluginSpi(new PorXDsBrowseSpi());
        dsPlugin.addPluginSpi(new PorXDefService());
        dsPlugin.addPluginSpi(new PorXTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MyCmdTemplateSpi());
        dsPlugin.addPluginSpi(new MyDataEditorSpi());
        dsPlugin.addPluginSpi(new PorXConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new MyDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new PorXResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorXSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new PorXSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PorXSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new PorXSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
