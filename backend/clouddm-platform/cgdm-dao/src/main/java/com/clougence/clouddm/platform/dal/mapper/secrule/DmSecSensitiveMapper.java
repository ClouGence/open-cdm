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
import com.clougence.clouddm.platform.dal.model.secrule.DmSecSensitiveDO;

/**
 * @author Ekko
 * @date 2024/7/10 15:11
*/
public interface DmSecSensitiveMapper extends BaseMapper<DmSecSensitiveDO> {

    DmSecSensitiveDO queryInnerBySenStrId(String senStrId);

    void markInnerDeprecatedById(String senStrId);

    int updateInnerSenById(long senId, DmSecSensitiveDO senDO);

    List<DmSecSensitiveDO> searchSens(String ownerUid, String searchKeywords);

    List<DmSecSensitiveDO> listByUid(String ownerUid);

    DmSecSensitiveDO queryById(String ownerUid, long senId);

    int deleteByUidAndId(String ownerUid, long senId);

    int updateSen(String ownerUid, long senId, DmSecSensitiveDO senDO);

    List<DmSecSensitiveDO> queryByIds(String ownerUid, Collection<Long> ids);
}
