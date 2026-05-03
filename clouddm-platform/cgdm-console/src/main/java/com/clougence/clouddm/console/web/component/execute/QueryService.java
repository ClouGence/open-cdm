package com.clougence.clouddm.console.web.component.execute;

import java.util.List;

import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.api.sidecar.session.execute.StatusDTO;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.dal.model.DmDsSessionDO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface QueryService {

    void testSessionWorker(String curUid, String sessionId);

    boolean hasSession(String curUid, String sessionId);

    DmDsSessionDO getSessionInfo(String curUid, String sessionId);

    String createSession(String curUid, DsLevels levels, SessionContextDTO context);

    void closeSession(String curUid, String sessionId);

    void commitSession(String curUid, String sessionId);

    void rollbackSession(String curUid, String sessionId);

    void cancelQuery(String curUid, String sessionId);

    void asyncExecuteQuery(String curUid, String sessionId, String batchId, List<QueryRequest> queryRequest);

    ResultList syncExecuteQuery(String curUid, String sessionId, QueryRequest queryRequest);

    boolean isExecuting(String curUid, String sessionId);

    boolean hasQueryResult(String curUid, String sessionId);

    ResultList fetchQueryResult(String curUid, String sessionId);

    void changeCatalog(String curUid, String sessionId, String catalog);

    void changeSchema(String curUid, String sessionId, String schema);

    void setAutoCommit(String curUid, String sessionId, boolean autoCommit);

    void setIsolation(String curUid, String sessionId, RdbIsolation isolation);

    void setReadOnly(String curUid, String sessionId, boolean readOnly);

    StatusDTO getAndUpdateStatus(String curUid, String sessionId);
}
