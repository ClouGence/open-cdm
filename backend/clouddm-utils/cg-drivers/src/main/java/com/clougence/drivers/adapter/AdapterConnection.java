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
package com.clougence.drivers.adapter;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timeout;
import com.clougence.utils.timer.TimerTask;

public abstract class AdapterConnection implements Closeable {

    private final static HashedWheelTimer TIMER;
    private final String                  objectId;
    private final AdapterInfo             adapterInfo;
    private final Map<String, Timeout>    timeout;

    static {
        TIMER = new HashedWheelTimer();
    }

    public AdapterConnection(String jdbcUrl, String userName){
        this.objectId = UUID.randomUUID().toString().replace("-", "");
        this.adapterInfo = new AdapterInfo();
        this.adapterInfo.setUrl(jdbcUrl);
        this.adapterInfo.setUserName(userName);
        this.adapterInfo.setDbVersion(this.jdbcVersion());
        this.adapterInfo.setDriverVersion(this.driverVersion());
        this.timeout = new ConcurrentHashMap<>();
    }

    public String getObjectId() { return this.objectId; }

    protected AdapterVersion driverVersion() {
        AdapterVersion jdbc = new AdapterVersion();
        jdbc.setName(JdbcDriver.NAME);
        jdbc.setVersion(JdbcDriver.VERSION);
        jdbc.setMajorVersion(JdbcDriver.VERSION_MAJOR);
        jdbc.setMinorVersion(JdbcDriver.VERSION_MINOR);
        return jdbc;
    }

    protected AdapterVersion jdbcVersion() {
        AdapterVersion jdbc = new AdapterVersion();
        jdbc.setName(JdbcDriver.NAME);
        jdbc.setVersion(JdbcDriver.VERSION);
        jdbc.setMajorVersion(JdbcDriver.VERSION_MAJOR);
        jdbc.setMinorVersion(JdbcDriver.VERSION_MINOR);
        return jdbc;
    }

    public AdapterInfo getInfo() { return this.adapterInfo; }

    public abstract void setCatalog(String catalog) throws SQLException;

    public abstract String getCatalog() throws SQLException;

    public abstract void setSchema(String schema) throws SQLException;

    public abstract String getSchema() throws SQLException;

    public final <T> T unwrap(Class<T> iface, Object target) throws SQLException {
        if (iface.isInstance(target)) {
            return iface.cast(target);
        }

        // find JdbcConnection.
        JdbcConnection jdbcConn;
        Statement jdbcStatement;
        ResultSet jdbcResultSet;
        if (target instanceof JdbcConnection) {
            jdbcResultSet = null;
            jdbcStatement = null;
            jdbcConn = (JdbcConnection) target;
        } else if (target instanceof JdbcDatabaseMetaData) {
            jdbcResultSet = null;
            jdbcStatement = null;
            jdbcConn = (JdbcConnection) ((JdbcDatabaseMetaData) target).getConnection();
        } else if (target instanceof JdbcParameterMetaData) {
            jdbcResultSet = null;
            jdbcStatement = ((JdbcParameterMetaData) target).getStatement();
            jdbcConn = (JdbcConnection) jdbcStatement.getConnection();
        } else if (target instanceof JdbcStatement) {
            jdbcResultSet = null;
            jdbcStatement = (Statement) target;
            jdbcConn = (JdbcConnection) jdbcStatement.getConnection();
        } else if (target instanceof JdbcResultSet) {
            jdbcResultSet = (ResultSet) target;
            jdbcStatement = ((JdbcResultSet) target).getStatement();
            jdbcConn = (JdbcConnection) jdbcStatement.getConnection();
        } else if (target instanceof JdbcResultSetMetaData) {
            jdbcResultSet = ((JdbcResultSetMetaData) target).getResultSet();
            jdbcStatement = ((JdbcResultSetMetaData) target).getStatement();
            jdbcConn = (JdbcConnection) ((JdbcResultSetMetaData) target).getStatement().getConnection();
        } else {
            jdbcResultSet = null;
            jdbcStatement = null;
            jdbcConn = null;
        }

        //
        if (TransactionSupport.class.isAssignableFrom(iface)) {
            return iface.cast(jdbcConn.txSupport());
        } else if (TypeSupport.class.isAssignableFrom(iface)) {
            return iface.cast(jdbcConn.typeSupport());
        } else if (Connection.class.isAssignableFrom(iface)) {
            return iface.cast(jdbcConn);
        } else if (Statement.class.isAssignableFrom(iface)) {
            return iface.cast(jdbcStatement);
        } else if (ResultSet.class.isAssignableFrom(iface)) {
            return iface.cast(jdbcResultSet);
        } else {
            return this.unwrap(iface);
        }
    }

    //

    public void startTimer(String traceId, int timeoutMs, TimerTask timerTask) {
        this.timeout.put(traceId, TIMER.newTimeout(t -> {
            try {
                timerTask.run(t);
            } finally {
                this.timeout.remove(traceId);
            }
        }, timeoutMs, TimeUnit.MILLISECONDS));
    }

    public void stopTimer(String traceId) {
        Timeout timeout = this.timeout.get(traceId);
        if (timeout != null) {
            timeout.cancel();
            this.timeout.remove(traceId);
        }
    }

    //

    protected <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public abstract AdapterRequest newRequest(String sql);

    public abstract void doRequest(AdapterRequest request, AdapterReceive receive) throws SQLException;

    public abstract void cancelRequest();

    @Override
    public final void close() throws IOException {
        try {
            this.doClose();
        } finally {
            AdapterConnManager.removeConnection(this);
        }
    }

    protected abstract void doClose() throws IOException;
}
