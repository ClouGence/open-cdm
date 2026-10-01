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
package com.clougence.clouddm.ds.hana.execute;

import java.sql.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.ds.hana.i18n.HanaConfigI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.dsfamily.execute.DefaultColReader;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class HanaHooks implements SessionHook {

    private String currentCatalog;

    @Override
    public ColReader createColReader() {
        return new DefaultColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new HanaMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        // Apply transaction settings before context queries can begin a transaction.
        this.setIsolation(resource, initContextDTO.getRdbTxIsolation());
        this.setAutoCommit(resource, initContextDTO.isRdbAutoCommit());
        this.setReadOnly(resource, initContextDTO.isRdbReadOnly());
        String catalog = getCurrentCatalog(resource);
        if (StringUtils.isNotBlank(initContextDTO.getRdbCatalog()) && !catalog.equals(initContextDTO.getRdbCatalog())) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_CATALOG_MISMATCH);
        }

        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }

        this.currentCatalog = catalog;
        initContextDTO.setRdbCatalog(catalog);
        initContextDTO.setRdbTxIsolation(this.getIsolation(resource));
        initContextDTO.setRdbAutoCommit(this.isAutoCommit(resource));
        initContextDTO.setRdbReadOnly(this.isReadOnly(resource));
        try (Statement statement = resource.createStatement(); ResultSet result = statement.executeQuery("SELECT CURRENT_SCHEMA FROM SYS.DUMMY")) {
            result.next();
            initContextDTO.setRdbSchema(result.getString(1));
        }
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_CATALOG_UNSUPPORTED);
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("SET SCHEMA " + HanaDialect.INSTANCE.fmtName(true, schemaName));
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
        if (isolation == null || isolation == RdbIsolation.DEFAULT) {
            isolation = RdbIsolation.READ_COMMITTED;
        }

        if (isolation == RdbIsolation.READ_UNCOMMITTED) {
            throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_ISOLATION_UNSUPPORTED);
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

    static String getCurrentCatalog(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement(); ResultSet result = statement.executeQuery("SELECT CURRENT_DATABASE() FROM SYS.DUMMY")) {
            result.next();
            return result.getString(1);
        }
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement(); ResultSet result = statement.executeQuery("SELECT CURRENT_CONNECTION FROM SYS.DUMMY")) {
            result.next();
            return result.getString(1);
        }
    }

    @Override
    public void killProcess(Connection connection, String queryID) throws SQLException {
        String sql = "ALTER SYSTEM CANCEL SESSION '" + Long.parseLong(queryID) + "'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    @Override
    public PreparedStatement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query.getQueryBody(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        try {
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        } catch (SQLException e) {
            try {
                stmt.close();
            } catch (SQLException closeError) {
                e.addSuppressed(closeError);
            }
            throw e;
        }
    }

    @Override
    public PreparedStatement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        if (!StringUtils.startsWithIgnoreCaseIgnoringLeadingWhitespace(query.getQueryBody(), "EXPLAIN ")) {
            throw new SQLException("Explain request does not contain an EXPLAIN statement");
        }

        return conn.prepareStatement(query.getQueryBody(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metaData, int columnIndex) throws SQLException {
        String schemaName = metaData.getSchemaName(columnIndex);
        String tableName = metaData.getTableName(columnIndex);
        String columnName = metaData.getColumnLabel(columnIndex);
        if (columnName == null || columnName.isEmpty()) {
            columnName = metaData.getColumnName(columnIndex);
        }

        int type = metaData.getColumnType(columnIndex);
        String columnTypeName = metaData.getColumnTypeName(columnIndex);

        ColMetaData colMetaData = new ColMetaData();
        colMetaData.setCatalog(this.currentCatalog);
        colMetaData.setSchema(schemaName);
        colMetaData.setTable(tableName);
        colMetaData.setColumn(columnName);
        colMetaData.setColumnType(columnTypeName.toLowerCase());
        JDBCType jdbcType = JDBCType.valueOf(type);
        colMetaData.setJdbcType(jdbcType);
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }
}
