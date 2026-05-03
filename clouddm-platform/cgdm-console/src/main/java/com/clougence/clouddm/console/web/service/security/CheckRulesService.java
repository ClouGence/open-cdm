package com.clougence.clouddm.console.web.service.security;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRange;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.enumeration.SecMatchMode;
import com.clougence.clouddm.console.web.dal.enumeration.SecRangeType;
import com.clougence.clouddm.console.web.dal.model.DmSecRangeDO;
import com.clougence.clouddm.console.web.dal.model.DmSecRefererDO;
import com.clougence.clouddm.console.web.dal.model.DmSecRuleDO;
import com.clougence.clouddm.console.web.dal.model.DmSecSpecDO;
import com.clougence.clouddm.console.web.model.fo.checkrules.RangeDeleteFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.RuleSaveFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecRulesFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecSaveRangeFO;
import com.clougence.clouddm.console.web.model.vo.checkrules.SecSettingDef;
import com.clougence.clouddm.console.web.service.security.mode.DmSecRuleMO;
import com.clougence.clouddm.sdk.service.secrules.SecParam;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface CheckRulesService {

    SecSettingDef ruleSettingDef();

    List<DmSecSpecDO> querySpecList(String ownerUid, String search);

    DmSecSpecDO querySpecById(String ownerUid, long specId);

    DmSecSpecDO createSpec(String ownerUid, String specName, String specDesc, boolean initSpec);

    void updateSpec(String ownerUid, long specId, String newName, String newDesc);

    void configStatus(String ownerUid, long specId, boolean enable);

    void deleteSpec(String ownerUid, long specId);

    void saveSpecRules(String ownerUid, long specId, List<SpecRulesFO> rules);

    List<DmSecRuleDO> listQueryRuleByUid(String ownerUid);

    DmSecRuleMO queryRuleById(String ownerUid, long refRuleOrSenId, RuleKind ruleKind);

    List<DmSecRuleMO> queryRuleListByUser(String ownerUid, RuleKind ruleKind, String search);

    List<DmSecRefererDO> queryRuleRefererBySpec(String ownerUid, long specId, RuleKind ruleKind);

    void deleteRule(String ownerUid, long refRuleOrSenId, RuleKind ruleKind);

    Map<String, String> getRuleScriptFormatByUid(String ownerUid);

    void updateRule(String ownerUid, long ruleId, RuleSaveFO fo);

    DmSecRuleMO createRule(String ownerUid, RuleSaveFO fo);

    List<DmSecSpecDO> querySpecListByRuleId(String ownerUid, long refRuleOrSenId, RuleKind ruleKind);

    List<SecParam> extractParameters(RuleScriptType contentType, String content);

    DmSecRefererDO querySpecRefererById(String ownerUid, long specId, long refRuleOrSenId, RuleKind ruleKind);

    DmSecRefererDO querySpecRefererById(String ownerUid, long refId);

    List<SecRange> fetchRangeBySpec(String ownerUid, long specId);

    List<SecRange> fetchRangeByRef(String ownerUid, long refId);

    DmSecRangeDO saveRange(String ownerUid, SpecSaveRangeFO fo);

    void deleteRange(String ownerUid, RangeDeleteFO fo);

    void deleteSpecRules(String ownerUid, long specId, List<SpecRulesFO> rules);

    boolean isSupportRangeType(SecRangeType rangeType, SecMatchMode matchType, RuleKind ruleKind, DataSourceType dsType);
}
