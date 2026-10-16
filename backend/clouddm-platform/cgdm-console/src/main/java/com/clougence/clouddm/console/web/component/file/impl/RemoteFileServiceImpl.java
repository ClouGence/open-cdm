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
package com.clougence.clouddm.console.web.component.file.impl;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.execute.ResultColDTO;
import com.clougence.clouddm.api.sidecar.session.execute.ResultFileReadDTO;
import com.clougence.clouddm.api.sidecar.session.execute.ResultPageDTO;
import com.clougence.clouddm.api.sidecar.session.execute.ResultSetRService;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.file.RemoteFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.editor.model.DataResultDataVO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.console.web.util.CallUtils;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.platform.dal.model.execution.FileStatus;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RemoteFileServiceImpl implements RemoteFileService, UnifiedPostConstruct {

    private static final int            CLEAR_BATCH_SIZE                        = 500;
    private static final int            ACTIVE_FILE_PROTECTION_MINUTES          = 2;
    private static final int            DEFAULT_RESULT_CACHE_CAPACITY_MEGABYTES = 1024;
    private static final long           BYTES_PER_MEGABYTE                      = 1024L * 1024L;

    @Resource
    private SystemDal                   systemDal;
    @Resource
    private ExecutionDal                execDal;
    @Resource
    private ResultSetRService           resultSetRService;
    private ScheduledThreadPoolExecutor scheduledExecutor;

    @Override
    public void init() throws Exception {
        ThreadFactory tf = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "FileClear-%s");
        this.scheduledExecutor = new ScheduledThreadPoolExecutor(1, tf);
        this.scheduledExecutor.scheduleWithFixedDelay(this::doClearJob, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {

    }

    private void doClearJob() {
        try {
            Integer capacityMegaBytes = this.systemDal.fetchSystemConf(RootUserConfig.Fields.onlineResultCacheCapacityMegaByte, Integer.class);
            if (capacityMegaBytes == null) {
                capacityMegaBytes = DEFAULT_RESULT_CACHE_CAPACITY_MEGABYTES;
            }
            long capacityBytes = capacityMegaBytes * BYTES_PER_MEGABYTE;

            Map<String, List<DmExecFileDO>> filesByWorker = this.loadFilesByWorker();
            Map<String, DmSysWorkerDO> connectedWorkers = new HashMap<>();
            List<DmSysWorkerDO> workers = this.systemDal.workerMapper().queryByConnStatus(WorkerConnStatus.CONNECTED);
            for (DmSysWorkerDO worker : workers) {
                connectedWorkers.put(worker.getWorkerSeqNumber(), worker);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -ACTIVE_FILE_PROTECTION_MINUTES);
            Date activeFileSafePoint = calendar.getTime();

            for (Map.Entry<String, List<DmExecFileDO>> entry : filesByWorker.entrySet()) {
                if (!connectedWorkers.containsKey(entry.getKey())) {
                    continue;
                }
                try {
                    this.clearWorkerFiles(entry.getKey(), entry.getValue(), capacityBytes, activeFileSafePoint);
                } catch (Exception e) {
                    log.error("Clear result cache failed, worker=" + entry.getKey(), e);
                }
            }
        } catch (Exception e) {
            log.error("Clear result cache failed.", e);
        }
    }

    private Map<String, List<DmExecFileDO>> loadFilesByWorker() {
        Map<String, List<DmExecFileDO>> result = new HashMap<>();
        long afterId = 0;
        while (true) {
            List<DmExecFileDO> files = this.execDal.fileMapper().queryAfterId(afterId, CLEAR_BATCH_SIZE);
            if (files.isEmpty()) {
                return result;
            }

            for (DmExecFileDO file : files) {
                afterId = file.getId();
                try {
                    URI fileUri = DmConvertUtils.createFileUri(file.getFileUri());
                    if (!StringUtils.equalsIgnoreCase(fileUri.getScheme(), "wsn")) {
                        continue;
                    }
                    result.computeIfAbsent(fileUri.getHost(), key -> new ArrayList<>()).add(file);
                } catch (Exception e) {
                    log.error("Parse result file URI failed, uniqueId=" + file.getUniqueId(), e);
                }
            }

            if (files.size() < CLEAR_BATCH_SIZE) {
                return result;
            }
        }
    }

    private void clearWorkerFiles(String wsn, List<DmExecFileDO> files, long capacityBytes, Date activeFileSafePoint) {
        Map<String, Long> fileSizes = new HashMap<>();
        long usageBytes = 0;
        boolean snapshotComplete = true;

        for (DmExecFileDO file : files) {
            try {
                long fileSize = this.fetchFileSize(wsn, DmConvertUtils.createFileUri(file.getFileUri()).getPath());
                if (fileSize < 0) {
                    if (file.getStatus() != FileStatus.Pending || file.getHeartbeat().before(activeFileSafePoint)) {
                        this.execDal.fileMapper().deleteFileByUniqueId(file.getUniqueId());
                    }
                    continue;
                }
                fileSizes.put(file.getUniqueId(), fileSize);
                usageBytes += fileSize;
            } catch (Exception e) {
                snapshotComplete = false;
                this.execDal.fileMapper().incrementTryCountByUniqueId(file.getUniqueId(), "file size check error: " + e.getMessage());
            }
        }

        for (DmExecFileDO file : files) {
            Long fileSize = fileSizes.get(file.getUniqueId());
            if (file.getStatus() == FileStatus.Delete) {
                try {
                    if (fileSize == null) {
                        DmExecFileDO current = this.execDal.fileMapper().queryFileByUniqueId(file.getUniqueId());
                        if (current == null) {
                            continue;
                        }
                    }
                    if (this.deleteFile(file)) {
                        if (fileSize != null) {
                            usageBytes -= fileSize;
                        }
                        fileSizes.remove(file.getUniqueId());
                    }
                } catch (Exception e) {
                    this.execDal.fileMapper().incrementTryCountByUniqueId(file.getUniqueId(), "file clear error: " + e.getMessage());
                }
            }
        }

        if (!snapshotComplete || usageBytes <= capacityBytes) {
            return;
        }

        List<DmExecFileDO> candidates = new ArrayList<>();
        for (DmExecFileDO file : files) {
            if (fileSizes.containsKey(file.getUniqueId()) && (file.getStatus() == FileStatus.Ready || file.getStatus() == FileStatus.Failed)
                && file.getHeartbeat().before(activeFileSafePoint)) {
                candidates.add(file);
            }
        }
        candidates.sort(Comparator.comparing(DmExecFileDO::getGmtModified).thenComparing(DmExecFileDO::getId));

        long usageBeforeBytes = usageBytes;
        int deletedCount = 0;
        for (DmExecFileDO candidate : candidates) {
            if (usageBytes <= capacityBytes) {
                break;
            }

            DmExecFileDO current = this.execDal.fileMapper().queryFileByUniqueId(candidate.getUniqueId());
            if (current == null || (current.getStatus() != FileStatus.Ready && current.getStatus() != FileStatus.Failed) || !current.getHeartbeat().before(activeFileSafePoint)
                || current.getGmtModified().after(candidate.getGmtModified())) {
                continue;
            }

            try {
                URI fileUri = DmConvertUtils.createFileUri(current.getFileUri());
                long previousSize = fileSizes.get(candidate.getUniqueId());
                long currentSize = this.fetchFileSize(wsn, fileUri.getPath());
                if (currentSize < 0) {
                    this.execDal.fileMapper().deleteFileByUniqueId(current.getUniqueId());
                    usageBytes -= previousSize;
                    continue;
                }

                usageBytes += currentSize - previousSize;
                if (usageBytes <= capacityBytes) {
                    break;
                }
                if (this.deleteFile(current)) {
                    usageBytes -= currentSize;
                    deletedCount++;
                }
            } catch (Exception e) {
                this.execDal.fileMapper().incrementTryCountByUniqueId(current.getUniqueId(), "file clear error: " + e.getMessage());
            }
        }

        log.info("Result cache clear finished, worker={}, capacityBytes={}, usageBeforeBytes={}, usageAfterBytes={}, deletedCount={}", wsn, capacityBytes, usageBeforeBytes, usageBytes, deletedCount);
    }

    private boolean deleteFile(DmExecFileDO file) {
        URI fileUri = DmConvertUtils.createFileUri(file.getFileUri());
        String wsn = fileUri.getHost();
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        this.resultSetRService.deleteFile(sendDTO, fileUri.getPath(), false);
        if (this.resultSetRService.fileSize(sendDTO, fileUri.getPath()) >= 0) {
            this.execDal.fileMapper().incrementTryCountByUniqueId(file.getUniqueId(), "physical file still exists after delete.");
            return false;
        }

        this.execDal.fileMapper().deleteFileByUniqueId(file.getUniqueId());
        log.info("Delete result file [{}] on worker [{}] success.", fileUri.getPath(), wsn);
        return true;
    }

    @Override
    public String submitFileConvert(String puid, String userId, String wsn, String srcFileId, String exportId, DmFileType dmFileType, String srcFile, String dstFile,
                                    String formatName, String option) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        return this.resultSetRService.convertFile(sendDTO, puid, userId, srcFileId, exportId, dmFileType, srcFile, dstFile, formatName, option);
    }

    @Override
    public String fetchFileExtensionByFormatName(String dstFormatName) {
        FileFormatConvert convert = PluginManager.findSpi(FileFormatConvert.class, dstFormatName);
        if (convert == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_EXPORT_DST_FORMAT_UNSUPPORT_ERROR.name(), dstFormatName));
        } else {
            return convert.extension();
        }
    }

    @Override
    public void deleteFile(String wsn, String filePath) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        this.resultSetRService.deleteFile(sendDTO, filePath, false);
    }

    @Override
    public void deleteTemp(String wsn, String filePath) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        this.resultSetRService.deleteFile(sendDTO, filePath, true);
    }

    @Override
    public long fetchFileSize(String wsn, String filePath) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        return this.resultSetRService.fileSize(sendDTO, filePath);
    }

    @Override
    public byte[] fetchFileData(String wsn, String filePath, long offset, int length) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        ResultFileReadDTO readDTO = this.resultSetRService.fileRead(sendDTO, filePath, offset, length);

        if (readDTO.isSuccess()) {
            return readDTO.getContent();
        } else {
            throw new ErrorMessageException(readDTO.getMessage());
        }
    }

    @Override
    public DataResultPageVO fetchResultPage(String wsn, String filePath, long rowOffset, int pageSize) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        ResultPageDTO readDTO = this.resultSetRService.resultPageRead(sendDTO, filePath, rowOffset, pageSize);

        if (readDTO.isSuccess()) {
            return DmConvertUtils.convertToDataResultPageVO(readDTO);
        } else {
            throw new ErrorMessageException(readDTO.getMessage());
        }
    }

    @Override
    public DataResultDataVO fetchResultCol(String wsn, String filePath, long rowNumber, long colNumber, long offset, int length) {
        RSocketSendDTO sendDTO = CallUtils.buildSendDTO(wsn);
        ResultColDTO readDTO = this.resultSetRService.resultDataRead(sendDTO, filePath, rowNumber, colNumber, offset, length);

        if (readDTO.isSuccess()) {
            DataResultDataVO vo = new DataResultDataVO();
            vo.setValue(readDTO.getValue());
            return vo;
        } else {
            throw new ErrorMessageException(readDTO.getMessage());
        }
    }
}
