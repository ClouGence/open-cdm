package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.dal.enumeration.RdpTicketProcessStatus;
import com.clougence.rdp.dal.enumeration.RdpTicketStage;

import lombok.Data;

/**
 * @author Ekko
 * @date 2024/5/6 16:57
*/
@Data
@TableName(value = "rdp_ticket_process")
public class RdpTicketProcessDO {

    @TableId(type = IdType.AUTO)
    private Long                   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtModified;

    private Long                   ticketId;

    private RdpTicketStage         ticketStage;

    private Date                   finishTime;

    private String                 stageContext;

    private RdpTicketProcessStatus processStatus;
}
