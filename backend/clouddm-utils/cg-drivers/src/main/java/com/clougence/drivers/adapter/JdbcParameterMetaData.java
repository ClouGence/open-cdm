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

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

class JdbcParameterMetaData implements ParameterMetaData {

    private final JdbcPreparedStatement statement;
    private final Map<String, JdbcArg>  parameters;

    JdbcParameterMetaData(JdbcPreparedStatement statement, Map<String, JdbcArg> parameters){
        this.statement = statement;
        this.parameters = parameters == null ? Collections.emptyMap() : parameters;
    }

    protected PreparedStatement getStatement() { return this.statement; }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        this.statement.checkOpen();
        return this.statement.jdbcConn.adapterConnection().unwrap(iface, this);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        this.statement.checkOpen();
        return this.statement.jdbcConn.adapterConnection().unwrap(iface, this) != null;
    }

    @Override
    public int getParameterCount() { return this.parameters.size(); }

    private JdbcArg checkAndGet(String paramName) throws SQLException {
        JdbcArg jdbcArg = this.parameters.get(paramName);
        if (jdbcArg == null) {
            throw new SQLException("invalid parameter.");
        } else {
            return jdbcArg;
        }
    }

    @Override
    public int getParameterMode(int param) throws SQLException {
        JdbcArgMode mode = this.checkAndGet("arg" + param).getMode();
        switch (mode) {
            case In:
                return parameterModeIn;
            case InOut:
                return parameterModeInOut;
            case Out:
                return parameterModeOut;
            default:
                return parameterModeUnknown;
        }
    }

    @Override
    public String getParameterTypeName(int param) throws SQLException {
        return this.checkAndGet("arg" + param).getType();
    }

    @Override
    public int getParameterType(int param) throws SQLException {
        String typeName = this.checkAndGet("arg" + param).getType();
        return this.statement.jdbcConn.typeSupport().getTypeNumber(typeName);
    }

    @Override
    public String getParameterClassName(int param) throws SQLException {
        String typeName = this.checkAndGet("arg" + param).getType();
        return this.statement.jdbcConn.typeSupport().getTypeClassName(typeName);
    }

    @Override
    public int isNullable(int param) {
        return parameterNullableUnknown;
    }

    @Override
    public boolean isSigned(int param) {
        return false;
    }

    @Override
    public int getPrecision(int param) {
        return 0;
    }

    @Override
    public int getScale(int param) {
        return 0;
    }
}
