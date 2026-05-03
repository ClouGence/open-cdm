package com.clougence.clouddm.console.web.service.cluster;

import java.util.List;

import com.clougence.clouddm.api.console.status.MetricStats;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerHeartbeatDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateInitialWorkerFO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

/**
 * @author bucketli 2020-01-20 21:04
 * @since 1.1.3
 */
public interface WorkerService {

    DmWorkerDO createInitialWorker(String ownerUid, CreateInitialWorkerFO fo);

    void deleteWorker(long workerId, boolean force);

    void updateToWaitToOnline(long workerId);

    void updateToWaitToOffline(long workerId);

    List<DmWorkerStatusDO> listConnectedWorkers(long clusterId);

    List<DmWorkerDO> listWorkers(long clusterId);

    DmWorkerDO getWorkerById(Long workerId);

    DmWorkerDO getWorkerByWsn(String wsn);

    String getClientDownloadUrl(long workerId);

    WorkerDeployConfigVO getClientDeployCoreConfig(Long workerId, String ownerUid);

    void updateStatus(Long workerId, WorkerState workerState);

    void updateLifecycleState(Long workerId, LifeCycleState lifeCycleState);

    void updateWorkerIp(long workerId, String workerIp, String externalIp);

    void updateWorkerDesc(Long workerId, String desc);

    void updateWorkerMetric(Long workerId, MetricStats metricStats);

    void upsertWorkerHeartbeat(DmWorkerHeartbeatDO heartbeatDO);
}
