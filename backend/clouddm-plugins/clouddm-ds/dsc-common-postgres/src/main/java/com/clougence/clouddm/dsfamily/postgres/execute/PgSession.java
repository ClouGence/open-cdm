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
package com.clougence.clouddm.dsfamily.postgres.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.SessionHook;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
public class PgSession extends DefaultRdbSession {

    protected Savepoint inTxRequestSavepoint;

    public PgSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new PgHooks());
    }

    public PgSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject, SessionHook sessionHook){
        super(newSessionId, dsConfig, dsObject, sessionHook);
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.beforeQueryRequest(beginTime, query, builder);

        if (!this.isAutoCommit()) {
            this.inTxRequestSavepoint = currentResource().setSavepoint("savepoint_by_session"); // current query is tx query. when error rollback to this point
        }
    }

    @Override
    protected void afterQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.afterQueryRequest(beginTime, query, builder);

        if (isClose()) {
            return;
        }

        if (this.inTxRequestSavepoint != null) {
            try {
                currentResource().releaseSavepoint(this.inTxRequestSavepoint);
            } catch (SQLException ignore) {
            } finally {
                this.inTxRequestSavepoint = null;
            }
        }
    }

    @Override
    protected void throwQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder, Exception e) {
        super.throwQueryRequest(beginTime, query, builder, e);

        if (isClose()) {
            return;
        }

        if (this.inTxRequestSavepoint != null) {
            try {
                currentResource().rollback(this.inTxRequestSavepoint); // FIX PG ERROR : current transaction is aborted, commands ignored until end of transaction block
            } catch (SQLException ignore) {
            } finally {
                this.inTxRequestSavepoint = null;
            }
        }
    }
}
