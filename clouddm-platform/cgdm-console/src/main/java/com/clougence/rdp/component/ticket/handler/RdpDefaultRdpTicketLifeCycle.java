package com.clougence.rdp.component.ticket.handler;

import org.springframework.stereotype.Service;

import com.clougence.rdp.component.ticket.RdpTicketLifeCycle;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RdpDefaultRdpTicketLifeCycle implements RdpTicketLifeCycle {

    @Override
    public void approvalCompleted(RdpApprovalBiz approvalBiz, long ticketId) {
        // default do nothing
    }

    @Override
    public void approvalRefuse(RdpApprovalBiz approvalBiz, long ticketId) {
        // default do nothing
    }

    @Override
    public void approvalFailed(RdpApprovalBiz approvalBiz, long ticketId) {
        // default do nothing
    }

    @Override
    public void approvalCanceled(RdpApprovalBiz approvalBiz, long ticketId) {
        // default do nothing
    }
}
