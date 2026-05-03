package com.clougence.clouddm.console.web.model.vo.cluster;

import java.util.Date;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
import com.clougence.clouddm.console.web.constants.DeployStatus;
import com.clougence.clouddm.console.web.constants.HealthLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-20 13:32
 * @since 1.1.3
 */
@Getter
@Setter
public class WorkerVO {

    private Long           id;

    private Date           gmtCreate;

    private Date           gmtModified;

    private long           clusterId;

    private String         privateIp;

    private String         publicIp;

    private CloudOrIdcName cloudOrIdcName;

    private String         region;

    private WorkerState    workerState;

    // changed
    private HealthLevel    healthLevel;

    private Double         workerLoad;

    private Double         cpuUseRatio;

    private Double         memUseRatio;

    private Integer        sessionPoolUse;

    private Integer        sessionPoolMax;

    private String         workerName;

    private String         workerSeqNumber;

    private String         workerDesc;

    private DeployStatus   deployStatus;

    private Date           installOrUpgradeDate;

    private String         installOrUpgradeVersion;
}
