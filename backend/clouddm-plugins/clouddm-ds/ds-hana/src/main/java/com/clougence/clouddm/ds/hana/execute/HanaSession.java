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

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;
import com.clougence.utils.HashUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2022/3/28 19:25:30
 */
@Slf4j
public class HanaSession extends DefaultRdbSession {

    private static final String QUERY_EXPLAIN_SQL  = """
            SELECT OPERATOR_ID, PARENT_OPERATOR_ID, OPERATOR_NAME, EXECUTION_ENGINE,
                   TABLE_NAME, OUTPUT_SIZE, SUBTREE_COST, OPERATOR_DETAILS
            FROM EXPLAIN_PLAN_TABLE
            WHERE STATEMENT_NAME = ?
            ORDER BY OPERATOR_ID
            """;
    private static final String DELETE_EXPLAIN_SQL = "DELETE FROM EXPLAIN_PLAN_TABLE WHERE STATEMENT_NAME = ?";
    private String              currentExplainId;

    public HanaSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new HanaHooks());
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

    // hana not support getLargeUpdateCount
    @Override
    public long getUpdateCount(Statement ps) throws SQLException {
        return ps.getUpdateCount();
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        if (!query.isUseExplain()) {
            return super.createStatement(conn, query);
        }
        query.setUsingValueProcess(false);
        PreparedStatement stmt = conn.prepareStatement(QUERY_EXPLAIN_SQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        return stmt;
    }

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (!query.isUseExplain()) {
            return super.executeStatement(ps, query, builder);
        }
        this.currentExplainId = "DM_DML_EXPLAIN_" + HashUtils.fnvHash(query.getQueryId());
        try (PreparedStatement stmt = ps.getConnection().prepareStatement(DELETE_EXPLAIN_SQL)) {
            stmt.setString(1, this.currentExplainId);
            stmt.executeUpdate();
        }
        try (PreparedStatement explain = (PreparedStatement) this.rdbHook().explainStatement(ps.getConnection(), query)) {
            super.applyArgs(query, explain);
            explain.execute();
        }
        ((PreparedStatement) ps).setString(1, this.currentExplainId);
        return ((PreparedStatement) ps).execute();
    }

    @Override
    protected void applyArgs(QueryRequest query, Statement statement) throws SQLException {
        if (!query.isUseExplain()) {
            super.applyArgs(query, statement);
        }
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
            log.error("cleanup HANA explain plan failed", cleanupError);
        }
        super.throwQueryRequest(beginTime, query, builder, e);
    }

    private void cleanupExplainPlan() throws SQLException {
        if (this.currentExplainId == null) {
            return;
        }
        try (PreparedStatement stmt = this.currentResource().prepareStatement(DELETE_EXPLAIN_SQL)) {
            stmt.setString(1, this.currentExplainId);
            stmt.executeUpdate();
        } finally {
            this.currentExplainId = null;
        }
    }
}
