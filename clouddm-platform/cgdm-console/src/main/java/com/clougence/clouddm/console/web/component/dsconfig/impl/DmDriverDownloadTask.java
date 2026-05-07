package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.clouddm.api.sidecar.session.drivers.DriversRService;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverRes;
import com.clougence.clouddm.api.sidecar.session.drivers.DsDriverVer;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.DriverFile;
import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.rdp.controller.model.vo.DriverVersionStatusVO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmDriverDownloadTask implements Runnable {

    private static final int           CHUNK_SIZE = 256 * 1024;

    private final String               uid;
    private final Long                 clusterId;
    private final String               driverFamily;
    private final String               driverVersion;
    private final DmWorkerStatusMapper dmWorkerStatusMapper;
    private final DriversRService      driversRService;

    public DmDriverDownloadTask(String uid, Long clusterId, String driverFamily, String driverVersion, DmWorkerStatusMapper dmWorkerStatusMapper, DriversRService driversRService){
        this.uid = uid;
        this.clusterId = clusterId;
        this.driverFamily = driverFamily;
        this.driverVersion = driverVersion;
        this.dmWorkerStatusMapper = dmWorkerStatusMapper;
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
        DmDriverServiceImpl.publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, 0, 0, "SYNCING", false, null, null, "sync started");
        syncFilesToWorkers(transferFiles, totalFileCount);

        DriverVersionStatusVO statusVO = checkDriverStatus();
        DmDriverServiceImpl.publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, totalFileCount, 100, "COMPLETED", statusVO
            .isAvailable(), null, null, "download finished");
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

        int totalResourceCount = resources.size();
        DmDriverServiceImpl
            .publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalResourceCount, 0, 0, "PREPARING", false, null, null, "prepare started");
        AtomicInteger completedCounter = new AtomicInteger();
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
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, totalResourceCount, completedCounter
                                .get(), 0, "PREPARING", false, buildResourceCoordinate(driverResource), null, "prepare started");
                    }

                    @Override
                    public void onProgress(DriverVersion driverVersionValue, ResDef driverResource, String fileName, long current, long total) {
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, totalResourceCount, completedCounter
                                .get(), calcPercent(current, total), "PREPARING", false, buildResourceCoordinate(driverResource), fileName, "preparing resource");
                    }

                    @Override
                    public void onComplete(DriverVersion driverVersionValue, ResDef resDef, int resourceIndex, int totalCount) {
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, totalResourceCount, completedCounter
                                .get(), 100, "PREPARING", false, buildResourceCoordinate(resDef), null, "resource prepared");
                    }

                    @Override
                    public void onError(DriverVersion driverVersionValue, ResDef resourceValue, Exception exception) {
                        DmDriverServiceImpl
                            .publishProgress(uid, DmDriverDownloadTask.this.clusterId, driverFamily, DmDriverDownloadTask.this.driverVersion, totalResourceCount, completedCounter
                                .get(), 0, "FAILED", false, buildResourceCoordinate(resourceValue), null, exception.getMessage());
                    }
                });
            }

            completedCounter.incrementAndGet();
            DmDriverServiceImpl.publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalResourceCount, completedCounter
                .get(), 100, "PREPARING", false, buildResourceCoordinate(resource), null, "resource prepared");
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

        List<DmWorkerStatusDO> workers = queryTargetWorkers();
        if (CollectionUtils.isEmpty(workers)) {
            statusVO.setAvailable(consoleAvailable);
            return statusVO;
        }

        boolean workersAvailable = true;
        for (DmWorkerStatusDO worker : workers) {
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
        List<DmWorkerStatusDO> workers = queryTargetWorkers();
        if (CollectionUtils.isEmpty(workers)) {
            return;
        }

        List<DmWorkerStatusDO> workersNeedTransfer = resolveWorkersNeedTransfer(workers);
        if (CollectionUtils.isEmpty(workersNeedTransfer)) {
            refreshWorkers(workers);
            return;
        }

        if (CollectionUtils.isEmpty(transferFiles)) {
            refreshWorkers(workersNeedTransfer);
            return;
        }

        for (DmWorkerStatusDO worker : workersNeedTransfer) {
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

    private List<DmWorkerStatusDO> resolveWorkersNeedTransfer(List<DmWorkerStatusDO> workers) {
        List<DmWorkerStatusDO> workersNeedTransfer = new ArrayList<>();
        for (DmWorkerStatusDO worker : workers) {
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

    private void refreshWorkers(List<DmWorkerStatusDO> workers) {
        for (DmWorkerStatusDO worker : workers) {
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

    private void syncFileToWorkers(List<DmWorkerStatusDO> workers, DriverFile driverFile, int totalFileCount, int currentIndex) {
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
        for (DmWorkerStatusDO worker : workers) {
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
                                                                                                                              1, calcPercent(currentBytes, totalBytes), "SYNCING", false, null, targetFileName, "syncing driver file");
                    }
                }
            } catch (Exception e) {
                log.warn("sync driver resource file to worker failed, family={}, version={}, workerIp={}, workerSeqNumber={}, file={}", this.driverFamily, this.driverVersion, worker
                    .getWorkerIp(), worker.getWorkerSeqNumber(), targetFileName, e);
            }
        }
        DmDriverServiceImpl
            .publishProgress(this.uid, this.clusterId, this.driverFamily, this.driverVersion, totalFileCount, currentIndex, 100, "SYNCING", false, null, targetFileName, "driver file synced");
    }

    private List<DmWorkerStatusDO> queryTargetWorkers() {
        if (this.clusterId != null && this.clusterId > 0) {
            return this.dmWorkerStatusMapper.queryByClusterIdAndStatus(this.clusterId, WorkerConnStatus.CONNECTED);
        }
        return this.dmWorkerStatusMapper.queryByConsoleIpAndStatus(HostUtil.getHostIp(), WorkerConnStatus.CONNECTED);
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

    private RSocketSendDTO buildSendDTO(DmWorkerStatusDO worker) {
        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setUid(worker.getUid());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }
}
