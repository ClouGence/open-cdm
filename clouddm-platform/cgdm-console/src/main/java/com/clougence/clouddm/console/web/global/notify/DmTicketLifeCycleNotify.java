package com.clougence.clouddm.console.web.global.notify;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.rdp.component.ticket.RdpTicketLifeCycle;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 17:11
 */
@Slf4j
@Service
public class DmTicketLifeCycleNotify implements RdpTicketLifeCycle {

    @Resource
    protected ImSenderService imSenderService;

    private boolean checkIgnore(RdpApprovalBiz approvalBiz) {
        return !(approvalBiz == RdpApprovalBiz.DATA_SOURCE_AUTH || approvalBiz == RdpApprovalBiz.DM_QUERY);
    }

    @Override
    public void approvalCompleted(RdpApprovalBiz approvalBiz, long ticketId) {
        if (checkIgnore(approvalBiz)) {
        }

        // imSenderService.sendMessage();
        //System.out.println("approvalCompleted ticketId:" + ticketId + " approvalBiz:" + approvalBiz);
    }

    @Override
    public void approvalRefuse(RdpApprovalBiz approvalBiz, long ticketId) {
        if (checkIgnore(approvalBiz)) {
        }

        //System.out.println("approvalRefuse ticketId:" + ticketId + " approvalBiz:" + approvalBiz);
    }

    @Override
    public void approvalFailed(RdpApprovalBiz approvalBiz, long ticketId) {
        if (checkIgnore(approvalBiz)) {
        }

        //System.out.println("approvalFailed ticketId:" + ticketId + " approvalBiz:" + approvalBiz);
    }

    @Override
    public void approvalCanceled(RdpApprovalBiz approvalBiz, long ticketId) {
        if (checkIgnore(approvalBiz)) {
        }

        //System.out.println("approvalCanceled ticketId:" + ticketId + " approvalBiz:" + approvalBiz);
    }
}
