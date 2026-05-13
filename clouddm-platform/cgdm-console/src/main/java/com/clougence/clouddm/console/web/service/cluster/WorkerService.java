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
package com.clougence.clouddm.console.web.service.cluster;

import java.util.List;

import com.clougence.clouddm.api.console.status.MetricStats;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateInitialWorkerFO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.clouddm.console.web.dal.enumeration.LifeCycleState;

/**
 * @author bucketli 2020-01-20 21:04
 * @since 1.1.3
 */
public interface WorkerService {

    DmWorkerDO createInitialWorker(String ownerUid, CreateInitialWorkerFO fo);

    void deleteWorker(long workerId, boolean force);

    void updateToWaitToOnline(long workerId);

    void updateToWaitToOffline(long workerId);

    List<DmWorkerDO> listConnectedWorkers(long clusterId);

    List<DmWorkerDO> listWorkers(long clusterId);

    DmWorkerDO getWorkerById(Long workerId);

    DmWorkerDO getWorkerByWsn(String wsn);

    DmWorkerDO queryWorkerByWsn(String wsn);

    String getClientDownloadUrl(long workerId);

    WorkerDeployConfigVO getClientDeployCoreConfig(Long workerId, String ownerUid);

    void updateStatus(Long workerId, WorkerState workerState);

    void updateLifecycleState(Long workerId, LifeCycleState lifeCycleState);

    void updateWorkerIp(long workerId, String workerIp, String externalIp);

    void updateWorkerDesc(Long workerId, String desc);

    void updateWorkerMetric(Long workerId, MetricStats metricStats);

    void upsertWorkerHeartbeat(String workerSeqNumber, String workerIp, java.util.Date workerSendTime);
}
