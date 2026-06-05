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
package com.clougence.clouddm.ds.sqlserver.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;
import com.clougence.utils.io.IOUtils;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
public class MsSqlSession extends DefaultRdbSession {

    public MsSqlSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new MsSqlHooks());
    }

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (query.isUseExplain()) {
            return ps.execute(query.getQueryBody());
        } else {
            return ((PreparedStatement) ps).execute();
        }
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.beforeQueryRequest(beginTime, query, builder);

        if (query.isUseExplain()) {
            Statement statement = this.currentResource().createStatement();
            try {
                statement.execute("set showplan_all on;");
            } finally {
                IOUtils.closeQuietly(statement);
            }

        }
    }

    @Override
    protected void afterQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.afterQueryRequest(beginTime, query, builder);

        if (query.isUseExplain()) {
            Statement statement = this.currentResource().createStatement();
            try {
                statement.execute("set showplan_all off;");
            } finally {
                IOUtils.closeQuietly(statement);
            }
        }
    }
}
