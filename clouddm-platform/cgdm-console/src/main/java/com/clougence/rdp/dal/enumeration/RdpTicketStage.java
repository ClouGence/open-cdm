package com.clougence.rdp.dal.enumeration;

import com.clougence.rdp.constant.I18nRdpLabelKeys;

/**
 * @author Ekko
 * @date 2024/6/28 14:45
*/
public enum RdpTicketStage {

    /** EXPLAIN before dingding */
    EXPLAIN(I18nRdpLabelKeys.TICKET_STAGE_EXPLAIN.name(), RdpApprovalBiz.DM_QUERY),

    /** Wait to approval */
    APPROVAL(I18nRdpLabelKeys.TICKET_STAGE_APPROVAL.name(), RdpApprovalBiz.DM_QUERY, RdpApprovalBiz.DATA_SOURCE_AUTH, RdpApprovalBiz.DM_CHANGE),

    /** wait to confirm */
    CONFIRM(I18nRdpLabelKeys.TICKET_STAGE_CONFIRM.name(), RdpApprovalBiz.DM_QUERY),

    /** In execution */
    EXECUTION(I18nRdpLabelKeys.TICKET_STAGE_EXECUTION.name(), RdpApprovalBiz.DM_QUERY, RdpApprovalBiz.DATA_SOURCE_AUTH);

    private final RdpApprovalBiz[] approvalBizs;
    private final String           i18nKey;

    RdpTicketStage(String i18nKey, RdpApprovalBiz... bizs){
        this.i18nKey = i18nKey;
        this.approvalBizs = bizs;
    }

    public String getI18nKey() { return i18nKey; }

    public boolean checkBiz(RdpApprovalBiz approvalBiz) {
        for (RdpApprovalBiz approval : this.approvalBizs) {
            if (approval == approvalBiz) {
                return true;
            }
        }
        return false;
    }
}
