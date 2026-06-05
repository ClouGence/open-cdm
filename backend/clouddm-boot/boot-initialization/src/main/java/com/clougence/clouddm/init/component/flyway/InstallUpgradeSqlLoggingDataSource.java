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
package com.clougence.clouddm.init.component.flyway;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.clougence.clouddm.init.component.log.InstallUpgradeLogBus;

class InstallUpgradeSqlLoggingDataSource implements DataSource {

    private final DataSource delegate;

    InstallUpgradeSqlLoggingDataSource(DataSource delegate){
        this.delegate = delegate;
    }

    @Override
    public Connection getConnection() throws SQLException { return wrapConnection(delegate.getConnection()); }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return wrapConnection(delegate.getConnection(username, password));
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException { return delegate.getLogWriter(); }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException { return delegate.getLoginTimeout(); }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException { return delegate.getParentLogger(); }

    private Connection wrapConnection(Connection connection) {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, (proxy, method, args) -> {
            try {
                Object result = method.invoke(connection, args);
                if (result instanceof Statement && "createStatement".equals(method.getName())) {
                    return wrapStatement((Statement) result);
                }
                return result;
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        });
    }

    private Statement wrapStatement(Statement statement) {
        return (Statement) Proxy.newProxyInstance(Statement.class.getClassLoader(), new Class[] { Statement.class }, (proxy, method, args) -> {
            String sql = extractSql(method, args);
            try {
                if (sql != null) {
                    InstallUpgradeLogBus.rememberSql(sql);
                    InstallUpgradeLogBus.info("[SQL] " + sql);
                }
                return method.invoke(statement, args);
            } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (sql != null) {
                    InstallUpgradeLogBus.error("[SQL FAILED] " + sql, target);
                }
                throw target;
            }
        });
    }

    private String extractSql(Method method, Object[] args) {
        if (args == null || args.length == 0 || !(args[0] instanceof String)) {
            return null;
        }

        if (!method.getName().startsWith("execute")) {
            return null;
        }

        return ((String) args[0]).trim();
    }
}
