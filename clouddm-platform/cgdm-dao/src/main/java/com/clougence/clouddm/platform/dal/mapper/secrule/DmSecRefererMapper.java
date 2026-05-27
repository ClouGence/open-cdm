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
package com.clougence.clouddm.platform.dal.mapper.secrule;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecRefererDO;

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
