package com.clougence.clouddm.console.web.component.project.model;

import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTicketInfo {

    private Long            ticketId;
    private String          ticketBizId;
    private RdpApprovalBiz  ticketBizType;
    private RdpApprovalType approvalType;
    private String          templateId;
    private String          templateName;

}
