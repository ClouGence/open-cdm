package com.clougence.clouddm.ds.hana;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.hana.analysis.HanaResAnalysisSpi;
import com.clougence.clouddm.ds.hana.analysis.HanaSecDomainResolveSpi;
import com.clougence.clouddm.ds.hana.analysis.HanaSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.hana.analysis.HanaSplitAnalysisSpi;
import com.clougence.clouddm.ds.hana.definition.ui.HanaDefService;
import com.clougence.clouddm.ds.hana.definition.ui.broswer.HanaDsBrowseSpi;
import com.clougence.clouddm.ds.hana.definition.ui.ddl.HanaConvertTableDDLSpi;
import com.clougence.clouddm.ds.hana.definition.ui.editor.data.HanaDataEditorSpi;
import com.clougence.clouddm.ds.hana.definition.ui.editor.table.HanaEditorProvider;
import com.clougence.clouddm.ds.hana.definition.ui.editor.table.HanaTableEditorUiDataSpi;
import com.clougence.clouddm.ds.hana.definition.ui.exception.HanaDetermineExceptionSpi;
import com.clougence.clouddm.ds.hana.definition.ui.template.HanaCmdTemplateSpi;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.ds.hana.dsconf.HanaConfigSpi;
import com.clougence.clouddm.ds.hana.dsconf.HanaSerializationSpi;
import com.clougence.clouddm.ds.hana.execute.HanaSessionFactory;
import com.clougence.clouddm.ds.hana.execute.HanaSessionSpi;
import com.clougence.clouddm.ds.hana.execute.HanaSupportSpi;
import com.clougence.clouddm.ds.hana.i18n.HanaDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.hana.execute.*"   //
}, dsProduct = DataSourceType.Hana)
public class HanaDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Hana);
        binder.bindTypes(DsType.Hana, HanaTypes.values(), HanaTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Hana, HanaTypes.values());
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
        dsPlugin.addPluginSpi(new HanaConfigSpi());
        dsPlugin.addPluginSpi(new HanaSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(HanaSessionFactory.class);
        dsPlugin.bindDsDriverFamily("SAP Hana JDBC");
        dsPlugin.addPluginSpi(new HanaSessionSpi());
        dsPlugin.addPluginSpi(new HanaSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(HanaDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(HanaEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(HanaDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new HanaDsBrowseSpi());
        dsPlugin.addPluginSpi(new HanaDefService());
        dsPlugin.addPluginSpi(new HanaTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new HanaCmdTemplateSpi());
        dsPlugin.addPluginSpi(new HanaDataEditorSpi());
        dsPlugin.addPluginSpi(new HanaConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new HanaDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new HanaResAnalysisSpi());
        dsPlugin.addPluginSpi(new HanaSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new HanaSecDomainResolveSpi());
        dsPlugin.addPluginSpi(new HanaSelectColumnAnalysisSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
