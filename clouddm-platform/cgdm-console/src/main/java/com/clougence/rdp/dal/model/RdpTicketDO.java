package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.dal.handler.RdpApprovalTypeHandler;

import lombok.Data;

/**
 * @author Ekko
 * @date 2024/5/6 10:58
*/
@Data
@TableName(value = "rdp_ticket_inst")
public class RdpTicketDO {

    @TableId(type = IdType.AUTO)
    private Long            id;

    private String          bizId;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date            gmtModified;

    private String          ownerUid;

    // resource Id,can be null
    private Long            bindDsId;

    private String          targetInfo;

    @TableField(typeHandler = RdpApprovalTypeHandler.class)
    private RdpApprovalType approType;

    private RdpApprovalBiz  approBiz;

    private String          approIdentity;

    private String          approTemplateName;

    private String          approTemplateIdentity;

    private String          approComment;

    private String          description;

    private String          ticketTitle;

    private RdpTicketStatus ticketStatus;

    private Date            finishTime;

    private String          statusMessage;

    private Boolean         deleted;

    private Integer         errorCount;

    private String          primaryUid;

    private String          envName;

    private String          approvalUrl;
}
