package com.clougence.clouddm.sec.rules;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.analysis.column.QueryConstraintService;
import com.clougence.clouddm.sec.rules.domain.func.FuncConstraintUtils;
import com.clougence.clouddm.sec.rules.execute.checker.SecRulesCheckerServiceProvider;
import com.clougence.clouddm.sec.rules.execute.sensitive.SecValueProcessServiceProvider;
import com.clougence.detectrule.parser.DetectRuleDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Plugin
public class SecRulesPlugin implements DsPlugin, DsFeatureIDs {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        CacheService cacheService = dsPlugin.findGlobalService(CacheService.class);
        ConfigService configService = dsPlugin.findGlobalService(ConfigService.class);

        DslHelper.register(new DetectRuleDslProvider());

        dsPlugin.addGlobalService(new SecRulesCheckerServiceProvider(cacheService));
        dsPlugin.addGlobalService(new SecValueProcessServiceProvider(cacheService, configService));

        FuncConstraintUtils.INSTANCE.setConstraintService(dsPlugin.findGlobalService(QueryConstraintService.class));

        dsPlugin.addPluginFeature(FUNC_RULE_CHECK_SUPPORT);
    }
}
