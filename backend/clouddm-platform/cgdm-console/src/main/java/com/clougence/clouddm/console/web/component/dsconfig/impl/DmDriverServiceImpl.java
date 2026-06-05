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
package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.drivers.DriversRService;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverRes;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverVer;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDriverService;
import com.clougence.clouddm.console.web.component.dsconfig.event.DriverDownloadEvent;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DriverDownloadProgressVO;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DmDriverServiceImpl implements DmDriverService {

    private static final int                 PROGRESS_LOG_STEP = 10;
    @Resource
    private SystemDal                        systemDal;
    @Resource
    private DriversRService                  driversRService;
    private final Map<String, Boolean>       runningTasks      = new ConcurrentHashMap<>();
    private static final Map<String, String> progressLogState  = new ConcurrentHashMap<>();
    private final ExecutorService            downloadExecutor;

    public DmDriverServiceImpl(){
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "driver-download-%s");
        this.downloadExecutor = Executors.newCachedThreadPool(threadFactory);
    }

    @Override
    public DriverVersionStatusVO checkDriverStatus(Long clusterId, String driverFamily, String driverVersion) {
        DriverVersionStatusVO statusVO = new DriverVersionStatusVO();
        statusVO.setDriverFamily(driverFamily);
        statusVO.setDriverVersion(driverVersion);
        statusVO.setWorkerWsn(new java.util.ArrayList<>());

        DriverVersion localVersion = PluginManager.driverLoader().findDriver(driverFamily, driverVersion);
        if (localVersion != null) {
            PluginManager.driverLoader().refreshDriverVersion(localVersion);
        }

        boolean consoleAvailable = isPrepared(localVersion);
        List<DmSysWorkerDO> workers = queryTargetWorkers(clusterId);
        if (CollectionUtils.isEmpty(workers)) {
            statusVO.setAvailable(consoleAvailable);
            return statusVO;
        }

        boolean workersAvailable = true;
        for (DmSysWorkerDO worker : workers) {
            DsDriverVer remoteVersion;
            try {
                remoteVersion = this.driversRService.refreshDriverVersion(buildSendDTO(worker), driverFamily, driverVersion);
            } catch (Exception e) {
                log.warn("check driver status on worker failed, family={}, version={}, workerIp={}, workerSeqNumber={}", //
                        driverFamily, driverVersion, worker.getWorkerIp(), worker.getWorkerSeqNumber(), e);
                workersAvailable = false;
                break;
            }

            if (!isPrepared(remoteVersion)) {
                workersAvailable = false;
                break;
            }

            statusVO.getWorkerWsn().add(worker.getWorkerSeqNumber());
        }

        statusVO.setAvailable(consoleAvailable && workersAvailable);
        return statusVO;
    }

    @Override
    public void downloadDriver(String uid, Long clusterId, String driverFamily, String driverVersion) {
        String taskKey = buildTaskKey(uid, clusterId, driverFamily, driverVersion);
        if (this.runningTasks.putIfAbsent(taskKey, Boolean.TRUE) != null) {
            publishDownloadStarted(uid, clusterId, driverFamily, driverVersion);
            return;
        }

        publishDownloadStarted(uid, clusterId, driverFamily, driverVersion);
        this.downloadExecutor.execute(() -> {
            try {
                new DmDriverDownloadTask(uid, clusterId, driverFamily, driverVersion, this.systemDal, this.driversRService).run();
            } catch (Exception e) {
                log.error("download driver failed, uid={}, clusterId={}, family={}, version={}", uid, clusterId, driverFamily, driverVersion, e);
                String summary = DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DRIVER_DOWNLOAD_FAILED_MESSAGE.name());
                publishProgress(uid, clusterId, driverFamily, driverVersion, 0, 0, 0, "FAILED", false, null, summary, buildDriverDownloadErrorDetail(e));
            } finally {
                this.runningTasks.remove(taskKey);
            }
        });
    }

    private boolean isPrepared(DriverVersion driverVersion) {
        if (driverVersion == null) {
            return false;
        }
        List<ResDef> resources = driverVersion.getResources();
        if (CollectionUtils.isEmpty(resources)) {
            return true;
        }
        for (ResDef resource : resources) {
            if (resource == null || !resource.isPrepared()) {
                return false;
            }
        }
        return true;
    }

    private boolean isPrepared(DsDriverVer driverVersion) {
        if (driverVersion == null) {
            return false;
        }
        List<DsDriverRes> resources = driverVersion.getResources();
        if (CollectionUtils.isEmpty(resources)) {
            return true;
        }
        for (DsDriverRes resource : resources) {
            if (resource == null || !resource.isPrepared()) {
                return false;
            }
        }
        return true;
    }

    private static void publishDownloadStarted(String uid, Long clusterId, String driverFamily, String driverVersion) {
        publishProgress(uid, clusterId, driverFamily, driverVersion, 0, 0, 0, "PREPARING", false, null, DmI18nUtils
            .getMessage(I18nDmMsgKeys.DS_DRIVER_PREPARE_STARTED_MESSAGE.name()));
    }

    public static void publishProgress(String uid, Long clusterId, String driverFamily, String driverVersion, int totalFileCount, int completedFileCount, int currentFilePercent,
                                       String status, boolean available, String currentFileName, String message) {
        publishProgress(uid, clusterId, driverFamily, driverVersion, totalFileCount, completedFileCount, currentFilePercent, status, available, currentFileName, message, null);
    }

    public static void publishProgress(String uid, Long clusterId, String driverFamily, String driverVersion, int totalFileCount, int completedFileCount, int currentFilePercent,
                                       String status, boolean available, String currentFileName, String message, String detailMessage) {
        DriverDownloadProgressVO progressVO = new DriverDownloadProgressVO();
        progressVO.setClusterId(clusterId);
        progressVO.setDriverFamily(driverFamily);
        progressVO.setDriverVersion(driverVersion);
        progressVO.setTotalFileCount(totalFileCount);
        progressVO.setCompletedFileCount(completedFileCount);
        progressVO.setCurrentFilePercent(currentFilePercent);
        progressVO.setStatus(status);
        progressVO.setAvailable(available);
        progressVO.setMessage(message);
        progressVO.setDetailMessage(detailMessage);
        progressVO.setCurrentFileName(currentFileName);
        logProgress(uid, progressVO);
        DmGlobalEventBus.triggerDriverDownloadEvent(new DriverDownloadEvent(uid, progressVO));
    }

    private static void logProgress(String uid, DriverDownloadProgressVO progressVO) {
        String taskKey = buildTaskKey(uid, progressVO.getClusterId(), progressVO.getDriverFamily(), progressVO.getDriverVersion());
        String signature = buildProgressSignature(progressVO);
        String previous = progressLogState.put(taskKey, signature);
        if (!signature.equals(previous)) {
            log.info("driver download progress, uid={}, clusterId={}, family={}, version={}, status={}, completed={}/{}, percent={}%, file={}, message={}",//
                    uid, progressVO.getClusterId(), progressVO.getDriverFamily(), progressVO.getDriverVersion(), progressVO.getStatus(), //
                    progressVO.getCompletedFileCount(), progressVO.getTotalFileCount(), progressVO.getCurrentFilePercent(), progressVO.getCurrentFileName(), progressVO
                        .getMessage());
        }

        if (isTerminalStatus(progressVO.getStatus())) {
            progressLogState.remove(taskKey);
        }
    }

    private static String buildTaskKey(String uid, Long clusterId, String driverFamily, String driverVersion) {
        String clusterKey = clusterId == null ? "ALL" : String.valueOf(clusterId);
        return uid + "::" + clusterKey + "::" + driverFamily + "::" + driverVersion;
    }

    private static String buildProgressSignature(DriverDownloadProgressVO progressVO) {
        int roundedPercent = roundPercent(progressVO.getCurrentFilePercent());
        String currentFileName = progressVO.getCurrentFileName() == null ? "" : progressVO.getCurrentFileName();
        String message = progressVO.getMessage() == null ? "" : progressVO.getMessage();
        String detailMessage = progressVO.getDetailMessage() == null ? "" : progressVO.getDetailMessage();
        return progressVO.getStatus() + "::" + progressVO.getCompletedFileCount() + '/' + progressVO.getTotalFileCount() + "::" + roundedPercent + "::" + currentFileName + "::"
               + message + "::" + detailMessage;
    }

    private static int roundPercent(int currentFilePercent) {
        if (currentFilePercent <= 0 || currentFilePercent >= 100) {
            return currentFilePercent;
        }
        return (currentFilePercent / PROGRESS_LOG_STEP) * PROGRESS_LOG_STEP;
    }

    private static boolean isTerminalStatus(String status) {
        return "COMPLETED".equals(status) || "FAILED".equals(status);
    }

    static String buildDriverDownloadErrorDetail(Throwable e) {
        if (e == null) {
            return "Driver download failed.";
        }

        Throwable[] eList = ExceptionUtils.getThrowables(e);
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        rootCause = rootCause == null ? e : rootCause;

        StringBuilder message = new StringBuilder("Driver download failed.");
        String rootMessage = StringUtils.trimToNull(ExceptionUtils.getMessage(rootCause));
        if (rootMessage != null) {
            message.append(" Root cause: ").append(rootMessage).append('.');
        }

        Throwable transferContext = findMavenTransferContext(eList, rootCause);
        String transferMessage = transferContext == null ? null : StringUtils.trimToNull(ExceptionUtils.getMessage(transferContext));
        if (transferMessage != null && message.indexOf(transferMessage) < 0) {
            message.append(" Maven transfer: ").append(transferMessage).append('.');
        }
        return message.toString();
    }

    private static Throwable findMavenTransferContext(Throwable[] eList, Throwable rootCause) {
        if (eList == null) {
            return null;
        }

        for (int i = eList.length - 1; i >= 0; i--) {
            Throwable candidate = eList[i];
            if (candidate == null || candidate == rootCause) {
                continue;
            }
            String text = StringUtils.defaultString(candidate.getMessage()).toLowerCase(Locale.ROOT);
            if (text.contains("could not transfer artifact") || text.contains("could not be resolved") || text.contains("artifact descriptor")) {
                return candidate;
            }
        }
        return null;
    }

    private List<DmSysWorkerDO> queryTargetWorkers(Long clusterId) {
        if (clusterId != null && clusterId > 0) {
            return this.systemDal.workerMapper().queryConnectedByClusterId(clusterId);
        }
        throw new IllegalArgumentException("clusterId is required to query target workers.");
    }

    private RSocketSendDTO buildSendDTO(DmSysWorkerDO worker) {
        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setUid(worker.getUid());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }
}
