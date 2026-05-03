package com.clougence.clouddm.console.web.component.detectrule.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.service.secrules.*;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.auth.model.EnvCacheEntry;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.component.detectrule.*;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.secrules.*;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.ui.browser.DsBrowseSpi;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
@Service
public class SecRulesEngineImpl implements SecRulesEngine {

    @Resource
    private SecRulesService         secRulesService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DmDsConfigService       configService;

    @Override
    public SecRulesCheckResult doQueryCheck(String ownerUid, String currentUid, String querySql, SecRulesCheckContext context) {
        if (!PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            log.warn("rule sec plugin is not init. or not exist.");
            return resultOK();
        }

        SecCheckerRules rules = this.secRulesService.fetchCheckerRules(ownerUid, context.getDsId());
        if (!rules.isValid() || CollectionUtils.isEmpty(rules.getQueryRuleList())) {
            return resultOK();
        }

        DataSourceType dsType = rules.getDsType();
        SecDomainResolveSpi resolveSpi = PluginManager.findSecDomainResolveSpi(dsType);
        DsBrowseSpi browseSpi = PluginManager.findDsBrowseSpi(dsType);

        List<RuleDomain> domainList;
        DsCacheEntry dsCache = this.ownerCacheService.queryByDsId(context.getDsId());
        try {
            Map<UmiTypes, Object> levelsParam = CollectionUtils.asMap(UmiTypes.Catalog, context.getCurrentCatalog(), UmiTypes.Schema, context.getCurrentSchema());
            ContextInfo ctxInfo = ContextInfo.builder()
                .puid(ownerUid)
                .cuid(currentUid)
                .dsId(context.getDsId())
                .levelsParam(levelsParam)
                .deepParser(true)
                .dataSourceConfig(configService.fetchDsConfigFromDM(context.getDsId(), dsCache.getDsType()))
                .build();
            CodeInfo codeInfo = CodeInfo.builder().baseLine(context.getBasicCodeLine()).baseColumn(context.getBasicCodeColumn()).query(querySql).build();
            domainList = resolveSpi.resolveDomain(dsType, codeInfo, ctxInfo);
            if (CollectionUtils.isEmpty(domainList)) {
                return this.resultUnsupported(context.getUnsupportedLevel(), querySql);
            }
        } catch (UnsupportedOperationException e) {
            return this.resultUnsupported(context.getUnsupportedLevel(), querySql);
        }

        // variables
        UserCacheEntry userCache = this.ownerCacheService.queryByUid(context.getCurrentUID());

        EnvCacheEntry envCache = this.ownerCacheService.queryByEnvId(dsCache.getEnvId());

        // doCheck
        SecRulesCheckResult result = new SecRulesCheckResult();
        result.setSpecName(rules.getDsUseSpecName());
        for (RuleDomain ruleDomain : domainList) {
            List<CheckerRule> checkerRules = rules.getQueryRuleList().stream().filter(r -> {
                return r.getTarget() == null || r.getTarget() == ruleDomain.getSqlTarget();
            }).collect(Collectors.toList());
            if (checkerRules.isEmpty()) {
                continue;
            }

            CheckerData checkerDomain = new CheckerData(querySql, ruleDomain);
            checkerDomain.setDsLevelsDef(browseSpi.getLevels());
            checkerDomain.setCurrentCatalog(context.getCurrentCatalog());
            checkerDomain.setCurrentSchema(context.getCurrentSchema());
            checkerDomain.setStartLine(ruleDomain.getSplitScript().getBodyStartCodeLine());
            checkerDomain.setStartColumn(ruleDomain.getSplitScript().getBodyStartCodeColumn());
            checkerDomain.getDomain().setEnvId(dsCache.getEnvId());
            checkerDomain.getDomain().setEnvName(envCache.getEnvName());
            checkerDomain.getDomain().setDsId(dsCache.getDsNumId());
            checkerDomain.getDomain().setDsName(dsCache.getDsInstId());
            checkerDomain.getDomain().setDsType(dsCache.getDsType());
            checkerDomain.getDomain().setUserName(userCache.getUserName());
            checkerDomain.getDomain().setUserRole(userCache.getRoleName());
            checkerDomain.getDomain().setPrimaryUid(userCache.getParentUid());

            this.doCheckDomain(checkerDomain, context, checkerRules, result);
        }
        return result;
    }

    private void doCheckDomain(CheckerData checkerDomain, SecRulesCheckContext context, List<CheckerRule> rules, SecRulesCheckResult result) {
        CheckerOptions options = new CheckerOptions();
        options.setDsType(checkerDomain.getDomain().getDsType());
        options.setRequester(context.getRequester());

        for (CheckerRule checker : rules) {
            // skip level is pass
            if (checker.getLevel() == RuleLevel.PASS) {
                continue;
            }

            //
            options.setParameters(checker.getParameters());

            SecResult res = this.secRulesService.checkerSpi().doChecker(checker, checkerDomain, options);
            boolean successful = res.isSuccessful();

            String nameI18n = DmConvertUtils.tryRuleI18nMessage(checker.getRuleName());
            String descI18n = DmConvertUtils.tryRuleI18nMessage(checker.getRuleDesc());

            result.addLogger(checker.getRuleName(), res.getLogger());
            if (!successful) {
                Map<String, String> messageParams = new HashMap<>();
                messageParams.putAll(options.getParameters());
                messageParams.putAll(res.getOutParams());
                String message = DmConvertUtils.resolveMessageArgs(descI18n, messageParams);
                result.addResult(nameI18n, checker.getLevel(), res.getResult(), message, checkerDomain.getDomain().getSplitScript());
            }
        }
    }

    private SecRulesCheckResult resultOK() {
        return new SecRulesCheckResult();
    }

    private SecRulesCheckResult resultUnsupported(WarnLevel level, String querySql) {
        if (level == WarnLevel.PASS) {
            return SecRulesCheckResult.EMPTY;
        }

        String unsupportedName = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_UNSUPPORTED_NAME_MESSAGE.name());
        String unsupportedMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_UNSUPPORTED_MSG_MESSAGE.name());
        SecRulesCheckResult result = new SecRulesCheckResult();
        result.addResult(unsupportedName, level.getRuleLevel(), null, unsupportedMsg);
        return result;
    }
}
