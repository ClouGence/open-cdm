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
package com.clougence.clouddm.init.component.fixtasks;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.CloudOrIdcName;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DmFixDefaultClusterWorker {
    @Resource
    private SystemDal systemDal;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void init() {
        if (isAloneMode()) {
            log.info("DmFixDefaultClusterWorker: skip in alone mode");
            return;
        }

        DmSysWorkerDO worker = systemDal.workerMapper().getByWsn(InitSeedConstants.DEFAULT_WORKER_WSN);
        DmSysClusterDO cluster = resolveCluster(worker);

        if (worker == null) {
            worker = createDefaultWorker(cluster.getId());
            log.info("DmFixDefaultClusterWorker: created default worker, wsn={}", InitSeedConstants.DEFAULT_WORKER_WSN);
        } else if (worker.getClusterId() != cluster.getId()) {
            worker.setClusterId(cluster.getId());
            systemDal.workerMapper().updateById(worker);
            log.info("DmFixDefaultClusterWorker: normalized default worker clusterId, wsn={}, clusterId={}", InitSeedConstants.DEFAULT_WORKER_WSN, cluster.getId());
        }

        if (worker.getConnStatus() != WorkerConnStatus.NEW) {
            worker.setConnStatus(WorkerConnStatus.NEW);
            worker.setLastHeartbeatReportMs(null);
            worker.setLastHeartbeatPingMs(null);
            systemDal.workerMapper().updateById(worker);
            log.info("DmFixDefaultClusterWorker: normalized default worker liveness fields, wsn={}", InitSeedConstants.DEFAULT_WORKER_WSN);
        }
    }

    private DmSysClusterDO resolveCluster(DmSysWorkerDO worker) {
        DmSysClusterDO cluster = systemDal.clusterMapper().getClusterByName(InitSeedConstants.DEFAULT_CLUSTER_NAME);
        if (cluster != null) {
            return cluster;
        }

        if (worker != null && worker.getClusterId() > 0) {
            cluster = systemDal.clusterMapper().queryById(worker.getClusterId());
            if (cluster != null) {
                return cluster;
            }
        }

        cluster = new DmSysClusterDO();
        cluster.setClusterName(InitSeedConstants.DEFAULT_CLUSTER_NAME);
        cluster.setClusterDesc(InitSeedConstants.DEFAULT_CLUSTER_DESC);
        cluster.setRegion(InitSeedConstants.DEFAULT_REGION);
        cluster.setCloudOrIdcName(CloudOrIdcName.SELF_MAINTENANCE);
        cluster.setUid(InitSeedConstants.ADMIN_UID);
        systemDal.clusterMapper().insert(cluster);
        log.info("DmFixDefaultClusterWorker: created default cluster, clusterId={}", cluster.getId());
        return cluster;
    }

    private DmSysWorkerDO createDefaultWorker(Long clusterId) {
        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setClusterId(clusterId);
        worker.setWorkerIp(InitSeedConstants.DEFAULT_WORKER_IP);
        worker.setCloudOrIdcName(CloudOrIdcName.SELF_MAINTENANCE);
        worker.setRegion(InitSeedConstants.DEFAULT_REGION);
        worker.setWorkerState(WorkerState.ONLINE);
        worker.setScheduleIp(InitSeedConstants.DEFAULT_CONSOLE_IP);
        worker.setWorkerName(InitSeedConstants.DEFAULT_WORKER_NAME);
        worker.setWorkerSeqNumber(InitSeedConstants.DEFAULT_WORKER_WSN);
        worker.setWorkerDesc(InitSeedConstants.DEFAULT_WORKER_NAME);
        worker.setExternalIp(InitSeedConstants.DEFAULT_EXTERNAL_IP);
        worker.setUid(InitSeedConstants.ADMIN_UID);
        worker.setConnStatus(WorkerConnStatus.NEW);
        worker.setSessionPoolUse(0);
        worker.setSessionPoolMax(100);
        systemDal.workerMapper().insert(worker);
        return worker;
    }

    private boolean isAloneMode() { return StringUtils.equalsIgnoreCase("embedded", System.getProperty("app.mode")); }
}
