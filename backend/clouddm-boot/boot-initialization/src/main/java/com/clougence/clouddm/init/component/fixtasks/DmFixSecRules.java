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
package com.clougence.clouddm.init.component.fixtasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.detectrule.local.SecRuleScriptInfo;
import com.clougence.clouddm.console.web.component.detectrule.local.SecRulesScriptUtils;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.platform.dal.access.SecRuleDal;
import com.clougence.clouddm.platform.dal.model.secrule.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.SecParam;
import com.clougence.detectrule.domain.ParamInfo;
import com.clougence.detectrule.parser.DetectRuleHelper;
import com.clougence.detectrule.parser.ast.statement.StatementList;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Slf4j
@Service
public class DmFixSecRules {

    @Resource
    private SecRuleDal        secRuleDal;
    @Resource
    private DmConsoleConfig   dmConfig;
    @Resource
    private CheckRulesService checkRulesService;

    public void init() throws IOException {
        if (!this.dmConfig.isAutoUpdateInnerRules()) {
            log.info("skip autoUpdateInnerRules.");
            return;
        }
        log.info("check and auto fix innerRules.");

        if (!PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            log.warn("rule sec plugin is not init. or not exist. use built-in parser fallback during initialization.");
        }

        for (SecRuleScriptInfo info : SecRulesScriptUtils.innerRules()) {
            if (info.getRuleKind() == RuleKind.QUERY) {
                DmSecRuleDO ruleDO;
                if (info.getOldId() != null) {
                    ruleDO = this.secRuleDal.rulesMapper().selectById(info.getOldId());
                } else {
                    ruleDO = this.secRuleDal.rulesMapper().queryInnerByRuleStrId(info.getRuleId());
                }

                this.upgradeQueryRule(info, ruleDO);
                if (info.isDeprecated()) {
                    this.cleanAndMarkDeprecated(info, ruleDO);
                }
            } else if (info.getRuleKind() == RuleKind.SENSITIVE) {
                DmSecSensitiveDO senDO;
                if (info.getOldId() != null) {
                    senDO = this.secRuleDal.sensitiveMapper().selectById(info.getOldId());
                } else {
                    senDO = this.secRuleDal.sensitiveMapper().queryInnerBySenStrId(info.getRuleId());
                }

                this.upgradeSensitiveRule(info, senDO);
                if (info.isDeprecated()) {
                    this.cleanAndMarkDeprecated(info, senDO);
                }
            }
        }
    }

    private void upgradeQueryRule(SecRuleScriptInfo info, DmSecRuleDO ruleDO) {
        List<SecParam> params = extractParameters(info);

        if (ruleDO == null) {
            // insert new record
            ruleDO = new DmSecRuleDO();
            ruleDO.setOwnerUid("");
            ruleDO.setRuleId(info.getRuleId());
            ruleDO.setName(info.getRuleName());
            ruleDO.setDescription(info.getRuleDesc());
            ruleDO.setScriptType(info.getScriptType());
            ruleDO.setScriptDef(JsonUtils.toJson(params));
            ruleDO.setScriptContent(info.getContent());
            ruleDO.setScriptMD5(info.getContentMD5());
            ruleDO.setRuleDsRange(info.getDsRange());
            ruleDO.setRuleTarget(info.getRuleTarget());
            ruleDO.setInnerShare(true);
            ruleDO.setDeprecated(info.isDeprecated());
            this.secRuleDal.rulesMapper().insert(ruleDO);
            log.warn("insert innerQueryRule strRuleId is " + ruleDO.getRuleId() + ", numRuleId is " + ruleDO.getId());
        } else {
            // update old record
            boolean oldEqNew = StringUtils.equalsIgnoreCase(ruleDO.getScriptMD5(), info.getContentMD5()) &&//
                               ruleDO.isDeprecated() == info.isDeprecated() &&//
                               ruleDO.getRuleTarget() == info.getRuleTarget() &&//
                               ruleDO.getScriptType() == info.getScriptType() &&//
                               Objects.equals(ruleDO.getRuleDsRange(), info.getDsRange());
            if (oldEqNew) {
                return;
            }

            log.warn("update innerQueryRule strRuleId is " + ruleDO.getRuleId() + ", numRuleId is " + ruleDO.getId());
            ruleDO.setRuleId(info.getRuleId());
            ruleDO.setName(info.getRuleName());
            ruleDO.setDescription(info.getRuleDesc());
            ruleDO.setScriptType(info.getScriptType());
            ruleDO.setScriptDef(JsonUtils.toJson(params));
            ruleDO.setScriptContent(info.getContent());
            ruleDO.setScriptMD5(info.getContentMD5());
            ruleDO.setRuleDsRange(info.getDsRange());
            ruleDO.setRuleTarget(info.getRuleTarget());
            ruleDO.setInnerShare(true);
            ruleDO.setDeprecated(info.isDeprecated());

            this.secRuleDal.rulesMapper().updateInnerRuleById(ruleDO.getId(), ruleDO);
            this.upgradeReferer(info, ruleDO.getId(), RuleKind.QUERY);
        }
    }

