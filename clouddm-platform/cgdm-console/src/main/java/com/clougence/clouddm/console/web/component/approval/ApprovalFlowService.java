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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.RdpApprovalFO;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalProcessDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalTemplateDO;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;

/**
 * @author Ekko
 * @date 2024/5/8 12:01
*/
public interface ApprovalFlowService {

    List<ApprovalProvider> SUPPORT_LIST      = Arrays.asList(//
            ApprovalProvider.DingTalk,  //
            ApprovalProvider.Feishu,    //
            ApprovalProvider.Wechat);
    String                 INNER_TEMPLATE_ID = "PROC-SELFMAKE-000000001";

    static RdpApproTemplateVO innerTemplate() {
        RdpApproTemplateVO innerTemplate = new RdpApproTemplateVO();
        innerTemplate.setTemplateIdentity(INNER_TEMPLATE_ID);
        innerTemplate.setApproUrl("");
        innerTemplate.setApproTemplateName(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        return innerTemplate;
    }

    void closeTicket(long ticketId, String statusMessage, String puid, String uid);

    void closeTicket(long ticketId, String statusMessage, String puid);

    void failTicket(long ticketId, String statusMessage, String puid);

    void cancelTicket(String puid, long ticketId, String statusMessage);

    void retryTicket(String puid, long ticketId);

    void approvalTicket(String puid, String uid, RdpApprovalFO fo);

    boolean isFinish(long ticketId);

    void createProcess(long ticketId, ApprovalBiz approvalBiz, boolean checkSuccess);

    List<DmApprovalProcessDO> getProcessList(long ticketId);

    void cancelProcess(long ticketId, long processId);

    void cancelAllProcess(long ticketId);

    void failedAllProcess(long ticketId);

    void cancelApprovalInst(Long ticketId);

    boolean checkEnableApproval(String ownerUid, ApprovalProvider type);

    void refreshApprovalStatus(long ticketId);

    DmApprovalTemplateDO checkApprovalAndReturnTemplate(String ownerUid, ApprovalType type, String templateId, Locale locale);

}
