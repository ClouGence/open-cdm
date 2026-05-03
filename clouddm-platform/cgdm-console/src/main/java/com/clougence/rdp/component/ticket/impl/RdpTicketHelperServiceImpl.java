package com.clougence.rdp.component.ticket.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.rdp.component.ticket.RdpTicketHelper;
import com.clougence.rdp.component.ticket.RdpTicketHelperService;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;

@Service
public class RdpTicketHelperServiceImpl implements RdpTicketHelperService {

    private final Map<RdpApprovalBiz, RdpTicketHelper> ticketHelperMap = new HashMap<>();

    public RdpTicketHelperServiceImpl(List<RdpTicketHelper> rdpTicketHelpers){
        for (RdpTicketHelper rdpTicketHelper : rdpTicketHelpers) {
            RdpApprovalBiz type = rdpTicketHelper.getHandleType();
            if (!ticketHelperMap.containsKey(type)) {
                ticketHelperMap.put(type, rdpTicketHelper);
            } else {
                throw new RuntimeException("RdpTicketHelper about " + type + " already exists");
            }
        }
    }

    @Override
    public RdpTicketHelper getTicketHelper(RdpApprovalBiz type) {
        return ticketHelperMap.get(type);
    }
}
