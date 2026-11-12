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

package com.clougence.clouddm.ds.greptimedb.execute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.net.SocketTimeoutException;
import java.sql.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.clougence.clouddm.ds.greptimedb.dsconf.GreptimeConfig;
import com.clougence.clouddm.ds.greptimedb.i18n.GreptimeConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.SessionCallback;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.drivers.DsObject;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.timer.Timeout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class GreptimeSession extends DefaultRdbSession {

    private static final I18nUtils I18N          = I18nUtils.initI18n(GreptimeConfigI18nKeys.class);

    private final Connection       connection;
    private final Object           executionLock = new Object();
    private final AtomicBoolean    closeNotified = new AtomicBoolean();
    private volatile String        disconnectReason;
    private Timeout                queryDeadline;

    GreptimeSession(String id, GreptimeConfig config, DsObject<Connection> resource){
        super(id, config, resource, new GreptimeSessionHook(config.getVersion()));
        this.connection = resource.getTarget();
    }

    @Override
    protected Connection currentResource() {
        return connection;
    }

    @Override
    public <T> T executeQuery(SessionCallback<T> callback) throws Exception {
        try {
            return super.executeQuery(conn -> {
                requireConnected();
                try {
                    return callback.doCallback(conn);
                } catch (SQLException e) {
                    throw handleJdbcError(e);
                }
            });
        } finally {
            if (disconnectReason != null) {
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
        synchronized (executionLock) {
            if (isExecuting()) {
                disconnect(GreptimeConfigI18nKeys.GREPTIMEDB_QUERY_CANCELLED);
            }
        }
    }

    @Override
    public void close() {
        try {
            super.close();
        } finally {
            notifySessionClosed();
        }
    }

    @Override
    protected void triggerCloseListener() {
        // Retain the logical session until its terminal query result can be read.
        if (disconnectReason == null) {
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
        synchronized (executionLock) {
            if (timeoutSec > 0 && disconnectReason == null) {
                queryDeadline = globalTimer.newTimeout(expired -> {
                    synchronized (executionLock) {
                        if (queryDeadline == expired) {
                            disconnect(GreptimeConfigI18nKeys.GREPTIMEDB_QUERY_TIMEOUT);
                        }
                    }
                }, timeoutSec, TimeUnit.SECONDS);
            }
        }
        try {
            super.execQuery(beginTime, query, builder, receiveSignal);
        } finally {
            synchronized (executionLock) {
                if (queryDeadline != null) {
                    queryDeadline.cancel();
                    queryDeadline = null;
                }
            }
            if (disconnectReason != null) {
                doClose();
            }
        }
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        requireConnected();
        if (query.hasQueryType(SplitQueryType.TRANSACTION)) {
            throw new SQLException(I18N.getMessage(GreptimeConfigI18nKeys.GREPTIMEDB_TRANSACTIONS_UNSUPPORTED));
        }
        try {
            return super.createStatement(conn, query);
        } catch (SQLException e) {
            throw handleJdbcError(e);
        }
    }

    @Override
    protected boolean executeStatement(Statement statement, QueryRequest query, ResultBuilder builder) throws SQLException {
        try {
            checkQuery();
            boolean hasRows = super.executeStatement(statement, query, builder);
            checkQuery();
            return hasRows;
        } catch (SQLException e) {
            throw handleJdbcError(e);
        }
    }

    @Override
    protected void receiveResultRows(QueryRequest query, String resultId, ResultSet rows, ResultBuilder builder) throws SQLException, IOException {
        // The result cache keys columns by label, and Connector/J looks up labels case-insensitively.
        ResultSetMetaData metadata = rows.getMetaData();
        Set<String> labels = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (int index = 1; index <= metadata.getColumnCount(); index++) {
            String label = metadata.getColumnLabel(index);
            if (!labels.add(label)) {
                throw new SQLException(I18N.getMessage(GreptimeConfigI18nKeys.GREPTIMEDB_DUPLICATE_COLUMN, new Object[] { label }));
            }
        }
        ResultSet guardedRows = (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class }, (proxy, method, args) -> {
            try {
                Object result = method.invoke(rows, args);
                if (method.getName().equals("next")) {
                    checkQuery();
                }
                return result;
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof SQLException sqlException) {
                    throw handleJdbcError(sqlException);
                }
                throw e.getCause();
            }
        });
        super.receiveResultRows(query, resultId, guardedRows, builder);
    }

    private void requireConnected() throws SQLException {
        if (disconnectReason != null) {
            throw new SQLException(I18N.getMessage(GreptimeConfigI18nKeys.GREPTIMEDB_SESSION_DISCONNECTED), "08003");
        }
    }

    private void checkQuery() throws SQLException {
        String reason = disconnectReason;
        if (reason != null) {
            throw new SQLException(I18N.getMessage(reason), "08006");
        }
    }

    private SQLException handleJdbcError(SQLException error) {
        synchronized (executionLock) {
            String state = error.getSQLState();
            if (disconnectReason == null && state != null && state.startsWith("08")) {
                String reason = GreptimeConfigI18nKeys.GREPTIMEDB_CONNECTION_LOST;
                if (ExceptionUtils.getRootCause(error) instanceof SocketTimeoutException) {
                    reason = GreptimeConfigI18nKeys.GREPTIMEDB_READ_TIMEOUT;
                }
                disconnect(reason);
            }
            if (disconnectReason != null) {
                return new SQLException(I18N.getMessage(disconnectReason), state, error);
            }
            return error;
        }
    }

    private void disconnect(String reason) {
        if (disconnectReason != null) {
            return;
        }
        disconnectReason = reason;
        try {
            // Connector/J abort closes the transport without waiting for the running command.
            connection.abort(Runnable::run);
        } catch (SQLException | RuntimeException e) {
            log.error("Abort GreptimeDB connection failed, sessionId={}", getSessionId(), e);
        }
    }
}
