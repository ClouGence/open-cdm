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
package com.clougence.clouddm.console.web.component.execute.impl;

import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.autoexec.AutoExecTaskPackageInfo;
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.analysis.AnalysisQueryOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.execute.AutoExecService;
import com.clougence.clouddm.console.web.component.execute.model.AutoExecCreateMO;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.service.security.AuditService;
import com.clougence.clouddm.console.web.util.CallUtils;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.DmTeamUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Utf8;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecServiceImpl implements AutoExecService {
    private static final int           AUTO_EXEC_TASK_INSERT_BATCH_SIZE = 100;
    private static final long          AUTO_EXEC_TASK_INSERT_MAX_BYTES  = 4L * 1024 * 1024;
    private static final int           TASK_FETCH_BATCH_SIZE            = 100;
    @Resource
    private SystemDal                  systemDal;
    @Resource
    private ExecutionDal               execDal;
    @Resource
    private DataSourceDal              dsDal;
    @Resource
    private ObjectCacheDao             cacheDao;
    @Resource
    private AutoExecRService           execRService;
    @Resource
    private DmDsConfigService          configService;
    @Resource
    private ApprovalStateService       approvalStateService;
    @Resource
    private QueryAnalysisService       analysisService;
    @Resource
    private AuditService               auditService;
    @Resource
    private LocalFileService           localFileService;
    @Resource
    private PlatformTransactionManager txManager;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void createJob(AutoExecCreateMO request, Stream<SplitScript> scripts) {
        if (StringUtils.isBlank(request.getJobBizId())) {
            throw new IllegalArgumentException("Auto execution job biz id is required.");
        }
        if (request.getErrorStrategy() == ErrorStrategy.RETRY) {
            if (request.getRetryWaitTime() == null || request.getRetryCount() == null) {
                throw new ErrorMessageException("retry wait time or retry count not should be null");
            }
            if (request.getRetryWaitTime() < 0 || request.getRetryCount() < 0) {
                throw new ErrorMessageException("retry wait time or retry count should be greater than 0");
            }
        }

        String bizId = request.getBizId();
        DmExecAutoJobDO job = new DmExecAutoJobDO();
        job.setLevels(request.getDsLevels().dbLevels());
        job.setDataSourceId(request.getDsLevels().dsDO().getId());
        job.setDependOnBizId(bizId);
        job.setBizId(request.getJobBizId());
        job.setExecType(request.getExecType());
        job.setStatus(AutoExecJobStatus.PREPARING);

        RsExecAutoJobConfigObj jobConfig = new RsExecAutoJobConfigObj();
        jobConfig.setEnableTransactional(request.isTransactional());
        jobConfig.setRetryWaitTime(request.getRetryWaitTime());
        jobConfig.setErrorStrategy(request.getErrorStrategy());
        jobConfig.setRetryCount(request.getRetryCount());
        job.setConfig(jobConfig);
        if (job.getExecType() == AutoExecType.IMMEDIATE) {
            job.setScheduleTime(new Date());
        } else {
            job.setScheduleTime(new Date(request.getExecTime()));
        }

        this.execDal.autoJobMapper().insert(job);

        try {
            int order = 1;
            long taskBatchBytes = 0;
            List<DmExecAutoTaskDO> taskBatch = new ArrayList<>(AUTO_EXEC_TASK_INSERT_BATCH_SIZE);
            Iterator<SplitScript> iterator = scripts.iterator();
            while (iterator.hasNext()) {
                SplitScript script = iterator.next();
                long scriptBytes = Utf8.encodedLength(script.getScript());
                if (!taskBatch.isEmpty() && taskBatchBytes + scriptBytes > AUTO_EXEC_TASK_INSERT_MAX_BYTES) {
                    if (this.execDal.autoTaskMapper().batchInsert(taskBatch) != taskBatch.size()) {
                        throw new IllegalStateException("Batch insert auto execution tasks failed.");
                    }
                    taskBatch.clear();
                    taskBatchBytes = 0;
                }

                DmExecAutoTaskDO execTask = new DmExecAutoTaskDO();
                execTask.setExecSql(script.getScript());
                execTask.setExecOrder(order++);
                execTask.setStatus(AutoExecTaskStatus.WAIT_EXEC);
                execTask.setAutoExecJobId(job.getId());
                execTask.setBizId(DmTeamUtils.nextExecTaskBizId());
                execTask.setQueryId(UUID.randomUUID().toString());
                taskBatch.add(execTask);
                taskBatchBytes += scriptBytes;
                if (taskBatch.size() == AUTO_EXEC_TASK_INSERT_BATCH_SIZE) {
                    if (this.execDal.autoTaskMapper().batchInsert(taskBatch) != taskBatch.size()) {
                        throw new IllegalStateException("Batch insert auto execution tasks failed.");
                    }
                    taskBatch.clear();
                    taskBatchBytes = 0;
                }
            }

            if (order == 1) {
                throw new IllegalStateException("Auto execution job must contain at least one SQL statement.");
            }

            if (!taskBatch.isEmpty()) {
                if (this.execDal.autoTaskMapper().batchInsert(taskBatch) != taskBatch.size()) {
                    throw new IllegalStateException("Batch insert auto execution tasks failed.");
                }
            }
        } catch (RuntimeException e) {
            try {
                TransactionTemplate cleanup = new TransactionTemplate(this.txManager);
                cleanup.executeWithoutResult(status -> this.doDeleteJob(job.getId()));
            } catch (RuntimeException cleanupError) {
                e.addSuppressed(cleanupError);
                log.error("Cleanup partially created auto execution job failed, jobId={}", job.getId(), cleanupError);
            }
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    @Override
    public void startJob(String jobBizId, String operatorUid) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByBizId(jobBizId);
        this.startPreparedJob(job, operatorUid, "jobBizId: " + jobBizId);
    }

    private void startPreparedJob(DmExecAutoJobDO job, String operatorUid, String jobIdentity) {
        if (StringUtils.isBlank(operatorUid)) {
            throw new IllegalArgumentException("Auto execution operator uid is required.");
        }
        if (job == null || this.execDal.autoJobMapper().startPreparedJob(job.getId(), operatorUid) != 1) {
            throw new IllegalStateException("Auto execution job is not ready to start, " + jobIdentity);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteJob(String jobBizId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByBizId(jobBizId);
        if (job != null) {
            this.doDeleteJob(job.getId());
        }
    }

    private void doDeleteJob(long jobId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByIdForUpdate(jobId);
        if (job == null) {
            return;
        }
        this.execDal.autoTaskMapper().deleteByJobId(jobId);
        this.execDal.autoJobMapper().deleteById(jobId);
    }

    @Override
    public void dispatchJob(Long jobId) {
        if (this.execDal.autoJobMapper().claimJobForPackaging(jobId) != 1) {
            return;
        }
        AutoExecTaskPackageInfo taskPackage;
        try {
            taskPackage = this.create(jobId);
        } catch (RuntimeException e) {
            DmExecAutoJobDO failedJob = this.execDal.autoJobMapper().queryById(jobId);
            if (failedJob != null && this.execDal.autoJobMapper().failPackaging(jobId) == 1) {
                this.approvalStateService.failExecution(failedJob.getDependOnBizId(), null);
            }
            throw e;
        }

        TransactionTemplate tx = new TransactionTemplate(this.txManager);
        Map.Entry<RSocketSendDTO, AutoExecJobDTO> dispatch = tx.execute(status -> {
            DmExecAutoJobDO jobDO = execDal.autoJobMapper().queryByIdForUpdate(jobId);
            if (jobDO == null || jobDO.getStatus() != AutoExecJobStatus.PACKAGING) {
                log.info("{} was dispatched by another console", jobId);
                return null;
            }

            DsCacheEntry dsCacheEntry = cacheDao.queryByDsId(jobDO.getDataSourceId());
            if (dsCacheEntry.getClusterId() == null) {
                execDal.autoJobMapper().updateJobStatus(jobDO.getId(), AutoExecJobStatus.FAILED);
                return null;
            }

            RSocketSendDTO sendDTO = buildRSocketSendDTO(dsCacheEntry.getClusterId());
            AutoExecJobDTO autoExecJob = prepareJobData(jobDO, taskPackage);

            jobDO.setStatus(AutoExecJobStatus.WAIT_EXEC);
            jobDO.setLastReportTime(new Date());
            jobDO.setWorkerSeqNumber(sendDTO.getWorkerSeqNumber());
            execDal.autoJobMapper().updateById(jobDO);
            return new AbstractMap.SimpleImmutableEntry<>(sendDTO, autoExecJob);
        });
        if (dispatch == null) {
            return;
        }

        DmExecAutoJobDO dispatchedJob = this.execDal.autoJobMapper().queryById(jobId);
        this.approvalStateService.markExecutionDispatched(dispatchedJob.getDependOnBizId());
        try {
            this.execRService.dispatchJob(dispatch.getKey(), dispatch.getValue());
        } catch (Throwable e) {
            log.error("dispatch auto exec job failed, jobId: " + jobId, e);
        }
    }

    @Override
    public AutoExecTaskPackageInfo create(long jobId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryById(jobId);
        if (job.getStatus() != AutoExecJobStatus.PACKAGING) {
            throw new IllegalStateException("Auto execution job is not ready for packaging, jobId: " + jobId);
        }

        DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(job.getDataSourceId());
        DataSourceConfig dsConfig = this.configService.fetchDsConfigFromExists(dsDO.getId());
        List<String> levels = new ArrayList<>();
        levels.add(dsDO.getDsEnvId().toString());
        levels.add(dsDO.getId().toString());
        levels.addAll(job.getLevels());

        AnalysisQueryOptions options = AnalysisQueryOptions.builder()
            .currentUid(job.getUid())
            .dataSourceId(dsDO.getId())
            .levels(this.configService.parseLevels(levels).levelsParam())
            .skip(QueryAnalysisFeature.REWRITE)
            .build();

        QueryRequest template = DmDsUtils.createRequestCtx(dsConfig);
        template.setRequester(Requester.TICKET);
        DmDsUtils.fillRequestConfig(Collections.singletonList(template), dsDO.getId());
        Long requestDsId = template.getDsId();
        QueryResultConf requestResultConf = template.getResultConf();

        String packageFileName = jobId + ".tasks.zip";
        Path writingFile = Path.of(GlobalConfUtils.getTempDataHome(), "exec", packageFileName + ".tmp");
        try {
            Files.createDirectories(writingFile.getParent());
            Files.deleteIfExists(writingFile);
            Files.createFile(writingFile);

            int maxExecOrder = this.execDal.autoTaskMapper().queryNeedExecTaskMaxOrder(jobId);
            int totalTaskCount = this.execDal.autoTaskMapper().queryNeedExecTaskCount(jobId);
            int processedTaskCount = 0;
            this.approvalStateService.reportExecutionPreparationProgress(job.getDependOnBizId(), 0, totalTaskCount);
            int fileNameWidth = Math.max(1, String.valueOf(maxExecOrder).length());
            MessageDigest digest = MessageDigest.getInstance("MD5");

            try (DigestOutputStream digestOutput = new DigestOutputStream(Files.newOutputStream(writingFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE),
                digest); ZipOutputStream zipOutput = new ZipOutputStream(digestOutput, StandardCharsets.UTF_8)) {
                zipOutput.setLevel(Deflater.BEST_SPEED);

                int afterExecOrder = 0;
                while (true) {
                    List<Long> taskIds = this.execDal.autoTaskMapper().queryNeedExecTaskIdsBatch(jobId, afterExecOrder, TASK_FETCH_BATCH_SIZE);
                    if (taskIds.isEmpty()) {
                        break;
                    }

                    List<DmExecAutoTaskDO> tasks = this.execDal.autoTaskMapper().queryNeedExecTasksByIds(jobId, taskIds);
                    if (tasks.size() != taskIds.size()) {
                        throw new IllegalStateException("Auto execution tasks changed while creating package.");
                    }

                    ZipEntry entry = new ZipEntry(String.format(Locale.ROOT, "%0" + fileNameWidth + "d", tasks.get(0).getExecOrder()));
                    entry.setTime(0L);
                    zipOutput.putNextEntry(entry);
                    try (JsonGenerator jsonOutput = JsonUtils.defaultObjectMapper().getFactory().createGenerator(zipOutput)) {
                        jsonOutput.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                        jsonOutput.setRootValueSeparator(null);
                        for (DmExecAutoTaskDO task : tasks) {
                            try (StringReader reader = new StringReader(task.getExecSql());
                                    Stream<QueryRequest> analyzed = this.analysisService.analysisRequestsStream(dsConfig, reader, Collections.emptyList(), 1, 0, options)) {
                                Iterator<QueryRequest> iterator = analyzed.iterator();
                                if (!iterator.hasNext()) {
                                    throw new IllegalStateException("Auto execution task must contain exactly one SQL statement.");
                                }

                                QueryRequest source = iterator.next();
                                if (iterator.hasNext()) {
                                    throw new IllegalStateException("Auto execution task must contain exactly one SQL statement.");
                                }

                                QueryRequest request = DmDsUtils.createRequestCtx(dsConfig);
                                request.setQueryId(task.getQueryId());
                                request.setQueryBody(source.getQueryBody());
                                request.setQueryArgs(source.getQueryArgs());
                                request.setQueryTypes(source.getQueryTypes());
                                request.setDsId(requestDsId);
                                request.setDsType(source.getDsType());
                                request.setRelations(source.getRelations());
                                request.setColumnList(source.getColumnList());
                                request.setUsingValueProcess(source.isUsingValueProcess());
                                request.setRequester(Requester.TICKET);
                                request.setRequestTime(Timestamp.valueOf(task.getGmtCreate()));
                                request.setResultConf(requestResultConf.clone());
                                this.auditService.prepareAudit(dsDO.getId(), job.getUid(), request);
                                JsonUtils.defaultObjectMapper().writeValue(jsonOutput, request);
                                jsonOutput.writeRaw('\n');
                            }
                            processedTaskCount++;
                            if (processedTaskCount == totalTaskCount || processedTaskCount % 10 == 0) {
                                this.approvalStateService.reportExecutionPreparationProgress(job.getDependOnBizId(), processedTaskCount, totalTaskCount);
                            }
                        }
                    }
                    zipOutput.closeEntry();
                    afterExecOrder = tasks.get(tasks.size() - 1).getExecOrder();
                    if (this.execDal.autoJobMapper().heartbeatPackaging(jobId) != 1) {
                        throw new IllegalStateException("Auto execution job stopped while creating task package.");
                    }
                }
            }

            String md5 = HexFormat.of().formatHex(digest.digest());
            long fileSize = Files.size(writingFile);
            long attachmentId = this.localFileService.addAsEditing(job.getUid(), writingFile, packageFileName, SysAttachmentType.SQL_FILE_TASK);
            AutoExecTaskPackageInfo info = new AutoExecTaskPackageInfo();
            info.setAttachmentId(attachmentId);
            info.setFileSize(fileSize);
            info.setMd5(md5);
            return info;
        } catch (Exception e) {
            try {
                Files.deleteIfExists(writingFile);
            } catch (Exception deleteError) {
                log.warn("delete incomplete auto execution task package failed: {}", writingFile, deleteError);
            }
            throw new IllegalStateException("Create auto execution task package failed, jobId: " + jobId, e);
        }
    }

    @Override
    public byte[] read(long jobId, long attachmentId, long offset, int length) {
        if (offset < 0 || length <= 0) {
            throw new IllegalArgumentException("Invalid auto execution task package read range.");
        }
        DmExecAutoJobDO job = this.requireJob(jobId);
        return this.localFileService.consumeEditing(job.getUid(), attachmentId, packageFile -> {
            try (FileChannel channel = FileChannel.open(packageFile, StandardOpenOption.READ)) {
                long fileSize = channel.size();
                if (offset >= fileSize) {
                    this.localFileService.renewEditing(job.getUid(), attachmentId);
                    return new byte[0];
                }

                int readLength = (int) Math.min(length, fileSize - offset);
                ByteBuffer buffer = ByteBuffer.allocate(readLength);
                channel.position(offset);
                while (buffer.hasRemaining() && channel.read(buffer) >= 0) {
                    // Continue until the requested block is full or EOF is reached.
                }

                byte[] result;
                if (buffer.position() == readLength) {
                    result = buffer.array();
                } else {
                    result = new byte[buffer.position()];
                    buffer.flip();
                    buffer.get(result);
                }
                this.localFileService.renewEditing(job.getUid(), attachmentId);
                return result;
            }
        });
    }

    @Override
    public void delete(long attachmentId) {
        this.localFileService.deleteRecord(attachmentId);
    }

    private DmExecAutoJobDO requireJob(long jobId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryById(jobId);
        if (job == null) {
            throw new IllegalStateException("Auto execution job does not exist, jobId: " + jobId);
        }
        return job;
    }

    private AutoExecJobDTO prepareJobData(DmExecAutoJobDO job, AutoExecTaskPackageInfo taskPackage) {
        AutoExecJobDTO job4Auto = new AutoExecJobDTO();
        job4Auto.setErrorStrategy(job.getConfig().getErrorStrategy());
        job4Auto.setRetryCount(job.getConfig().getRetryCount());
        job4Auto.setRetryWaitTime(job.getConfig().getRetryWaitTime());
        job4Auto.setEnableTransactional(job.getConfig().isEnableTransactional());
        DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(job.getDataSourceId());
        DataSourceConfig dsConfig = this.configService.fetchDsConfigFromExists(dsDO.getId());

        List<String> levels = new ArrayList<>();
        levels.add(dsDO.getDsEnvId().toString());
        levels.add(dsDO.getId().toString());
        levels.addAll(job.getLevels());
        Map<UmiTypes, Object> levelsParam = this.configService.parseLevels(levels).levelsParam();

        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());
        SessionContextDTO contextDTO = sessionSpi.createSessionContext(dsConfig, params);

        job4Auto.setContextDTO(contextDTO);
        job4Auto.setDsId(dsDO.getId());
        job4Auto.setJobId(job.getId());
        job4Auto.setTaskPackage(taskPackage);
        return job4Auto;
    }

    @Override
    public void continueTask(String bizId, long taskId) {
        DmExecAutoJobDO job = requireJob(bizId);
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }

        DmExecAutoTaskDO execTaskDO = execDal.autoTaskMapper().selectById(taskId);
        if (execTaskDO == null || !execTaskDO.getAutoExecJobId().equals(job.getId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_JOB_NOT_MATCH_ERROR_MESSAGE.name()));
        }
        if (execTaskDO.getStatus() != AutoExecTaskStatus.CANCELED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_NOT_SKIPPED.name()));
        }

        execDal.autoTaskMapper().updateStatusByTaskId(execTaskDO.getId(), AutoExecTaskStatus.WAIT_EXEC);
    }

    @Override
    public boolean skipTask(String bizId, long taskId) {
        DmExecAutoJobDO job = requireJob(bizId);
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        DmExecAutoTaskDO execTaskDO = execDal.autoTaskMapper().selectById(taskId);
        if (execTaskDO == null || !Objects.equals(execTaskDO.getAutoExecJobId(), job.getId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_JOB_NOT_MATCH_ERROR_MESSAGE.name()));
        }

        if (execTaskDO.getStatus() == AutoExecTaskStatus.FINISH || execTaskDO.getStatus() == AutoExecTaskStatus.WAIT_CONFIRM) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_IS_FINISH.name()));
        }

        execDal.autoTaskMapper().updateStatusByTaskId(execTaskDO.getId(), AutoExecTaskStatus.CANCELED);

        int count = this.execDal.autoTaskMapper().queryNeedExecTaskCount(job.getId());
        if (count == 0) {
            this.execDal.autoJobMapper().finishJob(job.getId());
            this.approvalStateService.completeExecution(job.getDependOnBizId());
            return true;
        }
        return false;
    }

    @Override
    public DmAutoExecJobVO queryAutoExecJob(String bizId, boolean canOperate) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job == null) {
            return null;
        }

        DmAutoExecJobVO vo = new DmAutoExecJobVO();
        vo.setExecType(job.getExecType());
        vo.setLastReportTime(DateFormatType.s_yyyyMMdd_HHmmss.format(job.getLastReportTime()));
        if (job.getStatus() == AutoExecJobStatus.PACKAGING) {
            vo.setStatus(AutoExecJobStatus.INIT);
        } else {
            vo.setStatus(job.getStatus());
        }
        vo.setExecTime(DateFormatType.s_yyyyMMdd_HHmmss.format(job.getScheduleTime()));
        vo.setQueryId(job.getQueryId());
        vo.setId(job.getId());
        vo.setEnableTransactional(job.getConfig().isEnableTransactional());

        if (job.getWorkerSeqNumber() != null && job.getStatus() != AutoExecJobStatus.INIT && job.getStatus() != AutoExecJobStatus.FINISH
            && job.getStatus() != AutoExecJobStatus.TERMINATION) {
            DmSysWorkerDO workerStatus = this.systemDal.workerMapper().getByWsn(job.getWorkerSeqNumber());
            vo.setWorkerIP(workerStatus.getWorkerIp());
            vo.setWorkerStatus(workerStatus.getConnStatus());
            vo.setWorkerSeqNumber(workerStatus.getWorkerSeqNumber());
        }

        if (!job.getNormal()) {
            vo.setNormal(false);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_ERROR_STATUS_MESSAGE.name()));
        }

        if (!canOperate) {
            return vo;
        }

        switch (job.getStatus()) {
            case INIT:
            case WAIT_EXEC:
            case EXECUTING: {
                vo.setCanPause(true);
                break;
            }
            case PAUSE: {
                vo.setCanRestart(true);
                vo.setCanEnd(true);
                break;
            }
            case FAILED: {
                vo.setCanRetry(true);
                vo.setCanEnd(true);
                break;
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void stopJob(String bizId) {
        DmExecAutoJobDO job = requireJob(bizId);
        this.stopJob(job.getId());
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void endJob(String bizId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job == null || job.getStatus() == AutoExecJobStatus.TERMINATION) {
            return;
        }
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_RETRY_JOB_ERROR_MESSAGE.name()));
        }

        job.setStatus(AutoExecJobStatus.TERMINATION);
        execDal.autoJobMapper().updateById(job);
        execDal.autoTaskMapper().cancelAllWaitTask(job.getId());
        this.approvalStateService.cancelExecution(job.getDependOnBizId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void retryJob(String bizId) {
        DmExecAutoJobDO job = requireJob(bizId);
        if (job.getStatus() != AutoExecJobStatus.FAILED && job.getStatus() != AutoExecJobStatus.PAUSE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_RETRY_JOB_ERROR_MESSAGE.name()));
        }

        job.setStatus(AutoExecJobStatus.INIT);
        int updateCount = execDal.autoJobMapper().retryJob(job.getId());
        if (updateCount <= 0) {
            return;
        }
        execDal.autoTaskMapper().retryTask(job.getId());
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskSummaryList(String bizId, boolean canOperate, AutoExecTaskStatus status, PageObj pageDO, int sqlSummaryLength) {
        Page<?> page = PageUtils.startPage(pageDO);
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job == null) {
            return new DmPageVO<>(page);
        }
        IPage<DmExecAutoTaskDO> iPage = this.execDal.autoTaskMapper().querySummaryListByJobId(page, job.getId(), status, sqlSummaryLength);
        return this.convertTaskPage(job, canOperate, iPage);
    }

    private DmPageVO<DmAutoExecTaskVO> convertTaskPage(DmExecAutoJobDO job, boolean canOperate, IPage<DmExecAutoTaskDO> iPage) {
        DmPageVO<DmAutoExecTaskVO> result = new DmPageVO<>(iPage);

        for (DmExecAutoTaskDO taskDO : iPage.getRecords()) {
            DmAutoExecTaskVO vo = new DmAutoExecTaskVO();
            vo.setTaskId(taskDO.getId());
            vo.setStatus(taskDO.getStatus());
            vo.setExecSql(taskDO.getExecSql());
            vo.setAffectLine(taskDO.getAffectRow() != null ? taskDO.getAffectRow() : 0L);
            vo.setExecCount(taskDO.getExecCount());
            vo.setExecuteOrder(taskDO.getExecOrder());
            vo.setActualStartTime(DateFormatType.s_yyyyMMdd_HHmmss.format(taskDO.getGmtLastStart()));
            vo.setActualEndTime(DateFormatType.s_yyyyMMdd_HHmmss.format(taskDO.getGmtLastEnd()));
            if (canOperate) {
                boolean jobPause = job.getStatus() == AutoExecJobStatus.PAUSE || job.getStatus() == AutoExecJobStatus.FAILED;
                boolean canSkip = jobPause && taskDO.getStatus() != AutoExecTaskStatus.FINISH && taskDO.getStatus() != AutoExecTaskStatus.CANCELED;
                boolean canCancelSkip = jobPause && taskDO.getStatus() == AutoExecTaskStatus.CANCELED;
                vo.setCanSkip(canSkip);
                vo.setCanCancelSkip(canCancelSkip);
            }
            result.getRecords().add(vo);
        }
        return result;
    }

    @Override
    public String queryAutoExecTaskSql(String bizId, long taskId) {
        DmExecAutoJobDO job = this.requireJob(bizId);
        DmExecAutoTaskDO task = this.execDal.autoTaskMapper().selectById(taskId);
        if (task == null || !job.getId().equals(task.getAutoExecJobId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        return task.getExecSql();
    }

    private DmExecAutoJobDO requireJob(String bizId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        return job;
    }

    @Transactional(rollbackFor = Throwable.class)
    private void stopJob(Long jobId) {
        DmExecAutoJobDO job = execDal.autoJobMapper().queryByIdForUpdate(jobId);

        AutoExecJobStatus status = job.getStatus();
        if (status == AutoExecJobStatus.INIT || status == AutoExecJobStatus.PACKAGING) {
            job.setStatus(AutoExecJobStatus.PAUSE);
            this.execDal.autoJobMapper().updateById(job);
            return;
        }

        if (status == AutoExecJobStatus.PAUSE || status == AutoExecJobStatus.FAILED || status == AutoExecJobStatus.FINISH) {
            log.warn("{} was already stop", jobId);
            return;
        }

        if (status == AutoExecJobStatus.PAUSING) {
            return;
        }

        this.execRService.pauseJob(CallUtils.buildSendDTO(job.getWorkerSeqNumber()), jobId);

        job.setStatus(AutoExecJobStatus.PAUSING);
        this.execDal.autoJobMapper().updateById(job);
    }

    private RSocketSendDTO buildRSocketSendDTO(long bindClusterId) {
        List<DmSysWorkerDO> workers = this.systemDal.workerMapper().queryConnectedByClusterId(bindClusterId);
        if (workers.isEmpty()) {
            throw new ErrorMessageException(DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code(), MessageUtils.getClusterHaveNoWorksErrorMessage(bindClusterId));
        }

        DmSysWorkerDO worker = workers.get(new Random(System.currentTimeMillis()).nextInt(workers.size()));

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);

        return sendDTO;
    }
}
