package com.clougence.clouddm.ds.selectdb;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.doris.definition.DrDefService;
import com.clougence.clouddm.ds.doris.definition.ui.browser.DrDsBrowseSpi;
import com.clougence.clouddm.ds.doris.definition.ui.ddl.DrConvertTableDDLSpi;
import com.clougence.clouddm.ds.doris.definition.ui.editor.data.DrDataEditorSpi;
import com.clougence.clouddm.ds.doris.definition.ui.editor.table.DrEditorProvider;
import com.clougence.clouddm.ds.doris.definition.ui.editor.table.DrTableEditorUiDataSpi;
import com.clougence.clouddm.ds.doris.definition.ui.exception.DrDetermineExceptionSpi;
import com.clougence.clouddm.ds.doris.definition.ui.template.DrCmdTemplateSpi;
import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.ds.selectdb.analysis.*;
import com.clougence.clouddm.ds.selectdb.analysis.rewrite.SelRewriteSpi;
import com.clougence.clouddm.ds.selectdb.dsconf.SelConfigSpi;
import com.clougence.clouddm.ds.selectdb.dsconf.SelSerializationSpi;
import com.clougence.clouddm.ds.selectdb.execute.SelSessionFactory;
import com.clougence.clouddm.ds.selectdb.execute.SelSupportSpi;
import com.clougence.clouddm.ds.selectdb.i18n.SelDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
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
                            "com.clougence.clouddm.ds.selectdb.execute.*",   //
                            "com.clougence.clouddm.ds.doris.execute.*"       //
}, dsProduct = DataSourceType.SelectDB)
public class SelPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.SelectDB);
        binder.bindTypes(DsType.SelectDB, DorisTypes.values(), DorisTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.SelectDB, DorisTypes.values());
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
        dsPlugin.addPluginSpi(new SelConfigSpi());
        dsPlugin.addPluginSpi(new SelSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(SelSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MySQL Connector/J");
        dsPlugin.addPluginSpi(new RdbSessionSpi());
        dsPlugin.addPluginSpi(new SelSupportSpi());
        dsPlugin.addPluginSpi(new SelRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(SelDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(DrEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(DorisDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new DrDsBrowseSpi());
        dsPlugin.addPluginSpi(new DrDefService());
        dsPlugin.addPluginSpi(new DrTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new DrCmdTemplateSpi());
        dsPlugin.addPluginSpi(new DrDataEditorSpi());
        dsPlugin.addPluginSpi(new DrConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new DrDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new SelResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new SelSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new SelSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new SelSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new SelSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
