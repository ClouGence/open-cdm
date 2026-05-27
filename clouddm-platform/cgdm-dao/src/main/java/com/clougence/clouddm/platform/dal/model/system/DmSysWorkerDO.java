/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.dal.model.system;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.platform.dal.model.system.CloudOrIdcName;
import com.clougence.clouddm.platform.dal.model.system.DeployStatus;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-10 19:47
 * @since 1.1.3
 */
@Getter
@Setter
@TableName(value = "dm_sys_worker")
public class DmSysWorkerDO {

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

    private WorkerConnStatus connStatus;

    private Long           lastHeartbeatReportMs;

    private Long           lastHeartbeatPingMs;

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
