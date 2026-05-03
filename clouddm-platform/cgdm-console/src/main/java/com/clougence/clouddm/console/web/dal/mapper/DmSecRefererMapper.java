package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.model.DmSecRefererDO;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public interface DmSecRefererMapper extends BaseMapper<DmSecRefererDO> {

    List<DmSecRefererDO> fixListAllRuleIdEmpty();

    void fixUpdateRuleId(long id, String ruleId);

    List<DmSecRefererDO> listBySpecId(String ownerUid, long refSpecId);

    List<DmSecRefererDO> listBySpecAndKind(String ownerUid, long refSpecId, RuleKind ruleKind);

    List<DmSecRefererDO> listByRuleId(String ownerUid, long refRuleOrSenId, RuleKind ruleKind);

    List<DmSecRefererDO> listAllByRuleId(long refRuleOrSenId, RuleKind ruleKind);

    DmSecRefererDO queryBySpecAndRuleOrSen(String ownerUid, long refSpecId, long refRuleOrSenId, RuleKind ruleKind);

    DmSecRefererDO queryById(String ownerUid, long refId);

    int deleteBySpecId(String ownerUid, long refSpecId);

    int deleteById(String ownerUid, long refId);

    void deleteByRuleId(String ownerUid, long refRuleOrSenId, RuleKind ruleKind);

    void updateRuleReferer(String ownerUid, long refId, DmSecRefererDO refererDO);

    void markDeprecatedByRefId(long refRuleOrSenId, RuleKind ruleKind);
}
