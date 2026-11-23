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
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.status.*;
import com.clougence.clouddm.api.sidecar.status.WorkerStatusRService;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.constants.HealthLevel;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.rsocket.DmServerSideRegistry;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateInitialWorkerFO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.console.web.service.system.AlertConfigService;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonAlertConfigDetailDO;
import com.clougence.clouddm.platform.dal.model.monitor.EventType;
import com.clougence.clouddm.platform.dal.model.system.ArgSysWorkerObj;
import com.clougence.clouddm.platform.dal.model.system.CloudOrIdcName;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;
import com.google.common.collect.Lists;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 21:11
 * @since 1.1.3
 */
@Service
@Slf4j
public class WorkerServiceImpl implements WorkerService, UnifiedPostConstruct {

    private static final long     HEARTBEAT_TIMEOUT_MS = 15_000L;
    private final AtomicBoolean   checking             = new AtomicBoolean();
    private Thread                checker;
    private boolean               stopped;
    @Resource
    private SystemDal             systemDal;
    @Resource
    private WorkerDetector        workerDetector;
    @Resource
    private NamingDao             namingDao;
    @Resource
    private RdpUserService        rdpUserService;
    @Resource
    private ConsoleConfig         config;
    @Resource
    private AlertConfigService    alertConfigService;
    @Resource
    private DmDsWebService        dmDsService;
    @Resource
    private WorkerStatusRService  statusRService;
    @Resource
    private ConsoleClusterService cluster;
    @Resource
    private DmServerSideRegistry  registry;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public DmSysWorkerDO createInitialWorker(String ownerUid, CreateInitialWorkerFO fo) {
        checkWorkerAndClusterPropMatch(fo.getCloudOrIdcName(), fo.getRegion(), fo.getClusterId());
        String workerSeqNumber = this.namingDao.genWorkerSequenceNumber();
        String workerName = this.namingDao.genWorkerName();

        DmSysWorkerDO workerDO = new DmSysWorkerDO();
        workerDO.setCloudOrIdcName(fo.getCloudOrIdcName());
        workerDO.setClusterId(fo.getClusterId());
        workerDO.setRegion(fo.getRegion());
        workerDO.setWorkerState(WorkerState.WAIT_TO_ONLINE);
        workerDO.setScheduleIp(HostUtil.getHostIp());
        workerDO.setWorkerSeqNumber(workerSeqNumber);
        workerDO.setWorkerName(workerName);
        workerDO.setWorkerDesc(workerName);
        workerDO.setUid(ownerUid);
        workerDO.setLifeCycleState(LifeCycleState.CREATING);
        workerDO.setConnStatus(WorkerConnStatus.NEW);

        this.systemDal.workerMapper().insert(workerDO);

        DmMonAlertConfigDetailDO detailDO = genDefaultWorkerAlertConfig(ownerUid, workerDO.getId());
        this.alertConfigService.addAlertConfig(Lists.newArrayList(detailDO), EventType.WORKER_EXCEPTION);
        return workerDO;
    }

    protected void checkWorkerAndClusterPropMatch(CloudOrIdcName deployEnvType, String region, Long clusterId) {
        DmSysClusterDO clusterDO = this.systemDal.clusterMapper().selectById(clusterId);
        if (clusterDO == null) {
            throw new IllegalArgumentException("cluster (" + clusterId + ") not exist.");
        }

        if (clusterDO.getRegion() == null || clusterDO.getCloudOrIdcName() == null) {
            return;
        }

        if (deployEnvType == null || deployEnvType != clusterDO.getCloudOrIdcName() || region == null || !region.equals(clusterDO.getRegion())) {
            throw new IllegalArgumentException("worker not match cluster(id:" + clusterDO.getId() + ")'s region or deploy env type");
        }
    }

