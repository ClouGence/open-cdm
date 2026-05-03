package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
import com.clougence.clouddm.console.web.constants.DeployStatus;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-10 19:47
 * @since 1.1.3
 */
@Getter
@Setter
@TableName(value = "dm_worker")
public class DmWorkerDO {

    @TableId(type = IdType.AUTO)
    private Long           id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date           gmtModified;

    private long           clusterId;

    private String         workerIp;

    private CloudOrIdcName cloudOrIdcName;

    private String         region;

    private WorkerState    workerState;

    private Long           physicMemMb;

    private Integer        physicCoreNum;

    private Long           physicDiskGb;

    private Double         cpuUseRatio;

    private Double         memUseRatio;

    private Long           freeMemMb;

    private Long           freeDiskGb;

    private Integer        sessionPoolUse;

    private Integer        sessionPoolMax;

    private Double         workerLoad;

    private String         scheduleIp;

    private String         workerName;

    private String         workerSeqNumber;

    private String         workerDesc;

    private Long           installConsoleJobId;

    private Long           uninstallConsoleJobId;

    private Long           upgradeAllConsoleJobId;

    private DeployStatus   deployStatus;

    private String         externalIp;

    private String         uid;

    private Long           consoleJobId;

    private LifeCycleState lifeCycleState;

    private Date           installOrUpgradeDate;

    private String         installOrUpgradeVersion;

}
