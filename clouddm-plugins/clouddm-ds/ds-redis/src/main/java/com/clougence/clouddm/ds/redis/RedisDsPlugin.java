package com.clougence.clouddm.ds.redis;

import com.clougence.adapter.redis.RedisTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.redis.analysis.*;
import com.clougence.clouddm.ds.redis.definition.auth.RedisAuthInfoSpi;
import com.clougence.clouddm.ds.redis.definition.ui.browser.RedisDsBrowseSpi;
import com.clougence.clouddm.ds.redis.definition.ui.exception.RedisDetermineExceptionSpi;
import com.clougence.clouddm.ds.redis.definition.ui.template.RedisCmdTemplateSpi;
import com.clougence.clouddm.ds.redis.dialect.RedisDialect;
import com.clougence.clouddm.ds.redis.dsconf.RedisConfigSpi;
import com.clougence.clouddm.ds.redis.dsconf.RedisSerializationSpi;
import com.clougence.clouddm.ds.redis.execute.RedisSessionFactory;
import com.clougence.clouddm.ds.redis.execute.RedisSessionSpi;
import com.clougence.clouddm.ds.redis.execute.RedisSupportSpi;
import com.clougence.clouddm.ds.redis.parser.RedisDslProvider;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.SchemaPlugin;

/** @author mode 2024/12/25 15:13 */
@Plugin(includePackages = { "com.clougence.clouddm.dsfamily.execute.*", //
                            "com.clougence.clouddm.ds.redis.execute.*"  //
}, dsProduct = DataSourceType.Redis)
public class RedisDsPlugin implements DsPlugin, SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        binder.initMappingService(DsType.Redis);
        binder.bindTypes(DsType.Redis, RedisTypes.values(), RedisTypes::valueOfCode);
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
        dsPlugin.addPluginSpi(new RedisConfigSpi());
        dsPlugin.addPluginSpi(new RedisSerializationSpi(dsPlugin.getPluginClassLoader()));
    }

    private void configExecute(DsPluginBinder dsPlugin) {
        DslHelper.register(new RedisDslProvider());

        dsPlugin.bindDsSessionFactory(RedisSessionFactory.class);
        dsPlugin.bindDsDriverFamily("Jedis");
        dsPlugin.addPluginSpi(new RedisSessionSpi());
        dsPlugin.addPluginSpi(new RedisSupportSpi());
    }

    private void configUi(DsPluginBinder dsPlugin) {
        //initI18n
        //dsPlugin.bindI18n(false, Ora18nKeys.class);
        //sqlBuilder
        //dsPlugin.bindSqlBuilder(OraEditorProvider.INSTANCE);
        dsPlugin.bindDsDialect(RedisDialect.INSTANCE);

        // SPIs
        //dsPlugin.addUiSpi(new MySQLTableEditorUiDataSpi());
        dsPlugin.addPluginSpi(new RedisDsBrowseSpi());
        dsPlugin.addPluginSpi(new RedisCmdTemplateSpi());
        dsPlugin.addPluginSpi(new RedisDetermineExceptionSpi());
    }

    private void configTeam(DsPluginBinder dsPlugin) {
        // SPIs
        dsPlugin.addGlobalSpi(new RedisAuthInfoSpi());
        dsPlugin.addPluginSpi(new RedisResAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new RedisSplitAnalysisSpi());
        dsPlugin.addPluginSpi(new RedisSecDomainResolveSpi(dsPlugin.findGlobalService(MetaService.class)));
        dsPlugin.addPluginSpi(new RedisSecRulesSupportSpi());
        dsPlugin.addPluginSpi(new RedisSelectColumnAnalysisSpi(dsPlugin.findGlobalService(MetaService.class)));
    }

    private void configFeature(DsPluginBinder dsPlugin) {
        // dsPlugin.addFeature(FUNC_LINES_SUPPORT);
    }
}
