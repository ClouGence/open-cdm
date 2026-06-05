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
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;

import com.clougence.drivers.adapter.lob.JdbcBob;
import com.clougence.drivers.adapter.lob.JdbcCob;

class JdbcConnection implements Connection, Closeable {

    private boolean                  closed = false;
    private final AdapterConnection  connection;
    private final TypeSupport        typeSupport;
    private final TransactionSupport txSupport;

    JdbcConnection(String jdbcUrl, Properties properties, ClassLoader cl) throws SQLException{
        Objects.requireNonNull(properties, "parameter properties is null.");
        String adapter = properties.getProperty(JdbcDriver.P_ADAPTER_NAME);

        AdapterFactory factory = AdapterManager.lookup(adapter, cl);
        TypeSupport ts = factory.createTypeSupport(properties);

        this.connection = factory.createConnection(this, jdbcUrl, properties);
        this.typeSupport = ts == null ? new AdapterTypeSupport(properties) : ts;
        this.txSupport = this.connection instanceof TransactionSupport ? (TransactionSupport) this.connection : null;
        AdapterConnManager.newConnection(this.connection);
    }

    protected TypeSupport typeSupport() {
        return this.typeSupport;
    }

    protected TransactionSupport txSupport() {
        return this.txSupport;
    }

    protected AdapterConnection adapterConnection() {
        return this.connection;
    }

    @Override
    public void close() {
        if (!this.isClosed()) {
            this.closed = true;
            AdapterConnManager.removeConnection(this.connection);
        }
    }

    @Override
    public boolean isClosed() { return this.closed; }

    protected void checkOpen() throws SQLException {
        if (this.isClosed()) {
            throw new SQLException("Connection is closed");
        }
    }

    private void checkOpenClientInfo() throws SQLClientInfoException {
        if (this.isClosed()) {
            throw new SQLClientInfoException("Connection closed", null);
        }
    }

    private void checkResultSet(int resultSetType, int resultSetConcurrency) throws SQLException {
        if (ResultSet.TYPE_FORWARD_ONLY != resultSetType) {
            throw new SQLFeatureNotSupportedException("ResultSet type can only be TYPE_FORWARD_ONLY");
        }
        if (ResultSet.CONCUR_READ_ONLY != resultSetConcurrency) {
            throw new SQLFeatureNotSupportedException("ResultSet concurrency can only be CONCUR_READ_ONLY");
        }
    }

    private void checkHoldability(int resultSetHoldability) throws SQLException {
        if (ResultSet.HOLD_CURSORS_OVER_COMMIT != resultSetHoldability) {
            throw new SQLFeatureNotSupportedException("holdability can only be HOLD_CURSORS_OVER_COMMIT");
        }
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        this.checkOpen();
        this.adapterConnection().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        this.checkOpen();
        return this.adapterConnection().getCatalog();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        this.checkOpen();
        this.adapterConnection().setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        this.checkOpen();
        return this.adapterConnection().getSchema();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.checkOpen();
        if (readOnly) {
            throw new SQLFeatureNotSupportedException("readOnly can only be false");
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        this.checkOpen();
        return false;
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        this.checkOpen();
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.checkOpen();
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.checkOpen();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("typeMap not supported");
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("typeMap not supported");
    }

    @Override
    public Statement createStatement() throws SQLException {
        this.checkOpen();
        return new JdbcStatement(this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        this.checkOpen();
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return new JdbcStatement(this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(resultSetHoldability);
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return new JdbcStatement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        this.checkOpen();
        return new JdbcPreparedStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.checkOpen();
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return new JdbcPreparedStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(resultSetHoldability);
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return new JdbcPreparedStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        this.checkOpen();
        return new JdbcCallableStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.checkOpen();
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return new JdbcCallableStatement(this, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.checkOpen();
        this.checkHoldability(resultSetHoldability);
        this.checkResultSet(resultSetType, resultSetConcurrency);
        return new JdbcCallableStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        this.checkOpen();
        if (Statement.NO_GENERATED_KEYS != autoGeneratedKeys) {
            throw new SQLFeatureNotSupportedException("Auto generated keys must be NO_GENERATED_KEYS");
        }
        return new JdbcPreparedStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("columnIndexes not supported");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("columnNames not supported");
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        checkOpen();
        return sql;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this.checkOpen();
        if (this.txSupport != null) {
            this.txSupport.setIsolation(level);
        } else {
            if (level != Connection.TRANSACTION_NONE) {
                throw new SQLFeatureNotSupportedException("Unsupported Isolation feature the isolation can only be Connection.TRANSACTION_NONE");
            }
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        this.checkOpen();
        if (this.txSupport != null) {
            return this.txSupport.getIsolation();
        } else {
            return Connection.TRANSACTION_NONE;
        }
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.checkOpen();
        if (this.txSupport != null) {
            this.txSupport.setAutoCommit(autoCommit);
        } else {
            if (!autoCommit) {
                throw new SQLFeatureNotSupportedException("Unsupported transaction feature the autoCommit can only be true");
            }
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        this.checkOpen();
        if (this.txSupport != null) {
            return this.txSupport.isAutoCommit();
        } else {
            return true;
        }
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoint not supported");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoint not supported");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoint not supported");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Savepoint not supported");
    }

    @Override
    public void commit() throws SQLException {
        this.checkOpen();
        if (this.txSupport != null) {
            this.txSupport.commit();
        } else {
            throw new SQLFeatureNotSupportedException("Unsupported transaction feature.");
        }
    }

    @Override
    public void rollback() throws SQLException {
        this.checkOpen();
        if (this.txSupport != null) {
            this.txSupport.rollback();
        } else {
            throw new SQLFeatureNotSupportedException("Unsupported transaction feature.");
        }
    }

    @Override
    public Clob createClob() {
        return new JdbcCob(null);
    }

    @Override
    public NClob createNClob() {
        return new JdbcCob(null);
    }

    @Override
    public Blob createBlob() {
        return new JdbcBob(null);
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type SQLXML not supported");
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        this.checkOpen();
        if (elements != null) {
            return new JdbcArray(this, typeName, new ArrayList<>(Arrays.asList(elements)));
        } else {
            return new JdbcArray(this, typeName, new ArrayList<>());
        }
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type Struct not supported");
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.checkOpenClientInfo();
        throw new SQLClientInfoException("Unsupported operation", null);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.checkOpenClientInfo();
        throw new SQLClientInfoException("Unsupported operation", null);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        this.checkOpenClientInfo();
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        this.checkOpenClientInfo();
        return new Properties();
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        this.checkOpen();
        return this.connection.unwrap(iface, this);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        this.checkOpen();
        return this.connection.unwrap(iface, this) != null;
    }

    @Override
    public DatabaseMetaData getMetaData() { return new JdbcDatabaseMetaData(this, this.connection); }
}
