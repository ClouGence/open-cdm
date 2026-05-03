package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmSecRuleDO;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public interface DmSecRulesMapper extends BaseMapper<DmSecRuleDO> {

    DmSecRuleDO queryInnerByRuleStrId(String ruleStrId);

    void markInnerDeprecatedById(String ruleStrId);

    int updateInnerRuleById(long ruleId, DmSecRuleDO ruleDO);

    List<DmSecRuleDO> searchRules(String ownerUid, String searchKeywords);

    List<DmSecRuleDO> listByUid(String ownerUid);

    DmSecRuleDO queryById(String ownerUid, long ruleId);

    int deleteByUidAndId(String ownerUid, long ruleId);

    int updateRule(String ownerUid, long ruleId, DmSecRuleDO ruleDO);

    List<DmSecRuleDO> queryByIds(String ownerUid, Collection<Long> ids);
}