    private static DmMonAlertConfigDetailDO genDefaultWorkerAlertConfig(String ownerUid, long workerId) {
        DmMonAlertConfigDetailDO conf = new DmMonAlertConfigDetailDO();
        conf.setUid(ownerUid);
        conf.setDingding(true);
        conf.setEmail(true);
        conf.setSms(true);
        conf.setPhone(false);
        conf.setDuplicated(false);
        conf.setSendAdmin(false);
        conf.setSendSystem(false);
        conf.setRuleName(DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_WORKER_ALIVE_ALERT.name()));
        conf.setEventType(EventType.WORKER_EXCEPTION);
        conf.setWorkerId(workerId);
        return conf;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void deleteWorker(long workerId, boolean force) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not exist.");
        }

        this.checkWorkerStateForDelete(workerDO);

        if (!force) {
            List<DmDsDO> bindDs = this.dmDsService.listDsByClusterId(workerDO.getClusterId());
            if (CollectionUtils.isNotEmpty(bindDs)) {
                List<DmSysWorkerDO> workerDOs = this.systemDal.workerMapper().listByCluster(workerDO.getClusterId());
                if (workerDOs.size() == 1) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_DEL_LEAST_WORK_ERROR.name()));
                }
            }
        }

        this.systemDal.workerMapper().deleteById(workerId);
        this.alertConfigService.deleteByWorkerId(workerId);
    }

    private void checkWorkerStateForDelete(DmSysWorkerDO workerDO) {
        // if worker ip is empty. just check worker still have tasks attached.
        if (StringUtils.isBlank(workerDO.getWorkerIp())) {
            return;
        }

        // if worker is sub healthy or unhealthy
        HealthLevel healthLevel = this.workerDetector.getHealthLevel(workerDO);
        if (healthLevel == HealthLevel.Unhealthy || healthLevel == HealthLevel.SubHealth) {
            return;
        }

        if (workerDO.getWorkerState() != WorkerState.OFFLINE && workerDO.getWorkerState() != WorkerState.WAIT_TO_OFFLINE) {
            throw new IllegalArgumentException("worker not in OFFLINE or WAIT_TO_OFFLINE status.can not uninstall or delete.");
        }
    }

    @Override
    public void updateToWaitToOnline(long workerId) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not in db.");
        }

        if (WorkerState.WAIT_TO_ONLINE == workerDO.getWorkerState() || WorkerState.ABNORMAL == workerDO.getWorkerState()) {
            return;
        }

        this.systemDal.workerMapper().updateWorkerState(workerDO.getId(), WorkerState.WAIT_TO_ONLINE);
    }

    @Override
    public void updateToWaitToOffline(long workerId) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not in db.");
        }

        if (workerDO.getWorkerState() != null && WorkerState.WAIT_TO_OFFLINE == workerDO.getWorkerState()) {
            return;
        }

        if (workerDO.getWorkerState() != WorkerState.ONLINE && workerDO.getWorkerState() != WorkerState.WAIT_TO_ONLINE && workerDO.getWorkerState() != WorkerState.ABNORMAL) {
            throw new IllegalArgumentException("worker not in ONLINE or WAIT_TO_ONLINE or ABNORMAL,can not update worker to WAIT_TO_OFFLINE.");
        }

        this.systemDal.workerMapper().updateWorkerState(workerDO.getId(), WorkerState.WAIT_TO_OFFLINE);
    }

    @Override
    public List<DmSysWorkerDO> listConnectedWorkers(long clusterId) {
        return this.systemDal.workerMapper().queryConnectedByClusterId(clusterId);
    }

    @Override
    public List<DmSysWorkerDO> listWorkers(long clusterId) {
        return this.systemDal.workerMapper().listByCluster(clusterId);
    }

    @Override
    public DmSysWorkerDO getWorkerById(Long workerId) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not in db.");
        }

        return workerDO;
    }

    @Override
    public DmSysWorkerDO getWorkerByWsn(String wsn) {
        DmSysWorkerDO workerDO = queryWorkerByWsn(wsn);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + wsn + ") not in db.");
        }

        return workerDO;
    }

    @Override
    public DmSysWorkerDO queryWorkerByWsn(String wsn) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().getByWsn(wsn);
        return workerDO;
    }

    @Override
    public void updateStatus(Long workerId, WorkerState workerState) {
        if (workerState != null) {
            int i = this.systemDal.workerMapper().updateWorkerState(workerId, workerState);
            log.info("update worker (" + workerId + ") state (" + workerState + "),affect row:" + i);
        }
    }

    @Override
    public void updateLifecycleState(Long workerId, LifeCycleState lifeCycleState) {
        if (lifeCycleState != null) {
            int i = this.systemDal.workerMapper().updateWorkerLifecycleState(workerId, lifeCycleState);
            log.info("update worker (" + workerId + ") life cycle state (" + lifeCycleState + "),affect row:" + i);
        }
    }

    @Override
    public void updateWorkerIp(WorkerIdentity identity, String workerIp) {
        if (StringUtils.isNotBlank(workerIp)) {
            DmSysWorkerDO update = new DmSysWorkerDO();
            update.setWorkerSeqNumber(identity.getWorkerSeqNumber());
            update.setOwnerInstanceId(identity.getOwnerConsoleId());
            update.setConnectionGeneration(identity.getConnectionGeneration());
            update.setWorkerIp(workerIp);
            this.systemDal.workerMapper().updateConnectionByWsnAndOwner(update);
        }
    }

    @Override
    public void updateWorkerDesc(Long workerId, String desc) {
        this.systemDal.workerMapper().updateWorkerDesc(workerId, desc);
    }

    @Override
    public void updateWorkerMetric(WorkerIdentity identity, MetricStats metricStats) {
        ArgSysWorkerObj param = new ArgSysWorkerObj();
        param.setWorkerSeqNumber(identity.getWorkerSeqNumber());
        param.setOwnerInstanceId(identity.getOwnerConsoleId());
        param.setConnectionGeneration(identity.getConnectionGeneration());

        // usageStats
        if (metricStats.getUsageStats() != null) {
            UsageStats usageStats = metricStats.getUsageStats();
            param.setSessionPoolMax(usageStats.getSessionPoolMax());
            param.setSessionPoolUse(usageStats.getSessionPoolUse());
        }
        // cpuStats
        if (metricStats.getCpuStats() != null) {
            CpuStats cpuStats = metricStats.getCpuStats();
            param.setCpuUseRatio(cpuStats.getUserRatio().doubleValue());
            param.setPhysicCoreNum(cpuStats.getLogicalCoreCount());
            param.setWorkerLoad(cpuStats.getAvgLoadRatio().doubleValue());
        }
        // memStats
        if (metricStats.getMemStats() != null) {
            MemStats memStats = metricStats.getMemStats();
            param.setPhysicMemMb(memStats.getJvmTotalMemoryMb().longValue());
            param.setFreeMemMb(memStats.getJvmFreeMemoryMb().longValue());
            param.setMemUseRatio(memStats.getJvmMemoryUsage().doubleValue());
        }
        // diskStats
        if (metricStats.getDiskStats() != null) {
            DiskStats diskStats = metricStats.getDiskStats();
            param.setPhysicDiskGb(diskStats.getTotalDiskGB().longValue());
            param.setFreeDiskGb(diskStats.getFreeDiskGB().longValue());
        }

        this.systemDal.workerMapper().updateWorkerDynamicStats(param);
    }

    @Override
    public String getClientDownloadUrl(long workerId) {
        return "";
    }

    @Override
    public WorkerDeployConfigVO getClientDeployCoreConfig(Long workerId, String uid) {
        if (StringUtils.isBlank(this.config.getConsoleRsocketDns())) {
            throw new IllegalArgumentException("console url is empty.");
        }

        DmAuthUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null || StringUtils.isBlank(userDO.getAccessKey()) || StringUtils.isBlank(userDO.getSecretKey())) {
            throw new IllegalArgumentException("current user info is not complete.");
        }

        DmSysWorkerDO workerDO = getWorkerById(workerId);
        if (workerDO == null || StringUtils.isBlank(workerDO.getWorkerSeqNumber())) {
            throw new IllegalArgumentException("worker (" + workerId + ") info is not complete.");
        }

        WorkerDeployConfigVO configVO = new WorkerDeployConfigVO();
        configVO.setUserAkValue(userDO.getAccessKey());
        configVO.setUserSkValue(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userDO.getSecretKey()));
        configVO.setWsnValue(workerDO.getWorkerSeqNumber());
        configVO.setConsoleHostValue(this.config.getConsoleRsocketDns());
        configVO.setConsolePortValue(String.valueOf(this.config.getRsocketConsolePort()));
        return configVO;
    }

    @Override
    public void updateOwnedStatus(WorkerIdentity identity, WorkerState previousState, WorkerState targetState) {
        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setWorkerSeqNumber(identity.getWorkerSeqNumber());
        worker.setOwnerInstanceId(identity.getOwnerConsoleId());
        worker.setConnectionGeneration(identity.getConnectionGeneration());
        worker.setWorkerState(targetState);
        worker.setLifeCycleState(LifeCycleState.CREATED);
        this.systemDal.workerMapper().updateWorkerStateByWsn(worker, previousState);
    }

    @Override
    public void upsertWorkerHeartbeat(WorkerIdentity identity) {
        DmSysWorkerDO worker = new DmSysWorkerDO();
        worker.setWorkerSeqNumber(identity.getWorkerSeqNumber());
        worker.setOwnerInstanceId(identity.getOwnerConsoleId());
        worker.setConnectionGeneration(identity.getConnectionGeneration());
        worker.setWorkerIp(identity.getLocalIp());
        worker.setLastHeartbeatReportMs(System.currentTimeMillis());
        this.systemDal.workerMapper().updateConnectionByWsnAndOwner(worker);
    }

    @Override
    public synchronized void init() {
        if (this.stopped || this.checker != null) {
            return;
        }

        checking.set(true);
        checker = ThreadUtils.daemonThread(() -> {
            while (checking.get()) {
                try {
                    checkWorkerStatus();
                } catch (Exception e) {
                    log.error("Check worker status failed", e);
                } finally {
                    ThreadUtils.sleep(10000);
                }
            }
        });

        checker.setName("checkWorkerStatus");
        checker.setDaemon(true);
        checker.start();
    }

    private void checkWorkerStatus() {
        if (!cluster.isLeaseValid()) {
            return;
        }

        List<DmSysWorkerDO> connectedWorkers = this.systemDal.workerMapper().queryByConnStatus(WorkerConnStatus.CONNECTED);
        long heartbeatCutoffMillis = System.currentTimeMillis() - HEARTBEAT_TIMEOUT_MS;
        for (DmSysWorkerDO worker : connectedWorkers) {
            if (!cluster.getInstanceId().equals(worker.getOwnerInstanceId())) {
                if (cluster.isAlive(worker.getOwnerInstanceId())) {
                    continue;
                }

                DmSysWorkerDO disconnected = new DmSysWorkerDO();
                disconnected.setWorkerSeqNumber(worker.getWorkerSeqNumber());
                disconnected.setOwnerInstanceId(worker.getOwnerInstanceId());
                disconnected.setConnectionGeneration(worker.getConnectionGeneration());
                disconnected.setConnStatus(WorkerConnStatus.DISCONNECTED);
                this.systemDal.workerMapper().updateConnectionByWsnAndOwner(disconnected);
                continue;
            }

            if (worker.getLastHeartbeatReportMs() != null && worker.getLastHeartbeatReportMs() >= heartbeatCutoffMillis) {
                updateWorkerPing(worker);
                continue;
            }

            worker.setConnStatus(WorkerConnStatus.DISCONNECTED);
            if (this.systemDal.workerMapper().updateConnStatusByWsnAndHeartbeat(worker, WorkerConnStatus.CONNECTED, heartbeatCutoffMillis) != 1) {
                continue;
            }

            RSocketRegisterInfo connection = registry.connection(worker.getWorkerSeqNumber());
            if (connection == null || connection.getConnectionGeneration() != worker.getConnectionGeneration()) {
                continue;
            }

            connection.getRequester().rsocket().dispose();
        }
    }

    private void updateWorkerPing(DmSysWorkerDO workerDO) {
        if (workerDO == null || StringUtils.isBlank(workerDO.getWorkerSeqNumber())) {
            return;
        }

        try {
            long startMs = System.currentTimeMillis();
            this.statusRService.ping(buildPingSendDTO(workerDO), startMs);

            DmSysWorkerDO updateDO = new DmSysWorkerDO();
            updateDO.setWorkerSeqNumber(workerDO.getWorkerSeqNumber());
            updateDO.setOwnerInstanceId(workerDO.getOwnerInstanceId());
            updateDO.setConnectionGeneration(workerDO.getConnectionGeneration());
            updateDO.setLastHeartbeatPingMs(System.currentTimeMillis() - startMs);
            this.systemDal.workerMapper().updateConnectionByWsnAndOwner(updateDO);
        } catch (Exception e) {
            log.debug("update worker ({}) ping failed, root cause: {}", workerDO.getWorkerSeqNumber(), e.getMessage());
        }
    }

    private RSocketSendDTO buildPingSendDTO(DmSysWorkerDO workerDO) {
        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(workerDO.getClusterId());
        sendDTO.setWorkerSeqNumber(workerDO.getWorkerSeqNumber());
        sendDTO.setWorkerIP(workerDO.getWorkerIp());
        sendDTO.setUid(workerDO.getUid());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }

    @Override
    public synchronized void stop() {
        this.stopped = true;
        checking.set(false);
        if (checker != null) {
            checker.interrupt();
        }
    }
}
