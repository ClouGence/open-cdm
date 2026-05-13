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
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecTaskMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmBizLogMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.exec.AutoExecJobConfig;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.DmTeamUtils;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.util.RdpPageDO;
import com.clougence.clouddm.console.web.util.RdpPageUtil;
import com.clougence.utils.format.DateFormatType;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutoExecServiceImpl implements AutoExecService {

    @Resource
    private DmAutoExecJobMapper   dmAutoExecJobMapper;
    @Resource
    private DmAutoExecTaskMapper  dmSqlTaskMapper;
    @Resource
    private DmBizLogMapper        dmBizLogMapper;
    @Resource
    private DmWorkerMapper        dmWorkerMapper;
    @Resource
    private AutoExecManager       autoExecManager;
    @Resource
    private RdpUserMapper         rdpUserMapper;
    @Resource
    private AutoExecHelperService execHelperService;

    @Override
    public void continueTask(String bizId, SQLJobBizType type, long taskId) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        DmAutoExecTaskDO execTaskDO = dmSqlTaskMapper.selectById(taskId);
        if (!execTaskDO.getAutoExecJobId().equals(job.getId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_JOB_NOT_MATCH_ERROR_MESSAGE.name()));
        }
        execTaskDO.setStatus(AutoExecTaskStatus.WAIT_EXEC);
        dmSqlTaskMapper.updateById(execTaskDO);
    }

    @Override
    public boolean skipTask(String bizId, SQLJobBizType type, long taskId, String uid) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
        if (job.getStatus() != AutoExecJobStatus.PAUSE && job.getStatus() != AutoExecJobStatus.FAILED) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_WRONG_OPERATE_ERROR_MESSAGE.name()));
        }
        DmAutoExecTaskDO execTaskDO = dmSqlTaskMapper.selectById(taskId);
        if (!execTaskDO.getAutoExecJobId().equals(job.getId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_JOB_NOT_MATCH_ERROR_MESSAGE.name()));
        }

        if (execTaskDO.getStatus() == AutoExecTaskStatus.FINISH || execTaskDO.getStatus() == AutoExecTaskStatus.WAIT_CONFIRM) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_IS_FINISH.name()));
        }

        dmSqlTaskMapper.updateStatusByTaskId(execTaskDO.getId(), AutoExecTaskStatus.CANCELED);

        RdpUserDO user = this.rdpUserMapper.queryByUid(uid);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_CONSOLE_SKIP.name(), user.getUsername(), user.getUid(), execTaskDO.getExecOrder());
        DmBizLogDO jobDO = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        DmBizLogDO taskLog = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_TASK, execTaskDO.getBizId());
        this.dmBizLogMapper.insert(jobDO);
        this.dmBizLogMapper.insert(taskLog);

        int count = this.dmSqlTaskMapper.queryNeedExecTaskCount(job.getId());
        if (count == 0) {
            this.dmAutoExecJobMapper.finishJob(job.getId());
            String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FINISH_MESSAGE.name());
            DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO, msg, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
            this.dmBizLogMapper.insert(logDO);

            this.execHelperService.getHelper(type).execCompleted(job.getDependOnBizType(), job.getBizId());
            return true;
        }
        return false;
    }

    @Override
    public DmAutoExecJobVO queryAutoExecJob(String bizId, SQLJobBizType type, boolean canOperate) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
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
            DmWorkerDO workerStatus = this.dmWorkerMapper.getByWsn(job.getWorkerSeqNumber());
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
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
        RdpUserDO user = rdpUserMapper.queryByUid(uid);
        autoExecManager.stopJob(job.getId(), user);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void endJob(String bizId, SQLJobBizType type, String uid) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
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
        dmAutoExecJobMapper.updateById(job);
        dmSqlTaskMapper.cancelAllWaitTask(job.getId());

        RdpUserDO user = rdpUserMapper.queryByUid(uid);
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_TERMINATION_MESSAGE.name(), user.getUsername(), user.getUid());

        DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.dmBizLogMapper.insert(logDO);

        this.execHelperService.getHelper(type).execAbort(job.getDependOnBizType(), job.getBizId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void retryJob(String bizId, SQLJobBizType type, String uid) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
        if (job.getStatus() != AutoExecJobStatus.FAILED && job.getStatus() != AutoExecJobStatus.PAUSE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_RETRY_JOB_ERROR_MESSAGE.name()));
        }

        job.setStatus(AutoExecJobStatus.INIT);
        int updateCount = dmAutoExecJobMapper.retryJob(job.getId());

        if (updateCount <= 0) {
            return;
        }
        dmSqlTaskMapper.retryTask(job.getId());
        RdpUserDO user = rdpUserMapper.queryByUid(uid);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_RETRY_JOB_MESSAGE.name(), user.getUsername(), user.getUid());
        DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.dmBizLogMapper.insert(logDO);
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryAutoExecTaskList(String bizId, SQLJobBizType type, boolean canOperate, AutoExecTaskStatus status, RdpPageDO pageDO) {
        DmAutoExecJobDO job = this.dmAutoExecJobMapper.queryByDependOnBizId(bizId);
        Page<?> page = RdpPageUtil.startPage(pageDO);
        IPage<DmAutoExecTaskDO> iPage = this.dmSqlTaskMapper.queryListByJobId(page, job.getId(), status);
        DmPageVO<DmAutoExecTaskVO> result = new DmPageVO<>(iPage);

        for (DmAutoExecTaskDO taskDO : iPage.getRecords()) {
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

        RdpUserDO confirmUser = this.rdpUserMapper.queryByUid(execUser);
        DmAutoExecJobDO job = new DmAutoExecJobDO();
        job.setLevels(dsLevels.dbLevels());
        job.setDependOnBizType(bizType);
        job.setDataSourceId(dsLevels.dsDO().getId());
        job.setDependOnBizId(bizId);
        job.setUid(confirmUser.getUid());
        job.setBizId(DmTeamUtils.nextExecJobBizId(bizType));
        job.setPrimaryUid(ownerUid);
        job.setExecType(config.getAutoExecType());
        job.setStatus(AutoExecJobStatus.INIT);

        AutoExecJobConfig jobConfig = new AutoExecJobConfig();
        jobConfig.setEnableTransactional(config.isEnableTransactional());
        jobConfig.setRetryWaitTime(config.getRetryWaitTime());
        jobConfig.setErrorStrategy(config.getErrorStrategy());
        jobConfig.setRetryCount(config.getRetryCount());
        job.setConfig(jobConfig);
        if (job.getExecType() == DmAutoExecType.IMMEDIATE) {
            job.setScheduleTime(new Date());
        } else {
            job.setScheduleTime(new Date(config.getExecTime()));
        }

        this.dmAutoExecJobMapper.insert(job);

        int order = 1;
        for (int i = 0; i < scripts.size(); i++) {
            SplitScript splitScript = scripts.get(i);
            if (splitScript.getType() == SecQueryType.SWITCH_CATALOG || splitScript.getType() == SecQueryType.SWITCH_SCHEMA) {
                throw new UnsupportedOperationException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NONSUPPORT_SWITCH_CTX_ERROR.name()));
            } else if (splitScript.getType() == SecQueryType.TRANSACTION) {
                throw new UnsupportedOperationException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NONSUPPORT_TRANSACTION_OPERATE_ERROR.name()));
            }

            DmAutoExecTaskDO execTask = new DmAutoExecTaskDO();
            execTask.setExecSql(splitScript.getScript());
            execTask.setSqlType(splitScript.getType());
            execTask.setExecOrder(order++);
            execTask.setStatus(AutoExecTaskStatus.WAIT_EXEC);

            execTask.setAutoExecJobId(job.getId());
            execTask.setBizId(DmTeamUtils.nextExecTaskBizId(bizType));
            dmSqlTaskMapper.insert(execTask);
        }

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CREATE_MESSAGE.name(), confirmUser.getUsername(), confirmUser.getUid());
        DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        dmBizLogMapper.insert(logDO);

        this.execHelperService.getHelper(bizType).execStart(job.getDependOnBizType(), job.getBizId());
    }
}
