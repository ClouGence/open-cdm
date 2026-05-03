package com.clougence.clouddm.sdk.execute.session;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

public interface SessionHook {

    void configSession(Connection resource, SessionContextDTO initContextDTO) throws Exception;

    DsMetaService createMetaService(Session session);

    void setCurrentCatalog(Connection conn, String catalogName) throws SQLException;

    void setCurrentSchema(Connection conn, String schemaName) throws SQLException;

    void setIsolation(Connection conn, RdbIsolation isolation) throws SQLException;

    RdbIsolation getIsolation(Connection conn) throws SQLException;

    void setReadOnly(Connection conn, boolean readOnly) throws SQLException;

    boolean isReadOnly(Connection conn) throws SQLException;

    void setAutoCommit(Connection conn, boolean autoCommit) throws SQLException;

    boolean isAutoCommit(Connection conn) throws SQLException;

    void commit(Connection conn) throws SQLException;

    void rollback(Connection conn) throws SQLException;

    ColReader createColReader();

    String getQueryID(Connection conn) throws SQLException;

    void killProcess(Connection con, String queryID) throws SQLException;

    Statement executeStatement(Connection conn, QueryRequest query) throws SQLException;

    Statement explainStatement(Connection conn, QueryRequest query) throws SQLException;

    ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException;
}
