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

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.console.web.util.DmTeamUtils;
import com.clougence.clouddm.platform.dal.util.PageObj;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.execution.*;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.format.DateFormatType;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutoExecServiceImpl implements AutoExecService {
    @Resource
    private SystemDal             systemDal;
    @Resource
    private MonitorDal            monitorDal;
    @Resource
    private ExecutionDal          executionDal;
    @Resource
    private AuthDal               authDal;
    @Resource
    private AutoExecManager       autoExecManager;
    @Resource
    private AutoExecHelperService execHelperService;

    @Override
    public void continueTask(String bizId, SQLJobBizType type, long taskId) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        DmExecAutoTaskDO execTaskDO = executionDal.autoTaskMapper().selectById(taskId);
        if (!execTaskDO.getAutoExecJobId().equals(job.getId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_JOB_NOT_MATCH_ERROR_MESSAGE.name()));
        }
        execTaskDO.setStatus(AutoExecTaskStatus.WAIT_EXEC);
        executionDal.autoTaskMapper().updateById(execTaskDO);
    }

    @Override
    public boolean skipTask(String bizId, SQLJobBizType type, long taskId, String uid) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        DmExecAutoTaskDO execTaskDO = executionDal.autoTaskMapper().selectById(taskId);
        if (!execTaskDO.getAutoExecJobId().equals(job.getId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_JOB_NOT_MATCH_ERROR_MESSAGE.name()));
        }

        if (execTaskDO.getStatus() == AutoExecTaskStatus.FINISH || execTaskDO.getStatus() == AutoExecTaskStatus.WAIT_CONFIRM) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_IS_FINISH.name()));
        }

        executionDal.autoTaskMapper().updateStatusByTaskId(execTaskDO.getId(), AutoExecTaskStatus.CANCELED);

        DmAuthUserDO user = this.authDal.userMapper().queryByUid(uid);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_CONSOLE_SKIP.name(), user.getUsername(), user.getUid(), execTaskDO.getExecOrder());
        DmMonBizLogDO jobDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        DmMonBizLogDO taskLog = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_TASK, execTaskDO.getBizId());
        this.monitorDal.bizLogMapper().insert(jobDO);
        this.monitorDal.bizLogMapper().insert(taskLog);

        int count = this.executionDal.autoTaskMapper().queryNeedExecTaskCount(job.getId());
        if (count == 0) {
            this.executionDal.autoJobMapper().finishJob(job.getId());
            String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FINISH_MESSAGE.name());
            DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, msg, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
            this.monitorDal.bizLogMapper().insert(logDO);

            this.execHelperService.getHelper(type).execCompleted(job.getDependOnBizType(), job.getBizId());
            return true;
        }
        return false;
    }

    @Override
    public DmAutoExecJobVO queryAutoExecJob(String bizId, SQLJobBizType type, boolean canOperate) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job == null) {
            return null;
        }

        DmAutoExecJobVO vo = new DmAutoExecJobVO();
        vo.setExecType(job.getExecType());
        vo.setLastReportTime(DateFormatType.s_yyyyMMdd_HHmmss.format(job.getLastReportTime()));
        vo.setStatus(job.getStatus());
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

    @Override
    public void stopJob(String bizId, SQLJobBizType type, String uid) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
        autoExecManager.stopJob(job.getId(), user);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void endJob(String bizId, SQLJobBizType type, String uid) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job == null) {
            return;
        }

        if (job.getStatus() == AutoExecJobStatus.TERMINATION) {
            return;
        }

        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_RETRY_JOB_ERROR_MESSAGE.name()));
        }

        job.setStatus(AutoExecJobStatus.TERMINATION);
        executionDal.autoJobMapper().updateById(job);
        executionDal.autoTaskMapper().cancelAllWaitTask(job.getId());

        DmAuthUserDO user = authDal.userMapper().queryByUid(uid);
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_TERMINATION_MESSAGE.name(), user.getUsername(), user.getUid());

        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.monitorDal.bizLogMapper().insert(logDO);

        this.execHelperService.getHelper(type).execAbort(job.getDependOnBizType(), job.getBizId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void retryJob(String bizId, SQLJobBizType type, String uid) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        if (job.getStatus() != AutoExecJobStatus.FAILED && job.getStatus() != AutoExecJobStatus.PAUSE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_RETRY_JOB_ERROR_MESSAGE.name()));
        }

        job.setStatus(AutoExecJobStatus.INIT);
        int updateCount = executionDal.autoJobMapper().retryJob(job.getId());

        if (updateCount <= 0) {
            return;
        }
        executionDal.autoTaskMapper().retryTask(job.getId());
        DmAuthUserDO user = authDal.userMapper().queryByUid(uid);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_RETRY_JOB_MESSAGE.name(), user.getUsername(), user.getUid());
        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.monitorDal.bizLogMapper().insert(logDO);
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskList(String bizId, SQLJobBizType type, boolean canOperate, AutoExecTaskStatus status, PageObj pageDO) {
        DmExecAutoJobDO job = this.executionDal.autoJobMapper().queryByDependOnBizId(bizId);
        Page<?> page = PageUtils.startPage(pageDO);
        IPage<DmExecAutoTaskDO> iPage = this.executionDal.autoTaskMapper().queryListByJobId(page, job.getId(), status);
        DmPageVO<DmAutoExecTaskVO> result = new DmPageVO<>(iPage);

        for (DmExecAutoTaskDO taskDO : iPage.getRecords()) {
            DmAutoExecTaskVO vo = new DmAutoExecTaskVO();
            vo.setTaskId(taskDO.getId());
            vo.setSqlType(taskDO.getSqlType());
            vo.setStatus(taskDO.getStatus());
            vo.setExecSql(taskDO.getExecSql());
            if (taskDO.getAffectRow() != null) {
                vo.setAffectLine(taskDO.getAffectRow());
            } else {
                vo.setAffectLine(0L);
            }
            vo.setExecCount(taskDO.getExecCount());
            vo.setExecuteOrder(taskDO.getExecOrder());
            if (canOperate) {
                boolean jobPause = job.getStatus() == AutoExecJobStatus.PAUSE || job.getStatus() == AutoExecJobStatus.FAILED;
                boolean canSkip = jobPause && taskDO.getStatus() != AutoExecTaskStatus.FINISH && taskDO.getStatus() != AutoExecTaskStatus.CANCELED;
                boolean canCanceledSkip = jobPause && taskDO.getStatus() == AutoExecTaskStatus.CANCELED;
                vo.setCanSkip(canSkip);
                vo.setCanCanceledSkip(canCanceledSkip);
            }

            result.getRecords().add(vo);
        }
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void createJob(String ownerUid, String execUser, DmAutoExecConfigFO config, DsLevels dsLevels, SQLJobBizType bizType, String bizId, List<SplitScript> scripts) {
        if (config.getErrorStrategy() == ErrorStrategy.RETRY) {
            if (config.getRetryWaitTime() == null || config.getRetryCount() == null) {
                throw new ErrorMessageException("retry wait time or retry count not should be null");
            }
            if (config.getRetryWaitTime() < 0 || config.getRetryCount() < 0) {
                throw new ErrorMessageException("retry wait time or retry count should be greater than 0");
            }
        }

        DmAuthUserDO confirmUser = this.authDal.userMapper().queryByUid(execUser);
        DmExecAutoJobDO job = new DmExecAutoJobDO();
        job.setLevels(dsLevels.dbLevels());
        job.setDependOnBizType(bizType);
        job.setDataSourceId(dsLevels.dsDO().getId());
        job.setDependOnBizId(bizId);
        job.setUid(confirmUser.getUid());
        job.setBizId(DmTeamUtils.nextExecJobBizId(bizType));
        job.setPrimaryUid(ownerUid);
        job.setExecType(config.getAutoExecType());
        job.setStatus(AutoExecJobStatus.INIT);

        RsExecAutoJobConfigObj jobConfig = new RsExecAutoJobConfigObj();
        jobConfig.setEnableTransactional(config.isEnableTransactional());
        jobConfig.setRetryWaitTime(config.getRetryWaitTime());
        jobConfig.setErrorStrategy(config.getErrorStrategy());
        jobConfig.setRetryCount(config.getRetryCount());
        job.setConfig(jobConfig);
        if (job.getExecType() == AutoExecType.IMMEDIATE) {
            job.setScheduleTime(new Date());
        } else {
            job.setScheduleTime(new Date(config.getExecTime()));
        }

        this.executionDal.autoJobMapper().insert(job);

        int order = 1;
        for (int i = 0; i < scripts.size(); i++) {
            SplitScript splitScript = scripts.get(i);
            if (splitScript.getType() == SecQueryType.SWITCH_CATALOG || splitScript.getType() == SecQueryType.SWITCH_SCHEMA) {
                throw new UnsupportedOperationException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NONSUPPORT_SWITCH_CTX_ERROR.name()));
            } else if (splitScript.getType() == SecQueryType.TRANSACTION) {
                throw new UnsupportedOperationException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NONSUPPORT_TRANSACTION_OPERATE_ERROR.name()));
            }

            DmExecAutoTaskDO execTask = new DmExecAutoTaskDO();
            execTask.setExecSql(splitScript.getScript());
            execTask.setSqlType(splitScript.getType());
            execTask.setExecOrder(order++);
            execTask.setStatus(AutoExecTaskStatus.WAIT_EXEC);

            execTask.setAutoExecJobId(job.getId());
            execTask.setBizId(DmTeamUtils.nextExecTaskBizId(bizType));
            executionDal.autoTaskMapper().insert(execTask);
        }

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CREATE_MESSAGE.name(), confirmUser.getUsername(), confirmUser.getUid());
        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        monitorDal.bizLogMapper().insert(logDO);

        this.execHelperService.getHelper(bizType).execStart(job.getDependOnBizType(), job.getBizId());
    }
}
