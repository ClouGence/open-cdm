package com.clougence.clouddm.ds.sqlserver.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.sqlserver.dialect.SqlServerDialect;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;
import com.microsoft.sqlserver.jdbc.SQLServerResultSetMetaData;

/**
 * https://learn.microsoft.com/en-us/sql/t-sql/statements/set-transaction-isolation-level-transact-sql?view=sql-server-ver16
 * @author mode create time is 2021/1/12
 **/
public class MsSqlHooks implements SessionHook {

    @Override
    public ColReader createColReader() {
        return new MsSqlColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new MsSqlMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
        this.setReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) throws SQLException {
        if (StringUtils.isNotBlank(catalogName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("use " + SqlServerDialect.INSTANCE.fmtName(true, catalogName));
            }
        }
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) {
        throw new UnsupportedOperationException("SQL SERVER Unsupported.");
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
        PreparedStatement stmt;
        if (query.getResultConf().isReturnAutoIncrKey()) {
            stmt = conn.prepareStatement(query.getQueryBody(), Statement.RETURN_GENERATED_KEYS);
        } else {
            stmt = conn.prepareStatement(query.getQueryBody(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        }
        stmt.setFetchSize(100);
        return stmt;
    }

    @Override
    public Statement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        return conn.createStatement();
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet resultSet = s.executeQuery("select @@SPID")) {
            return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("kill ?")) {
            ps.setString(1, queryID);
            ps.executeUpdate();
        }
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        SQLServerResultSetMetaData m = (SQLServerResultSetMetaData) metaData;
        String catalogName = m.getCatalogName(columnIndex);
        String schemaName = m.getSchemaName(columnIndex);
        String tableName = m.getTableName(columnIndex);
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
}
