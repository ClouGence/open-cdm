package com.clougence.rdp.component.ticket;

import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;

public interface RdpTicketHelperService {

    RdpTicketHelper getTicketHelper(RdpApprovalBiz type);
}
