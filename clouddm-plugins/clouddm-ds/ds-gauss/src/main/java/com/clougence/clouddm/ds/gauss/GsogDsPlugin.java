package com.clougence.clouddm.ds.gauss;

import com.clougence.adapter.gauss.GaussDBTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.gauss.analysis.gsog.*;
import com.clougence.clouddm.ds.gauss.analysis.gsog.rewrite.GsogRewriteSpi;
import com.clougence.clouddm.ds.gauss.broswer.gsog.GsogDsBrowseSpi;
import com.clougence.clouddm.ds.gauss.definition.gsog.GsogDefService;
import com.clougence.clouddm.ds.gauss.definition.gsog.ui.ddl.GsogConvertTableDDLSpi;
import com.clougence.clouddm.ds.gauss.definition.gsog.ui.editor.table.GsogEditorProvider;
import com.clougence.clouddm.ds.gauss.definition.gsog.ui.editor.table.GsogTableEditorUiDataSpi;
import com.clougence.clouddm.ds.gauss.dialect.GaussDBDialect;
import com.clougence.clouddm.ds.gauss.dsconf.gsog.GsogConfigSpi;
import com.clougence.clouddm.ds.gauss.dsconf.gsog.GsogSerializationSpi;
import com.clougence.clouddm.ds.gauss.execute.gsog.GsogSessionFactory;
import com.clougence.clouddm.ds.gauss.execute.gsog.GsogSessionSpi;
import com.clougence.clouddm.ds.gauss.execute.gsog.GsogSupportSpi;
import com.clougence.clouddm.ds.gauss.i18n.gsog.GsogDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data.PgDataEditorSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
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
                            "com.clougence.clouddm.dsfamily.postgres.execute.*",//
                            "com.clougence.clouddm.ds.gauss.execute.gsog.*"//
}, dsProduct = DataSourceType.GaussDBForOpenGauss)
public class GsogDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PostgreSQL);
        binder.bindTypes(DsType.PostgreSQL, PostgresTypes.values(), PostgresTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.GaussDBForOpenGauss, GaussDBTypes.values());
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
        dsPlugin.addPluginSpi(new GsogConfigSpi());
        dsPlugin.addPluginSpi(new GsogSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(GsogSessionFactory.class);
        dsPlugin.bindDsDriverFamily("GaussDB JDBC Driver", "PostgreSQL JDBC");
        dsPlugin.addPluginSpi(new GsogSessionSpi());
        dsPlugin.addPluginSpi(new GsogSupportSpi());
        dsPlugin.addPluginSpi(new GsogRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(GsogDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(GsogEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(GaussDBDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new GsogDsBrowseSpi());
        dsPlugin.addPluginSpi(new GsogDefService());
        dsPlugin.addPluginSpi(new GsogTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new PgDataEditorSpi());
        dsPlugin.addPluginSpi(new GsogConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new PgDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new GsogResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GsogSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new GsogSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GsogSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new GsogSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
