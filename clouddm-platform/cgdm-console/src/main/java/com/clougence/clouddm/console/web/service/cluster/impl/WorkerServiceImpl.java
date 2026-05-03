package com.clougence.clouddm.console.web.service.cluster.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.console.status.*;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
import com.clougence.clouddm.console.web.constants.HealthLevel;
import com.clougence.clouddm.console.web.constants.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DmEventType;
import com.clougence.clouddm.console.web.dal.mapper.DmClusterMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerHeartbeatMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.mapper.param.WorkerParam;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.cluster.CreateInitialWorkerFO;
import com.clougence.clouddm.console.web.model.vo.cluster.WorkerDeployConfigVO;
import com.clougence.clouddm.console.web.service.cluster.WorkerService;
import com.clougence.clouddm.console.web.service.system.AlertConfigService;
import com.clougence.clouddm.console.web.service.system.NamingService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.dal.enumeration.LifeCycleState;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 21:11
 * @since 1.1.3
 */
@Service
@Slf4j
public class WorkerServiceImpl implements WorkerService, UnifiedPostConstruct {

    @Resource
    private DmWorkerMapper          workerMapper;
    @Resource
    private DmWorkerStatusMapper    workerStatusMapper;
    @Resource
    private DmWorkerHeartbeatMapper heartbeatMapper;
    @Resource
    private DmClusterMapper         clusterMapper;
    @Resource
    private WorkerDetector          workerDetector;
    @Resource
    private NamingService           namingService;
    @Resource
    private RdpUserService          rdpUserService;
    @Resource
    private DmConsoleConfig         dmConfig;
    @Resource
    private AlertConfigService      alertConfigService;
    @Resource
    private DmDsService             dmDsService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public DmWorkerDO createInitialWorker(String ownerUid, CreateInitialWorkerFO fo) {
        checkWorkerAndClusterPropMatch(fo.getCloudOrIdcName(), fo.getRegion(), fo.getClusterId());
        String workerSeqNumber = this.namingService.genWorkerSequenceNumber();
        String workerName = this.namingService.genWorkerName();

        DmWorkerDO workerDO = new DmWorkerDO();
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

        this.workerMapper.insert(workerDO);

        AlertConfigDetailDO detailDO = genDefaultWorkerAlertConfig(ownerUid, workerDO.getId());
        this.alertConfigService.addAlertConfig(Lists.newArrayList(detailDO), DmEventType.WORKER_EXCEPTION);

        DmWorkerStatusDO statusDO = genDefaultWorkerStatusDO(ownerUid, workerDO);
        this.workerStatusMapper.insert(statusDO);
        return workerDO;
    }

    protected void checkWorkerAndClusterPropMatch(CloudOrIdcName deployEnvType, String region, Long clusterId) {
        DmClusterDO clusterDO = this.clusterMapper.selectById(clusterId);
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

    private static AlertConfigDetailDO genDefaultWorkerAlertConfig(String ownerUid, long workerId) {
        AlertConfigDetailDO conf = new AlertConfigDetailDO();
        conf.setUid(ownerUid);
        conf.setDingding(true);
        conf.setEmail(true);
        conf.setSms(true);
        conf.setPhone(false);
        conf.setDuplicated(false);
        conf.setSendAdmin(false);
        conf.setSendSystem(false);
        conf.setRuleName(DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_WORKER_ALIVE_ALERT.name()));
        conf.setEventType(DmEventType.WORKER_EXCEPTION);
        conf.setWorkerId(workerId);
        return conf;
    }

