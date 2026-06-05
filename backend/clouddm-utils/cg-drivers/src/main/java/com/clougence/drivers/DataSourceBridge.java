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
package com.clougence.drivers;

import java.io.Closeable;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

public class DataSourceBridge implements DataSource {

    private final Properties            baseProperties;
    private final DsFactory<Connection> dsFactory;
    private int                         loginTimeout;
    private PrintWriter                 printWriter;

    public DataSourceBridge(Properties properties, DsFactory<Connection> dsFactory){
        this.baseProperties = Objects.requireNonNull(properties, "properties is null.");
        this.dsFactory = Objects.requireNonNull(dsFactory, "dsFactory is null.");
    }

    /** Returns 0, indicating the default system timeout is to be used. */
    @Override
    public int getLoginTimeout() { return this.loginTimeout; }

    /** Setting a login timeout is not supported. */
    @Override
    public void setLoginTimeout(int loginTimeout) {
        this.loginTimeout = loginTimeout;
        this.baseProperties.put(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey(), loginTimeout);
    }

    /** LogWriter methods are not supported. */
    @Override
    public PrintWriter getLogWriter() { return printWriter; }

    /** LogWriter methods are not supported. */
    @Override
    public void setLogWriter(PrintWriter pw) { this.printWriter = pw; }

    //---------------------------------------------------------------------
    // Implementation of JDBC 4.0's Wrapper interface
    //---------------------------------------------------------------------

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        throw new SQLException("DataSource of type [" + getClass().getName() + "] cannot be unwrapped as [" + iface.getName() + "]");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }

    //---------------------------------------------------------------------
    // Implementation of JDBC 4.1's getParentLogger method
    //---------------------------------------------------------------------

    @Override
    public java.util.logging.Logger getParentLogger() { return java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME); }

    @Override
    public Connection getConnection() throws SQLException { return getConnection(this.baseProperties); }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Properties copyProperties = new Properties(this.baseProperties);
        copyProperties.put(DsConfigKeys.USER.getConfigKey(), username);
        copyProperties.put(DsConfigKeys.PASSWORD.getConfigKey(), password);
        return getConnection(copyProperties);
    }

    private Connection getConnection(Properties properties) throws SQLException {
        try {
            DsObject<Connection> dsObject = dsFactory.create(properties);
            ConnectionBridgeInvocationHandler bridge = new ConnectionBridgeInvocationHandler(dsObject); // when close to DsObject close
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class, Closeable.class }, bridge);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException(e);
            }
        }
    }

    /** 负责 Connection 到 DsObject<Connection> 的桥接 */
    private static class ConnectionBridgeInvocationHandler implements InvocationHandler {

        private final DsObject<Connection> dsObject;
        private final Connection           target;

        ConnectionBridgeInvocationHandler(DsObject<Connection> dsObject){
            this.dsObject = dsObject;
            this.target = dsObject.getTarget();
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            switch (method.getName()) {
                case "toString":
                    return this.dsObject.toString();
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                default:
                    break;
            }

            if ("close".equals(method.getName())) {
                if (!this.dsObject.isClose()) {
                    this.dsObject.close();
                }
                return null;
            }
            if ("isClosed".equals(method.getName())) {
                return this.dsObject.isClose() || this.target.isClosed();
            }

            if (this.dsObject.isClose()) {
                throw new SQLException("connection is closed.");
            }

            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }
}
