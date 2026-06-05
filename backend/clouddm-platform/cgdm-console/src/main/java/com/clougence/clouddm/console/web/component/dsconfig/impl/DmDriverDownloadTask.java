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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.api.sidecar.session.drivers.DriversRService;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverRes;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverVer;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.DriverFile;
import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.FileDef;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmDriverDownloadTask implements Runnable {

    private static final int      CHUNK_SIZE = 256 * 1024;

    private final String          uid;
    private final Long            clusterId;
    private final String          driverFamily;
    private final String          driverVersion;
    private final SystemDal       systemDal;
    private final DriversRService driversRService;

    public DmDriverDownloadTask(String uid, Long clusterId, String driverFamily, String driverVersion, SystemDal systemDal, DriversRService driversRService){
        this.uid = uid;
        this.clusterId = clusterId;
        this.driverFamily = driverFamily;
        this.driverVersion = driverVersion;
        this.systemDal = systemDal;
        this.driversRService = driversRService;
    }

    @Override
    public void run() {
        DriverVersion localVersion = PluginManager.driverLoader().findDriver(this.driverFamily, this.driverVersion);
        if (localVersion == null) {
            throw new IllegalArgumentException("driver not found: " + this.driverFamily + " / " + this.driverVersion);
        }

        log.info("start driver download, clusterId={}, family={}, version={}", this.clusterId, this.driverFamily, this.driverVersion);
        resetLocalPreparedResources(localVersion);
        ensurePreparedResources(localVersion);
        refreshPreparedState(localVersion);
        List<DriverFile> transferFiles = resolveTransferFiles(localVersion);
        int totalFileCount = transferFiles.size();
        DmDriverServiceImpl
            .publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, 0, 0, "SYNCING", false, null, i18n(I18nDmMsgKeys.DS_DRIVER_SYNC_STARTED_MESSAGE));
        syncFilesToWorkers(transferFiles, totalFileCount);

        DriverVersionStatusVO statusVO = checkDriverStatus();
        DmDriverServiceImpl.publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, totalFileCount, 100, "COMPLETED", statusVO
            .isAvailable(), null, statusVO.isAvailable() ? i18n(I18nDmMsgKeys.DS_DRIVER_READY_MESSAGE) : i18n(I18nDmMsgKeys.DS_DRIVER_UNAVAILABLE_MESSAGE));
        log.info("driver download finished, clusterId={}, family={}, version={}, available={}, workerWsn={}", this.clusterId, this.driverFamily, this.driverVersion, statusVO
            .isAvailable(), statusVO.getWorkerWsn());
    }

    private void resetLocalPreparedResources(DriverVersion localVersion) {
        if (localVersion == null) {
            return;
        }

        localVersion.deleteFiles();
        localVersion.setPrepared(false);
        if (CollectionUtils.isEmpty(localVersion.getResources())) {
            return;
        }

        for (ResDef resource : localVersion.getResources()) {
            if (resource == null) {
                continue;
            }

            resource.setPrepared(false);
            resource.setFileDefList(null);
        }
    }

    private void ensurePreparedResources(DriverVersion localVersion) {
        List<ResDef> resources = localVersion.getResources();
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }

        DmDriverServiceImpl
            .publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, resolveDriverFileCount(resources), 0, 0, "PREPARING", false, null, i18n(I18nDmMsgKeys.DS_DRIVER_PREPARE_STARTED_MESSAGE));
        Set<String> completedFiles = ConcurrentHashMap.newKeySet();
        for (ResDef resource : resources) {
            if (resource == null || StringUtils.isBlank(resource.getCoordinate())) {
                continue;
            }

            log.info("prepare driver resource, clusterId={}, family={}, version={}, resource={}", this.clusterId, this.driverFamily, this.driverVersion, resource.getCoordinate());

            if (!resource.isPrepared()) {
                PluginManager.driverLoader().prepareDriverVersion(localVersion, current -> current != resource, new DriverPrepareProgress() {

                    @Override
                    public void onStart(DriverVersion driverVersionValue, ResDef driverResource, int resourceIndex, int totalCount) {
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, resolveDriverFileCount(resources), completedFiles
                                .size(), 0, "PREPARING", false, null, i18n(I18nDmMsgKeys.DS_DRIVER_PREPARE_STARTED_MESSAGE));
                    }

                    @Override
                    public void onProgress(DriverVersion driverVersionValue, ResDef driverResource, String fileName, long current, long total) {
                        markCompletedFile(completedFiles, driverResource, fileName, current, total);
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, resolveDriverFileCount(resources), completedFiles
                                .size(), calcPercent(current, total), "PREPARING", false, fileName, buildDownloadMessage(fileName, current, total));
                    }

                    @Override
                    public void onComplete(DriverVersion driverVersionValue, ResDef resDef, int resourceIndex, int totalCount) {
                        markCompletedResourceFiles(completedFiles, resDef);
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, resolveDriverFileCount(resources), completedFiles
                                .size(), 100, "PREPARING", false, null, i18n(I18nDmMsgKeys.DS_DRIVER_FILE_DOWNLOAD_COMPLETE_MESSAGE));
                    }

                    @Override
                    public void onError(DriverVersion driverVersionValue, ResDef resourceValue, Exception exception) {
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, resolveDriverFileCount(resources), completedFiles
                                .size(), 0, "FAILED", false, null, i18n(I18nDmMsgKeys.DS_DRIVER_PREPARE_FAILED_MESSAGE), DmDriverServiceImpl
                                    .buildDriverDownloadErrorDetail(exception));
                    }
                });
            }

            markCompletedResourceFiles(completedFiles, resource);
            DmDriverServiceImpl.publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, resolveDriverFileCount(resources), completedFiles
                .size(), 100, "PREPARING", false, null, i18n(I18nDmMsgKeys.DS_DRIVER_FILE_DOWNLOAD_COMPLETE_MESSAGE));
        }
    }

    private DriverVersionStatusVO checkDriverStatus() {
        DriverVersionStatusVO statusVO = new DriverVersionStatusVO();
        statusVO.setDriverFamily(this.driverFamily);
        statusVO.setDriverVersion(this.driverVersion);
        statusVO.setWorkerWsn(new ArrayList<>());

        DriverVersion localVersion = PluginManager.driverLoader().findDriver(this.driverFamily, this.driverVersion);
        refreshPreparedState(localVersion);
        boolean consoleAvailable = isPrepared(localVersion);

        List<DmSysWorkerDO> workers = queryTargetWorkers();
        if (CollectionUtils.isEmpty(workers)) {
            statusVO.setAvailable(consoleAvailable);
            return statusVO;
        }

        boolean workersAvailable = true;
        for (DmSysWorkerDO worker : workers) {
            DsDriverVer remoteVersion;
            try {
                remoteVersion = this.driversRService.refreshDriverVersion(buildSendDTO(worker), this.driverFamily, this.driverVersion);
            } catch (Exception e) {
                log.warn("check driver status on worker failed, family={}, version={}, workerIp={}, workerSeqNumber={}", this.driverFamily, this.driverVersion, worker
                    .getWorkerIp(), worker.getWorkerSeqNumber(), e);
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

    //

    private void syncFilesToWorkers(List<DriverFile> transferFiles, int totalFileCount) {
        List<DmSysWorkerDO> workers = queryTargetWorkers();
        if (CollectionUtils.isEmpty(workers)) {
            return;
        }

        List<DmSysWorkerDO> workersNeedTransfer = resolveWorkersNeedTransfer(workers);
        if (CollectionUtils.isEmpty(workersNeedTransfer)) {
            refreshWorkers(workers);
            return;
        }

        if (CollectionUtils.isEmpty(transferFiles)) {
            refreshWorkers(workersNeedTransfer);
            return;
        }

        for (DmSysWorkerDO worker : workersNeedTransfer) {
            try {
                log.info("clear worker driver resource, clusterId={}, family={}, version={}, workerWsn={}", this.clusterId, this.driverFamily, this.driverVersion, worker
                    .getWorkerSeqNumber());
                this.driversRService.deleteDriverResource(buildSendDTO(worker), this.driverFamily, this.driverVersion);
            } catch (Exception e) {
                log.warn("clear worker driver resource failed, family={}, version={}, workerIp={}, workerSeqNumber={}", this.driverFamily, this.driverVersion, worker
                    .getWorkerIp(), worker.getWorkerSeqNumber(), e);
            }
        }

        for (int index = 0; index < transferFiles.size(); index++) {
            syncFileToWorkers(workersNeedTransfer, transferFiles.get(index), totalFileCount, index + 1);
        }

        refreshWorkers(workers);
    }

    private List<DmSysWorkerDO> resolveWorkersNeedTransfer(List<DmSysWorkerDO> workers) {
        List<DmSysWorkerDO> workersNeedTransfer = new ArrayList<>();
        for (DmSysWorkerDO worker : workers) {
            try {
                log.info("refresh worker driver before transfer, clusterId={}, family={}, version={}, workerWsn={}", this.clusterId, this.driverFamily, this.driverVersion, worker
                    .getWorkerSeqNumber());
                DsDriverVer remoteVersion = this.driversRService.refreshDriverVersion(buildSendDTO(worker), this.driverFamily, this.driverVersion);
                if (isPrepared(remoteVersion)) {
                    log.info("driver already prepared on worker after refresh, skip transfer, clusterId={}, family={}, version={}, workerWsn={}", this.clusterId, this.driverFamily, this.driverVersion, worker
                        .getWorkerSeqNumber());
                    continue;
                }
            } catch (Exception e) {
                log.warn("refresh worker driver before transfer failed, continue transfer, family={}, version={}, workerIp={}, workerSeqNumber={}", this.driverFamily, this.driverVersion, worker
                    .getWorkerIp(), worker.getWorkerSeqNumber(), e);
            }

            workersNeedTransfer.add(worker);
        }
        return workersNeedTransfer;
    }

    private void refreshWorkers(List<DmSysWorkerDO> workers) {
        for (DmSysWorkerDO worker : workers) {
            try {
                log.info("refresh worker driver status, clusterId={}, family={}, version={}, workerWsn={}", this.clusterId, this.driverFamily, this.driverVersion, worker
                    .getWorkerSeqNumber());
                this.driversRService.refreshDriverVersion(buildSendDTO(worker), this.driverFamily, this.driverVersion);
            } catch (Exception e) {
                log.warn("refresh worker driver status failed, family={}, version={}, workerIp={}, workerSeqNumber={}", this.driverFamily, this.driverVersion, worker
                    .getWorkerIp(), worker.getWorkerSeqNumber(), e);
            }
        }
    }

    private void syncFileToWorkers(List<DmSysWorkerDO> workers, DriverFile driverFile, int totalFileCount, int currentIndex) {
        File sourceFile = new File(driverFile.getAbsolutePath());
        if (!sourceFile.isFile() || !sourceFile.canRead()) {
            throw new IllegalStateException("driver resource file not found: " + sourceFile.getAbsolutePath());
        }
        String targetFileName = driverFile.getRelativePath();
        log.info("sync driver file, clusterId={}, family={}, version={}, sourceFile={}, targetFile={}", this.clusterId, this.driverFamily, this.driverVersion, sourceFile
            .getAbsolutePath(), targetFileName);

        long totalBytes = sourceFile.length() * workers.size();
        long currentBytes = 0;
        byte[] buffer = new byte[CHUNK_SIZE];
        for (DmSysWorkerDO worker : workers) {
            RSocketSendDTO sendDTO = buildSendDTO(worker);
            try {
                log.info("sync driver file to worker, clusterId={}, family={}, version={}, workerWsn={}, targetFile={}", this.clusterId, this.driverFamily, this.driverVersion, worker
                    .getWorkerSeqNumber(), targetFileName);
                try (FileInputStream inputStream = new FileInputStream(sourceFile)) {
                    long offset = 0;
                    int readLength;
                    while ((readLength = inputStream.read(buffer)) >= 0) {
                        byte[] chunk = readLength == buffer.length ? buffer.clone() : java.util.Arrays.copyOf(buffer, readLength);
                        this.driversRService.transferDriverResource(sendDTO, this.driverFamily, this.driverVersion, targetFileName, offset, chunk);

                        offset += readLength;
                        currentBytes += readLength;
                        DmDriverServiceImpl
                            .publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, currentIndex -
                                                                                                                              1, calcPercent(currentBytes, totalBytes), "SYNCING", false, targetFileName, buildSyncMessage(targetFileName, currentBytes, totalBytes));
                    }
                }
            } catch (Exception e) {
                log.warn("sync driver resource file to worker failed, family={}, version={}, workerIp={}, workerSeqNumber={}, file={}", this.driverFamily, this.driverVersion, worker
                    .getWorkerIp(), worker.getWorkerSeqNumber(), targetFileName, e);
            }
        }
        DmDriverServiceImpl
            .publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, currentIndex, 100, "SYNCING", false, targetFileName, i18n(I18nDmMsgKeys.DS_DRIVER_FILE_SYNC_COMPLETE_MESSAGE));
    }

    private List<DmSysWorkerDO> queryTargetWorkers() {
        if (this.clusterId != null && this.clusterId > 0) {
            return this.systemDal.workerMapper().queryConnectedByClusterId(this.clusterId);
        }
        throw new IllegalArgumentException("clusterId is required to query target workers.");
    }

    private void refreshPreparedState(DriverVersion localVersion) {
        if (localVersion != null) {
            PluginManager.driverLoader().refreshDriverVersion(localVersion);
        }
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

    private int calcPercent(long current, long total) {
        if (total <= 0) {
            return current > 0 ? 100 : 0;
        }
        return (int) Math.min(100, Math.max(0, (current * 100) / total));
    }

    private int resolveDriverFileCount(List<ResDef> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return 1;
        }

        int total = 0;
        for (ResDef resource : resources) {
            total += resolveDriverFileCount(resource);
        }
        return total <= 0 ? 1 : total;
    }

    private int resolveDriverFileCount(ResDef resource) {
        if (resource == null || CollectionUtils.isEmpty(resource.getFileDefList())) {
            return 1;
        }
        return resource.getFileDefList().size();
    }

    private void markCompletedFile(Set<String> completedFiles, ResDef resource, String fileName, long current, long total) {
        if (StringUtils.isBlank(fileName) || total <= 0 || current < total) {
            return;
        }
        completedFiles.add(buildCompletedFileKey(resource, fileName));
    }

    private void markCompletedResourceFiles(Set<String> completedFiles, ResDef resource) {
        if (resource == null) {
            return;
        }

        if (CollectionUtils.isEmpty(resource.getFileDefList())) {
            completedFiles.add(buildCompletedFileKey(resource, resource.getCoordinate()));
            return;
        }

        for (FileDef fileDef : resource.getFileDefList()) {
            if (fileDef == null) {
                continue;
            }
            completedFiles.add(buildCompletedFileKey(resource, fileDef.getRelativePath()));
        }
    }

    private String buildCompletedFileKey(ResDef resource, String fileName) {
        return StringUtils.defaultString(buildResourceCoordinate(resource)) + "::" + StringUtils.defaultString(fileName);
    }

    private String buildDownloadMessage(String fileName, long current, long total) {
        String displayName = StringUtils.defaultIfBlank(fileName, i18n(I18nDmMsgKeys.DS_DRIVER_FILE_DEFAULT_NAME));
        return i18n(I18nDmMsgKeys.DS_DRIVER_FILE_DOWNLOADING_MESSAGE, displayName, calcPercent(current, total));
    }

    private String buildSyncMessage(String fileName, long current, long total) {
        String displayName = StringUtils.defaultIfBlank(fileName, i18n(I18nDmMsgKeys.DS_DRIVER_FILE_DEFAULT_NAME));
        return i18n(I18nDmMsgKeys.DS_DRIVER_FILE_SYNCING_MESSAGE, displayName, calcPercent(current, total));
    }

    private String i18n(I18nDmMsgKeys key, Object... args) {
        return DmI18nUtils.getMessage(key.name(), args);
    }

    private String buildResourceCoordinate(ResDef resource) {
        return resource == null ? null : resource.getCoordinate();
    }

    private List<DriverFile> resolveTransferFiles(DriverVersion driverVersion) {
        List<DriverFile> result = new ArrayList<>();
        if (driverVersion == null || CollectionUtils.isEmpty(driverVersion.getFiles())) {
            return result;
        }

        for (DriverFile file : driverVersion.getFiles()) {
            if (file == null || !file.isPrepared() || StringUtils.isBlank(file.getAbsolutePath()) || StringUtils.isBlank(file.getRelativePath())) {
                continue;
            }
            result.add(file);
        }
        return result;
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
