/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.component.detectrule.impl;

import java.util.*;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.detectrule.*;
import com.clougence.clouddm.console.web.component.detectrule.impl.SecRulesCheckSession4Batch.PreparedRule;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ObjectQueryDao;
import com.clougence.clouddm.platform.dal.access.entry.DsEntry;
import com.clougence.clouddm.platform.dal.access.entry.EnvEntry;
import com.clougence.clouddm.platform.dal.access.entry.UserEntry;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;
import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
@Service
public class SecRulesEngineImpl implements SecRulesEngine {

    @Resource
    private SecRulesService   secRulesService;
    @Resource
    private ObjectQueryDao    objectQueryDao;
    @Resource
    private DmDsConfigService configService;

    @Override
    public SecRulesCheckSession openQueryCheck(String currentUid, DataSourceConfig dsConfig, SecRulesCheckContext context) {
        if (!PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            log.warn("rule sec plugin is not init. or not exist.");
            return SecRulesCheckSession4Disabled.getInstance();
        }

        DsEntry dsEntry = this.objectQueryDao.queryByDsId(context.getDsId());
        return this.openQueryCheck(dsEntry.getOwnerUid(), currentUid, dsConfig, context, dsEntry);
    }

    private SecRulesCheckSession openQueryCheck(String ownerUid, String currentUid, DataSourceConfig dsConfig, SecRulesCheckContext context, DsEntry dsEntry) {
        if (!PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            log.warn("rule sec plugin is not init. or not exist.");
            return SecRulesCheckSession4Disabled.getInstance();
        }

        SecCheckerRules rules = this.secRulesService.fetchCheckerRules(ownerUid, context.getDsId());
        if (!rules.isValid() || CollectionUtils.isEmpty(rules.getQueryRuleList())) {
            return SecRulesCheckSession4Disabled.getInstance();
        }

        DataSourceType dsType = rules.getDsType();
        Map<UmiTypes, Object> levelsParam = CollectionUtils.asMap(UmiTypes.Catalog, context.getCurrentCatalog(), UmiTypes.Schema, context.getCurrentSchema());
        ContextInfo contextInfo = ContextInfo.builder().puid(ownerUid).cuid(currentUid).dsId(context.getDsId()).levelsParam(levelsParam).dataSourceConfig(dsConfig).build();

        SqlEngineSpi sqlEngine = this.configService.fetchSqlEngineSpi(dsConfig);
        SecDomainResolveSpi resolveSpi = sqlEngine == null ? null : sqlEngine.secDomainResolveSpi(context.getSqlParameters());
        if (resolveSpi == null) {
            return SecRulesCheckSession4Disabled.getInstance();
        }

        List<PreparedRule> preparedRules = new ArrayList<>(rules.getQueryRuleList().size());
        for (CheckerRule checkerRule : rules.getQueryRuleList()) {
            if (checkerRule.getLevel() != RuleLevel.PASS) {
                preparedRules
                    .add(new PreparedRule(checkerRule, DmConvertUtils.tryRuleI18nMessage(checkerRule.getRuleName()), DmConvertUtils.tryRuleI18nMessage(checkerRule.getRuleDesc())));
            }
        }

        List<PreparedRule> genericRules = new ArrayList<>();
        Map<TargetType, List<PreparedRule>> rulesByTarget = new EnumMap<>(TargetType.class);
        for (TargetType targetType : TargetType.values()) {
            rulesByTarget.put(targetType, new ArrayList<>());
        }
        for (PreparedRule preparedRule : preparedRules) {
            TargetType target = preparedRule.checkerRule().getTarget();
            if (target == null) {
                genericRules.add(preparedRule);
                rulesByTarget.values().forEach(r -> r.add(preparedRule));
            } else {
                rulesByTarget.get(target).add(preparedRule);
            }
        }
        rulesByTarget.replaceAll((target, r) -> List.copyOf(r));

        UserEntry userEntry = this.objectQueryDao.queryByUid(context.getCurrentUID());
        EnvEntry envEntry = this.objectQueryDao.queryByEnvId(dsEntry.getEnvId());
        DsBrowseSpi browseSpi = PluginManager.findDsBrowseSpi(dsType);
        return new SecRulesCheckSession4Batch(dsType,
            rules.getDsUseSpecName(),
            resolveSpi,
            contextInfo,
            dsEntry,
            userEntry,
            envEntry,
            browseSpi,
            this.secRulesService.checkerSpi(),
            context,
            List.copyOf(genericRules),
            Collections.unmodifiableMap(rulesByTarget));
    }
}
