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
package com.clougence.clouddm.console.web.model.vo.cluster;

import java.util.Date;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.platform.dal.model.system.CloudOrIdcName;
import com.clougence.clouddm.platform.dal.model.system.DeployStatus;
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
