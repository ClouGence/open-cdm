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
