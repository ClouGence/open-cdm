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
package com.clougence.clouddm.console.web.component.autoexec.impl;

import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.autoexec.AutoExecTaskPackageInfo;
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecJobPackageService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.autoexec.model.AutoExecJobCreateRequest;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.util.CallUtils;
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
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.google.common.base.Utf8;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecServiceImpl implements AutoExecService {
    private static final int           AUTO_EXEC_TASK_INSERT_BATCH_SIZE = 100;
    private static final long          AUTO_EXEC_TASK_INSERT_MAX_BYTES  = 4L * 1024 * 1024;
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
    private AutoExecHelperService      execHelperService;
    @Resource
    private AutoExecJobPackageService  taskPackageService;
    @Resource
    private PlatformTransactionManager txManager;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void createJob(AutoExecJobCreateRequest request, Stream<SplitScript> scripts) {
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

        SQLJobBizType bizType = request.getBizType();
        String bizId = request.getBizId();
        DmExecAutoJobDO job = new DmExecAutoJobDO();
        job.setLevels(request.getDsLevels().dbLevels());
        job.setDependOnBizType(bizType);
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
                execTask.setBizId(DmTeamUtils.nextExecTaskBizId(bizType));
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

        this.execHelperService.getHelper(job.getDependOnBizType()).execStart(job.getDependOnBizType(), job.getBizId());
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
            taskPackage = this.taskPackageService.create(jobId);
        } catch (RuntimeException e) {
            DmExecAutoJobDO failedJob = this.execDal.autoJobMapper().queryById(jobId);
            if (failedJob != null && this.execDal.autoJobMapper().failPackaging(jobId) == 1) {
                this.execHelperService.getHelper(failedJob.getDependOnBizType()).execFailed(failedJob.getDependOnBizType(), failedJob.getBizId());
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

        try {
            this.execRService.dispatchJob(dispatch.getKey(), dispatch.getValue());
        } catch (Throwable e) {
            log.error("dispatch auto exec job failed, jobId: " + jobId, e);
        }
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
    public void continueTask(String bizId, SQLJobBizType type, long taskId) {
        DmExecAutoJobDO job = requireJob(bizId, type);
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
    public boolean skipTask(String bizId, SQLJobBizType type, long taskId) {
        DmExecAutoJobDO job = requireJob(bizId, type);
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
            this.execHelperService.getHelper(type).execCompleted(job.getDependOnBizType(), job.getBizId());
            return true;
        }
        return false;
    }

    @Override
    public DmAutoExecJobVO queryAutoExecJob(String bizId, SQLJobBizType type, boolean canOperate) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBiz(bizId, type);
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
    public void stopJob(String bizId, SQLJobBizType type) {
        DmExecAutoJobDO job = requireJob(bizId, type);
        this.stopJob(job.getId());
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void endJob(String bizId, SQLJobBizType type) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBiz(bizId, type);
        if (job == null || job.getStatus() == AutoExecJobStatus.TERMINATION) {
            return;
        }
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_RETRY_JOB_ERROR_MESSAGE.name()));
        }

        job.setStatus(AutoExecJobStatus.TERMINATION);
        execDal.autoJobMapper().updateById(job);
        execDal.autoTaskMapper().cancelAllWaitTask(job.getId());
        this.execHelperService.getHelper(type).execAbort(job.getDependOnBizType(), job.getBizId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void retryJob(String bizId, SQLJobBizType type) {
        DmExecAutoJobDO job = requireJob(bizId, type);
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
    public DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskList(String bizId, SQLJobBizType type, boolean canOperate, AutoExecTaskStatus status, PageObj pageDO) {
        Page<?> page = PageUtils.startPage(pageDO);
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBiz(bizId, type);
        if (job == null) {
            return new DmPageVO<>(page);
        }
        IPage<DmExecAutoTaskDO> iPage = this.execDal.autoTaskMapper().queryListByJobId(page, job.getId(), status);
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

    private DmExecAutoJobDO requireJob(String bizId, SQLJobBizType type) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryByDependOnBiz(bizId, type);
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
