package com.clougence.rdp.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.rdp.component.asyntask.RdpAsyncTaskType;
import com.clougence.rdp.dal.enumeration.RdpAsyncTaskProcessType;
import com.clougence.rdp.dal.enumeration.RdpAsyncTaskStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * The type async_task do.
 *
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
@Getter
@Setter
@TableName(value = "rdp_async_task")
public class RdpAsyncTaskDO {

    @TableId(type = IdType.AUTO)
    private Long                    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                    gmtModified;

    private String                  title;

    private String                  description;

    private String                  bizId;

    private RdpAsyncTaskType        bizType;

    private String                  dependOnBizId;

    private RdpAsyncTaskType        dependOnBizType;

    @TableField(value = "owner_uid")
    private String                  uid;

    private String                  handlerName;

    private String                  handlerType;

    private String                  configData;

    private boolean                 showInDock;

    private RdpAsyncTaskProcessType processType;

    private String                  processValue;

    private boolean                 fastFail;

    private String                  consoleIp;

    private RdpAsyncTaskStatus      status;

    private String                  statusMsg;

    private Date                    timeOfStart;

    private Date                    timeOfLast;

    private Date                    timeOfFinish;
}
