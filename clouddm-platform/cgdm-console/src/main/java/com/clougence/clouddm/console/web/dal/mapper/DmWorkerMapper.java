package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.constants.DeployStatus;
import com.clougence.clouddm.console.web.dal.mapper.param.WorkerParam;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

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

    DmWorkerDO getByWsn(String workerSeqNumber);

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

    /**
     * update worker dynamic stats,include physicMemMb,physicCoreNum,physicDiskGb,cpuUseRatio,memUseRatio,workerLoad,freeDiskGb,freeMemMb
     */
    int updateWorkerDynamicStats(WorkerParam workerParam);

    int updateInstallOrUpgradeInfo(Long workerId, Date installOrUpgradeDate, String installOrUpgradeVersion);
}
