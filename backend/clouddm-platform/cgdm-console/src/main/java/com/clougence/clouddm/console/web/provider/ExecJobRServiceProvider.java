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
package com.clougence.clouddm.console.web.provider;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.console.autoexec.ExecJobRService;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecTaskStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class ExecJobRServiceProvider extends AbstractBasicProvider implements ExecJobRService {
    @Resource
    private MonitorDal            monitorDal;
    @Resource
    private ExecutionDal          execDal;
    @Resource
    private AutoExecHelperService execHelperService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean startJob(WorkerIdentity identity, Long jobId) {
        if (!checkAccessKey(identity)) {
            return false;
        }
        return this.execDal.autoJobMapper().startJob(jobId, identity.getWorkerSeqNumber()) > 0;
    }

    @Override
    public void reportActiveJobs(WorkerIdentity identity, List<Long> jobIdList) {
        if (!checkAccessKey(identity) || CollectionUtils.isEmpty(jobIdList)) {
            return;
        }
        this.execDal.autoJobMapper().updateReportTime(jobIdList);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void reportExecMessage(WorkerIdentity identity, List<AutoExecMessageDTO> messages) {
        if (!checkAccessKey(identity) || CollectionUtils.isEmpty(messages)) {
            return;
        }
        for (AutoExecMessageDTO message : messages) {
            switch (message.getType()) {
                // task
                case TASK_START: {
                    taskStart(message);
                    break;
                }
                case TASK_FAILED: {
                    taskFailed(message);
                    break;
                }
                case TASK_FINISH: {
                    taskFinish(message);
                    break;
                }
                case TASK_WAIT_CONFIRM: {
                    taskWaitConfirm(message);
                    break;
                }
                case TASK_RETRY: {
                    taskRetry(message);
                    break;
                }
                // job
                case JOB_FAILED: {
                    jobFailed(message);
                    break;
                }
                case JOB_PAUSE: {
                    jobPause(message);
                    break;
                }
                case JOB_FINISH: {
                    jobFinish(message);
                    break;
                }
                case CREATE_SESSION_FAILED: {
                    createSessionFailed(message);
                    break;
                }
                case QUERY_ID: {
                    this.execDal.autoJobMapper().updateQueryIdByJobId(message.getJobId(), message.getQueryId());
                    break;
                }
                case TRANSACTION_FINISH: {
                    transactionFinish(message);
                    break;
                }
                case TRANSACTION_ROLLBACK: {
                    transactionRollback(message);
                    break;
                }
                case TASK_SKIP: {
                    taskSkip(message);
                    break;
                }
            }
        }
    }

    private void taskSkip(AutoExecMessageDTO dto) {
        DmExecAutoTaskDO taskDO = execDal.autoTaskMapper().selectById(dto.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.CANCELED) {
            return;
        }

        int updateCount = this.execDal.autoTaskMapper().taskSkip(dto.getJobId(), dto.getTaskId());
        if (updateCount == 0) {
            return;
        }

        this.taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_ERROR_SKIP_MESSAGE.name()), taskDO.getBizId());

        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TRANSACTION_SKIP_MESSAGE.name(), taskDO.getExecOrder(), taskDO.getExecSql());
        this.jobLog(Loglevel.WARING, msg, dto.getJobId());
    }

    private void jobPause(AutoExecMessageDTO dto) {
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.PAUSE) {
            return;
        }
        this.execDal.autoJobMapper().updateJobStatus(dto.getJobId(), AutoExecJobStatus.PAUSE);
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_PAUSE_MESSAGE.name()), dto.getJobId());
    }

    private void transactionRollback(AutoExecMessageDTO message) {
        int updateCount = this.execDal.autoTaskMapper().transactionRollback(message.getJobId());
        if (updateCount == 0) {
            return;
        }
        List<DmExecAutoTaskDO> taskList = this.execDal.autoTaskMapper().queryGroupTaskListByStatus(message.getJobId(), AutoExecTaskStatus.WAIT_CONFIRM);
        for (DmExecAutoTaskDO execTaskDO : taskList) {
            this.taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_ROLLBACK_MESSAGE.name()), execTaskDO.getBizId());
        }
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_GROUP_ROLLBACK_MESSAGE.name()), message.getJobId());
    }

    private void transactionFinish(AutoExecMessageDTO dto) {
        this.execDal.autoTaskMapper().transactionCommit(dto.getJobId());
    }

    private void createSessionFailed(AutoExecMessageDTO dto) {
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.FAILED) {
            return;
        }
        this.execDal.autoJobMapper().updateJobStatus(dto.getJobId(), AutoExecJobStatus.FAILED);
        this.jobLog(Loglevel.ERROR, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_CREATE_SESSION_ERROR_MESSAGE.name(), dto.getMessage()), dto.getJobId());

        this.execHelperService.getHelper(jobDO.getDependOnBizType()).execFailed(jobDO.getDependOnBizType(), jobDO.getBizId());
    }

    private void jobFinish(AutoExecMessageDTO dto) {
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.FINISH) {
            return;
        }
        this.execDal.autoJobMapper().finishJob(dto.getJobId());
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FINISH_MESSAGE.name()), dto.getJobId());

        this.execHelperService.getHelper(jobDO.getDependOnBizType()).execCompleted(jobDO.getDependOnBizType(), jobDO.getBizId());
    }

    private void jobFailed(AutoExecMessageDTO dto) {
        DmExecAutoTaskDO taskDO = execDal.autoTaskMapper().selectById(dto.getTaskId());
        if (taskDO == null) {
            return;
        }
        this.execDal.autoJobMapper().updateJobStatus(dto.getJobId(), AutoExecJobStatus.FAILED);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FAILED_MESSAGE.name(), taskDO.getExecOrder(), taskDO.getExecSql());
        this.jobLog(Loglevel.ERROR, msg, dto.getJobId());
    }

    private void taskRetry(AutoExecMessageDTO message) {
        taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_RETRY_MESSAGE.name()), message.getTaskId());
    }

    private void taskWaitConfirm(AutoExecMessageDTO message) {
        DmExecAutoTaskDO taskDO = execDal.autoTaskMapper().selectById(message.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.WAIT_CONFIRM) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.WAIT_CONFIRM);
        taskDO.setExecCount(taskDO.getExecCount() + message.getExecCount());
        taskDO.setAffectRow(message.getAffectLine());
        taskDO.setGmtLastEnd(message.getTime());
        execDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_FINISH_MESSAGE.name()), message.getTaskId());
    }

    private void taskFinish(AutoExecMessageDTO dto) {
        DmExecAutoTaskDO taskDO = execDal.autoTaskMapper().selectById(dto.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.FINISH) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.FINISH);
        taskDO.setExecCount(taskDO.getExecCount() + dto.getExecCount());
        taskDO.setAffectRow(dto.getAffectLine());
        taskDO.setGmtLastEnd(dto.getTime());
        execDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_FINISH_MESSAGE.name()), dto.getTaskId());
    }

    private void taskFailed(AutoExecMessageDTO message) {
        DmExecAutoTaskDO taskDO = execDal.autoTaskMapper().selectById(message.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.FAILED) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.FAILED);
        taskDO.setExecCount(taskDO.getExecCount() + message.getExecCount());
        taskDO.setAffectRow(0L);
        taskDO.setGmtLastEnd(message.getTime());
        execDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.ERROR, message.getMessage(), message.getTaskId());
    }

    private void taskStart(AutoExecMessageDTO message) {
        DmExecAutoTaskDO taskDO = execDal.autoTaskMapper().selectById(message.getTaskId());
        // repeat message
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.EXECUTING) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.EXECUTING);
        taskDO.setGmtLastStart(message.getTime());
        execDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_START_MESSAGE.name()), message.getTaskId());
    }

    private void taskLogByBizId(Loglevel logLevel, String message, Long taskId) {
        DmExecAutoTaskDO execTaskDO = execDal.autoTaskMapper().selectById(taskId);
        DmMonBizLogDO logDO = new DmMonBizLogDO(logLevel, message, LogDependBizType.AUTO_EXEC_TASK, execTaskDO.getBizId());
        monitorDal.bizLogMapper().insert(logDO);
    }

    private void taskLogByBizId(Loglevel logLevel, String message, String bizId) {
        DmMonBizLogDO logDO = new DmMonBizLogDO(logLevel, message, LogDependBizType.AUTO_EXEC_TASK, bizId);
        monitorDal.bizLogMapper().insert(logDO);
    }

    private void jobLog(Loglevel logLevel, String message, Long jobId) {
        DmExecAutoJobDO job = execDal.autoJobMapper().selectById(jobId);
        DmMonBizLogDO logDO = new DmMonBizLogDO(logLevel, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        monitorDal.bizLogMapper().insert(logDO);
    }

}
