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
package com.clougence.clouddm.ds.kingbasees.execute.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.kingbasees.execute.dsfactory.KingbaseESDsObject;
import com.clougence.clouddm.dsfamily.oracle.execute.OraSession;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.kingbase8.core.BaseConnection;

public class KingbaseESOracleSession extends OraSession {

    public KingbaseESOracleSession(String sessionId, DataSourceConfig dsConfig, KingbaseESDsObject dsObject){
        super(sessionId, dsConfig, dsObject, new KingbaseESOracleHooks(dsConfig));
    }

    @Override
    protected Statement createStatement(Connection connection, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            return rdbHook().explainStatement(connection, query);
        }
        return super.createStatement(connection, query);
    }

    @Override
    protected boolean executeStatement(Statement statement, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (query.isUseExplain()) {
            return ((PreparedStatement) statement).execute();
        }
        return super.executeStatement(statement, query, builder);
    }

    @Override
    public void killCurrentQuery() throws Exception {
        currentResource().unwrap(BaseConnection.class).cancelQuery();
    }
}
