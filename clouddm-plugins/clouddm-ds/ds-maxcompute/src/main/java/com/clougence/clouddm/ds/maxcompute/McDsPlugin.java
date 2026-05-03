package com.clougence.clouddm.ds.maxcompute;

import com.clougence.adapter.mc.McSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.maxcompute.analysis.*;
import com.clougence.clouddm.ds.maxcompute.analysis.rewrite.McRewriteSpi;
import com.clougence.clouddm.ds.maxcompute.definition.ui.McDefService;
import com.clougence.clouddm.ds.maxcompute.definition.ui.browser.McDsBrowseSpi;
import com.clougence.clouddm.ds.maxcompute.definition.ui.ddl.McConvertTableDDLSpi;
import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table.McEditorProvider;
import com.clougence.clouddm.ds.maxcompute.definition.ui.template.McCmdTemplateSpi;
import com.clougence.clouddm.ds.maxcompute.dialect.McDialect;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfigSpi;
import com.clougence.clouddm.ds.maxcompute.dsconf.McSerializationSpi;
import com.clougence.clouddm.ds.maxcompute.execute.McSessionFactory;
import com.clougence.clouddm.ds.maxcompute.execute.McSessionSpi;
import com.clougence.clouddm.ds.maxcompute.execute.McSupportSpi;
import com.clougence.clouddm.ds.maxcompute.i18n.McI18nKeys;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.maxcompute.execute.*" //
}, dsProduct = DataSourceType.MaxCompute)
public class McDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.MaxCompute);
        binder.bindTypes(DsType.MaxCompute, McSqlTypes.values(), McSqlTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.MaxCompute, McSqlTypes.values());
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
        dsPlugin.addPluginSpi(new McConfigSpi());
        dsPlugin.addPluginSpi(new McSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(McSessionFactory.class);
        dsPlugin.bindDsDriverFamily("ODPS JDBC");
        dsPlugin.addPluginSpi(new McSessionSpi());
        dsPlugin.addPluginSpi(new McSupportSpi());
        dsPlugin.addPluginSpi(new McRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(McI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(McEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(McDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new McDsBrowseSpi());
        dsPlugin.addPluginSpi(new McDefService());
        //        dsPlugin.addSpi(new McTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new McCmdTemplateSpi());
        //        dsPlugin.addSpi(new McDataEditorSpi());
        dsPlugin.addPluginSpi(new McConvertTableDDLSpi());
        //        dsPlugin.addSpi(new McDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new McResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new McSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new McSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new McSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new McSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
