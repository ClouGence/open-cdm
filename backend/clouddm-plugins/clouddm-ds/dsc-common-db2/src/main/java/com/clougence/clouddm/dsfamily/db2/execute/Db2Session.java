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
package com.clougence.clouddm.dsfamily.db2.execute;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HashUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
@Slf4j
public class Db2Session extends DefaultRdbSession {

    private static final String CREATE_EXPLAIN_TABLES_SQL =
            "CALL SYSPROC.SYSINSTALLOBJECTS('EXPLAIN', 'C', NULL, NULL)";

    private static final String QUERY_EXPLAIN_SCHEMA_SQL = """
            SELECT TABSCHEMA
            FROM SYSCAT.TABLES
            WHERE TABSCHEMA IN (CURRENT USER, 'SYSTOOLS')
                AND TABNAME IN ('EXPLAIN_INSTANCE', 'EXPLAIN_STATEMENT', 'EXPLAIN_OPERATOR', 'EXPLAIN_STREAM')
            GROUP BY TABSCHEMA
            HAVING COUNT(DISTINCT TABNAME) = 4
            WITH UR
            """;

    private final String QUERY_EXPLAIN_SQL     = """
            SELECT O.OPERATOR_ID, O.OPERATOR_TYPE, X.OBJECT_SCHEMA, X.OBJECT_NAME, X.STREAM_COUNT, X.SOURCE_TYPE
            FROM %1$s.EXPLAIN_STATEMENT S
            JOIN %1$s.EXPLAIN_OPERATOR O
                ON S.EXPLAIN_REQUESTER = O.EXPLAIN_REQUESTER
                AND S.EXPLAIN_TIME     = O.EXPLAIN_TIME
                AND S.SOURCE_NAME      = O.SOURCE_NAME
                AND S.SOURCE_SCHEMA    = O.SOURCE_SCHEMA
                AND S.SOURCE_VERSION   = O.SOURCE_VERSION
                AND S.EXPLAIN_LEVEL    = O.EXPLAIN_LEVEL
                AND S.STMTNO           = O.STMTNO
                AND S.SECTNO           = O.SECTNO
            LEFT JOIN %1$s.EXPLAIN_STREAM X
                ON O.EXPLAIN_REQUESTER = X.EXPLAIN_REQUESTER
                AND O.EXPLAIN_TIME     = X.EXPLAIN_TIME
                AND O.SOURCE_NAME      = X.SOURCE_NAME
                AND O.SOURCE_SCHEMA    = X.SOURCE_SCHEMA
                AND O.SOURCE_VERSION   = X.SOURCE_VERSION
                AND O.EXPLAIN_LEVEL    = X.EXPLAIN_LEVEL
                AND O.STMTNO           = X.STMTNO
                AND O.SECTNO           = X.SECTNO
                AND O.OPERATOR_ID      = X.TARGET_ID
            WHERE S.QUERYNO      = ?
                AND S.EXPLAIN_LEVEL = 'P'
            ORDER BY O.OPERATOR_ID, X.STREAM_ID
            WITH UR
            """;

    private final String DELETE_EXPLAIN_RECODE = "DELETE FROM %1$s.EXPLAIN_INSTANCE I WHERE EXISTS (SELECT 1\n" + " FROM %1$s.EXPLAIN_STATEMENT S  WHERE S.EXPLAIN_TIME = I.EXPLAIN_TIME\n"
                                                 + "  AND S.SOURCE_NAME = I.SOURCE_NAME AND S.SOURCE_SCHEMA = I.SOURCE_SCHEMA\n"
                                                 + "  AND S.SOURCE_VERSION = I.SOURCE_VERSION AND QUERYNO = ?)";

    private Integer      currentExplainQueryNo;
    private String       currentExplainSchema;

