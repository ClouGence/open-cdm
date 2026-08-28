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

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckSession;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.access.entry.EnvCacheEntry;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.sdk.service.secrules.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.utils.CollectionUtils;

final class SecRulesCheckSession4Batch implements SecRulesCheckSession {

    private final DataSourceType                      dsType;
    private final String                              specName;
    private final SecDomainResolveSpi                 resolveSpi;
    private final ContextInfo                         contextInfo;
    private final DsCacheEntry                        dsCache;
    private final UserCacheEntry                      userCache;
    private final EnvCacheEntry                       envCache;
    private final DsBrowseSpi                         browseSpi;
    private final SecRulesCheckerService              checkerSpi;
    private final String                              currentCatalog;
    private final String                              currentSchema;
    private final Requester                           requester;
    private final WarnLevel                           unsupportedLevel;
    private final List<PreparedRule>                  genericRules;
    private final Map<TargetType, List<PreparedRule>> rulesByTarget;

    SecRulesCheckSession4Batch(DataSourceType dsType, String specName, SecDomainResolveSpi resolveSpi, ContextInfo contextInfo, DsCacheEntry dsCache, UserCacheEntry userCache,
                               EnvCacheEntry envCache, DsBrowseSpi browseSpi, SecRulesCheckerService checkerSpi, SecRulesCheckContext context, List<PreparedRule> genericRules,
                               Map<TargetType, List<PreparedRule>> rulesByTarget){
        this.dsType = dsType;
        this.specName = specName;
        this.resolveSpi = resolveSpi;
        this.contextInfo = contextInfo;
        this.dsCache = dsCache;
        this.userCache = userCache;
        this.envCache = envCache;
        this.browseSpi = browseSpi;
        this.checkerSpi = checkerSpi;
        this.currentCatalog = context.getCurrentCatalog();
        this.currentSchema = context.getCurrentSchema();
        this.requester = context.getRequester();
        this.unsupportedLevel = context.getUnsupportedLevel();
        this.genericRules = genericRules;
        this.rulesByTarget = rulesByTarget;
    }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public SecRulesCheckResult applyCheck(String querySql, int baseCodeLine, int baseCodeColumn) {
        List<RuleDomain> domainList;
        try {
            try (StringReader reader = new StringReader(querySql); Stream<RuleDomain> stream = this.resolveSpi.resolveDomainStream(//
                    this.dsType, reader, baseCodeLine, baseCodeColumn, this.contextInfo)) {
                domainList = stream.toList();
            }
            if (CollectionUtils.isEmpty(domainList)) {
                return this.resultUnsupported();
            }
        } catch (UnsupportedOperationException e) {
            return this.resultUnsupported();
        }

        SecRulesCheckResult result = new SecRulesCheckResult();
        result.setSpecName(this.specName);
        for (RuleDomain ruleDomain : domainList) {
            List<PreparedRule> checkerRules = ruleDomain.getSqlTarget() == null ? this.genericRules : this.rulesByTarget.get(ruleDomain.getSqlTarget());
            if (CollectionUtils.isEmpty(checkerRules)) {
                continue;
            }

            CheckerData checkerDomain = new CheckerData(querySql, ruleDomain);
            checkerDomain.setDsLevelsDef(this.browseSpi.getLevels());
            checkerDomain.setCurrentCatalog(this.currentCatalog);
            checkerDomain.setCurrentSchema(this.currentSchema);
            checkerDomain.setStartLine(ruleDomain.getSplitScript().getBodyStartCodeLine());
            checkerDomain.setStartColumn(ruleDomain.getSplitScript().getBodyStartCodeColumn());
            checkerDomain.getDomain().setEnvId(this.dsCache.getEnvId());
            checkerDomain.getDomain().setEnvName(this.envCache.getEnvName());
            checkerDomain.getDomain().setDsId(this.dsCache.getDsNumId());
            checkerDomain.getDomain().setDsName(this.dsCache.getDsInstId());
            checkerDomain.getDomain().setDsType(this.dsCache.getDsType());
            checkerDomain.getDomain().setUserName(this.userCache.getUserName());
            checkerDomain.getDomain().setUserRole(this.userCache.getRoleName());
            checkerDomain.getDomain().setPrimaryUid(this.userCache.getParentUid());

            this.doCheckDomain(checkerDomain, checkerRules, result);
        }
        return result;
    }

    private void doCheckDomain(CheckerData checkerDomain, List<PreparedRule> rules, SecRulesCheckResult result) {
        CheckerOptions options = new CheckerOptions();
        options.setDsType(checkerDomain.getDomain().getDsType());
        options.setRequester(this.requester);

        for (PreparedRule preparedRule : rules) {
            CheckerRule checker = preparedRule.checkerRule();
            options.setParameters(checker.getParameters());

            SecResult res = this.checkerSpi.doChecker(checker, checkerDomain, options);
            result.addLogger(checker.getRuleName(), res.getLogger());
            if (!res.isSuccessful()) {
                Map<String, String> messageParams = new HashMap<>();
                messageParams.putAll(options.getParameters());
                messageParams.putAll(res.getOutParams());
                String message = DmConvertUtils.resolveMessageArgs(preparedRule.description(), messageParams);
                result.addResult(preparedRule.name(), checker.getLevel(), res.getResult(), message, checkerDomain.getDomain().getSplitScript());
            }
        }
    }

    private SecRulesCheckResult resultUnsupported() {
        if (this.unsupportedLevel == WarnLevel.PASS) {
            return SecRulesCheckResult.EMPTY;
        }

        String unsupportedName = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_UNSUPPORTED_NAME_MESSAGE.name());
        String unsupportedMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_UNSUPPORTED_MSG_MESSAGE.name());
        SecRulesCheckResult result = new SecRulesCheckResult();
        result.addResult(unsupportedName, this.unsupportedLevel.getRuleLevel(), null, unsupportedMsg);
        return result;
    }

    record PreparedRule(CheckerRule checkerRule, String name, String description) {
    }
}
