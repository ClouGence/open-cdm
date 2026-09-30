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
package com.clougence.clouddm.ds.kafka.execute;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaConnection;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.drivers.adapter.AdapterConnManager;
import com.clougence.drivers.adapter.AdapterConnection;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

public class KafkaHooks implements SessionHook {

    @Override
    public ColReader createColReader() {
        return new KafkaColReader();
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new KafkaMetaService(session);
    }

    @Override
    public void configSession(Connection resource, SessionContextDTO initContextDTO) throws SQLException {
        if (StringUtils.isNotBlank(initContextDTO.getRdbSchema())) {
            this.setCurrentSchema(resource, initContextDTO.getRdbSchema());
        }
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalogName) {
        throw new UnsupportedOperationException("Kafka Unsupported.");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schemaName) throws SQLException {
        if (StringUtils.isNotBlank(schemaName)) {
            conn.setSchema(schemaName);
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
                conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
            } else {
                throw new UnsupportedOperationException("Kafka Unsupported.");
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
        PreparedStatement stmt = conn.prepareStatement(query.getQueryBody(), java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        return stmt;
    }

    @Override
    public Statement explainStatement(Connection conn, QueryRequest query) throws SQLException {
        return conn.createStatement();
    }

    @Override
    public String getQueryID(Connection conn) throws SQLException {
        AdapterConnection adapterConn = conn.unwrap(AdapterConnection.class);
        if (adapterConn == null) {
            throw new SQLException("failed to unwrap AdapterConnection from " + conn.getClass().getName());
        }
        return adapterConn.getObjectId();
    }

    @Override
    public void killProcess(Connection conn, String connID) throws SQLException {
        AdapterConnection target = AdapterConnManager.getConnection(connID);
        if (target instanceof KafkaConnection) {
            ((KafkaConnection) target).killDriverConnection(connID);
            return;
        }
        if (target != null) {
            try {
                target.close();
            } catch (Throwable e) {
                Throwable root = ExceptionUtils.getRootCause(e);
                if (root instanceof SQLException) {
                    throw (SQLException) root;
                }
                throw new SQLException(e);
            }
        }
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
        colMetaData.setColumnType(columnTypeName == null ? "" : columnTypeName.toLowerCase());
        try {
            colMetaData.setJdbcType(JDBCType.valueOf(type));
        } catch (Exception e) {
            colMetaData.setJdbcType(JDBCType.OTHER);
        }
        colMetaData.setIndex(columnIndex);
        return colMetaData;
    }
}
