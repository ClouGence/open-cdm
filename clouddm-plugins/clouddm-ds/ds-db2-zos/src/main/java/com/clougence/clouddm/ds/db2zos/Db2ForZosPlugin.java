package com.clougence.clouddm.ds.db2zos;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.db2zos.definition.Db2ForZosDefService;
import com.clougence.clouddm.ds.db2zos.definition.ui.editor.table.Db2ForZosEditorProvider;
import com.clougence.clouddm.ds.db2zos.dsconf.Db2ForZosConfigSpi;
import com.clougence.clouddm.ds.db2zos.dsconf.Db2ForZosSerializationSpi;
import com.clougence.clouddm.ds.db2zos.execute.Db2ForZosSessionFactory;
import com.clougence.clouddm.ds.db2zos.execute.Db2ForZosSessionSpi;
import com.clougence.clouddm.ds.db2zos.execute.Db2ForZosSupportSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.*;
import com.clougence.clouddm.dsfamily.db2.definition.ui.browser.Db2DsBrowseSpi;
import com.clougence.clouddm.dsfamily.db2.definition.ui.ddl.Db2ConvertTableDDLSpi;
import com.clougence.clouddm.dsfamily.db2.definition.ui.editor.data.Db2DataEditorSpi;
import com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table.Db2TableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.db2.definition.ui.exception.Db2DetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.db2.definition.ui.template.Db2CmdTemplateSpi;
import com.clougence.clouddm.dsfamily.db2.dialect.Db2Dialect;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",    //
                            "com.clougence.clouddm.dsfamily.db2.execute.*",//
                            "com.clougence.clouddm.ds.db2zos.execute.*"    //
}, dsProduct = DataSourceType.Db2)
public class Db2ForZosPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Db2);
        binder.bindTypes(DsType.Db2, Db2Types.values(), Db2Types::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Db2, Db2Types.values());
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
        dsPlugin.addPluginSpi(new Db2ForZosConfigSpi());
        dsPlugin.addPluginSpi(new Db2ForZosSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(Db2ForZosSessionFactory.class);
        dsPlugin.bindDsDriverFamily("IBM JCC", "IBM JTOpen");
        dsPlugin.addPluginSpi(new Db2ForZosSessionSpi());
        dsPlugin.addPluginSpi(new Db2ForZosSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(Db2DsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(Db2ForZosEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(Db2Dialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new Db2DsBrowseSpi());
        dsPlugin.addPluginSpi(new Db2ForZosDefService());
        dsPlugin.addPluginSpi(new Db2TableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new Db2CmdTemplateSpi());
        dsPlugin.addPluginSpi(new Db2DataEditorSpi());
        dsPlugin.addPluginSpi(new Db2ConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new Db2DetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new Db2ResAnalysisSpi());
        dsPlugin.addPluginSpi(new Db2SplitAnalysisSpi());
        dsPlugin.addPluginSpi(new Db2SecDomainResolveSpi());
        dsPlugin.addPluginSpi(new Db2SecRulesSupportSpi());
        dsPlugin.addPluginSpi(new Db2SelectColumnAnalysisSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
