package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.WorkerHeartbeatType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-22 15:10
 * @since 1.1.3
 */
@Getter
@Setter
@TableName(value = "dm_worker_heartbeat")
public class DmWorkerHeartbeatDO {

    @TableId(type = IdType.AUTO)
    private Long                id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                gmtModified;

    private String              workerSeqNumber;

    private String              workerIp;

    private WorkerHeartbeatType heartbeatType;

    private Date                workerSendTime;
}
