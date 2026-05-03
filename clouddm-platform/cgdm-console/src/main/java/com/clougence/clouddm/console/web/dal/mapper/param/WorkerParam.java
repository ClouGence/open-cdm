package com.clougence.clouddm.console.web.dal.mapper.param;

import com.clougence.clouddm.api.console.status.WorkerState;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-10 20:12
 * @since 1.1.3
 */
@Getter
@Setter
public class WorkerParam {

    private Long        workerId;
    private String      workerIp;
    private WorkerState workerState;
    private String      workerDesc;
    private Long        clusterId;

    private Long        physicMemMb;
    private Long        freeMemMb;
    private Double      memUseRatio;

    private Integer     physicCoreNum;
    private Double      cpuUseRatio;

    private Long        physicDiskGb;
    private Long        freeDiskGb;

    private Double      workerLoad;
    private String      scheduleIp;

    private Integer     sessionPoolUse;
    private Integer     sessionPoolMax;
}