    private static DmWorkerStatusDO genDefaultWorkerStatusDO(String ownerUid, DmWorkerDO workerDO) {
        DmWorkerStatusDO statusDO = new DmWorkerStatusDO();
        statusDO.setUid(ownerUid);
        statusDO.setWorkerConnStatus(WorkerConnStatus.NEW);
        statusDO.setWorkerSeqNumber(workerDO.getWorkerSeqNumber());
        statusDO.setClusterId(workerDO.getClusterId());
        return statusDO;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteWorker(long workerId, boolean force) {
        DmWorkerDO workerDO = this.workerMapper.selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not exist.");
        }

        this.checkWorkerStateForDelete(workerDO);

        if (!force) {
            List<RdpDataSourceDO> bindDs = this.dmDsService.listDsByClusterId(workerDO.getClusterId());
            if (CollectionUtils.isNotEmpty(bindDs)) {
                List<DmWorkerDO> workerDOs = this.workerMapper.listByCluster(workerDO.getClusterId());
                if (workerDOs.size() == 1) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_DEL_LEAST_WORK_ERROR.name()));
                }
            }
        }

        this.workerMapper.deleteById(workerId);
        this.alertConfigService.deleteByWorkerId(workerId);
        this.workerStatusMapper.deleteByWsn(workerDO.getWorkerSeqNumber());
    }

    private void checkWorkerStateForDelete(DmWorkerDO workerDO) {
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
        DmWorkerDO workerDO = this.workerMapper.selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not in db.");
        }

        if (WorkerState.WAIT_TO_ONLINE == workerDO.getWorkerState() || WorkerState.ABNORMAL == workerDO.getWorkerState()) {
            return;
        }

        this.workerMapper.updateWorkerState(workerDO.getId(), WorkerState.WAIT_TO_ONLINE);
    }

    @Override
    public void updateToWaitToOffline(long workerId) {
        DmWorkerDO workerDO = this.workerMapper.selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not in db.");
        }

        if (workerDO.getWorkerState() != null && WorkerState.WAIT_TO_OFFLINE == workerDO.getWorkerState()) {
            return;
        }

        if (workerDO.getWorkerState() != WorkerState.ONLINE && workerDO.getWorkerState() != WorkerState.WAIT_TO_ONLINE && workerDO.getWorkerState() != WorkerState.ABNORMAL) {
            throw new IllegalArgumentException("worker not in ONLINE or WAIT_TO_ONLINE or ABNORMAL,can not update worker to WAIT_TO_OFFLINE.");
        }

        this.workerMapper.updateWorkerState(workerDO.getId(), WorkerState.WAIT_TO_OFFLINE);
    }

    @Override
    public List<DmWorkerStatusDO> listConnectedWorkers(long clusterId) {
        return this.workerStatusMapper.queryByClusterIdAndStatus(clusterId, WorkerConnStatus.CONNECTED);
    }

    @Override
    public List<DmWorkerDO> listWorkers(long clusterId) {
        return this.workerMapper.listByCluster(clusterId);
    }

    @Override
    public DmWorkerDO getWorkerById(Long workerId) {
        DmWorkerDO workerDO = this.workerMapper.selectById(workerId);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + workerId + ") not in db.");
        }

        return workerDO;
    }

    @Override
    public DmWorkerDO getWorkerByWsn(String wsn) {
        DmWorkerDO workerDO = this.workerMapper.getByWsn(wsn);
        if (workerDO == null) {
            throw new IllegalArgumentException("worker (" + wsn + ") not in db.");
        }

        return workerDO;
    }

    @Override
    public void updateStatus(Long workerId, WorkerState workerState) {
        if (workerState != null) {
            int i = this.workerMapper.updateWorkerState(workerId, workerState);
            log.info("update worker (" + workerId + ") state (" + workerState + "),affect row:" + i);
        }
    }

    @Override
    public void updateLifecycleState(Long workerId, LifeCycleState lifeCycleState) {
        if (lifeCycleState != null) {
            int i = this.workerMapper.updateWorkerLifecycleState(workerId, lifeCycleState);
            log.info("update worker (" + workerId + ") life cycle state (" + lifeCycleState + "),affect row:" + i);
        }
    }

    @Override
    public void updateWorkerIp(long workerId, String workerIp, String externalIp) {
        if (StringUtils.isNotBlank(workerIp)) {
            this.workerMapper.updateWorkerIp(workerId, workerIp);
        }
        if (StringUtils.isNotBlank(externalIp)) {
            int i = this.workerMapper.updateExternalIp(workerId, externalIp);
            log.info("update worker (" + workerId + ") life external ip (" + externalIp + "),affect row:" + i);
        }
    }

    @Override
    public void updateWorkerDesc(Long workerId, String desc) {
        this.workerMapper.updateWorkerDesc(workerId, desc);
    }

    @Override
    public void updateWorkerMetric(Long workerId, MetricStats metricStats) {
        WorkerParam param = new WorkerParam();
        param.setWorkerId(workerId);

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

        this.workerMapper.updateWorkerDynamicStats(param);
    }

    @Override
    public String getClientDownloadUrl(long workerId) {
        //        Region region = this.dmConfig.getOssDownloadRegion();
        //        LocalDateTime expireDate = LocalDateTime.now().plusHours(1);
        //
        //        String ossAk = this.dmConfig.getOssDownloadAk();
        //        String ossSk = this.dmConfig.getOssDownloadSk();
        //        String ossEndpoint = this.rdpRegionService.getOssEndpoint(region, true);
        //        String ossBucket = this.rdpRegionService.getOssBucket(region);
        //        String ossDownPackName = this.dmConfig.getOssDownloadPackageName();
        //
        //        URL url = OssOpenApiImpl.getInstance().genTempUrlForFile(ossAk, ossSk, ossEndpoint, ossBucket, ossDownPackName, expireDate);
        //        return this.dmConfig.getOssDownloadSite() + url.getFile();
        return "";
    }

    @Override
    public WorkerDeployConfigVO getClientDeployCoreConfig(Long workerId, String uid) {
        if (StringUtils.isBlank(this.dmConfig.getConsoleRsocketDns())) {
            throw new IllegalArgumentException("console url is empty.");
        }

        RdpUserDO userDO = this.rdpUserService.getUserByUid(uid);
        if (userDO == null || StringUtils.isBlank(userDO.getAccessKey()) || StringUtils.isBlank(userDO.getSecretKey())) {
            throw new IllegalArgumentException("current user info is not complete.");
        }

        DmWorkerDO workerDO = getWorkerById(workerId);
        if (workerDO == null || StringUtils.isBlank(workerDO.getWorkerSeqNumber())) {
            throw new IllegalArgumentException("worker (" + workerId + ") info is not complete.");
        }

        WorkerDeployConfigVO configVO = new WorkerDeployConfigVO();
        configVO.setUserAkValue(userDO.getAccessKey());
        configVO.setUserSkValue(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(userDO.getSecretKey()));
        configVO.setWsnValue(workerDO.getWorkerSeqNumber());
        configVO.setConsoleHostValue(this.dmConfig.getConsoleRsocketDns());
        configVO.setConsolePortValue(String.valueOf(this.dmConfig.getRsocketConsolePort()));
        return configVO;
    }

    @Override
    public void upsertWorkerHeartbeat(DmWorkerHeartbeatDO heartbeatDO) {
        DmWorkerHeartbeatDO h = this.heartbeatMapper.queryHeartbeatByWsn(heartbeatDO.getWorkerSeqNumber());
        if (h == null) {
            this.heartbeatMapper.insert(heartbeatDO);
        } else {
            this.heartbeatMapper.updateHeartbeatByWsn(heartbeatDO.getWorkerSendTime(), heartbeatDO.getHeartbeatType(), heartbeatDO.getWorkerSeqNumber());
        }

        DmWorkerStatusDO workerStatusDO = this.workerStatusMapper.queryByWsn(heartbeatDO.getWorkerSeqNumber());
        if (workerStatusDO != null) {
            workerStatusDO.setWorkerConnStatus(WorkerConnStatus.CONNECTED);
            workerStatusDO.setGmtModified(new Date());
            this.workerStatusMapper.updateConnInfoByWsn(workerStatusDO);
            log.debug("update worker stats to connected......");
        }
    }

    @Override
    public void init() {
        Thread checkWorkerStatus = ThreadUtils.daemonThread(() -> {
            while (true) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -5);
                    List<DmWorkerStatusDO> statusList = this.workerStatusMapper.queryInactivity(calendar.getTime());
                    if (statusList != null) {
                        for (DmWorkerStatusDO statusDO : statusList) {
                            statusDO.setWorkerConnStatus(WorkerConnStatus.DISCONNECTED);
                            this.workerStatusMapper.updateConnInfoByWsn(statusDO);
                        }
                    }

                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    ThreadUtils.sleep(10000);
                }
            }
        });

        checkWorkerStatus.setName("checkWorkerStatus");
        checkWorkerStatus.setDaemon(true);
        checkWorkerStatus.start();
    }

    @Override
    public void stop() {

    }
}
