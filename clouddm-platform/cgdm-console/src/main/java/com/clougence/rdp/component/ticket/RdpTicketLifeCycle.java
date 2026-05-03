package com.clougence.rdp.component.ticket;

import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;

public interface RdpTicketLifeCycle {

    void approvalCompleted(RdpApprovalBiz approvalBiz, long ticketId);

    void approvalRefuse(RdpApprovalBiz approvalBiz, long ticketId);

    void approvalFailed(RdpApprovalBiz approvalBiz, long ticketId);

    void approvalCanceled(RdpApprovalBiz approvalBiz, long ticketId);
}
