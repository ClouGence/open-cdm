package com.clougence.clouddm.ds.mongodb.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class MongoHooks implements SessionHook {

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) throws SQLException {
        throw new UnsupportedOperationException("MongoDB Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        conn.setSchema(schemaName);
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) throws SQLException {
        if (isolation != null) {
            if (isolation == RdbIsolation.DEFAULT) {
                conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
            } else {
                throw new UnsupportedOperationException("MongoDB Unsupported.");
            }
        }
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        return RdbIsolation.valueOfCode(conn.getTransactionIsolation());
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        return conn.isReadOnly();
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    @Override
    public boolean isAutoCommit(Connection conn) throws SQLException {
        return conn.getAutoCommit();
    }

    @Override
    public void commit(Connection conn) throws SQLException {
        conn.commit();
    }

    @Override
    public void rollback(Connection conn) throws SQLException {
        conn.rollback();
    }

    @Override
    public ColReader createColReader() {
        return new MongoColReader();
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        return null;
    }

    @Override
    public void killProcess(Connection con, String queryID) throws SQLException {
        throw new UnsupportedOperationException("MongoDB Unsupported.");
    }

    @Override
    public Statement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        PreparedStatement stmt;
        stmt = conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        return stmt;
    }

    @Override
    public Statement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        return conn.createStatement();
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        String catalogName = metaData.getCatalogName(columnIndex);
        String schemaName = metaData.getSchemaName(columnIndex);
        String tableName = metaData.getTableName(columnIndex);
        String columnName = metaData.getColumnLabel(columnIndex);
        if (columnName == null || columnName.isEmpty()) {
            columnName = metaData.getColumnName(columnIndex);
        }

        int type = metaData.getColumnType(columnIndex);
        String columnTypeName = metaData.getColumnTypeName(columnIndex);

        ColMetaData colMetaData = new ColMetaData();
        colMetaData.setCatalog(catalogName);
        colMetaData.setSchema(schemaName);
        colMetaData.setTable(tableName);
        colMetaData.setColumn(columnName);
        colMetaData.setColumnType(columnTypeName.toLowerCase());
        try {
            colMetaData.setJdbcType(JDBCType.valueOf(type));
        } catch (Exception e) {
            colMetaData.setJdbcType(JDBCType.OTHER); // JDBCType has not this type
        }
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws Exception {
        setCurrentSchema(resource, initContextDTO.getRdbSchema());
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new MongoMetaService(session);
    }
}
