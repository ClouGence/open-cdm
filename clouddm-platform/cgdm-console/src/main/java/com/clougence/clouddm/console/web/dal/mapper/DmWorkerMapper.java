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
package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.constants.DeployStatus;
import com.clougence.clouddm.console.web.dal.mapper.param.WorkerParam;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.enumeration.LifeCycleState;

/**
 * @author bucketli 2020-01-10 20:10
 * @since 1.1.3
 */
public interface DmWorkerMapper extends BaseMapper<DmWorkerDO> {

    List<DmWorkerDO> queryByStateAndClusterId(WorkerState workerState, long clusterId);

    List<DmWorkerDO> queryByStateAndScheduleIp(WorkerState workerState, String scheduleIp);

    //    List<WorkerDO> queryByScheduleIp(String scheduleIp);

    List<DmWorkerDO> listByCluster(long clusterId);

    List<DmWorkerDO> queryByClusterAndIds(long clusterId, List<Long> workerIds);

    List<DmWorkerDO> queryByClusters(@Param("clusterIds") List<Long> clusterIds);

    List<DmWorkerDO> queryByCondition(Long clusterId, String workerIp);

    List<DmWorkerDO> queryByCluster(long clusterId);

    List<DmWorkerDO> queryConnectedByClusterId(Long clusterId);

    List<DmWorkerDO> queryByConnStatus(WorkerConnStatus connStatus);

    List<DmWorkerDO> queryInactiveConnectedByHeartbeatReport(@Param("checkPointEpochMs") Long checkPointEpochMs);

    DmWorkerDO getByWsn(String workerSeqNumber);

    DmWorkerDO queryConnectedByWsn(String workerSeqNumber);

    DmWorkerDO getWorkerIdentityByWsn(String workerSeqNumber);

    DmWorkerDO getByName(String workerName);

    /**
     * lock the worker resource
     */
    DmWorkerDO lockWorkerByTaskTotalMemory(long workerId, long totalTaskMemMb);

    int updateWorkerState(long workerId, WorkerState workerState);

    int updateWorkerLifecycleState(long workerId, LifeCycleState lifeCycleState);

    int updateExternalIp(long workerId, String externalIp);

    int updateWorkerIp(long workerId, String workerIp);

    int updateWorkerDesc(long workerId, String workerDesc);

    int updateInstallInfo(long installConsoleJobId, long uninstallConsoleJobId, long upgradeAllConsoleJobId, long workerId);

    int updateUpgradeInfo(long upgradeAllConsoleJobId, long workerId);

    int updateDeployStatus(DeployStatus deployStatus, long workerId);

    int updateConsoleJobId(long workerId, long consoleJobId);

    int updateWorkerLivenessByWsn(DmWorkerDO workerDO);

    int updateWorkerConnStatusById(long id, WorkerConnStatus connStatus);

    /**
     * update worker dynamic stats,include physicMemMb,physicCoreNum,physicDiskGb,cpuUseRatio,memUseRatio,workerLoad,freeDiskGb,freeMemMb
     */
    int updateWorkerDynamicStats(WorkerParam workerParam);

    int updateInstallOrUpgradeInfo(Long workerId, Date installOrUpgradeDate, String installOrUpgradeVersion);
}
