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
package com.clougence.clouddm.console.web.service.approval;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;

/**
 * @author Ekko
 * @date 2024/5/7 16:36
*/
public interface ApprovalControlService {

    //
    // control
    //

    DmTicketResultVO createSqlTicket(String puid, String uid, DmAddTicketFO fo);

    String confirmTicket(String puid, long ticketId, DmConfirmTicketFO fo);

    void createAuthTicket(String ownerUid, String uid, RdpAddAuthTicketFO fo);

    void retryJob(String puid, String uid, long ticketId);

    void skipTask(String puid, String uid, DmQueryAutoExecFO fo);

    void canceledSkipTask(String puid, String uid, DmQueryAutoExecFO fo);

    void stopJob(String puid, String uid, long ticketId);

    void endAutoExecJob(String puid, String uid, long ticketId);

    //
    // query
    //

    DmPageVO<RdpTicketBasicVO> queryTicketListByPage(String puid, RdpListTicketFO fo);

    RdpTicketBaseInfoVO queryTicketBaseInfo(String puid, String uid, RdpQueryTicketDetailFO fo);

    DmQueryTicketVO queryTicketDetail(String puid, DmQueryTicketDetailFO fo);

    DmApprovalSqlPreviewVO previewSqlFile(long approvalId, int startLine, int lineCount);

    RdpAuthTicketDetailVO queryAuthTicketDetail(String ownerUid, String uid, long ticketId);

    DmAutoExecJobVO queryExecJobInfo(String puid, String uid, long ticketId);

    DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String puid, String uid, DmQueryTaskListFO fo);

    List<DmBizLogVO> queryExecLog(DmQueryExecLogFO fo);

    List<RdpApproTemplateVO> listTemplates(String ownerUid, ApprovalType approvalType);

    List<Map<String, Object>> getTicketTypes(String ownerUid);

    //
    // assistant
    //

    List<RdpApproTemplateVO> refreshTemplates(String ownerUid, ApprovalType approvalType);

    void addTemplateByUrl(String ownerUid, ApprovalType approvalType, String templateUrl);

    void removeTemplateById(String ownerUid, ApprovalType approvalType, String templateId);
}
