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
package com.clougence.clouddm.ds.iotdb.execute;

import java.sql.*;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

final class IotdbSessionHook implements SessionHook {

    private final IotdbJdbcSession jdbc;
    private final Connection       connection;

    IotdbSessionHook(IotdbJdbcSession jdbc, Connection connection){
        this.jdbc = jdbc;
        this.connection = connection;
    }

    @Override
    public void configSession(Connection conn, SessionContextDTO context) {
        setAutoCommit(conn, context.isRdbAutoCommit());
        setReadOnly(conn, context.isRdbReadOnly());
        setIsolation(conn, context.getRdbTxIsolation());
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new IotdbSessionMetaService(session, jdbc, connection);
    }

    @Override
    public void setCurrentCatalog(Connection conn, String catalog) {
        throw unsupported("catalog");
    }

    @Override
    public void setCurrentSchema(Connection conn, String schema) {
        throw unsupported("schema");
    }

    @Override
    public void setIsolation(Connection conn, RdbIsolation isolation) {
        if (isolation != null && isolation != RdbIsolation.DEFAULT) {
            throw unsupported("isolation");
        }
    }

    @Override
    public RdbIsolation getIsolation(Connection conn) {
        return RdbIsolation.DEFAULT;
    }

    @Override
    public void setReadOnly(Connection conn, boolean readOnly) {
        if (readOnly) {
            throw unsupported("read-only");
        }
    }

    @Override
    public boolean isReadOnly(Connection conn) {
        return false;
    }

    @Override
    public void setAutoCommit(Connection conn, boolean autoCommit) {
        if (!autoCommit) {
            throw unsupported("transactions");
        }
    }

    @Override
    public boolean isAutoCommit(Connection conn) {
        return true;
    }

    @Override
    public void commit(Connection conn) {
        throw unsupported("commit");
    }

    @Override
    public void rollback(Connection conn) {
        throw unsupported("rollback");
    }

    @Override
    public ColReader createColReader() {
        return new IotdbColReader();
    }

    @Override
    public String getQueryID(Connection conn) {
        return null;
    }

    @Override
    public void killProcess(Connection conn, String queryId) {
        throw unsupported("kill process");
    }

    @Override
    public Statement executeStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseCallable() || (query.getQueryArgs() != null && !query.getQueryArgs().isEmpty())) {
            throw unsupported("statement arguments");
        }
        if (query.getRequester() == Requester.CONSOLE) {
            verifyQueryScope(conn, query);
        }
        Statement statement = conn.createStatement();
        long pageSize = query.getResultConf().getFetchPageSize();
        if (pageSize > 0) {
            try {
                statement.setFetchSize((int) Math.min(pageSize, statement.getFetchSize()));
            } catch (SQLException e) {
                try {
                    statement.close();
                } catch (SQLException closeError) {
                    e.addSuppressed(closeError);
                }
                throw e;
            }
        }
        return statement;
    }

    private void verifyQueryScope(Connection conn, QueryRequest query) throws SQLException {
        var scope = query.getResolvedResources();
        IotdbSqlDialect model = jdbc.dialect();
        if (scope == null || !List.of(model.name()).equals(scope.get(TargetType.Catalog))) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_QUERY_SCOPE_MISMATCH);
        }
        if (model == IotdbSqlDialect.TABLE) {
            String database = conn.getSchema();
            List<String> actual = List.of();
            if (database != null && !database.isBlank()) {
                actual = List.of(database);
            }
            if (!actual.equals(scope.getOrDefault(TargetType.Schema, List.of()))) {
                throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_QUERY_SCOPE_MISMATCH);
            }
        }
        // Recheck team targets on the execution connection in case they changed while the request was queued.
        for (TargetType type : List.of(TargetType.Table, TargetType.Timeseries)) {
            for (String path : scope.getOrDefault(type, List.of())) {
                String[] parts = path.split("/");
                if (parts.length != 4 || !model.name().equals(parts[1])) {
                    throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_QUERY_SCOPE_MISMATCH);
                }
                verifyBaseObject(conn, model, parts[2], parts[3]);
            }
        }
    }

    private void verifyBaseObject(Connection conn, IotdbSqlDialect model, String database, String name) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            Value value;
            boolean base;
            if (model == IotdbSqlDialect.TREE) {
                value = IotdbTreeMetadata.detail(statement, database, UmiTypes.Timeseries, name);
                base = value != null && name.equals(value.asValue()) && "BASE".equals(value.getAttributes().get("viewtype"));
            } else {
                value = IotdbTableMetadata.detail(statement, database, null, name);
                base = value instanceof RdbTable table && table.getUmiType() == UmiTypes.Table && name.equals(table.getName()) && "BASE TABLE".equals(table.getTableType());
            }
            if (!base) {
                throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_QUERY_SCOPE_MISMATCH);
            }
        }
    }

    @Override
    public Statement explainStatement(Connection conn, QueryRequest query) {
        throw unsupported("explain request");
    }

    @Override
    public ColMetaData getColumnMetaData(QueryRequest query, ResultSetMetaData metadata, int index) throws SQLException {
        ColMetaData column = new ColMetaData();
        column.setColumn(metadata.getColumnLabel(index));
        column.setIndex(index);
        column.setColumnType(metadata.getColumnTypeName(index));
        column.setJdbcType(JDBCType.valueOf(metadata.getColumnType(index)));
        return column;
    }

    static ThirdPartyApiException unsupported(String operation) {
        return ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_OPERATION_UNSUPPORTED, operation);
    }
}
