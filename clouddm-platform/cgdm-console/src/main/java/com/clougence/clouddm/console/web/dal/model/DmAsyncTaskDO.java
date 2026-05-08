package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskProcessType;
import com.clougence.clouddm.console.web.dal.enumeration.DmAsyncTaskStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * The type async_task do.
 *
 * @author mode create time is 2019/12/11 10:10 下午 finish
 */
@Getter
@Setter
@TableName(value = "dm_async_task")
public class DmAsyncTaskDO {

    @TableId(type = IdType.AUTO)
    private Long                   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtModified;

    private String                 title;

    private String                 description;

    private String                 bizId;

    private String                 bizType;

    private String                 dependOnBizId;

    private String                 dependOnBizType;

    @TableField(value = "owner_uid")
    private String                 uid;

    private String                 handlerName;

    private String                 handlerType;

    private String                 configData;

    private boolean                showInDock;

    private DmAsyncTaskProcessType processType;

    private String                 processValue;

    private boolean                fastFail;

    /**
     * Legacy field from the old console ownership model for async tasks.
     * Retained only for backward compatibility during single-console cleanup.
     */
    @Deprecated
    private String                 consoleIp;

    private DmAsyncTaskStatus      status;

    private String                 statusMsg;

    private Date                   timeOfStart;

    private Date                   timeOfLast;

    private Date                   timeOfFinish;
}
