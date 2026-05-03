package com.clougence.rdp.component.ticket;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.vo.RdpApproTemplateVO;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.model.RdpCacheApproTemplateDO;
import com.clougence.rdp.util.RdpI18nUtils;

/**
 * @author Ekko
 * @date 2024/5/6 15:25
*/
public interface RdpApprovalService {

    List<ApprovalProvider> SupportList       = Arrays.asList(//
            ApprovalProvider.DingTalk,  //
            ApprovalProvider.Feishu,    //
            ApprovalProvider.Wechat);
    String                        INNER_TEMPLATE_ID = "PROC-SELFMAKE-000000001";

    static RdpApproTemplateVO innerTemplate() {
        RdpApproTemplateVO innerTemplate = new RdpApproTemplateVO();
        innerTemplate.setTemplateIdentity(INNER_TEMPLATE_ID);
        innerTemplate.setApproUrl("");
        innerTemplate.setApproTemplateName(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
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
