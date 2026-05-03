package com.clougence.clouddm.dsfamily.mysql.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleRowMapper;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class MyHooks implements SessionHook {

    private String mainVersion;

    public MyHooks(String mainVersion){
        this.mainVersion = mainVersion;
    }

    @Override
    public ColReader createColReader() {
        return new MyColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new MyMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
        //this.setReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) throws SQLException {
        throw new UnsupportedOperationException("MySQL Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.execute("use " + MySqlDialect.INSTANCE.fmtName(true, schemaName));
            }
        }
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) throws SQLException {
        try (Statement s = conn.createStatement()) {
            if (autoCommit) {
                s.execute("set autocommit = on");
            } else {
                s.execute("set autocommit = off");
            }
        }
    }

    @Override
    public boolean isAutoCommit(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("show variables like 'autocommit'")) {
            String status = ((SingleRowMapper<String>) r -> r.getString(2)).mapRow(rs);
            return StringUtils.equalsIgnoreCase(status, "on") || StringUtils.equalsIgnoreCase(status, "1");
        }
    }

    @Override
    public void commit(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.execute("commit");
        }
    }

    @Override
    public void rollback(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.execute("rollback");
        }
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) throws SQLException {
        if (isolation != null) {
            try (Statement s = conn.createStatement()) {
                switch (isolation) {
                    case DEFAULT:
                    case REPEATABLE_READ:
                        s.execute("set session transaction isolation level repeatable read");
                        break;
                    case READ_COMMITTED:
                        s.execute("set session transaction isolation level read committed");
                        break;
                    case READ_UNCOMMITTED:
                        s.execute("set session transaction isolation level read uncommitted");
                        break;
                    case SERIALIZABLE:
                        s.execute("set session transaction isolation level serializable");
                        break;
                }
            }
        }
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        if (StringUtils.isBlank(this.mainVersion)) {
            synchronized (this) {
                if (StringUtils.isBlank(this.mainVersion)) {
                    this.mainVersion = new MyMetaProviderDm(conn).getVersion();
                }
            }
        }

        try (Statement s = conn.createStatement()) {
            try (ResultSet rs = s.executeQuery("select @@transaction_isolation")) {
                return passerIsolation(((SingleRowMapper<String>) r -> r.getString(1)).mapRow(rs));
            } catch (Exception e) {
                if (e.getMessage().contains("Unknown system variable 'transaction_isolation'")) {
                    try (ResultSet rs = s.executeQuery("select @@tx_isolation")) {
                        return passerIsolation(((SingleRowMapper<String>) r -> r.getString(1)).mapRow(rs));
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    protected RdbIsolation passerIsolation(String isolation) {
        if (StringUtils.equalsIgnoreCase(isolation, "READ-UNCOMMITTED")) {
            return RdbIsolation.READ_UNCOMMITTED;
        } else if (StringUtils.equalsIgnoreCase(isolation, "READ-COMMITTED")) {
            return RdbIsolation.READ_COMMITTED;
        } else if (StringUtils.equalsIgnoreCase(isolation, "REPEATABLE-READ")) {
            return RdbIsolation.REPEATABLE_READ;
        } else if (StringUtils.equalsIgnoreCase(isolation, "SERIALIZABLE")) {
            return RdbIsolation.SERIALIZABLE;
        } else {
            // TODO need print Warnings to console.
            return null;
        }
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        throw new UnsupportedOperationException("MySQL Unsupported.");
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("show variables like 'read_only'")) {
            String status = ((SingleRowMapper<String>) r -> r.getString(2)).mapRow(rs);
            return StringUtils.equalsIgnoreCase(status, "on") || StringUtils.equalsIgnoreCase(status, "1");
        }
    }

    @Override
    public PreparedStatement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        PreparedStatement stmt;
        if (query.getResultConf().isReturnAutoIncrKey()) {
            stmt = conn.prepareStatement(query.getQueryBody(), Statement.RETURN_GENERATED_KEYS);
        } else {
            stmt = conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        }
        stmt.setFetchSize(Integer.MIN_VALUE);
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
        try (Statement s = conn.createStatement(); ResultSet resultSet = s.executeQuery("select connection_id()")) {
            return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("kill query ?")) {
            ps.setString(1, queryID);
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
        JDBCType jdbcType = JDBCType.valueOf(type);
        //        colMetaData.setColumnType(MySQLTypes.valueOfCode(columnType));
        colMetaData.setJdbcType(jdbcType);
        colMetaData.setIndex(columnIndex);
        colMetaData.setPrecision(metaData.getPrecision(columnIndex));
        return colMetaData;
    }
}
