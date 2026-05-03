package com.clougence.clouddm.ds.oceanbase;

import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.*;
import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.rewrite.ObRewriteSpi;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.ObDefService;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.browser.ObDsBrowseSpi;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.ddl.ObDDLSpiConvert;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.data.ObDataEditorSpi;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.table.ObEditorProvider;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.editor.table.ObTableEditorUiDataSpi;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.exception.ObDetermineExceptionSpi;
import com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.template.ObCmdTemplateSpi;
import com.clougence.clouddm.ds.oceanbase.dialect.obformysql.ObForMySQLDialect;
import com.clougence.clouddm.ds.oceanbase.dsconf.obformysql.ObConfigSpi;
import com.clougence.clouddm.ds.oceanbase.dsconf.obformysql.ObSerializationSpi;
import com.clougence.clouddm.ds.oceanbase.execute.obformysql.ObSessionFactory;
import com.clougence.clouddm.ds.oceanbase.execute.obformysql.ObSessionSpi;
import com.clougence.clouddm.ds.oceanbase.execute.obformysql.ObSupportSpi;
import com.clougence.clouddm.ds.oceanbase.i18n.ObDsI18nKeys;
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
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*",              //
                            "com.clougence.clouddm.dsfamily.mysql.execute.*",        //
                            "com.clougence.clouddm.ds.oceanbase.execute.obformysql.*"//
}, dsProduct = DataSourceType.OceanBase)
public class ObDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.OceanBase);
        binder.bindTypes(DsType.OceanBase, ObForMySQLTypes.values(), ObForMySQLTypes::valueOfCode);
        TypeMapUtils.addColumnTypes(DataSourceType.OceanBase, ObForMySQLTypes.values());
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
        dsPlugin.addPluginSpi(new ObConfigSpi());
        dsPlugin.addPluginSpi(new ObSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(ObSessionFactory.class);
        dsPlugin.bindDsDriverFamily("OceanBase Client", "MySQL Connector/J");
        dsPlugin.addPluginSpi(new ObSessionSpi());
        dsPlugin.addPluginSpi(new ObSupportSpi());
        dsPlugin.addPluginSpi(new ObRewriteSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        dsPlugin.bindPluginI18n(ObDsI18nKeys.class);
        //sqlBuilder
        dsPlugin.bindDsSqlBuilder(ObEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(ObForMySQLDialect.INSTANCE);
        // SPIs
        dsPlugin.addPluginSpi(new ObDsBrowseSpi());
        dsPlugin.addPluginSpi(new ObDefService());
        dsPlugin.addPluginSpi(new ObTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new ObCmdTemplateSpi());
        dsPlugin.addPluginSpi(new ObDataEditorSpi());
        dsPlugin.addPluginSpi(new ObDDLSpiConvert());
        dsPlugin.addPluginSpi(new ObDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new ObResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ObSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new ObSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new ObSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new ObSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        dsPlugin.addPluginFeature(FUNC_LINES_SUPPORT);
    }
}
