package com.clougence.rdp.controller.model.vo.ticket;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpTicketBaseInfoVO {

    private Long                     id;

    private String                   bizId;

    private String                   gmtCreate;

    private String                   gmtModified;

    private String                   userName;

    private Long                     dataSourceId;

    private String                   dataSourceInstName;

    private String                   dataSourceDesc;

    private DataSourceType           dataSourceType;

    private DeployEnvType            dsDeployType;

    private String                   dsOwnerUid;

    private String                   dsEnvName;

    private String                   targetInfo;

    private RdpApprovalType          approType;

    private String                   approTypeName;

    private RdpApprovalBiz           approBiz;

    private String                   approIdentity;

    private String                   approTemplateName;

    private String                   approComment;

    private String                   description;

    private String                   statusMessage;

    private String                   ticketTitle;

    private RdpTicketStatus          ticketStatus;

    private String                   finishTime;

    private List<RdpTicketProcessVO> ticketProcessVOList;

    private boolean                  canApproval;

    private boolean                  canClose;

    private boolean                  canExecute;

    private boolean                  canAutoExec;

    private String                   pcUrl;

    private String                   mobileUrl;

    private String                   ticketMessage;

}
