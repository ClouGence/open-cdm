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

    private final String QUERY_EXPLAIN_SQL     = """
            SELECT O.OPERATOR_ID, O.OPERATOR_TYPE, X.OBJECT_SCHEMA, X.OBJECT_NAME, X.STREAM_COUNT, X.SOURCE_TYPE
            FROM EXPLAIN_STATEMENT S
            JOIN EXPLAIN_OPERATOR O
                ON S.EXPLAIN_REQUESTER = O.EXPLAIN_REQUESTER
                AND S.EXPLAIN_TIME     = O.EXPLAIN_TIME
                AND S.SOURCE_NAME      = O.SOURCE_NAME
                AND S.SOURCE_SCHEMA    = O.SOURCE_SCHEMA
                AND S.SOURCE_VERSION   = O.SOURCE_VERSION
                AND S.EXPLAIN_LEVEL    = O.EXPLAIN_LEVEL
                AND S.STMTNO           = O.STMTNO
                AND S.SECTNO           = O.SECTNO
            LEFT JOIN EXPLAIN_STREAM X
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

    private final String DELETE_EXPLAIN_RECODE = "DELETE FROM EXPLAIN_INSTANCE I WHERE EXISTS (SELECT 1\n" + " FROM EXPLAIN_STATEMENT S  WHERE S.EXPLAIN_TIME = I.EXPLAIN_TIME\n"
                                                 + "  AND S.SOURCE_NAME = I.SOURCE_NAME AND S.SOURCE_SCHEMA = I.SOURCE_SCHEMA\n"
                                                 + "  AND S.SOURCE_VERSION = I.SOURCE_VERSION AND QUERYNO = ?)";

    private Integer      currentExplainQueryNo;

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
                eps.execute();
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
        try (PreparedStatement dbStat = currentResource().prepareStatement(DELETE_EXPLAIN_RECODE)) {
            dbStat.setInt(1, this.currentExplainQueryNo);
            dbStat.execute();
        } finally {
            this.currentExplainQueryNo = null;
        }
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            PreparedStatement stmt = conn.prepareStatement(QUERY_EXPLAIN_SQL, java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
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
