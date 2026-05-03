package com.clougence.rdp.controller.model.fo.ticket;

import com.clougence.rdp.dal.enumeration.RdpApprovalType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/5/7 16:46
*/
@Getter
@Setter
public class RdpAddTemplateFO {

    private RdpApprovalType approvalType;
    private String          templateUrl;
}
