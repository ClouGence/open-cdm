package com.clougence.clouddm.ds.oracle.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.oracle.constr.OracleMainVersion;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class OraHooks implements SessionHook {

    private final boolean changeCatalog;

    public OraHooks(DataSourceConfig config){
        this.changeCatalog = new OraSupportSpi().supportChangeCatalog(config) == RdbSupportLevel.Allow;
    }

    @Override
    public ColReader createColReader() {
        return new OraColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new OraMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema()) && this.changeCatalog) {
            this.setCurrentCatalog(resource, initContextDTO.getRdbSchema());
        }
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
        this.setReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) throws SQLException {
        if (StringUtils.isNotBlank(catalogName)) {
            try (Statement s = conn.createStatement()) {
                s.executeQuery("alter session set container = " + catalogName);
            }
        }
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeQuery("alter session set current_schema = " + schemaName);
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
                // oracle only support READ_COMMITTED 和 SERIALIZABLE
                conn.setTransactionIsolation(isolation.getValue());
            }
        }
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        return RdbIsolation.valueOfCode(conn.getTransactionIsolation());
    }

    @Override
    public void setReadOnly(Connection conn, boolean autoCommit) throws SQLException {
        conn.setReadOnly(autoCommit);
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        return conn.isReadOnly();
    }

    @Override
    public Statement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseCallable()) {
            CallableStatement stmt = conn.prepareCall(query.getQueryBody());
            stmt.setFetchSize(200);
            return stmt;
        } else if (CollectionUtils.isNotEmpty(query.getQueryArgs())) {
            PreparedStatement stmt = conn.prepareStatement(query.getQueryBody(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        } else {
            Statement stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        }
    }

    @Override
    public PreparedStatement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        String queryBody = query.getQueryBody();
        int pos = queryBody.length() - StringUtils.trimBlankStart(queryBody).length();
        StringBuilder explainBody = new StringBuilder(queryBody);
        explainBody.insert(pos, "explain plan for ");

        PreparedStatement stmt = conn.prepareStatement(explainBody.toString(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        return stmt;
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet resultSet = s.executeQuery("select userenv('SID') from dual")) {
            return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        String serial = null;
        try (PreparedStatement ps = connection.prepareStatement("select sid, serial#, sql_id from v$session where status = 'ACTIVE' and sid = ?")) {
            ps.setString(1, queryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    serial = rs.getString("SERIAL#");
                }
            }
        }

        if (StringUtils.isBlank(serial)) {
            throw new SQLException("serial is empty");
        }

        //https://oracle-base.com/articles/misc/killing-oracle-sessions#cancel-sql
        OracleMainVersion mainVersion = OracleMainVersion.getMainVersion(new OraMetaProviderDm(connection).getVersion());
        String killSql;
        if (mainVersion.isGe(OracleMainVersion.Oracle_18c)) {
            killSql = "alter system disconnect session '" + String.format("%s,%s", queryID, serial) + "' immediate";
        } else {
            //killSql = "alter system cancel sql '" + String.format("%s,%s", queryID, sqlId) + "' immediate";
            killSql = "alter system disconnect session '" + String.format("%s,%s", queryID, serial) + "' immediate";
        }

        try (PreparedStatement ps = connection.prepareStatement(killSql)) {
            ps.execute();
        }
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
        try {
            colMetaData.setJdbcType(JDBCType.valueOf(type));
        } catch (Exception e) {
            colMetaData.setJdbcType(JDBCType.OTHER); // JDBCType has not this type
        }
        //        colMetaData.setColumnType(TiDBTypes.valueOfCode(columnType));
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }
}
