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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecTaskDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.security.AuditService;
import com.clougence.clouddm.console.web.util.CallUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.platform.dal.access.*;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.ExecuteVariables;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecManagerImpl implements AutoExecManager {
    @Resource
    private SystemDal            systemDal;
    @Resource
    private MonitorDal           monitorDal;
    @Resource
    private ExecutionDal         executionDal;
    @Resource
    private DataSourceDal        dataSourceDal;
    @Resource
    private ObjectCacheDao       objectCacheDao;
    @Resource
    private AutoExecRService     autoExecRService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private QueryAnalysisService analysisService;
    @Resource
    private AuditService         auditService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void dispatchJob(Long jobId) {
        DmExecAutoJobDO dmAutoExecJobDO = executionDal.autoJobMapper().queryByIdForUpdate(jobId);
        if (dmAutoExecJobDO.getStatus() != AutoExecJobStatus.INIT) {
            log.info("{} was dispatch by another console", jobId);
            return;
        }

        // dispatch
        DsCacheEntry dsCacheEntry = objectCacheDao.queryByDsId(dmAutoExecJobDO.getDataSourceId());
        if (dsCacheEntry.getClusterId() == null) {
            DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO,
                DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_DATASOURCE_ERROR_MESSAGE.name()),
                LogDependBizType.AUTO_EXEC_JOB,
                dmAutoExecJobDO.getBizId());
            monitorDal.bizLogMapper().insert(logDO);
            this.executionDal.autoJobMapper().updateJobStatus(dmAutoExecJobDO.getId(), AutoExecJobStatus.FAILED);
            return;
        }
        RSocketSendDTO dto = this.buildRSocketSendDTO(dsCacheEntry.getClusterId());
        AutoExecJobDTO job = this.prepareJob(dmAutoExecJobDO);

        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO,
            DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_START_MESSAGE.name(), dto.getWorkerIP()),
            LogDependBizType.AUTO_EXEC_JOB,
            dmAutoExecJobDO.getBizId());

        monitorDal.bizLogMapper().insert(logDO);

        dmAutoExecJobDO.setStatus(AutoExecJobStatus.WAIT_EXEC);
        dmAutoExecJobDO.setLastReportTime(new Date());
        dmAutoExecJobDO.setWorkerSeqNumber(dto.getWorkerSeqNumber());
        this.executionDal.autoJobMapper().updateById(dmAutoExecJobDO);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    autoExecRService.dispatchJob(dto, job);
                } catch (Throwable e) {
                    log.error("dispatch auto exec job failed after commit, jobId: " + jobId, e);
                }
            }
        });
    }

    private AutoExecJobDTO prepareJob(DmExecAutoJobDO job) {
        AutoExecJobDTO job4Auto = new AutoExecJobDTO();
        Requester requester;
        if (job.getDependOnBizType() == SQLJobBizType.TICKET) {
            requester = Requester.TICKET;
        } else if (job.getDependOnBizType() == SQLJobBizType.CHANGE) {
            requester = Requester.CHANGE;
        } else {
            throw new UnsupportedOperationException("Unsupported type : " + job.getDependOnBizType());
        }

        job4Auto.setErrorStrategy(job.getConfig().getErrorStrategy());
        job4Auto.setRetryCount(job.getConfig().getRetryCount());
        job4Auto.setRetryWaitTime(job.getConfig().getRetryWaitTime());
        job4Auto.setEnableTransactional(job.getConfig().isEnableTransactional());
        DmDsDO dsDO = this.dataSourceDal.dsMapper().queryDsIdentityById(job.getDataSourceId());
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsDO.getId());

        ArrayList<String> levels = new ArrayList<>();
        levels.add(dsDO.getDsEnvId().toString());
        levels.add(dsDO.getId().toString());
        levels.addAll(job.getLevels());
        Map<UmiTypes, Object> levelsParam = this.dmDsConfigService.parseLevels(levels).levelsParam();

        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());
        SessionContextDTO contextDTO = sessionSpi.createSessionContext(dsConfig, params);

        QueryAnalysisOptions analysisOptions = QueryAnalysisOptions.builder()//
            .dataSourceId(dsDO.getId())
            .levels(levelsParam)
            .deepParser(false)
            .skip(QueryAnalysisFeature.REWRITE, QueryAnalysisFeature.PROVENANCE, QueryAnalysisFeature.MASKING)
            .build();

        List<DmExecAutoTaskDO> taskList = this.executionDal.autoTaskMapper().queryNeedExecTaskList(job.getId());
        for (DmExecAutoTaskDO task : taskList) {
            String queryId = "A" + job.getId() + "-" + task.getId();
            AutoExecTaskDTO taskDTO = new AutoExecTaskDTO();
            taskDTO.setTaskId(task.getId());
            taskDTO.setQueryId(queryId);
            taskDTO.setExecSql(task.getExecSql());
            taskDTO.setExecOrder(task.getExecOrder());
            job4Auto.getTaskList().add(taskDTO);

            List<QueryRequest> requests = this.analysisService.analysisRequests(dsConfig, task.getExecSql(), null, 1, 0, analysisOptions);
            if (requests.size() != 1) {
                throw new IllegalStateException("Auto execution task must contain exactly one SQL statement.");
            }

            QueryRequest auditRequest = requests.get(0);
            auditRequest.setQueryId(queryId);
            auditRequest.setRequester(requester);
            auditRequest.setRequestTime(new Date());
            Map<String, String> variables = new HashMap<>();
            variables.put(ExecuteVariables.CURRENT_UID, job.getUid());
            variables.put(ExecuteVariables.DS_ID, String.valueOf(dsDO.getId()));
            variables.put(ExecuteVariables.CURRENT_CATALOG, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
            variables.put(ExecuteVariables.CURRENT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
            auditRequest.setVariables(variables);
            this.auditService.prepareAudit(dsDO.getId(), auditRequest);
        }

        job4Auto.setContextDTO(contextDTO);
        job4Auto.setDsId(dsDO.getId());
        job4Auto.setJobId(job.getId());
        return job4Auto;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void stopJob(Long jobId, DmAuthUserDO user) {
        DmExecAutoJobDO job = executionDal.autoJobMapper().queryByIdForUpdate(jobId);

        AutoExecJobStatus status = job.getStatus();
        if (status == AutoExecJobStatus.INIT) {
            job.setStatus(AutoExecJobStatus.PAUSE);
            this.executionDal.autoJobMapper().updateById(job);

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_DIRECT_PAUSE_MESSAGE.name(), user.getUsername(), user.getUid());
            DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
            this.monitorDal.bizLogMapper().insert(logDO);
            return;
        }

        if (status == AutoExecJobStatus.PAUSE || status == AutoExecJobStatus.FAILED || status == AutoExecJobStatus.FINISH) {
            log.warn("{} was already stop", jobId);
            return;
        }

        if (status == AutoExecJobStatus.PAUSING) {
            return;
        }

        this.autoExecRService.pauseJob(CallUtils.buildSendDTO(job.getWorkerSeqNumber()), jobId);

        job.setStatus(AutoExecJobStatus.PAUSING);
        this.executionDal.autoJobMapper().updateById(job);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_PAUSE_MESSAGE.name(), user.getUsername(), user.getUid());
        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.monitorDal.bizLogMapper().insert(logDO);
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
