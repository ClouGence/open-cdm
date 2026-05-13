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
package com.clougence.clouddm.boot;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
import com.clougence.clouddm.console.web.constants.DeployStatus;
import com.clougence.clouddm.console.web.dal.enumeration.LifeCycleState;
import com.clougence.clouddm.console.web.dal.mapper.DmClusterMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.model.DmClusterDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateClusterFO;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateInitialWorkerFO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.clouddm.console.web.service.cluster.WorkerService;
import com.clougence.clouddm.console.web.service.system.NamingService;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmbeddedWorkerBootstrap {

    private static final String  DEFAULT_REGION = "customer";
    @Resource
    private RdpUserMapper        rdpUserMapper;
    @Resource
    private ClusterService       clusterService;
    @Resource
    private DmClusterMapper      dmClusterMapper;
    @Resource
    private DmWorkerMapper       workerMapper;
    @Resource
    private WorkerService        workerService;
    @Resource
    private NamingService        namingService;

    @Transactional(rollbackFor = Throwable.class)
    public void init() {
        System.setProperty("app.mode", "embedded");

        RdpUserDO primaryUser = requirePrimaryUser();
        ensureUserCredentials(primaryUser);

        Long clusterId = ensureCluster(primaryUser.getUid());
        DmWorkerDO worker = ensureWorker(primaryUser.getUid(), clusterId);
        resetWorkerStatus(worker);
        applyEmbeddedWorkerConfig(worker.getId(), primaryUser.getUid());

        log.info("embedded worker bootstrap ready, uid={}, workerId={}, wsn={}", primaryUser.getUid(), worker.getId(), worker.getWorkerSeqNumber());
    }

    private RdpUserDO requirePrimaryUser() {
        List<RdpUserDO> primaryUsers = this.rdpUserMapper.listPrimaryAccount();
        if (CollectionUtils.isEmpty(primaryUsers)) {
            throw new IllegalStateException("embedded alone requires an existing primary account from initialized data.");
        }

        for (RdpUserDO primaryUser : primaryUsers) {
            if (StringUtils.isNotBlank(primaryUser.getAccessKey()) && StringUtils.isNotBlank(primaryUser.getSecretKey())) {
                return primaryUser;
            }
        }

        throw new IllegalStateException("embedded alone requires a primary account with access key and secret key.");
    }

    private Long ensureCluster(String ownerUid) {
        List<ClusterVO> clusters = this.clusterService.listByOwnerUid(ownerUid);
        if (CollectionUtils.isNotEmpty(clusters)) {
            return clusters.get(0).getId();
        }

        CreateClusterFO fo = new CreateClusterFO();
        fo.setCloudOrIdcName(CloudOrIdcName.SELF_MAINTENANCE);
        fo.setRegion(DEFAULT_REGION);
        fo.setClusterDesc("Default Cluster");
        return this.clusterService.addCluster(ownerUid, ownerUid, fo);
    }

    private void ensureUserCredentials(RdpUserDO primaryUser) {
        if (StringUtils.isBlank(primaryUser.getUid()) || StringUtils.isBlank(primaryUser.getAccessKey()) || StringUtils.isBlank(primaryUser.getSecretKey())) {
            throw new IllegalStateException("embedded alone requires initialized primary user credentials.");
        }
    }

    private DmWorkerDO ensureWorker(String ownerUid, Long clusterId) {
        List<DmWorkerDO> workers = this.workerService.listWorkers(clusterId);
        if (CollectionUtils.isNotEmpty(workers)) {
            return normalizeWorker(workers.get(0), ownerUid, clusterId);
        }

        DmClusterDO cluster = this.dmClusterMapper.selectById(clusterId);
        CreateInitialWorkerFO fo = new CreateInitialWorkerFO();
        fo.setClusterId(clusterId);
        fo.setCloudOrIdcName(cluster.getCloudOrIdcName() == null ? CloudOrIdcName.SELF_MAINTENANCE : cluster.getCloudOrIdcName());
        fo.setRegion(StringUtils.defaultIfBlank(cluster.getRegion(), DEFAULT_REGION));
        DmWorkerDO worker = this.workerService.createInitialWorker(ownerUid, fo);
        return normalizeWorker(worker, ownerUid, clusterId);
    }

    private DmWorkerDO normalizeWorker(DmWorkerDO worker, String ownerUid, Long clusterId) {
        DmClusterDO cluster = this.dmClusterMapper.selectById(clusterId);
        boolean changed = false;
        String localIp = HostUtil.getHostIp();

        if (!StringUtils.equals(worker.getUid(), ownerUid)) {
            worker.setUid(ownerUid);
            changed = true;
        }
        if (worker.getClusterId() != clusterId) {
            worker.setClusterId(clusterId);
            changed = true;
        }
        if (worker.getCloudOrIdcName() == null) {
            worker.setCloudOrIdcName(cluster != null && cluster.getCloudOrIdcName() != null ? cluster.getCloudOrIdcName() : CloudOrIdcName.SELF_MAINTENANCE);
            changed = true;
        }
        if (StringUtils.isBlank(worker.getRegion())) {
            worker.setRegion(cluster != null ? StringUtils.defaultIfBlank(cluster.getRegion(), DEFAULT_REGION) : DEFAULT_REGION);
            changed = true;
        }
        if (StringUtils.isBlank(worker.getWorkerSeqNumber())) {
            worker.setWorkerSeqNumber(this.namingService.genWorkerSequenceNumber());
            changed = true;
        }
        if (StringUtils.isBlank(worker.getWorkerName())) {
            worker.setWorkerName(this.namingService.genWorkerName());
            changed = true;
        }
        if (StringUtils.isBlank(worker.getWorkerDesc())) {
            worker.setWorkerDesc(worker.getWorkerName());
            changed = true;
        }
        if (StringUtils.isBlank(worker.getScheduleIp())) {
            worker.setScheduleIp(localIp);
            changed = true;
        }
        if (worker.getLifeCycleState() == null) {
            worker.setLifeCycleState(LifeCycleState.CREATED);
            changed = true;
        }
        if (worker.getDeployStatus() == null) {
            worker.setDeployStatus(DeployStatus.INSTALLED);
            changed = true;
        }
        if (worker.getWorkerState() != WorkerState.WAIT_TO_ONLINE) {
            worker.setWorkerState(WorkerState.WAIT_TO_ONLINE);
            changed = true;
        }

        if (changed) {
            worker.setGmtModified(new Date());
            this.workerMapper.updateById(worker);
        }

        return worker;
    }

    private void resetWorkerStatus(DmWorkerDO worker) {
        worker.setConnStatus(WorkerConnStatus.NEW);
        worker.setLastHeartbeatReportMs(null);
        worker.setLastHeartbeatPingMs(null);
        worker.setGmtModified(new Date());
        this.workerMapper.updateById(worker);
    }

    private void applyEmbeddedWorkerConfig(Long workerId, String ownerUid) {
        WorkerDeployConfigVO config = this.workerService.getClientDeployCoreConfig(workerId, ownerUid);
        GlobalConfUtils.config.clear();
        GlobalConfUtils.config.put(config.getConsoleHostLabel(), config.getConsoleHostValue());
        GlobalConfUtils.config.put(config.getConsolePortLabel(), config.getConsolePortValue());
        GlobalConfUtils.config.put(config.getWsnLabel(), config.getWsnValue());
        GlobalConfUtils.config.put(config.getUserAkLabel(), config.getUserAkValue());
        GlobalConfUtils.config.put(config.getUserSkLabel(), config.getUserSkValue());
    }
}
