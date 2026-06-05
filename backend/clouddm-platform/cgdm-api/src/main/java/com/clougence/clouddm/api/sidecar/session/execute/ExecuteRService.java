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