    public Db2Session(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject, Db2Hooks sessionHook){
        super(newSessionId, dsConfig, dsObject, sessionHook);
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.beforeQueryRequest(beginTime, query, builder);

        String queryBody = query.getQueryBody();
        if (queryBody.trim().endsWith(";")) {
            int index = queryBody.lastIndexOf(";");
            queryBody = queryBody.substring(0, index);
            query.setQueryBody(queryBody);
        }
    }

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (query.isUseExplain()) {
            this.currentExplainQueryNo = HashUtils.fnvHash(query.getQueryId());
            try (PreparedStatement eps = (PreparedStatement) this.rdbHook().explainStatement(ps.getConnection(), query)) {
                try {
                    eps.execute();
                } catch (SQLException e) {
                    throw this.convertExplainTableError(e);
                }
            }
            PreparedStatement preparedStatement = (PreparedStatement) ps;
            preparedStatement.setInt(1, this.currentExplainQueryNo);
            return preparedStatement.execute();
        }

        //
        if (query.isUseCallable()) {
            CallableStatement call = (CallableStatement) ps;
            if (CollectionUtils.isNotEmpty(query.getQueryArgs())) {
                List<QueryArg> inParams = query.getQueryArgs().stream().filter(item -> !item.isOutParam()).collect(Collectors.toList());
                for (QueryArg inParam : inParams) {
                    call.registerOutParameter(inParam.getIndex(), inParam.getJdbcType());
                }
            }
            return call.execute();
        }
        return super.executeStatement(ps, query, builder);
    }

    @Override
    protected void afterQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        try {
            super.afterQueryRequest(beginTime, query, builder);
        } finally {
            this.cleanupExplainPlan();
        }
    }

    @Override
    protected void throwQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder, Exception e) {
        try {
            this.cleanupExplainPlan();
        } catch (SQLException cleanupError) {
            log.error("cleanup DB2 explain plan failed", cleanupError);
        }
        super.throwQueryRequest(beginTime, query, builder, e);
    }

    private void cleanupExplainPlan() throws SQLException {
        if (this.currentExplainQueryNo == null) {
            return;
        }
        String sql = DELETE_EXPLAIN_RECODE.formatted(this.currentExplainSchema);
        try (PreparedStatement dbStat = currentResource().prepareStatement(sql)) {
            dbStat.setInt(1, this.currentExplainQueryNo);
            dbStat.execute();
        } finally {
            this.currentExplainQueryNo = null;
            this.currentExplainSchema = null;
        }
    }

    private SQLException convertExplainTableError(SQLException e) {
        if (e.getErrorCode() != -219 || !"42704".equals(e.getSQLState())) {
            return e;
        }
        String msg = "DB2 Explain tables are not installed. Run: " + CREATE_EXPLAIN_TABLES_SQL;
        return new SQLException(msg, e.getSQLState(), e.getErrorCode(), e);
    }

    private String resolveExplainSchema(Connection conn) throws SQLException {
        String defaultSchema = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(QUERY_EXPLAIN_SCHEMA_SQL)) {
            while (rs.next()) {
                String schema = rs.getString(1);
                if (!"SYSTOOLS".equalsIgnoreCase(schema)) {
                    return '"' + schema.replace("\"", "\"\"") + '"';
                }
                defaultSchema = schema;
            }
        }
        if (defaultSchema != null) {
            return '"' + defaultSchema.replace("\"", "\"\"") + '"';
        }
        String msg = "DB2 Explain tables are not installed. Run: " + CREATE_EXPLAIN_TABLES_SQL;
        throw new SQLException(msg, "42704", -219);
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            this.currentExplainSchema = this.resolveExplainSchema(conn);
            String sql = QUERY_EXPLAIN_SQL.formatted(this.currentExplainSchema);
            PreparedStatement stmt = conn.prepareStatement(sql, java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        } else {
            return rdbHook().executeStatement(conn, query);
        }
    }

    @Override
    protected void doClose() {
        if (!this.isClose()) {
            this.setAutoCommit(true);
        }
        super.doClose();
    }
}
