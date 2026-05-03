package com.clougence.clouddm.api.sidecar.session.execute;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * @author mode 2021/1/14 14:10
 */
@RSocketApiClass
public interface ExecuteRService {

    boolean createSession(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, SessionContextDTO context);

    void closeSession(RSocketSendDTO sendDTO, String sessionId);

    AsyncWaitResult asyncExecuteQuery(RSocketSendDTO sendDTO, String sessionId, String batchId, List<QueryRequest> queryRequest);

    @Deprecated
    boolean isExecuting(RSocketSendDTO sendDTO, String sessionId);

    @Deprecated
    boolean hasMoreQueryResult(RSocketSendDTO sendDTO, String sessionId);

    @Deprecated
    ResultList lastResultList(RSocketSendDTO sendDTO, String sessionId);

    void cancelAllQuery(RSocketSendDTO sendDTO, String sessionId);

    boolean hasSession(RSocketSendDTO sendDTO, String sessionId);

    void commitSession(RSocketSendDTO sendDTO, String sessionId);

    void rollbackSession(RSocketSendDTO sendDTO, String sessionId);

    void setAutoCommit(RSocketSendDTO sendDTO, String sessionId, boolean autoCommit);

    void setIsolation(RSocketSendDTO sendDTO, String sessionId, RdbIsolation isolation);

    void setReadOnly(RSocketSendDTO sendDTO, String sessionId, boolean readOnly);

    void setCurrentCatalog(RSocketSendDTO sendDTO, String sessionId, String catalog);

    void setCurrentSchema(RSocketSendDTO sendDTO, String sessionId, String schema);

    StatusDTO getStatus(RSocketSendDTO sendDTO, String sessionId);
}
