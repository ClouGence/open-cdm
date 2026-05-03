package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.clickhouse.dialect.ClickHouseDialect;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * only for integration test
 *
 * @author Ekko 2022/11/03 16:48
 **/
@Slf4j
public class ChHooks implements SessionHook {

    private final boolean changeSchema;

    public ChHooks(DataSourceConfig config){
        this.changeSchema = new ChSupportSpi().supportChangeSchema(config) == RdbSupportLevel.Allow;
    }

    @Override
    public ColReader createColReader() {
        return new ChColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new ChMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema()) && this.changeSchema) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        //this.setCurrentAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        //this.setCurrentIsolation(resource, initContextDTO.getRdbTxIsolation());
        //this.setCurrentReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("ClickHouse Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (!this.changeSchema) {
            throw new UnsupportedOperationException("ClickHouse Driver Unsupported.");
        }

        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("use " + ClickHouseDialect.INSTANCE.fmtName(true, schemaName));
            } catch (SQLException e) {
                throw ChExceptionUtils.toException(e);
            }
        }
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) {
        throw new UnsupportedOperationException("ClickHouse Unsupported.");
    }

    @Override
    public boolean isAutoCommit(Connection conn) throws SQLException {
        return conn.getAutoCommit();
    }

    @Override
    public void commit(Connection conn) throws SQLException {

    }

    @Override
    public void rollback(Connection conn) throws SQLException {

    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) {
        throw new UnsupportedOperationException("ClickHouse Unsupported.");
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) throws SQLException {
        return RdbIsolation.valueOfCode(conn.getTransactionIsolation());
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        throw new UnsupportedOperationException("ClickHouse Unsupported.");
    }

    @Override
    public boolean isReadOnly(Connection conn) throws SQLException {
        return conn.isReadOnly();
    }

    @Override
    public PreparedStatement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            return stmt;
        } catch (SQLException e) {
            throw ChExceptionUtils.toException(e);
        }
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
        } catch (SQLException e) {
            throw ChExceptionUtils.toException(e);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        try {
            String sql = "kill query " + queryID;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw ChExceptionUtils.toException(e);
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
        colMetaData.setColumnType(columnTypeName);
        JDBCType jdbcType = JDBCType.valueOf(type);
        //        colMetaData.setColumnType(MySQLTypes.valueOfCode(columnType));
        colMetaData.setJdbcType(jdbcType);
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }

}
