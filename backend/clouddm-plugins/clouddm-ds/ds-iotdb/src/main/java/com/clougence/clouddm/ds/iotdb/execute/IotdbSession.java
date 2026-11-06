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
package com.clougence.clouddm.ds.iotdb.execute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbConfig;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.SessionCallback;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;
import com.clougence.utils.timer.Timeout;

final class IotdbSession extends DefaultRdbSession {

    private final IotdbJdbcSession jdbc;
    private final Connection       connection;
    private final AtomicBoolean    closeNotified = new AtomicBoolean();
    private final Object           deadlineLock  = new Object();
    private Timeout                queryDeadline;

    IotdbSession(String id, IotdbConfig config, DsObject<Connection> resource, IotdbJdbcSession jdbc){
        super(id, config, resource, new IotdbSessionHook(jdbc, resource.getTarget()));
        this.jdbc = jdbc;
        this.connection = resource.getTarget();
    }

    @Override
    protected Connection currentResource() {
        return connection;
    }

    @Override
    public <V> V executeQuery(SessionCallback<V> callback) throws Exception {
        try {
            return super.executeQuery(conn -> {
                jdbc.checkConnected();
                return callback.doCallback(conn);
            });
        } finally {
            if (jdbc.isDisconnected()) {
                doClose();
            }
        }
    }

    @Override
    public void cancel() {
        killCurrentQuery();
    }

    @Override
    public void killCurrentQuery() {
        if (isExecuting()) {
            jdbc.disconnect(IotdbI18nKeys.IOTDB_REQUEST_CANCELLED);
        }
    }

    @Override
    public void close() {
        super.close();
        notifySessionClosed();
    }

    @Override
    protected void triggerCloseListener() {
        // Release a broken JDBC connection while keeping the logical session available to deliver its final result.
        if (!jdbc.isDisconnected()) {
            notifySessionClosed();
        }
    }

    private void notifySessionClosed() {
        if (closeNotified.compareAndSet(false, true)) {
            super.triggerCloseListener();
        }
    }

    @Override
    protected void execQuery(long beginTime, QueryRequest query, ResultBuilder builder, AtomicBoolean receiveSignal) throws Exception {
        int timeoutSec = query.getResultConf().getQueryTimeoutSec();
        if (timeoutSec > 0) {
            synchronized (deadlineLock) {
                queryDeadline = globalTimer.newTimeout(expired -> {
                    synchronized (deadlineLock) {
                        if (queryDeadline == expired) {
                            jdbc.disconnect(IotdbI18nKeys.IOTDB_QUERY_TIMEOUT);
                        }
                    }
                }, timeoutSec, TimeUnit.SECONDS);
            }
        }
        try {
            super.execQuery(beginTime, query, builder, receiveSignal);
        } finally {
            synchronized (deadlineLock) {
                if (queryDeadline != null) {
                    queryDeadline.cancel();
                    queryDeadline = null;
                }
            }
            if (jdbc.isDisconnected()) {
                doClose();
            }
        }
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        try {
            jdbc.checkConnected();
            return super.createStatement(conn, query);
        } catch (ThirdPartyApiException e) {
            throw IotdbJdbcSession.localized(e);
        }
    }

    @Override
    protected boolean executeStatement(Statement statement, QueryRequest query, ResultBuilder builder) throws SQLException {
        try {
            return statement.execute(query.getQueryBody());
        } catch (ThirdPartyApiException e) {
            throw IotdbJdbcSession.localized(e);
        }
    }

    @Override
    protected void receiveResultRows(QueryRequest query, String resultId, ResultSet rows, ResultBuilder builder) throws SQLException, IOException {
        // The SDK identifies columns by label. Require distinct aliases for projections with repeated labels.
        ResultSetMetaData metadata = rows.getMetaData();
        Set<String> labels = new HashSet<>();
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            String label = metadata.getColumnLabel(i);
            if (!labels.add(label)) {
                throw IotdbJdbcSession.localized(ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_DUPLICATE_COLUMN, label));
            }
        }

        // Check between buffered rows as well as between JDBC fetch RPCs.
        ResultSet guardedRows = (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class }, (proxy, method, args) -> {
            try {
                if (method.getName().equals("next")) {
                    jdbc.checkRequest();
                }
                return method.invoke(rows, args);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof ThirdPartyApiException apiError) {
                    throw IotdbJdbcSession.localized(apiError);
                }
                throw e.getCause();
            } catch (ThirdPartyApiException e) {
                throw IotdbJdbcSession.localized(e);
            }
        });
        super.receiveResultRows(query, resultId, guardedRows, builder);
    }

    @Override
    public long getUpdateCount(Statement statement) throws SQLException {
        return statement.getUpdateCount();
    }
}
