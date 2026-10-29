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
package com.clougence.clouddm.console.web.component.approval;

import java.util.List;

import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpTicketActivityVO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public interface CustomApprovalService {

    boolean isCustomApproval(DmApprovalDO ticket);

    void initializeApproval(DmApprovalDO ticket);

    void startApproval(long ticketId);

    void submitApproval(String puid, String uid, RdpApprovalFO request);

    boolean canApprove(DmApprovalDO ticket, String uid);

    List<Long> listParticipantTicketIds(String puid, DmAuthUserDO viewer, boolean pendingOnly);

    boolean canRead(DmApprovalDO ticket, String uid);

    boolean isAutomaticExecution(DmApprovalDO ticket);

    boolean canExecute(DmApprovalDO ticket, String uid);

    void cancelPendingApprovals(long ticketId);

    List<RdpTicketActivityVO> listApprovalNodeDetails(long ticketId, long processId);

    static ApprovalStageMO parseStageContext(DmApprovalProcessDO process) {
        if (process == null || StringUtils.isBlank(process.getStageContext())) {
            return new ApprovalStageMO();
        }
        return JsonUtils.toObj(process.getStageContext(), ApprovalStageMO.class);
    }
}
