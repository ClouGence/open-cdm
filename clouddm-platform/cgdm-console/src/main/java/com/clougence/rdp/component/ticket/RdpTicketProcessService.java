package com.clougence.rdp.component.ticket;

import java.util.List;

import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.model.RdpTicketProcessDO;

/**
 * @author Ekko
 * @date 2024/5/8 12:01
*/
public interface RdpTicketProcessService {

    void createProcess(long ticketId, RdpApprovalBiz approvalBiz, boolean checkSuccess);

    List<RdpTicketProcessDO> getProcessList(long ticketId);

    void cancelProcess(long ticketId, long processId);

    void cancelAllProcess(long ticketId);

    void failedAllProcess(long ticketId);
}
