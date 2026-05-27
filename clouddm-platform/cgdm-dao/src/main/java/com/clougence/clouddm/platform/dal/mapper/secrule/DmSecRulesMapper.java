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

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecRuleDO;

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
