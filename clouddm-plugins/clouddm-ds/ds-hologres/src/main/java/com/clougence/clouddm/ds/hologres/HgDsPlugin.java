package com.clougence.clouddm.ds.hologres;

import com.clougence.adapter.hologres.HgSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.hologres.analysis.HgResAnalysisSpi;
import com.clougence.clouddm.ds.hologres.analysis.HgSecDomainResolveSpi;
import com.clougence.clouddm.ds.hologres.analysis.HgSplitAnalysisSpi;
import com.clougence.clouddm.ds.hologres.broswer.HgDsBrowseSpi;
import com.clougence.clouddm.ds.hologres.definition.ui.template.HgCmdTemplateSpi;
import com.clougence.clouddm.ds.hologres.dsconf.HgConfigSpi;
import com.clougence.clouddm.ds.hologres.dsconf.HgSerializationSpi;
import com.clougence.clouddm.ds.hologres.execute.HgSessionFactory;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecRulesSupportSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.rewrite.PgRewriteSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data.PgDataEditorSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgEditorProvider;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgTableEditorUiDataSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSessionSpi;
import com.clougence.clouddm.dsfamily.postgres.execute.PgSupportSpi;
import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",         //
                            "com.clougence.clouddm.dsfamily.postgres.execute.*",//
                            "com.clougence.clouddm.ds.hologres.execute.*"       //
}, dsProduct = DataSourceType.Hologres)
public class HgDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Hologres);
        binder.bindTypes(DsType.Hologres, HgSqlTypes.values(), HgSqlTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Hologres, HgSqlTypes.values());
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
        dsPlugin.addPluginSpi(new HgConfigSpi());
        dsPlugin.addPluginSpi(new HgSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(HgSessionFactory.class);
        dsPlugin.bindDsDriverFamily("PostgreSQL JDBC");
        dsPlugin.addPluginSpi(new PgSessionSpi());
        dsPlugin.addPluginSpi(new PgSupportSpi());
        dsPlugin.addPluginSpi(new PgRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(PgDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(PgEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PostgreDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new HgDsBrowseSpi());
        //        dsPlugin.addSpi(new PgDefService());
        dsPlugin.addPluginSpi(new PgTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new HgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new PgDataEditorSpi());
        //        dsPlugin.addSpi(new PgConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new PgDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new HgResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new HgSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new HgSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new PgSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new PgSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
