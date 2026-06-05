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
package com.clougence.clouddm.ds.oracle.execute;

import java.io.IOException;
import java.sql.*;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.clouddm.sdk.execute.session.rdb.KillCurrentQueryAble;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2022/3/28 19:25:30
 */
@Slf4j
public class OraSession extends DefaultRdbSession {

    private static final String explainSql       = "SELECT plan_table_output FROM TABLE(DBMS_XPLAN.DISPLAY())";
    private static final String compileResultSql = "select line,position,text,sequence from all_errors where owner= ? and name= ?  order by SEQUENCE";

    public OraSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new OraHooks(dsConfig));
    }

    public OraSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject, SessionHook sessionHook){
        super(newSessionId, dsConfig, dsObject, sessionHook);
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.beforeQueryRequest(beginTime, query, builder);

        String queryBody = query.getQueryBody();
        if (query.isUseCallable()) {
            return;
        }

        //end  need ;
        if (queryBody.trim().endsWith(";") && !queryBody.toLowerCase().endsWith("end;")) {
            int index = queryBody.lastIndexOf(";");
            queryBody = queryBody.substring(0, index);
            query.setQueryBody(queryBody);
        }
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            PreparedStatement stmt = conn.prepareStatement(explainSql, java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        } else if (query.isUseCompile()) {
            query.setUsingValueProcess(false);
            PreparedStatement stmt = conn.prepareStatement(compileResultSql, java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        } else {
            return rdbHook().executeStatement(conn, query);
        }
    }

    @Override
    protected void handleRs(QueryRequest query, boolean retVal, Statement ps, ResultBuilder builder, KillCurrentQueryAble killAble) throws SQLException, IOException {
        if (query.isUseCompile()) {
            compileRs(query, ps, builder);
        } else {
            super.handleRs(query, retVal, ps, builder, killAble);
        }
    }

    private static void compileRs(QueryRequest query, Statement ps, ResultBuilder builder) throws SQLException {
        StringBuilder sb = new StringBuilder();

        try (ResultSet resultSet = ps.getResultSet()) {
            while (resultSet.next()) {
                sb.append(resultSet.getString(3)).append("\n");
                sb.append("Compile object error at line ").append(resultSet.getLong(1));
                sb.append(", column ").append(resultSet.getLong(2));
            }
        }

        String string = sb.toString();
        if (StringUtils.isNotEmpty(string)) {
            ResultBuilder.ResultMessageBuild rb = builder.newMessage(query);
            rb.receiveMessage(MessageLevel.Error, string);
            rb.finishRecord(true);
        } else {
            ResultBuilder.ResultMessageBuild rb = builder.newMessage(query);
            rb.receiveMessage(MessageLevel.Info, "SUCCESS");
            rb.finishRecord(true);
        }
    }

    @Override
    protected void applyArgs(QueryRequest query, Statement statement) throws SQLException {
        if (query.isUseExplain()) {
            // do nothing
        } else {
            super.applyArgs(query, statement);
        }
    }

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
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

        if (query.isUseExplain()) {
            try (PreparedStatement eps = (PreparedStatement) this.rdbHook().explainStatement(ps.getConnection(), query)) {
                super.applyArgs(query, eps);
                eps.execute();
            }
        } else if (query.isUseCompile()) {
            try (Statement eps = getCompileStatement(ps.getConnection())) {
                eps.execute(query.getQueryBody());
            }
        }

        if (CollectionUtils.isNotEmpty(query.getQueryArgs())) {
            return ((PreparedStatement) ps).execute();
        } else {
            return ps.execute(query.getQueryBody());
        }
    }

    private Statement getCompileStatement(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(200);
        stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        return stmt;
    }

    @Override
    public void killCurrentQuery() throws Exception {
        try {
            super.killCurrentQuery();
        } catch (SQLException e) {
            log.error("session " + this.getSessionId() + " killCurrentQuery failed, " + e.getMessage());
        } finally {
            super.markExecutingFinish();
            this.doClose();
        }
    }
}
