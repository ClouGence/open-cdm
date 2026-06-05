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
package com.clougence.clouddm.sdk.execute.session;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

public interface Session extends AutoCloseable {

    String getSessionId();

    long getLastQueryTime();

    DsMetaService getMetaService();

    DataSourceConfig getDsConfig();

    void cancel();

    boolean isExecuting();

    void executeQuery(QueryRequest query, ResultBuilder rb);

    <V> V executeQuery(SessionCallback<V> callback) throws Exception;

    void addCloseListener(SessionCloseListener closeListener);

    //

    String getCurrentQueryId();

    void commit();

    void rollback();

    boolean isAutoCommit();

    void setAutoCommit(boolean autoCommit);

    RdbIsolation getIsolation();

    void setIsolation(RdbIsolation isolation);

    boolean isReadOnly();

    void setReadOnly(boolean readOnly);

    boolean hasUnCommitted();

    void setCurrentCatalog(String catalog);

    void setCurrentSchema(String schemaName);

}