    private void upgradeSensitiveRule(SecRuleScriptInfo info, DmSecSensitiveDO senDO) {
        List<SecParam> params = extractParameters(info);

        if (senDO == null) {
            // insert new record
            senDO = new DmSecSensitiveDO();
            senDO.setOwnerUid("");
            senDO.setSenId(info.getRuleId());
            senDO.setName(info.getRuleName());
            senDO.setDescription(info.getRuleDesc());
            senDO.setScriptType(info.getScriptType());
            senDO.setScriptDef(JsonUtils.toJson(params));
            senDO.setScriptContent(info.getContent());
            senDO.setScriptMD5(info.getContentMD5());
            senDO.setSenMode(info.getSenMode());
            senDO.setInnerShare(true);
            senDO.setDeprecated(info.isDeprecated());
            this.secRuleDal.sensitiveMapper().insert(senDO);
            log.warn("insert innerSenRule strSenId is " + senDO.getSenId() + ", numSenId is " + senDO.getId());
        } else {
            // update old record
            boolean oldEqNew = StringUtils.equalsIgnoreCase(senDO.getScriptMD5(), info.getContentMD5()) &&//
                               senDO.isDeprecated() == info.isDeprecated() &&//
                               senDO.getSenMode() == info.getSenMode() &&//
                               senDO.getScriptType() == info.getScriptType();
            if (oldEqNew) {
                return;
            }

            log.warn("update innerSenRule strSenId is " + senDO.getSenId() + ", numSenId is " + senDO.getId());

            senDO.setSenId(info.getRuleId());
            senDO.setName(info.getRuleName());
            senDO.setDescription(info.getRuleDesc());
            senDO.setScriptType(info.getScriptType());
            senDO.setScriptDef(JsonUtils.toJson(params));
            senDO.setScriptContent(info.getContent());
            senDO.setScriptMD5(info.getContentMD5());
            senDO.setSenMode(info.getSenMode());
            senDO.setInnerShare(true);
            senDO.setDeprecated(info.isDeprecated());

            this.secRuleDal.sensitiveMapper().updateInnerSenById(senDO.getId(), senDO);
            this.upgradeReferer(info, senDO.getId(), RuleKind.SENSITIVE);
        }
    }

    private void upgradeReferer(SecRuleScriptInfo info, long refId, RuleKind ruleKind) {
        List<DmSecRefererDO> refs = this.secRuleDal.refererMapper().listAllByRuleId(refId, ruleKind);

        for (DmSecRefererDO refDO : refs) {
            if (StringUtils.isBlank(refDO.getRefMD5())) {
                refDO.setRefMD5(info.getContentMD5());
                this.secRuleDal.refererMapper().updateRuleReferer(refDO.getOwnerUid(), refDO.getId(), refDO);
            }
        }
    }

    private void cleanAndMarkDeprecated(SecRuleScriptInfo info, DmSecRuleDO ruleDO) {
        if (info.isDeprecated() && ruleDO != null) {
            this.secRuleDal.rulesMapper().markInnerDeprecatedById(ruleDO.getRuleId());
            this.secRuleDal.refererMapper().markDeprecatedByRefId(ruleDO.getId(), RuleKind.QUERY);
        }
    }

    private void cleanAndMarkDeprecated(SecRuleScriptInfo info, DmSecSensitiveDO senDO) {
        if (info.isDeprecated() && senDO != null) {
            this.secRuleDal.sensitiveMapper().markInnerDeprecatedById(senDO.getSenId());
            this.secRuleDal.refererMapper().markDeprecatedByRefId(senDO.getId(), RuleKind.SENSITIVE);
        }
    }

    private List<SecParam> extractParameters(SecRuleScriptInfo info) {
        if (info.getScriptType() == RuleScriptType.DetectRules && !PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            return fallbackExtractParameters(info);
        }

        List<SecParam> params = this.checkRulesService.extractParameters(info.getScriptType(), info.getContent());
        if (!params.isEmpty() || info.getScriptType() != RuleScriptType.DetectRules) {
            return params;
        }

        return fallbackExtractParameters(info);
    }

    private List<SecParam> fallbackExtractParameters(SecRuleScriptInfo info) {
        try {
            StatementSet statementSet = DslHelper.parserDsl("DetectRule", info.getContent());
            List<ParamInfo> paramInfos = DetectRuleHelper.extractParameters((StatementList) statementSet);
            List<SecParam> fallbackParams = new ArrayList<>(paramInfos.size());
            for (int i = 0; i < paramInfos.size(); i++) {
                ParamInfo paramInfo = paramInfos.get(i);
                SecParam secParam = new SecParam();
                secParam.setName(paramInfo.getName());
                secParam.setType(StringUtils.defaultIfBlank(paramInfo.getType(), "string"));
                secParam.setDefaultValue(paramInfo.getDefaultValue());
                secParam.setRange(paramInfo.getEnums());
                secParam.setHint(StringUtils.defaultIfBlank(paramInfo.getHint(), "the arg" + i));
                fallbackParams.add(secParam);
            }
            return fallbackParams;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse built-in rule parameters: " + info.getRuleId(), e);
        }
    }
}
