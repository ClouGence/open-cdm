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
package com.clougence.clouddm.ds.kingbasees.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.kingbasees.execute.postgresql.KingbaseESPostgreSQLMetaService;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;
import com.kingbase8.KBResultSetMetaData;

public class KingbaseESHooks implements SessionHook {

    @Override
    public ColReader createColReader() {
        return new KingbaseESColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new KingbaseESPostgreSQLMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }
        setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        setIsolation(resource, initContextDTO.getRdbTxIsolation());
        setReadOnly(resource, initContextDTO.isRdbReadOnly());
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("KingbaseES does not switch databases in an existing connection.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isBlank(schemaName)) {
            return;
        }
        String escapedSchemaName = schemaName.replace("\"", "\"\"");
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("SET search_path = \"" + escapedSchemaName + "\"");
        }
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) throws SQLException {
        if (isolation == null) {
            return;
        }
        if (isolation == RdbIsolation.DEFAULT) {
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return;
        }
        conn.setTransactionIsolation(isolation.getValue());
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
    public String getQueryID(Connection conn) throws SQLException {
        return getQueryId(conn);
    }

    public static String getQueryId(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT sys_catalog.sys_backend_pid()")) {
            return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        cancelQuery(connection, queryID);
    }

    public static void cancelQuery(Connection connection, String queryID) throws SQLException {
        if (StringUtils.isBlank(queryID) || !queryID.chars().allMatch(Character::isDigit)) {
            throw new SQLException("Invalid KingbaseES query ID.");
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT sys_catalog.sys_cancel_backend(?)")) {
            statement.setInt(1, Integer.parseInt(queryID));
            try (ResultSet ignored = statement.executeQuery()) {
                // Closing the result set completes the cancellation command lifecycle.
            }
        }
    }

    @Override
    public PreparedStatement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(200);
        return statement;
    }

    @Override
    public PreparedStatement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        String queryBody = query.getQueryBody();
        int position = queryBody.length() - StringUtils.trimBlankStart(queryBody).length();
        StringBuilder explainBody = new StringBuilder(queryBody);
        explainBody.insert(position, "EXPLAIN ");
        PreparedStatement statement = conn.prepareStatement(explainBody.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(200);
        statement.setFetchDirection(ResultSet.FETCH_FORWARD);
        return statement;
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        String schemaName = metaData.getSchemaName(columnIndex);
        String tableName = metaData.getTableName(columnIndex);
        if (metaData instanceof KBResultSetMetaData kingbaseESMetaData) {
            schemaName = kingbaseESMetaData.getBaseSchemaName(columnIndex);
            tableName = kingbaseESMetaData.getBaseTableName(columnIndex);
        }

        String columnName = metaData.getColumnLabel(columnIndex);
        if (StringUtils.isBlank(columnName)) {
            columnName = metaData.getColumnName(columnIndex);
        }
        int type = metaData.getColumnType(columnIndex);
        JDBCType jdbcType;
        try {
            jdbcType = JDBCType.valueOf(type);
        } catch (IllegalArgumentException e) {
            jdbcType = JDBCType.OTHER;
        }

        ColMetaData colMetaData = new ColMetaData();
        colMetaData.setCatalog(metaData.getCatalogName(columnIndex));
        colMetaData.setSchema(schemaName);
        colMetaData.setTable(tableName);
        colMetaData.setColumn(columnName);
        colMetaData.setColumnType(StringUtils.defaultString(metaData.getColumnTypeName(columnIndex), "").toLowerCase());
        colMetaData.setJdbcType(jdbcType);
        colMetaData.setPrecision(metaData.getPrecision(columnIndex));
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }
}
