package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/7/11
 **/
@Getter
@Setter
@TableName(value = "dm_worker_status")
public class DmWorkerStatusDO {

    @TableId(type = IdType.AUTO)
    private Long             id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;

    private WorkerConnStatus workerConnStatus;

    private String           uid;

    private String           workerSeqNumber;

    /**
     * Legacy field from the old consoleIp-based routing model.
     * Keep it only for backward compatibility while single-console cleanup is in progress.
     */
    @Deprecated
    private String           consoleIp;

    private String           workerIp;

    private Long             clusterId;

}
