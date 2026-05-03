package com.clougence.rdp.component.ticket;

import java.util.List;

import com.clougence.rdp.controller.model.vo.PrimaryUserVO;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;

public interface RdpTicketHelper {

    RdpApprovalBiz getHandleType();

    //
    // for Create
    //

    void createApproval(long ticketId);

    //
    // for execute
    //

    void executeTicket(long ticketId);

    void runningCheck(long ticketId);

    //
    // for query person
    //

    List<PrimaryUserVO> queryPerson(long ticketId);

    //
    // for callback
    //

    void approvalCompleted(long ticketId);

    void approvalRefuse(long ticketId);

    void approvalFailed(long ticketId);

    void approvalCanceled(long ticketId);
}
