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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.console.autoexec.ExecJobRService;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecTaskDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.*;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class ExecJobRServiceProvider extends AbstractBasicProvider implements ExecJobRService {
    @Resource
    private MonitorDal            monitorDal;
    @Resource
    private ExecutionDal          executionDal;
    @Resource
    private DataSourceDal         dsDal;
    @Resource
    private DmDsConfigService     dmDsConfigService;
    @Resource
    private AutoExecHelperService execHelperService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public AutoExecJobDTO fetchJobInfo(WorkerIdentity identity, Long jobId) {
        if (!checkAccessKey(identity)) {
            return null;
        }
        DmExecAutoJobDO dmAutoExecJobDO = executionDal.autoJobMapper().queryByIdForUpdate(jobId);

        AutoExecJobDTO jobDTO = new AutoExecJobDTO();
        if (dmAutoExecJobDO == null) {
            jobDTO.setJobNotExists(true);
            return jobDTO;
        }
        if (dmAutoExecJobDO.getStatus() == AutoExecJobStatus.EXECUTING) {
            jobDTO.setJobIsExecByAnother(true);
            return jobDTO;
        }
        if (dmAutoExecJobDO.getDependOnBizType() == SQLJobBizType.TICKET) {
            jobDTO.setRequester(Requester.TICKET);
        } else if (dmAutoExecJobDO.getDependOnBizType() == SQLJobBizType.CHANGE) {
            jobDTO.setRequester(Requester.CHANGE);
        } else {
            throw new UnsupportedOperationException("Unsupported type : " + dmAutoExecJobDO.getDependOnBizType());
        }
        jobDTO.setLevels(dmAutoExecJobDO.getLevels());
        jobDTO.setUid(dmAutoExecJobDO.getUid());
        jobDTO.setErrorStrategy(dmAutoExecJobDO.getConfig().getErrorStrategy());
        jobDTO.setRetryCount(dmAutoExecJobDO.getConfig().getRetryCount());
        jobDTO.setRetryWaitTime(dmAutoExecJobDO.getConfig().getRetryWaitTime());
        jobDTO.setEnableTransactional(dmAutoExecJobDO.getConfig().isEnableTransactional());
        DmDsDO dsDO = dsDal.dsMapper().queryDsIdentityById(dmAutoExecJobDO.getDataSourceId());
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());

        ArrayList<String> levels = new ArrayList<>();
        levels.add(dsDO.getDsEnvId().toString());
        levels.add(dsDO.getId().toString());
        levels.addAll(dmAutoExecJobDO.getLevels());

        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());

        //
        Map<String, Object> params = new HashMap<>();
        Map<UmiTypes, Object> levelsParam = this.dmDsConfigService.parseLevels(levels).levelsParam();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        SessionContextDTO contextDTO = sessionSpi.createSessionContext(dsConfig, params);

        List<DmExecAutoTaskDO> dmAutoExecTaskDOS = executionDal.autoTaskMapper().queryNeedExecTaskList(jobId);
        for (DmExecAutoTaskDO task : dmAutoExecTaskDOS) {
            AutoExecTaskDTO taskDTO = new AutoExecTaskDTO();
            taskDTO.setTaskId(task.getId());
            taskDTO.setExecSql(task.getExecSql());
            //            taskDTO.setTransactionGroup(task.getTransactionalGroup());
            taskDTO.setExecOrder(task.getExecOrder());
            jobDTO.getTaskList().add(taskDTO);
        }
        jobDTO.setContextDTO(contextDTO);
        jobDTO.setDsId(dsDO.getId());
        jobDTO.setDsType(dsDO.getDataSourceType());
        jobDTO.setJobId(dmAutoExecJobDO.getId());

        dmAutoExecJobDO.setStatus(AutoExecJobStatus.EXECUTING);
        executionDal.autoJobMapper().updateById(dmAutoExecJobDO);

        return jobDTO;
    }

    @Override
    public void reportActiveJobs(WorkerIdentity identity, List<Long> jobIdList) {
        if (!checkAccessKey(identity) || CollectionUtils.isEmpty(jobIdList)) {
            return;
        }
        this.executionDal.autoJobMapper().updateReportTime(jobIdList);
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
                    this.executionDal.autoJobMapper().updateQueryIdByJobId(message.getJobId(), message.getQueryId());
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
        DmExecAutoTaskDO taskDO = executionDal.autoTaskMapper().selectById(dto.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.CANCELED) {
            return;
        }

        int updateCount = this.executionDal.autoTaskMapper().taskSkip(dto.getJobId(), dto.getTaskId());
        if (updateCount == 0) {
            return;
        }

        this.taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_ERROR_SKIP_MESSAGE.name()), taskDO.getBizId());

        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TRANSACTION_SKIP_MESSAGE.name(), taskDO.getExecOrder(), taskDO.getExecSql());
        this.jobLog(Loglevel.WARING, msg, dto.getJobId());
    }

    private void jobPause(AutoExecMessageDTO dto) {
        DmExecAutoJobDO jobDO = this.executionDal.autoJobMapper().selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.PAUSE) {
            return;
        }
        this.executionDal.autoJobMapper().updateJobStatus(dto.getJobId(), AutoExecJobStatus.PAUSE);
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_PAUSE_MESSAGE.name()), dto.getJobId());
    }

    private void transactionRollback(AutoExecMessageDTO message) {
        int updateCount = this.executionDal.autoTaskMapper().transactionRollback(message.getJobId());
        if (updateCount == 0) {
            return;
        }
        List<DmExecAutoTaskDO> taskList = this.executionDal.autoTaskMapper().queryGroupTaskListByStatus(message.getJobId(), AutoExecTaskStatus.WAIT_CONFIRM);
        for (DmExecAutoTaskDO execTaskDO : taskList) {
            this.taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_ROLLBACK_MESSAGE.name()), execTaskDO.getBizId());
        }
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_GROUP_ROLLBACK_MESSAGE.name()), message.getJobId());
    }

    private void transactionFinish(AutoExecMessageDTO dto) {
        this.executionDal.autoTaskMapper().transactionCommit(dto.getJobId());
    }

    private void createSessionFailed(AutoExecMessageDTO dto) {
        DmExecAutoJobDO jobDO = this.executionDal.autoJobMapper().selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.FAILED) {
            return;
        }
        this.executionDal.autoJobMapper().updateJobStatus(dto.getJobId(), AutoExecJobStatus.FAILED);
        this.jobLog(Loglevel.ERROR, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_CREATE_SESSION_ERROR_MESSAGE.name(), dto.getMessage()), dto.getJobId());

        this.execHelperService.getHelper(jobDO.getDependOnBizType()).execFailed(jobDO.getDependOnBizType(), jobDO.getBizId());
    }

    private void jobFinish(AutoExecMessageDTO dto) {
        DmExecAutoJobDO jobDO = this.executionDal.autoJobMapper().selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.FINISH) {
            return;
        }
        this.executionDal.autoJobMapper().finishJob(dto.getJobId());
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FINISH_MESSAGE.name()), dto.getJobId());

        this.execHelperService.getHelper(jobDO.getDependOnBizType()).execCompleted(jobDO.getDependOnBizType(), jobDO.getBizId());
    }

    private void jobFailed(AutoExecMessageDTO dto) {
        DmExecAutoTaskDO taskDO = executionDal.autoTaskMapper().selectById(dto.getTaskId());
        if (taskDO == null) {
            return;
        }
        this.executionDal.autoJobMapper().updateJobStatus(dto.getJobId(), AutoExecJobStatus.FAILED);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FAILED_MESSAGE.name(), taskDO.getExecOrder(), taskDO.getExecSql());
        this.jobLog(Loglevel.ERROR, msg, dto.getJobId());
    }

    private void taskRetry(AutoExecMessageDTO message) {
        taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_RETRY_MESSAGE.name()), message.getTaskId());
    }

    private void taskWaitConfirm(AutoExecMessageDTO message) {
        DmExecAutoTaskDO taskDO = executionDal.autoTaskMapper().selectById(message.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.WAIT_CONFIRM) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.WAIT_CONFIRM);
        taskDO.setExecCount(taskDO.getExecCount() + message.getExecCount());
        taskDO.setAffectRow(message.getAffectLine());
        taskDO.setGmtLastEnd(message.getTime());
        executionDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_FINISH_MESSAGE.name()), message.getTaskId());
    }

    private void taskFinish(AutoExecMessageDTO dto) {
        DmExecAutoTaskDO taskDO = executionDal.autoTaskMapper().selectById(dto.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.FINISH) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.FINISH);
        taskDO.setExecCount(taskDO.getExecCount() + dto.getExecCount());
        taskDO.setAffectRow(dto.getAffectLine());
        taskDO.setGmtLastEnd(dto.getTime());
        executionDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_FINISH_MESSAGE.name()), dto.getTaskId());
    }

    private void taskFailed(AutoExecMessageDTO message) {
        DmExecAutoTaskDO taskDO = executionDal.autoTaskMapper().selectById(message.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.FAILED) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.FAILED);
        taskDO.setExecCount(taskDO.getExecCount() + message.getExecCount());
        taskDO.setAffectRow(0L);
        taskDO.setGmtLastEnd(message.getTime());
        executionDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.ERROR, message.getMessage(), message.getTaskId());
    }

    private void taskStart(AutoExecMessageDTO message) {
        DmExecAutoTaskDO taskDO = executionDal.autoTaskMapper().selectById(message.getTaskId());
        // repeat message
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.EXECUTING) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.EXECUTING);
        taskDO.setGmtLastStart(message.getTime());
        executionDal.autoTaskMapper().updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_START_MESSAGE.name()), message.getTaskId());
    }

    private void taskLogByBizId(Loglevel logLevel, String message, Long taskId) {
        DmExecAutoTaskDO execTaskDO = executionDal.autoTaskMapper().selectById(taskId);
        DmMonBizLogDO logDO = new DmMonBizLogDO(logLevel, message, LogDependBizType.AUTO_EXEC_TASK, execTaskDO.getBizId());
        monitorDal.bizLogMapper().insert(logDO);
    }

    private void taskLogByBizId(Loglevel logLevel, String message, String bizId) {
        DmMonBizLogDO logDO = new DmMonBizLogDO(logLevel, message, LogDependBizType.AUTO_EXEC_TASK, bizId);
        monitorDal.bizLogMapper().insert(logDO);
    }

    private void jobLog(Loglevel logLevel, String message, Long jobId) {
        DmExecAutoJobDO job = executionDal.autoJobMapper().selectById(jobId);
        DmMonBizLogDO logDO = new DmMonBizLogDO(logLevel, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        monitorDal.bizLogMapper().insert(logDO);
    }

}
