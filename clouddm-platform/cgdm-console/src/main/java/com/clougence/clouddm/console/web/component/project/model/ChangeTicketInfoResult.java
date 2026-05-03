package com.clougence.clouddm.console.web.component.project.model;

import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTicketInfoResult {

    private Long            ticketId;
    private String          ticketBizId;
    private RdpApprovalBiz  ticketBizType;
    private RdpApprovalType approvalType;

    private RdpTicketStatus ticketStatus;
}
