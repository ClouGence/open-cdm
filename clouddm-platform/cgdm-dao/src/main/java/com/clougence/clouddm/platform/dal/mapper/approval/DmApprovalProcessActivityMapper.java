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
package com.clougence.clouddm.platform.dal.mapper.approval;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessActivityDO;

public interface DmApprovalProcessActivityMapper extends BaseMapper<DmApprovalProcessActivityDO> {

    List<DmApprovalProcessActivityDO> queryByTicketId(Long ticketId);

    DmApprovalProcessActivityDO queryByProcessIdAndActivityIdForUpdate(@Param("processId") Long processId, @Param("activityId") String activityId);

    void updateContext(@Param("processId") Long processId, @Param("activityId") String activityId, @Param("context") String context);
}