package com.clougence.clouddm.init.component.fixtasks;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.detectrule.local.SecRuleScriptInfo;
import com.clougence.clouddm.console.web.component.detectrule.local.SecRulesScriptUtils;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.mapper.DmSecRefererMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmSecRulesMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmSecSensitiveMapper;
import com.clougence.clouddm.console.web.dal.model.DmSecRefererDO;
import com.clougence.clouddm.console.web.dal.model.DmSecRuleDO;
import com.clougence.clouddm.console.web.dal.model.DmSecSensitiveDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.SecParam;
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
    private DmConsoleConfig      dmConfig;
    @Resource
    private CheckRulesService    checkRulesService;
    @Resource
    private DmSecRulesMapper     secRulesMapper;
    @Resource
    private DmSecSensitiveMapper secSenMapper;
    @Resource
    private DmSecRefererMapper   secRefMapper;

    public void init() throws IOException {
        if (!this.dmConfig.isAutoUpdateInnerRules() || this.dmConfig.getDmMode() == DmMode.desktop) {
            log.info("skip autoUpdateInnerRules.");
            return;
        } else {
            log.info("check and auto fix innerRules.");
        }

        if (!PluginManager.hasFeature(DsFeatureIDs.FUNC_RULE_CHECK_SUPPORT)) {
            log.warn("rule sec plugin is not init. or not exist.");
            return;
        }

        for (SecRuleScriptInfo info : SecRulesScriptUtils.innerRules()) {
            if (info.getRuleKind() == RuleKind.QUERY) {
                DmSecRuleDO ruleDO;
                if (info.getOldId() != null) {
                    ruleDO = this.secRulesMapper.selectById(info.getOldId());
                } else {
                    ruleDO = this.secRulesMapper.queryInnerByRuleStrId(info.getRuleId());
                }

                this.upgradeQueryRule(info, ruleDO);
                if (info.isDeprecated()) {
                    this.cleanAndMarkDeprecated(info, ruleDO);
                }
            } else if (info.getRuleKind() == RuleKind.SENSITIVE) {
                DmSecSensitiveDO senDO;
                if (info.getOldId() != null) {
                    senDO = this.secSenMapper.selectById(info.getOldId());
                } else {
                    senDO = this.secSenMapper.queryInnerBySenStrId(info.getRuleId());
                }

                this.upgradeSensitiveRule(info, senDO);
                if (info.isDeprecated()) {
                    this.cleanAndMarkDeprecated(info, senDO);
                }
            }
        }
    }

    private void upgradeQueryRule(SecRuleScriptInfo info, DmSecRuleDO ruleDO) {
        List<SecParam> params = this.checkRulesService.extractParameters(info.getScriptType(), info.getContent());

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
            this.secRulesMapper.insert(ruleDO);
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

            this.secRulesMapper.updateInnerRuleById(ruleDO.getId(), ruleDO);
            this.upgradeReferer(info, ruleDO.getId(), RuleKind.QUERY);
        }
    }

    private void upgradeSensitiveRule(SecRuleScriptInfo info, DmSecSensitiveDO senDO) {
        List<SecParam> params = this.checkRulesService.extractParameters(info.getScriptType(), info.getContent());

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
            this.secSenMapper.insert(senDO);
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

            this.secSenMapper.updateInnerSenById(senDO.getId(), senDO);
            this.upgradeReferer(info, senDO.getId(), RuleKind.SENSITIVE);
        }
    }

    private void upgradeReferer(SecRuleScriptInfo info, long refId, RuleKind ruleKind) {
        List<DmSecRefererDO> refs = this.secRefMapper.listAllByRuleId(refId, ruleKind);

        for (DmSecRefererDO refDO : refs) {
            if (StringUtils.isBlank(refDO.getRefMD5())) {
                refDO.setRefMD5(info.getContentMD5());
                this.secRefMapper.updateRuleReferer(refDO.getOwnerUid(), refDO.getId(), refDO);
            }
        }
    }

    private void cleanAndMarkDeprecated(SecRuleScriptInfo info, DmSecRuleDO ruleDO) {
        if (info.isDeprecated() && ruleDO != null) {
            this.secRulesMapper.markInnerDeprecatedById(ruleDO.getRuleId());
            this.secRefMapper.markDeprecatedByRefId(ruleDO.getId(), RuleKind.QUERY);
        }
    }

    private void cleanAndMarkDeprecated(SecRuleScriptInfo info, DmSecSensitiveDO senDO) {
        if (info.isDeprecated() && senDO != null) {
            this.secSenMapper.markInnerDeprecatedById(senDO.getSenId());
            this.secRefMapper.markDeprecatedByRefId(senDO.getId(), RuleKind.SENSITIVE);
        }
    }
}
