package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;

import lombok.Data;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Data
@TableName(value = "rdp_alert_event_log")
public class RdpAlertEventLogDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    private AlertEventStatus status;

    private AlertMediaType   alertMediaType;

    private String           content;

    /**
     * console ip
     */
    private String           ip;

    private String           errMsg;

    private String           uid;
}
