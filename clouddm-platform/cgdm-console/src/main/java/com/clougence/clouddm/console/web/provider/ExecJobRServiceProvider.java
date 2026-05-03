package com.clougence.clouddm.console.web.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.autoexec.ExecJobRService;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecTaskDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecTaskMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmBizLogMapper;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class ExecJobRServiceProvider extends AbstractBasicProvider implements ExecJobRService {

    @Resource
    private DmAutoExecJobMapper   jobMapper;
    @Resource
    private DmAutoExecTaskMapper  taskMapper;
    @Resource
    private RdpDataSourceMapper   rdpDataSourceMapper;
    @Resource
    private DmDsConfigService     dmDsConfigService;
    @Resource
    private DmBizLogMapper        dmBizLogMapper;
    @Resource
    private AutoExecHelperService execHelperService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public AutoExecJobDTO fetchJobInfo(WorkerIdentity identity, Long jobId) {
        if (!checkAccessKey(identity)) {
            return null;
        }
        DmAutoExecJobDO dmAutoExecJobDO = jobMapper.queryByIdForUpdate(jobId);

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
        RdpDataSourceDO dsDO = rdpDataSourceMapper.queryDsIdentityById(dmAutoExecJobDO.getDataSourceId());
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());

        ArrayList<String> levels = new ArrayList<>();
        levels.add(dsDO.getDsEnvId().toString());
        levels.add(dsDO.getId().toString());
        levels.addAll(dmAutoExecJobDO.getLevels());

        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());

        //
        Map<String, Object> params = new HashMap<>();
        Map<UmiTypes, Object> levelsParam = this.dmDsConfigService.parseLevels(levels).getLevelsParam();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        SessionContextDTO contextDTO = sessionSpi.createSessionContext(dsConfig, params);

        List<DmAutoExecTaskDO> dmAutoExecTaskDOS = taskMapper.queryNeedExecTaskList(jobId);
        for (DmAutoExecTaskDO task : dmAutoExecTaskDOS) {
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
        jobMapper.updateById(dmAutoExecJobDO);

        return jobDTO;
    }

    @Override
    public void reportActiveJobs(WorkerIdentity identity, List<Long> jobIdList) {
        if (!checkAccessKey(identity) || CollectionUtils.isEmpty(jobIdList)) {
            return;
        }
        this.jobMapper.updateReportTime(jobIdList);
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
                    this.jobMapper.updateQueryIdByJobId(message.getJobId(), message.getQueryId());
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
        DmAutoExecTaskDO taskDO = taskMapper.selectById(dto.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.CANCELED) {
            return;
        }

        int updateCount = this.taskMapper.taskSkip(dto.getJobId(), dto.getTaskId());
        if (updateCount == 0) {
            return;
        }

        this.taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_ERROR_SKIP_MESSAGE.name()), taskDO.getBizId());

        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TRANSACTION_SKIP_MESSAGE.name(), taskDO.getExecOrder(), taskDO.getExecSql());
        this.jobLog(Loglevel.WARING, msg, dto.getJobId());
    }

    private void jobPause(AutoExecMessageDTO dto) {
        DmAutoExecJobDO jobDO = this.jobMapper.selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.PAUSE) {
            return;
        }
        this.jobMapper.updateJobStatus(dto.getJobId(), AutoExecJobStatus.PAUSE);
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_PAUSE_MESSAGE.name()), dto.getJobId());
    }

    private void transactionRollback(AutoExecMessageDTO message) {
        int updateCount = this.taskMapper.transactionRollback(message.getJobId());
        if (updateCount == 0) {
            return;
        }
        List<DmAutoExecTaskDO> taskList = this.taskMapper.queryGroupTaskListByStatus(message.getJobId(), AutoExecTaskStatus.WAIT_CONFIRM);
        for (DmAutoExecTaskDO execTaskDO : taskList) {
            this.taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_ROLLBACK_MESSAGE.name()), execTaskDO.getBizId());
        }
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_GROUP_ROLLBACK_MESSAGE.name()), message.getJobId());
    }

    private void transactionFinish(AutoExecMessageDTO dto) {
        this.taskMapper.transactionCommit(dto.getJobId());
    }

    private void createSessionFailed(AutoExecMessageDTO dto) {
        DmAutoExecJobDO jobDO = this.jobMapper.selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.FAILED) {
            return;
        }
        this.jobMapper.updateJobStatus(dto.getJobId(), AutoExecJobStatus.FAILED);
        this.jobLog(Loglevel.ERROR, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_CREATE_SESSION_ERROR_MESSAGE.name(), dto.getMessage()), dto.getJobId());

        this.execHelperService.getHelper(jobDO.getDependOnBizType()).execFailed(jobDO.getDependOnBizType(), jobDO.getBizId());
    }

    private void jobFinish(AutoExecMessageDTO dto) {
        DmAutoExecJobDO jobDO = this.jobMapper.selectById(dto.getJobId());
        if (jobDO == null || jobDO.getStatus() == AutoExecJobStatus.FINISH) {
            return;
        }
        this.jobMapper.finishJob(dto.getJobId());
        this.jobLog(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FINISH_MESSAGE.name()), dto.getJobId());

        this.execHelperService.getHelper(jobDO.getDependOnBizType()).execCompleted(jobDO.getDependOnBizType(), jobDO.getBizId());
    }

    private void jobFailed(AutoExecMessageDTO dto) {
        DmAutoExecTaskDO taskDO = taskMapper.selectById(dto.getTaskId());
        if (taskDO == null) {
            return;
        }
        this.jobMapper.updateJobStatus(dto.getJobId(), AutoExecJobStatus.FAILED);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_FAILED_MESSAGE.name(), taskDO.getExecOrder(), taskDO.getExecSql());
        this.jobLog(Loglevel.ERROR, msg, dto.getJobId());
    }

    private void taskRetry(AutoExecMessageDTO message) {
        taskLogByBizId(Loglevel.WARING, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_RETRY_MESSAGE.name()), message.getTaskId());
    }

    private void taskWaitConfirm(AutoExecMessageDTO message) {
        DmAutoExecTaskDO taskDO = taskMapper.selectById(message.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.WAIT_CONFIRM) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.WAIT_CONFIRM);
        taskDO.setExecCount(taskDO.getExecCount() + message.getExecCount());
        taskDO.setAffectRow(message.getAffectLine());
        taskDO.setGmtLastEnd(message.getTime());
        taskMapper.updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_FINISH_MESSAGE.name()), message.getTaskId());
    }

    private void taskFinish(AutoExecMessageDTO dto) {
        DmAutoExecTaskDO taskDO = taskMapper.selectById(dto.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.FINISH) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.FINISH);
        taskDO.setExecCount(taskDO.getExecCount() + dto.getExecCount());
        taskDO.setAffectRow(dto.getAffectLine());
        taskDO.setGmtLastEnd(dto.getTime());
        taskMapper.updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_FINISH_MESSAGE.name()), dto.getTaskId());
    }

    private void taskFailed(AutoExecMessageDTO message) {
        DmAutoExecTaskDO taskDO = taskMapper.selectById(message.getTaskId());
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.FAILED) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.FAILED);
        taskDO.setExecCount(taskDO.getExecCount() + message.getExecCount());
        taskDO.setAffectRow(0L);
        taskDO.setGmtLastEnd(message.getTime());
        taskMapper.updateById(taskDO);
        taskLogByBizId(Loglevel.ERROR, message.getMessage(), message.getTaskId());
    }

    private void taskStart(AutoExecMessageDTO message) {
        DmAutoExecTaskDO taskDO = taskMapper.selectById(message.getTaskId());
        // repeat message
        if (taskDO == null || taskDO.getStatus() == AutoExecTaskStatus.EXECUTING) {
            return;
        }
        taskDO.setStatus(AutoExecTaskStatus.EXECUTING);
        taskDO.setGmtLastStart(message.getTime());
        taskMapper.updateById(taskDO);
        taskLogByBizId(Loglevel.INFO, DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_TASK_START_MESSAGE.name()), message.getTaskId());
    }

    private void taskLogByBizId(Loglevel logLevel, String message, Long taskId) {
        DmAutoExecTaskDO execTaskDO = taskMapper.selectById(taskId);
        DmBizLogDO logDO = new DmBizLogDO(logLevel, message, DmLogDependBizType.AUTO_EXEC_TASK, execTaskDO.getBizId());
        dmBizLogMapper.insert(logDO);
    }

    private void taskLogByBizId(Loglevel logLevel, String message, String bizId) {
        DmBizLogDO logDO = new DmBizLogDO(logLevel, message, DmLogDependBizType.AUTO_EXEC_TASK, bizId);
        dmBizLogMapper.insert(logDO);
    }

    private void jobLog(Loglevel logLevel, String message, Long jobId) {
        DmAutoExecJobDO job = jobMapper.selectById(jobId);
        DmBizLogDO logDO = new DmBizLogDO(logLevel, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        dmBizLogMapper.insert(logDO);
    }

}
