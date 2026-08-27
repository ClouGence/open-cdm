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
package com.clougence.clouddm.ds.goldendb.execute.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.oracle.execute.OraSession;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public class GoldenDBOracleSession extends OraSession {

    public GoldenDBOracleSession(String sessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(sessionId, dsConfig, dsObject, new GoldenDBOracleHooks(dsConfig));
    }

    @Override
    protected Statement createStatement(Connection connection, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            String queryBody = StringUtils.defaultIfBlank(query.getOriginalBody(), query.getQueryBody()).stripTrailing();
            if (queryBody.endsWith(";")) {
                queryBody = queryBody.substring(0, queryBody.length() - 1);
            }
            query.setQueryBody("EXPLAIN " + queryBody);
            return rdbHook().explainStatement(connection, query);
        }
        return super.createStatement(connection, query);
    }

    @Override
    protected boolean executeStatement(Statement statement, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (!query.isUseExplain()) {
            return super.executeStatement(statement, query, builder);
        }

        PreparedStatement preparedStatement = (PreparedStatement) statement;
        List<QueryArg> queryArgs = query.getQueryArgs();
        if (CollectionUtils.isNotEmpty(queryArgs)) {
            for (int i = 0; i < queryArgs.size(); i++) {
                QueryArg queryArg = queryArgs.get(i);
                preparedStatement.setObject(i + 1, queryArg.getValue(), queryArg.getJdbcType());
            }
        }
        return preparedStatement.execute();
    }

}
