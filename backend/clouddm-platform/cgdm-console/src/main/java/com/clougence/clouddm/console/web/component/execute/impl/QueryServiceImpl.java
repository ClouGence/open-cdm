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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.execute.AsyncWaitResult;
import com.clougence.clouddm.api.sidecar.session.execute.ExecuteRService;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.api.sidecar.session.execute.StatusDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsStatusService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.platform.dal.model.execution.DsSessionType;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:11
 * @since 1.1.3
 */
@Slf4j
@Service
public class QueryServiceImpl implements QueryService {

    @Resource
    private SystemDal         systemDal;
    @Resource
    private ExecutionDal      executionDal;
    @Resource
    private ObjectCacheDao    objectCacheDao;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private ExecuteRService   sessionRService;
    @Resource
    private DmDsStatusService dmDsStatusService;

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

    private RSocketSendDTO buildRSocketSendDTO(DmSysWorkerDO worker) {
        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setUid(worker.getUid());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }

    private RSocketSendDTO buildRSocketSendDTO(DmExecSessionDO sessionDO) {
        DmSysWorkerDO worker = this.systemDal.workerMapper().queryConnectedByWsn(sessionDO.getWsn());
        if (worker != null) {
            return this.buildRSocketSendDTO(worker);
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_WORKER_STATUS_OFFLINE_ERROR.name(), sessionDO.getWsn()));
        }
    }

    @Override
    public void testSessionWorker(String curUid, String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        DmSysWorkerDO worker = this.systemDal.workerMapper().queryConnectedByWsn(sessionDO.getWsn());
        if (worker == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_WORKER_STATUS_OFFLINE_ERROR.name(), sessionDO.getWsn()));
        }
    }

    @Override
    public boolean hasSession(String curUid, String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return false;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        return this.sessionRService.hasSession(sendDTO, sessionId);
    }

    @Override
    public DmExecSessionDO getSessionInfo(String curUid, String sessionId) {
        return this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
    }

    @Override
    public String createSession(String curUid, DsLevels levels, SessionContextDTO context) {
        DmDsDO dsDO = levels.dsDO();
        String sessionId = context.getSessionId();
        if (StringUtils.isBlank(sessionId)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NEED_SESSION_ID_ERROR.name()));
        }

        // close and remove old data.
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO != null) {
            RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
            this.sessionRService.closeSession(sendDTO, sessionId);
            this.executionDal.sessionMapper().deleteBySessionId(sessionId);
        }

        // gen new session.
        DsCacheEntry cacheEntry = this.objectCacheDao.queryByDsId(dsDO.getId());
        RSocketSendDTO sendDTO = this.buildRSocketSendDTO(cacheEntry.getClusterId());
        sessionDO = new DmExecSessionDO();
        sessionDO.setUid(curUid);
        sessionDO.setSessionId(sessionId);
        sessionDO.setSessionType(DsSessionType.QUERY);
        sessionDO.setWsn(sendDTO.getWorkerSeqNumber());
        sessionDO.setClusterId(String.valueOf(cacheEntry.getClusterId()));
        sessionDO.setDatasourceId(dsDO.getId());
        sessionDO.setDatasourceType(dsDO.getDataSourceType().getShortName());
        sessionDO.setConfig(JsonUtils.toJson(context));
        sessionDO.setGmtCreate(new Date());
        sessionDO.setGmtModified(new Date());

        int insert = this.executionDal.sessionMapper().insert(sessionDO);
        if (insert != 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_STORE_SESSION_DATA_ERROR.name()));
        }

        // create session
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
        try {
            this.sessionRService.createSession(sendDTO, dsConfig, context);
            this.dmDsStatusService.resetStatus(sendDTO.getUid(), dsConfig);
        } catch (Exception e) {
            dmDsStatusService.handleException(curUid, dsConfig, e);
            throw e;
        }
        return sessionId;
    }

    @Override
    public void closeSession(String curUid, String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }

        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        DmSysWorkerDO worker = this.systemDal.workerMapper().queryConnectedByWsn(sessionDO.getWsn());
        if (worker != null) {
            RSocketSendDTO sendDTO = this.buildRSocketSendDTO(worker);
            this.sessionRService.closeSession(sendDTO, sessionId);
        }

        this.executionDal.sessionMapper().deleteBySessionId(sessionId);
    }

    /**
     * for service API '/query/commit'
     */
    @Override
    public void commitSession(String curUid, final String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.commitSession(sendDTO, sessionId);
    }

    /**
     * for service API '/query/rollback'
     */
    @Override
    public void rollbackSession(String curUid, final String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.rollbackSession(sendDTO, sessionId);
    }

    /**
     * for service API '/query/cancel'
     */
    @Override
    public void cancelQuery(final String curUid, final String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.cancelAllQuery(sendDTO, sessionDO.getSessionId());
    }

    @Override
    public void asyncExecuteQuery(String curUid, String sessionId, String batchId, List<QueryRequest> queryRequest) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SESSION_NOT_EXIST_ERROR.name()));
        }

        // update SessionInfo
        this.executionDal.sessionMapper().updateSessionQueryTime(curUid, sessionId);

        // record Statistics
        //this.recordStatistics(sessionDO);

        // exec query
        try {
            RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
            DmDsUtils.fillRequestVariables(queryRequest, sessionDO.getDatasourceId(), curUid);
            AsyncWaitResult result = this.sessionRService.asyncExecuteQuery(sendDTO, sessionId, batchId, queryRequest);
            if (!result.isSuccess()) {
                throw new ErrorMessageException(result.getMessage());
            }
        } catch (Exception e) {
            throw new ErrorMessageException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @Override
    public ResultList syncExecuteQuery(String curUid, String sessionId, QueryRequest queryRequest) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_SESSION_NOT_EXIST_ERROR.name()));
        }
        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);

        // update SessionInfo
        this.executionDal.sessionMapper().updateSessionQueryTime(curUid, sessionId);

        // record Statistics
        //this.recordStatistics(sessionDO);

        // exec query
        try {
            List<QueryRequest> queryList = Collections.singletonList(queryRequest);

            DmDsUtils.fillRequestVariables(queryList, sessionDO.getDatasourceId(), curUid);
            String batchId = "sync-" + System.currentTimeMillis();
            AsyncWaitResult result = this.sessionRService.asyncExecuteQuery(sendDTO, sessionId, batchId, queryList);
            if (!result.isSuccess()) {
                throw new ErrorMessageException(result.getMessage());
            }
        } catch (Exception e) {
            throw new ErrorMessageException(ExceptionUtils.getRootCauseMessage(e));
        }

        // wait finish.
        while (this.sessionRService.isExecuting(sendDTO, sessionId)) {
            ThreadUtils.sleep(500);
        }

        return this.sessionRService.lastResultList(sendDTO, sessionId);
    }

    @Override
    public boolean isExecuting(String curUid, String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return false;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        return this.sessionRService.isExecuting(sendDTO, sessionId);
    }

    @Override
    public boolean hasQueryResult(String curUid, String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return false;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        return this.sessionRService.hasMoreQueryResult(sendDTO, sessionId);
    }

    @Override
    public ResultList fetchQueryResult(String curUid, String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return null;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        return this.sessionRService.lastResultList(sendDTO, sessionId);
    }

    @Override
    public void changeCatalog(String curUid, String sessionId, String catalog) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.setCurrentCatalog(sendDTO, sessionId, catalog);
        this.updateSessionCtx(sessionDO, sendDTO);
    }

    @Override
    public void changeSchema(String curUid, String sessionId, String schema) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.setCurrentSchema(sendDTO, sessionId, schema);
        this.updateSessionCtx(sessionDO, sendDTO);
    }

    @Override
    public void setAutoCommit(String curUid, String sessionId, boolean autoCommit) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.setAutoCommit(sendDTO, sessionId, autoCommit);
        this.updateSessionCtx(sessionDO, sendDTO);
    }

    @Override
    public void setIsolation(String curUid, String sessionId, RdbIsolation isolation) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.setIsolation(sendDTO, sessionId, isolation);
        this.updateSessionCtx(sessionDO, sendDTO);
    }

    @Override
    public void setReadOnly(String curUid, String sessionId, boolean readOnly) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        this.sessionRService.setReadOnly(sendDTO, sessionId, readOnly);
        this.updateSessionCtx(sessionDO, sendDTO);
    }

    @Override
    public StatusDTO getAndUpdateStatus(String curUid, String sessionId) {
        DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return null;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO);
        StatusDTO status = this.sessionRService.getStatus(sendDTO, sessionId);
        this.updateSessionCtx(sessionDO, status);
        return status;
    }

    private void updateSessionCtx(DmExecSessionDO sessionDO, RSocketSendDTO sendDTO) {
        StatusDTO status = this.sessionRService.getStatus(sendDTO, sessionDO.getSessionId());
        this.updateSessionCtx(sessionDO, status);
    }

    private void updateSessionCtx(DmExecSessionDO sessionDO, StatusDTO status) {
        if (sessionDO == null || status == null) {
            return;
        }
        SessionContextDTO ctx = sessionDO.toRdbCtx();
        ctx.setRdbCatalog(status.getCurCatalog());
        ctx.setRdbSchema(status.getCurSchema());
        ctx.setRdbAutoCommit(status.isAutoCommit());
        ctx.setRdbTxIsolation(status.getIsolation());
        ctx.setRdbReadOnly(status.isReadOnly());

        sessionDO.setConfig(JsonUtils.toJson(ctx));
        this.executionDal.sessionMapper().updateSessionConfig(sessionDO);
    }
}
