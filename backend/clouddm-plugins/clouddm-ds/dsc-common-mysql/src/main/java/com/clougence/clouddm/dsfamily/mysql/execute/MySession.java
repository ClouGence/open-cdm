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
package com.clougence.clouddm.dsfamily.mysql.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.drivers.DsObject;
import com.mysql.cj.jdbc.ConnectionImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
@Slf4j
public class MySession extends DefaultRdbSession {

    public MySession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new MyHooks(dsConfig.getVersion()));
    }

    public MySession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject, SessionHook sessionHook){
        super(newSessionId, dsConfig, dsObject, sessionHook);
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.beforeQueryRequest(beginTime, query, builder);
    }

    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        QueryRequest queryObject = query;
        if (!query.isUseExplain() && query.getResultConf().getFetchRecordCountLimit() > 0 && query.getQueryType() == SecQueryType.SELECT) {
            QueryRequest tmp = query.clone();
            String trimQueryBody = tmp.getQueryBody().trim();
            if (trimQueryBody.endsWith(";")) {
                tmp.setQueryBody(trimQueryBody.substring(0, trimQueryBody.length() - 1));
            }
            queryObject = tmp;
        }

        return super.createStatement(conn, queryObject);
    }

    //    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
    //        QueryRequest queryObject = query;
    //        if (!query.isRdbUseExplain() && query.getResultConf().getFetchRecordCountLimit() > 0 && query.getQueryType() == SecQueryType.SELECT) {
    //            try {
    //                QueryRequest tmp = query.clone();
    //
    //                List<AstSplitScript> scripts = DslHelper.splitDsl(MySqlDslProvider.INSTANCE, tmp.getQueryBody());
    //                Parser parser = scripts.get(0).getParser();
    //                ParseTree astTree = scripts.get(0).getAstTree();
    //
    //                CommonTokenStream tokens = (CommonTokenStream) parser.getTokenStream();
    //                TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
    //
    //                long maxLimit = query.getResultConf().getFetchRecordCountLimit() + 1;
    //                MyReWriteUtils.rewriterLimit(rewriter, astTree, maxLimit);
    //                tmp.setQueryBody(rewriter.getText());
    //
    //                queryObject = tmp;
    //            } catch (Exception e) {
    //                throw e;
    //            }
    //        }
    //
    //        return super.createStatement(conn, queryObject);
    //    }

    @Override
    public void killCurrentQuery() {
        try {
            ((ConnectionImpl) this.currentResource()).abortInternal();
        } catch (SQLException e) {
            log.error("session " + this.getSessionId() + " killCurrentQuery failed, " + e.getMessage());
        } finally {
            super.markExecutingFinish();
            this.doClose();
        }
    }
}
