package com.clougence.clouddm.sdk.service.approval;

import com.clougence.clouddm.sdk.service.Service;

public interface RdpApprovalConsoleService extends Service {

    // The approval process has been fully completed, will call the final update status has been updated
    void refreshTicket(RdpApprovalTicketInfo dto);

    // Approval process activity tasks have changed, update activity activity status
    void updateActivity(RdpApprovalActivityInfo activity);
}
