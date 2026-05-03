package com.clougence.clouddm.ds.mongodb;

import com.clougence.adapter.mongo.MongoTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.ds.mongodb.analysis.*;
import com.clougence.clouddm.ds.mongodb.analysis.rewrite.MongoRewriteSpi;
import com.clougence.clouddm.ds.mongodb.definition.ui.browser.MongoDsBrowseSpi;
import com.clougence.clouddm.ds.mongodb.definition.ui.template.MongoCmdTemplateSpi;
import com.clougence.clouddm.ds.mongodb.dsconf.MongoConfigSpi;
import com.clougence.clouddm.ds.mongodb.dsconf.MongoSerializationSpi;
import com.clougence.clouddm.ds.mongodb.execute.MongoSessionFactory;
import com.clougence.clouddm.ds.mongodb.execute.MongoSessionSpi;
import com.clougence.clouddm.ds.mongodb.execute.MongoSupportSpi;
import com.clougence.clouddm.ds.mongodb.i18n.MongoDsI18nKeys;
import com.clougence.clouddm.dsfamily.schema.dialect.DefaultDialect;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/**
 * @author mode 2021/4/25 15:13
 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.mongodb.execute.*"//
}, dsProduct = DataSourceType.MongoDB)
public class MongoDsPlugin implements DsPlugin, SchemaPlugin, DsFeatureIDs {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.MongoDB);
        binder.bindTypes(DsType.MongoDB, MongoTypes.values(), MongoTypes::valueOfCode);
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
        dsPlugin.addPluginSpi(new MongoConfigSpi());
        dsPlugin.addPluginSpi(new MongoSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        dsPlugin.bindDsSessionFactory(MongoSessionFactory.class);
        dsPlugin.bindDsDriverFamily("MongoDB Driver");
        dsPlugin.addPluginSpi(new MongoConfigSpi());
        dsPlugin.addPluginSpi(new MongoSupportSpi());
        dsPlugin.addPluginSpi(new MongoSessionSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        dsPlugin.bindPluginI18n(MongoDsI18nKeys.class);
        // defService
        //dsPlugin.bindDefinitionService(new ClickHouseDefinitionService());
        dsPlugin.bindDsDialect(DefaultDialect.DEFAULT);
        // SPIs
        //dsPlugin.addUiSpi(new MySQLTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new MongoDsBrowseSpi());
        dsPlugin.addPluginSpi(new MongoCmdTemplateSpi());
        //        dsPlugin.addSpi(new RedisDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addPluginSpi(new MongoResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MongoSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new MongoSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MongoSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new MongoSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new MongoRewriteSpi());
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
