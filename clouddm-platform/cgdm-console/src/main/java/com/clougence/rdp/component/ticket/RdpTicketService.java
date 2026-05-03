package com.clougence.rdp.component.ticket;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.rdp.controller.model.fo.security.ListMyAuthTicketFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpApprovalTicketFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpListTicketFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpQueryTicketDetailFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpTicketBasicVO;
import com.clougence.rdp.controller.model.vo.ticket.RdpTicketBaseInfoVO;

/**
 * @author Ekko
 * @date 2024/5/8 12:01
*/
public interface RdpTicketService {

    void closeTicket(long ticketId, String statusMessage, String puid, String uid);

    void closeTicket(long ticketId, String statusMessage, String puid);

    void failTicket(long ticketId, String statusMessage, String puid);

    void cancelTicket(String puid, long ticketId, String statusMessage);

    void retryTicket(String puid, long ticketId);

    IPage<RdpTicketBasicVO> queryTicketListByPage(String puid, RdpListTicketFO fo);

    IPage<RdpTicketBasicVO> queryAuthTicketListByPage(String puid, ListMyAuthTicketFO fo);

    RdpTicketBaseInfoVO queryTicketBaseInfo(String puid, String uid, RdpQueryTicketDetailFO fo);

    void approvalTicket(String puid, String uid, RdpApprovalTicketFO fo);

    boolean isFinish(long ticketId);
}
