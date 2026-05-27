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

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DmFixDefaultClusterWorker {
    @Resource
    private SystemDal           systemDal;

    private static final String ALONE_APP_MODE       = "embedded";
    private static final String DEFAULT_CLUSTER_NAME = "cluster1aw2byj490";
    private static final String DEFAULT_CLUSTER_DESC = "Default Cluster";
    private static final String DEFAULT_REGION       = "customer";
    private static final String DEFAULT_WORKER_NAME  = "workers8c4qs80l26";
    private static final String DEFAULT_WORKER_WSN   = "wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f";
    private static final String DEFAULT_WORKER_IP    = "172.31.239.4";
    private static final String DEFAULT_CONSOLE_IP   = "172.31.239.3";
    private static final String DEFAULT_EXTERNAL_IP  = "183.134.161.226";

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void init() {
        if (isAloneMode()) {
            log.info("DmFixDefaultClusterWorker: skip in alone mode");
            return;
        }

        DmSysWorkerDO worker = systemDal.workerMapper().getByWsn(DEFAULT_WORKER_WSN);
        DmSysClusterDO cluster = resolveCluster(worker);

        if (worker == null) {
            worker = createDefaultWorker(cluster.getId());
            log.info("DmFixDefaultClusterWorker: created default worker, wsn={}", DEFAULT_WORKER_WSN);
        } else if (worker.getClusterId() != cluster.getId()) {
            worker.setClusterId(cluster.getId());
            systemDal.workerMapper().updateById(worker);
            log.info("DmFixDefaultClusterWorker: normalized default worker clusterId, wsn={}, clusterId={}", DEFAULT_WORKER_WSN, cluster.getId());
        }

        if (worker.getConnStatus() != WorkerConnStatus.NEW) {
            worker.setConnStatus(WorkerConnStatus.NEW);
            worker.setLastHeartbeatReportMs(null);
            worker.setLastHeartbeatPingMs(null);
            systemDal.workerMapper().updateById(worker);
            log.info("DmFixDefaultClusterWorker: normalized default worker liveness fields, wsn={}", DEFAULT_WORKER_WSN);
        }
    }

    private DmSysClusterDO resolveCluster(DmSysWorkerDO worker) {
        DmSysClusterDO cluster = systemDal.clusterMapper().getClusterByName(DEFAULT_CLUSTER_NAME);
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
        cluster.setClusterName(DEFAULT_CLUSTER_NAME);
        cluster.setClusterDesc(DEFAULT_CLUSTER_DESC);
        cluster.setRegion(DEFAULT_REGION);
        cluster.setCloudOrIdcName(CloudOrIdcName.SELF_MAINTENANCE);
        cluster.setUid(InitSeedConstants.ADMIN_UID);
        systemDal.clusterMapper().insert(cluster);
        log.info("DmFixDefaultClusterWorker: created default cluster, clusterId={}", cluster.getId());
        return cluster;
    }

    private DmSysWorkerDO createDefaultWorker(Long clusterId) {
        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setClusterId(clusterId);
        worker.setWorkerIp(DEFAULT_WORKER_IP);
        worker.setCloudOrIdcName(CloudOrIdcName.SELF_MAINTENANCE);
        worker.setRegion(DEFAULT_REGION);
        worker.setWorkerState(WorkerState.ONLINE);
        worker.setScheduleIp(DEFAULT_CONSOLE_IP);
        worker.setWorkerName(DEFAULT_WORKER_NAME);
        worker.setWorkerSeqNumber(DEFAULT_WORKER_WSN);
        worker.setWorkerDesc(DEFAULT_WORKER_NAME);
        worker.setExternalIp(DEFAULT_EXTERNAL_IP);
        worker.setUid(InitSeedConstants.ADMIN_UID);
        worker.setConnStatus(WorkerConnStatus.NEW);
        worker.setSessionPoolUse(0);
        worker.setSessionPoolMax(100);
        systemDal.workerMapper().insert(worker);
        return worker;
    }

    private boolean isAloneMode() { return ALONE_APP_MODE.equals(System.getProperty("app.mode")); }
}
