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
package com.clougence.clouddm.platform.dal.mapper.cicd;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.platform.dal.model.cicd.ArgChangeQueryObj;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeStatus;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeStep;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;

public interface DmChangeMapper extends BaseMapper<DmChangeDO> {
    IPage<DmChangeDO> listChangeByConditionAndPage(Page<?> page, ArgChangeQueryObj param);

    DmChangeDO queryChangeById(@Param("changeId") long changeId);

    List<DmChangeDO> queryByIds(String ownerUid, Collection<Long> changeIds);

    int countUnfinishedChangeByFlowId(String ownerUid, long flowId);

    List<DmChangeDO> queryReadyChangeListByDate(Date date, int limit);

    List<DmChangeDO> queryByBatchIds(String ownerUid, Collection<Long> batchIds);

    List<DmChangeDO> queryByBatchIdForUpdate(String ownerUid, long batchId);

    List<DmChangeDO> queryUnlockedChangesByFlowIds(String ownerUid, Collection<Long> flowIds);

    int assignReadyChange(long changeId, int version);

    int increTryTimes(long changeId, int version, String remark);

    int updateStatusTo(long changeId, int version, ChangeStatus toStatus, String remark);

    int updateStepTo(long changeId, int version, ChangeStep toStep, String remark);

    int lockChangeById(long changeId, int version);

    int updateBatch(long changeId, long batchId);

    int updateTriggerUid(long changeId, String triggerUid);

    List<DmChangeDO> queryUnlockedChange(String ownerUid, long flowId);

}
