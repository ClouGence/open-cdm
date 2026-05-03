package com.clougence.clouddm.ds.greenplum;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.greenplum.analysis.*;
import com.clougence.clouddm.ds.greenplum.broswer.GpDsBrowseSpi;
import com.clougence.clouddm.ds.greenplum.definition.GpDefService;
import com.clougence.clouddm.ds.greenplum.definition.ui.ddl.GpConvertTableDDLSpi;
import com.clougence.clouddm.ds.greenplum.definition.ui.editor.data.GpDataEditorSpi;
import com.clougence.clouddm.ds.greenplum.definition.ui.editor.table.GpEditorProvider;
import com.clougence.clouddm.ds.greenplum.definition.ui.editor.table.GpTableEditorUiDataSpi;
import com.clougence.clouddm.ds.greenplum.definition.ui.exception.GpDetermineExceptionSpi;
import com.clougence.clouddm.ds.greenplum.dsconf.GpConfigSpi;
import com.clougence.clouddm.ds.greenplum.dsconf.GpSerializationSpi;
import com.clougence.clouddm.ds.greenplum.execute.GpSessionFactory;
import com.clougence.clouddm.ds.greenplum.execute.GpSessionSpi;
import com.clougence.clouddm.ds.greenplum.execute.GpSupportSpi;
import com.clougence.clouddm.ds.greenplum.i18n.GpDsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.dsfamily.postgres.analysis.rewrite.PgRewriteSpi;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
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
                            "com.clougence.clouddm.ds.greenplum.execute.*"      //
}, dsProduct = DataSourceType.Greenplum)
public class GpDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.PostgreSQL);
        binder.bindTypes(DsType.PostgreSQL, PostgresTypes.values(), PostgresTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.Greenplum, PostgresTypes.values());
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
        dsPlugin.addPluginSpi(new GpConfigSpi());
        dsPlugin.addPluginSpi(new GpSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(GpSessionFactory.class);
        dsPlugin.bindDsDriverFamily("PostgreSQL JDBC");
        dsPlugin.addPluginSpi(new GpSessionSpi());
        dsPlugin.addPluginSpi(new GpSupportSpi());
        dsPlugin.addPluginSpi(new PgRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(GpDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(GpEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(PostgreDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new GpDsBrowseSpi());
        dsPlugin.addPluginSpi(new GpDefService());
        dsPlugin.addPluginSpi(new GpTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new PgCmdTemplateSpi());
        dsPlugin.addPluginSpi(new GpDataEditorSpi());
        dsPlugin.addPluginSpi(new GpConvertTableDDLSpi());
        dsPlugin.addPluginSpi(new GpDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new GpResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GpSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new GpSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new GpSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new GpSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
