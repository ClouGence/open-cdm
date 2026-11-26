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
package com.clougence.clouddm.ds.metadata;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.Session;

public class EmptyMetadataJdbc implements InvocationHandler {
    private final SQLException      failure;
    private final String            version;
    private int                     queryCount;
    private final EmptyMetadataJdbc owner;
    private final String            sql;
    private boolean                 rowRead;

    public EmptyMetadataJdbc(SQLException failure, String version){
        this.version = version;
        this.failure = failure;
        this.owner = this;
        this.sql = "";
    }

    private EmptyMetadataJdbc(EmptyMetadataJdbc owner, String sql){
        this.failure = owner.failure;
        this.version = owner.version;
        this.owner = owner;
        this.sql = sql.toLowerCase(Locale.ROOT);
    }

    public Session session() {
        return proxy(Session.class);
    }

    public Connection connection() {
        return proxy(Connection.class);
    }

    public int queryCount() {
        return this.queryCount;
    }

    private <T> T proxy(Class<T> type) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, this));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws SQLException {
        switch (method.getName()) {
            case "prepareStatement", "createStatement", "prepareCall":
                this.owner.queryCount++;
                if (this.failure != null) {
                    throw this.failure;
                }
                return method.getReturnType()
                    .cast(Proxy.newProxyInstance(method.getReturnType().getClassLoader(), new Class<?>[] { method.getReturnType() }, new EmptyMetadataJdbc(this.owner,
                        args == null ? "" : (String) args[0])));
            case "executeQuery":
                String query = this.sql;
                if (args != null && args.length > 0) {
                    query = (String) args[0];
                }
                return Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class }, new EmptyMetadataJdbc(this.owner, query));
            case "getMetaData":
                return Proxy.newProxyInstance(method.getReturnType().getClassLoader(), new Class<?>[] { method.getReturnType() }, this);
            case "getDsConfig":
                return new DataSourceConfig();
            case "getDatabaseProductVersion":
                return this.version;
            case "getCatalog":
                return "TESTDB";
            case "getSchema":
                return "TEST_SCHEMA";
            case "getColumnCount":
                return this.sql.contains("version") || this.sql.contains("current_database") ? 1 : 0;
            case "getColumnType":
                return Types.VARCHAR;
            case "getColumnTypeName":
                return "VARCHAR";
            case "getColumnClassName":
                return String.class.getName();
            case "getString":
                return this.sql.contains("database") ? "TESTDB" : this.version;
            case "next":
                if (!this.rowRead && (this.sql.contains("version") || this.sql.contains("current_database"))) {
                    this.rowRead = true;
                    return true;
                }
                return false;
            case "isClosed", "getAutoCommit":
                return false;
            case "close", "setString", "setInt", "setObject", "setFetchSize", "setQueryTimeout":
                return null;
            case "toString":
                return "EmptyMetadataJdbc";
            default:
                throw new AssertionError("Unexpected JDBC call: " + method);
        }
    }
}
