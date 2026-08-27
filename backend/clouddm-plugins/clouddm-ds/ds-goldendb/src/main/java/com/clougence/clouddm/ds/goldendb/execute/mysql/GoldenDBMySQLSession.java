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
package com.clougence.clouddm.ds.goldendb.execute.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

public class GoldenDBMySQLSession extends DefaultRdbSession {

    public GoldenDBMySQLSession(String sessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(sessionId, dsConfig, dsObject, new GoldenDBMySQLHooks(dsConfig.getVersion()));
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
}
