package com.clougence.clouddm.console.web.component.project.model;

import com.clougence.rdp.dal.enumeration.RdpApprovalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeApprovalInfo {

    private RdpApprovalType approvalType;
    private String          templateId;
    private String          templateName;

    public ChangeApprovalInfo(RdpApprovalType approvalType, String templateId, String templateName){
        this.approvalType = approvalType;
        this.templateId = templateId;
        this.templateName = templateName;
    }
}
