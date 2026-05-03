package com.clougence.clouddm.dsfamily.postgres.execute;

import java.sql.*;

import org.postgresql.jdbc.PgResultSetMetaData;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class PgHooks implements SessionHook {

    @Override
    public ColReader createColReader() {
        return new PgColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new PgMetaService(session);
    }

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
        throw new UnsupportedOperationException("PostgreSQL Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("set search_path = \"" + schemaName + "\"");
            }
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
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
        PreparedStatement stmt = conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        return stmt;
    }

    @Override
    public PreparedStatement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        String queryBody = query.getQueryBody();
        int pos = queryBody.length() - StringUtils.trimBlankStart(queryBody).length();
        StringBuilder explainBody = new StringBuilder(queryBody);
        explainBody.insert(pos, "explain ");

        PreparedStatement stmt = conn.prepareStatement(explainBody.toString(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        return stmt;
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet resultSet = s.executeQuery("select pg_backend_pid()")) {
            return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        String res;
        try {
            String sqlQuery = "select pg_cancel_backend(" + queryID + ")";
            try (Statement s = connection.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
                res = ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
            }
        } catch (Exception e) {
            String sqlQuery = "select pg_terminate_backend(" + queryID + ")";
            try (Statement s = connection.createStatement(); ResultSet resultSet = s.executeQuery(sqlQuery)) {
                res = ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
            }
        }
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        PgResultSetMetaData pgResultSetMetaData = (PgResultSetMetaData) metaData;
        String catalogName = pgResultSetMetaData.getCatalogName(columnIndex);
        String schemaName = pgResultSetMetaData.getBaseSchemaName(columnIndex);
        String tableName = pgResultSetMetaData.getBaseTableName(columnIndex);
        String columnName = metaData.getColumnLabel(columnIndex);
        if (columnName == null || columnName.isEmpty()) {
            columnName = metaData.getColumnName(columnIndex);
        }

        //String columnName = pgResultSetMetaData.getBaseColumnName(columnIndex);
        int type = metaData.getColumnType(columnIndex);
        String columnTypeName = metaData.getColumnTypeName(columnIndex);
        //        String columnType = pgResultSetMetaData.getColumnTypeName(columnIndex);

        ColMetaData colMetaData = new ColMetaData();
        colMetaData.setCatalog(catalogName);
        colMetaData.setSchema(schemaName);
        colMetaData.setTable(tableName);
        colMetaData.setColumn(columnName);
        //        colMetaData.setColumnType(PostgresTypes.valueOfCode(columnType));
        colMetaData.setColumnType(columnTypeName.toLowerCase());
        JDBCType jdbcType = JDBCType.valueOf(type);
        colMetaData.setJdbcType(jdbcType);
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }

}
