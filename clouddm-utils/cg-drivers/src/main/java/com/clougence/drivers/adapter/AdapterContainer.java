/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.drivers.adapter;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class AdapterContainer implements AdapterReceive {

    private final JdbcConnection              jdbcConn;
    private volatile AdapterReceiveState      state;
    private volatile AdapterRequest           request;
    private final Map<String, JdbcColumn>     parameterDefs;
    private final Map<String, Object>         parameterValues;
    private final LinkedList<AdapterResponse> response;
    private final Object                      syncObj;

    public AdapterContainer(JdbcConnection jdbcConn){
        this.jdbcConn = jdbcConn;
        this.state = AdapterReceiveState.Ready;
        this.parameterDefs = new HashMap<>();
        this.parameterValues = new HashMap<>();
        this.response = new LinkedList<>();
        this.syncObj = new Object();
    }

    public AdapterReceiveState getState() { return this.state; }

    public void prepareReceive(AdapterRequest request) throws SQLException {
        if (this.state != AdapterReceiveState.Ready) {
            throw new SQLException("there is already query in the processing.", JdbcErrorCode.SQL_STATE_QUERY_IS_PENDING);
        }

        synchronized (this.syncObj) {
            this.state = AdapterReceiveState.Pending;
            this.request = request;
            this.parameterDefs.clear();
            this.parameterValues.clear();
            this.response.clear();
        }
    }

    public AdapterRequest getRequest() { return this.request; }

    public AdapterCursor getOutParameters() throws SQLException {
        if (this.state == AdapterReceiveState.Pending) {
            throw new SQLException("the query in progress, result is Pending.", JdbcErrorCode.SQL_STATE_QUERY_IS_PENDING);
        }

        JdbcColumn[] toCols = new JdbcColumn[this.parameterDefs.size()];
        Object[][] toVals = new Object[1][this.parameterDefs.size()];
        AtomicInteger counter = new AtomicInteger(0);
        this.parameterDefs.forEach((k, v) -> {
            int i = counter.getAndIncrement();
            toCols[i] = v;
            toVals[0][i] = parameterValues.get(k);
        });

        return new AdapterMemoryCursor(Arrays.asList(toCols), toVals);
    }

    private boolean ifErrorStatusForResponse(AdapterRequest request) {
        if (this.request == null || !StringUtils.equals(this.request.getTraceId(), request.getTraceId())) {
            log.warn("received an unrelated response, traceId " + request.getTraceId());
            return true;
        }

        if (this.state == AdapterReceiveState.Ready) {
            log.warn("received error, no query in progress, traceId " + request.getTraceId());
            return true;
        }

        return false;
    }

    @Override
    public boolean responseFailed(AdapterRequest request, Throwable e) {
        Objects.requireNonNull(e, "received error is null.");
        if (this.ifErrorStatusForResponse(request)) {
            return false;
        }

        this.response.add(AdapterResponse.ofError(e));
        this.onReceive();
        return true;
    }

    @Override
    public boolean responseResult(AdapterRequest request, AdapterCursor cursor) {
        Objects.requireNonNull(cursor, "received cursor is null.");
        if (this.ifErrorStatusForResponse(request)) {
            return false;
        }

        this.response.add(AdapterResponse.ofCursor(cursor));
        this.onReceive();
        return true;
    }

    @Override
    public boolean responseUpdateCount(AdapterRequest request, long updateCount) {
        if (this.ifErrorStatusForResponse(request)) {
            return false;
        }

        this.response.add(AdapterResponse.ofUpdateCount(updateCount));
        this.onReceive();
        return true;
    }

    @Override
    public boolean responseParameter(AdapterRequest request, String paramName, String paramType, Object value) {
        if (this.ifErrorStatusForResponse(request)) {
            return false;
        }

        if (StringUtils.isBlank(paramName) || StringUtils.isBlank(paramType)) {
            throw new NullPointerException("received an unrelated data, paramName or paramType is blank.");
        }

        this.parameterDefs.put(paramName, new JdbcColumn(paramName, paramType, "", "", ""));
        this.parameterValues.put(paramName, value);
        return true;
    }

    @Override
    public boolean responseFinish(AdapterRequest request) {
        if (this.ifErrorStatusForResponse(request)) {
            log.warn("received an unrelated finish, traceId " + request.getTraceId());
            return false;
        }

        this.onReceive();
        this.onReady();
        return true;
    }

    protected void onReceive() {
        synchronized (this.syncObj) {
            this.state = AdapterReceiveState.Receive;
            this.syncObj.notifyAll();
        }
    }

    protected void onReady() {
        synchronized (this.syncObj) {
            this.state = AdapterReceiveState.Ready;
            this.syncObj.notifyAll();
        }
    }

    public boolean nextResult(int timeout, TimeUnit timeUnit) throws SQLException {
        if (this.state == AdapterReceiveState.Pending) {
            throw new SQLException("querying in progress.", JdbcErrorCode.SQL_STATE_QUERY_IS_PENDING);
        }

        if (!this.response.isEmpty()) {
            AdapterResponse res = this.response.removeFirst();
            if (res.isResult()) {
                IOUtils.closeQuietly(res.toCursor());
            }
        }

        boolean empty = this.response.isEmpty();
        if (empty && this.state == AdapterReceiveState.Receive) {
            this.waitFor(timeout, timeUnit);
            return !this.response.isEmpty();
        } else {
            return !empty;
        }
    }

    public AdapterResponse firstResult() throws SQLException {
        if (this.state == AdapterReceiveState.Pending) {
            throw new SQLException("querying in progress.", JdbcErrorCode.SQL_STATE_QUERY_IS_PENDING);
        }

        if (this.response.isEmpty()) {
            return null;
        } else {
            return this.response.getFirst();
        }
    }

    public void waitFor(int timeout, TimeUnit timeUnit) throws SQLException {
        try {
            if (this.response.isEmpty()) {
                synchronized (this.syncObj) {
                    if (this.state == AdapterReceiveState.Pending) {
                        if (timeout == 0) {
                            this.syncObj.wait();
                        } else {
                            this.syncObj.wait(timeUnit.toMillis(timeout));
                        }
                    }
                }
            }

            if (this.response.isEmpty()) {
                throw new SQLException("no data received, or wait timeout.");
            }

            AdapterResponse res = this.response.getFirst();
            if (res.isError()) {
                this.response.removeFirst();
                throw res.toError();
            }
        } catch (InterruptedException e) {
            if (this.jdbcConn.isClosed()) {
                throw new SQLException("connection closed.");
            }
        }
    }
}
