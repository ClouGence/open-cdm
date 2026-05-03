package com.clougence.clouddm.dsfamily.db2.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;

/**
 * https://www.ibm.com/docs/zh/i/7.5?topic=statements-set-transaction
 * @author mode create time is 2021/1/12
 **/
public abstract class Db2Hooks implements SessionHook {

    @Override
    public ColReader createColReader() {
        return new Db2ColReader();
    }

    @Override
    public abstract Db2MetaService createMetaService(Session session);

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
        this.setReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("DB2 Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.execute("set schema " + schemaName);
        }
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
    public void setIsolation(Connection conn, RdbIsolation isolation) throws SQLException {
        if (isolation != null) {
            if (isolation == RdbIsolation.DEFAULT) {
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            } else {
                conn.setTransactionIsolation(isolation.getValue());
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
    public PreparedStatement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        PreparedStatement stmt;
        if (query.getResultConf().isReturnAutoIncrKey()) {
            stmt = conn.prepareStatement(query.getQueryBody(), Statement.RETURN_GENERATED_KEYS);
        } else {
            stmt = conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        }
        return stmt;
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        return null;
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        String schemaName = metaData.getCatalogName(columnIndex);
        String tableName = metaData.getTableName(columnIndex);
        String columnName = metaData.getColumnLabel(columnIndex);
        if (columnName == null || columnName.isEmpty()) {
            columnName = metaData.getColumnName(columnIndex);
        }

        int type = metaData.getColumnType(columnIndex);
        String columnTypeName = metaData.getColumnTypeName(columnIndex);

        ColMetaData colMetaData = new ColMetaData();
        colMetaData.setCatalog("");
        colMetaData.setSchema(schemaName);
        colMetaData.setTable(tableName);
        colMetaData.setColumn(columnName);
        colMetaData.setColumnType(columnTypeName.toLowerCase());
        JDBCType jdbcType = JDBCType.valueOf(type);
        //        colMetaData.setColumnType(MySQLTypes.valueOfCode(columnType));
        colMetaData.setJdbcType(jdbcType);
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }

    @Override
    public Statement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        String queryBody = query.getQueryBody();
        int pos = queryBody.length() - StringUtils.trimBlankStart(queryBody).length();
        StringBuilder explainBody = new StringBuilder(queryBody);
        explainBody.insert(pos, "EXPLAIN PLAN SET QUERYNO = " + Db2Session.STMT_NO + " FOR ");

        PreparedStatement stmt = conn.prepareStatement(explainBody.toString(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        return stmt;
    }
}
