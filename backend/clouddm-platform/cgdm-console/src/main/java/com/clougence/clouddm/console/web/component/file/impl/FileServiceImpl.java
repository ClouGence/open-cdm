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
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.file.FileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.editor.model.DataResultDataVO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService, UnifiedPostConstruct {
    @Resource
    private SystemDal                   systemDal;
    @Resource
    private ExecutionDal                executionDal;
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

    private RSocketSendDTO buildRSocketSendDTO(String wsn) {
        DmSysWorkerDO worker = this.systemDal.workerMapper().queryConnectedByWsn(wsn);
        if (worker != null) {
            RSocketSendDTO sendDTO = new RSocketSendDTO();
            sendDTO.setClusterId(worker.getClusterId());
            sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
            sendDTO.setWorkerIP(worker.getWorkerIp());
            sendDTO.setUid(worker.getUid());
            sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
            return sendDTO;
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
        }
    }

    private void doClearJob() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, -2); // 2min pre check.

        while (true) {
            List<DmExecFileDO> files = this.executionDal.fileMapper().queryByAfterHeartbeatTime(c.getTime(), 500);
            if (files.isEmpty()) {
                break;
            }

            Map<String, Integer> timeoutConfigCache = new HashMap<>();
            for (DmExecFileDO f : files) {
                try {
                    URI fileUri = DmConvertUtils.createFileUri(f.getFileUri());
                    String fsName = fileUri.getScheme().toLowerCase();
                    if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
                        String wsn = fileUri.getHost();
                        DmSysWorkerDO worker = this.systemDal.workerMapper().queryConnectedByWsn(wsn);
                        if (worker == null) {
                            this.executionDal.fileMapper().incrementTryCountByUniqueId(f.getUniqueId(), "worker offline.");
                            continue;
                        }
                    }

                    if (f.getTryCount() >= 10) {
                        deleteFile(f);
                        continue;
                    }

                    switch (f.getStatus()) {
                        case Pending: {
                            if (this.existsFile(f)) {
                                this.executionDal.fileMapper().updateAccessTimeByUniqueId(f.getUniqueId(), "File exists during pending check.");
                            } else {
                                this.executionDal.fileMapper().deleteFileByUniqueId(f.getUniqueId());
                            }
                            break;
                        }
                        case Delete: {
                            deleteFile(f);
                            break;
                        }
                        default: {
                            int cacheTimeoutSec = this.getTimeoutByOwnerUid(f.getOwnerUid(), timeoutConfigCache);
                            long fileLastTime = f.getGmtModified().getTime() + (cacheTimeoutSec * 1000L);
                            if (fileLastTime > System.currentTimeMillis()) {
                                this.executionDal.fileMapper().updateHeartbeatByUniqueId(f.getUniqueId());
                            } else {
                                deleteFile(f);
                            }
                        }
                    }
                } catch (Exception e) {
                    this.executionDal.fileMapper().incrementTryCountByUniqueId(f.getUniqueId(), "file clear error: " + e.getMessage());
                }
            }
        }
    }

    private int getTimeoutByOwnerUid(String ownerUid, Map<String, Integer> timeoutConfigCache) {
        if (timeoutConfigCache.containsKey(ownerUid)) {
            return timeoutConfigCache.get(ownerUid);
        }

        Integer configValue = this.systemDal.fetchSystemConf(UserDefinedConfig.Fields.onlineResultCacheTimeoutSec, Integer.class);
        if (configValue == null) {
            timeoutConfigCache.put(ownerUid, 300);
            return 300;
        } else {
            timeoutConfigCache.put(ownerUid, configValue);
            return configValue;
        }
    }

    private void deleteFile(DmExecFileDO f) {
        URI fileUri = DmConvertUtils.createFileUri(f.getFileUri());
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            RSocketSendDTO sendDTO = this.buildRSocketSendDTO(wsn);
            this.resultSetRService.deleteFile(sendDTO, fileUri.getPath(), false);
            log.info("delete file [{}] on worker [{}] success.", fileUri.getPath(), wsn);
        }
        this.executionDal.fileMapper().deleteFileByUniqueId(f.getUniqueId());
    }

    private boolean existsFile(DmExecFileDO f) {
        URI fileUri = DmConvertUtils.createFileUri(f.getFileUri());
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            RSocketSendDTO sendDTO = this.buildRSocketSendDTO(wsn);
            return this.resultSetRService.fileSize(sendDTO, fileUri.getPath()) >= 0;
        } else {
            return false;
        }
    }

    @Override
    public String submitFileConvert(String puid, String userId, String wsn, String srcFileId, String exportId, DmFileType dmFileType, String srcFile, String dstFile,
                                    String formatName, String option) {
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
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
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
        this.resultSetRService.deleteFile(sendDTO, filePath, false);
    }

    @Override
    public void deleteTemp(String wsn, String filePath) {
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
        this.resultSetRService.deleteFile(sendDTO, filePath, true);
    }

    @Override
    public long fetchFileSize(String wsn, String filePath) {
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
        return this.resultSetRService.fileSize(sendDTO, filePath);
    }

    @Override
    public byte[] fetchFileData(String wsn, String filePath, long offset, int length) {
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
        ResultFileReadDTO readDTO = this.resultSetRService.fileRead(sendDTO, filePath, offset, length);

        if (readDTO.isSuccess()) {
            return readDTO.getContent();
        } else {
            throw new ErrorMessageException(readDTO.getMessage());
        }
    }

    @Override
    public DataResultPageVO fetchResultPage(String wsn, String filePath, long rowOffset, int pageSize) {
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
        ResultPageDTO readDTO = this.resultSetRService.resultPageRead(sendDTO, filePath, rowOffset, pageSize);

        if (readDTO.isSuccess()) {
            return DmConvertUtils.convertToDataResultPageVO(readDTO);
        } else {
            throw new ErrorMessageException(readDTO.getMessage());
        }
    }

    @Override
    public DataResultDataVO fetchResultCol(String wsn, String filePath, long rowNumber, long colNumber, long offset, int length) {
        RSocketSendDTO sendDTO = buildRSocketSendDTO(wsn);
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
