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
import com.clougence.clouddm.platform.dal.model.approval.ApprovalProcessStatus;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStage;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessDO;

/**
 * @author wanshao create time is 2021/1/26
 **/
public interface DmApprovalProcessMapper extends BaseMapper<DmApprovalProcessDO> {

    /** @return Ticket process items are returned with order */
    List<DmApprovalProcessDO> listByTicketId(Long ticketId);

    int updateContextById(Long id, String context);

    int updateModified(Long id);

    DmApprovalProcessDO queryByStage(long ticketId, ApprovalStage ticketStage);

    default DmApprovalProcessDO queryTicketProcessById(long ticketId, long processId) {
        DmApprovalProcessDO processDO = selectById(processId);
        if (processDO.getTicketId() == ticketId) {
            return processDO;
        } else {
            return null;
        }
    }

    void updateNotEndProcessByTicketId(long ticketId, ApprovalProcessStatus status);

    void updateProcessStatusByTicketIdAndStage(@Param("ticketId") long ticketId, @Param("stage") ApprovalStage stage, @Param("status") ApprovalProcessStatus status);

    void updateProcessStatus(@Param("id") Long id, @Param("status") String status, @Param("context") String context);

    default void updateTicketStatusByEnum(Long id, ApprovalProcessStatus status, String context) {
        this.updateProcessStatus(id, status.name(), context);
    }
}
