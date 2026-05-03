package com.clougence.clouddm.worker.provider;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.execute.AsyncWaitResult;
import com.clougence.clouddm.api.sidecar.session.execute.ExecuteRService;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.api.sidecar.session.execute.StatusDTO;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.worker.component.resource.OnlineDsResourceManager;
import com.clougence.clouddm.worker.component.session.SessionAgent;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class SessionRServiceProvider implements ExecuteRService, UnifiedPostConstruct {

    @Resource
    private OnlineDsResourceManager onlineRM;
    @Resource
    private SessionManager          sessionManager;

    /* ---------------------------------------------------------------------------------- */
    /*  commons  */
    /* ---------------------------------------------------------------------------------- */

    @Override
    public void init() throws Exception {
    }

    @Override
    public void stop() {

    }

    protected SessionAgent getSessionById(String sessionId) {
        SessionAgent querySession = this.sessionManager.getSessionById(sessionId);
        if (querySession == null) {
            throw new RuntimeException("not found querySession -> " + sessionId);
        }
        return querySession;
    }

    @Override
    public boolean createSession(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, SessionContextDTO context) {
        String newSessionId = context.getSessionId();

        if (StringUtils.isBlank(newSessionId)) {
            throw new RuntimeException(newSessionId + " session is exist.");
        }
        try {
            Session agent = this.sessionManager.createSession(this.onlineRM, dsConfig, context);
            return agent != null;
        } catch (Exception e) {
            String msg = "createToolSession error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void closeSession(RSocketSendDTO sendDTO, String sessionId) {
        if (this.sessionManager.hasSessionById(sessionId)) {
            this.sessionManager.closeSessionById(sessionId);
        }
    }

    @Override
    public AsyncWaitResult asyncExecuteQuery(RSocketSendDTO sendDTO, String sessionId, String batchId, List<QueryRequest> queryRequest) {
        if (!this.sessionManager.hasSessionById(sessionId)) {
            throw new IllegalStateException("sessionId '" + sessionId + "' is not exist.");
        }

        SessionAgent agent = this.getSessionById(sessionId);
        return agent.submitQueries(batchId, queryRequest);
    }

    @Override
    public boolean isExecuting(RSocketSendDTO sendDTO, String sessionId) {
        if (this.hasSession(sendDTO, sessionId)) {
            return this.getSessionById(sessionId).isExecuting();
        } else {
            return false;
        }
    }

    @Override
    public boolean hasMoreQueryResult(RSocketSendDTO sendDTO, String sessionId) {
        SessionAgent agent = this.getSessionById(sessionId);
        return agent != null && agent.hasMore();
    }

    @Override
    public ResultList lastResultList(RSocketSendDTO sendDTO, String sessionId) {
        ResultList result = new ResultList();
        result.setSessionId(sessionId);
        SessionAgent agent = this.getSessionById(sessionId);
        if (agent != null) {
            result.setStatus(agent.getSessionStatus());
            result.setResultList(agent.popList());
        } else {
            result.setResultList(Collections.emptyList());
        }
        return result;
    }

    @Override
    public void cancelAllQuery(RSocketSendDTO sendDTO, String sessionId) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).cancel();
        }
    }

    @Override
    public boolean hasSession(RSocketSendDTO sendDTO, String sessionId) {
        return this.sessionManager.hasSessionById(sessionId);
    }

    /* ---------------------------------------------------------------------------------- */
    /*  rdb  */
    /* ---------------------------------------------------------------------------------- */

    @Override
    public void commitSession(RSocketSendDTO sendDTO, String sessionId) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).commit();
        }
    }

    @Override
    public void rollbackSession(RSocketSendDTO sendDTO, String sessionId) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).rollback();
        }
    }

    @Override
    public void setAutoCommit(RSocketSendDTO sendDTO, String sessionId, boolean autoCommit) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).setAutoCommit(autoCommit);
        }
    }

    @Override
    public void setIsolation(RSocketSendDTO sendDTO, String sessionId, RdbIsolation isolation) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).setIsolation(isolation);
        }
    }

    @Override
    public void setReadOnly(RSocketSendDTO sendDTO, String sessionId, boolean readOnly) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).setReadOnly(readOnly);
        }
    }

    @Override
    public StatusDTO getStatus(RSocketSendDTO sendDTO, String sessionId) {
        if (this.hasSession(sendDTO, sessionId)) {
            return this.getSessionById(sessionId).getSessionStatus();
        } else {
            return null;
        }
    }

    @Override
    public void setCurrentCatalog(RSocketSendDTO sendDTO, String sessionId, String catalog) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).setCurrentCatalog(catalog);
        }
    }

    @Override
    public void setCurrentSchema(RSocketSendDTO sendDTO, String sessionId, String schema) {
        if (this.hasSession(sendDTO, sessionId)) {
            this.getSessionById(sessionId).setCurrentSchema(schema);
        }
    }
}
