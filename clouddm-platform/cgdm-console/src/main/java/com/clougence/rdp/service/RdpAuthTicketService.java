package com.clougence.rdp.service;

import com.clougence.rdp.controller.model.fo.ticket.RdpAddAuthTicketFO;
import com.clougence.rdp.controller.model.vo.ticket.RdpAuthTicketDetailVO;

public interface RdpAuthTicketService {

    void createAuthTicket(String ownerUid, String uid, RdpAddAuthTicketFO fo);

    RdpAuthTicketDetailVO queryAuthTicketDetail(String ownerUid, String uid, long ticketId);
}
