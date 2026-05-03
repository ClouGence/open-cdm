package com.clougence.clouddm.console.web.model.vo.envparam;

import com.clougence.rdp.dal.enumeration.RdpApprovalType;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-03 17:37
 */
@Getter
@Setter
public class DmEnvApprovalTemVO {

    private RdpApprovalType approvalType;
    private String          approvalTypeName;
    private String          templateId;
    private String          templateName;
}
