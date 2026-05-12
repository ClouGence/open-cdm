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
package com.clougence.rdp.component.ticket;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.dal.enumeration.RdpApprovalType;
import com.clougence.clouddm.console.web.dal.model.RdpCacheApproTemplateDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;

/**
 * @author Ekko
 * @date 2024/5/6 15:25
*/
public interface RdpApprovalService {

    List<ApprovalProvider> SupportList       = Arrays.asList(//
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

    void cancelApprovalInst(Long ticketId);

    List<RdpApproTemplateVO> listTemplates(String ownerUid, RdpApprovalType approvalType);

    List<RdpApproTemplateVO> refreshTemplates(String ownerUid, RdpApprovalType approvalType);

    List<Map<String, Object>> getTicketTypes(String ownerUid);

    boolean checkEnableApproval(String ownerUid, ApprovalProvider type);

    // get last status from third party
    void refreshApprovalStatus(long ticketId);

    RdpCacheApproTemplateDO checkApprovalAndReturnTemplate(String ownerUid, RdpApprovalType type, String templateId, Locale locale);

    void addTemplateByUrl(String ownerUid, RdpApprovalType approvalType, String templateUrl);

    void removeTemplateById(String ownerUid, RdpApprovalType approvalType, String templateId);
}
