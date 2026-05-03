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

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Map;

/**
 *
 */
class JdbcArray implements Array {

    private final JdbcConnection connection;
    private final String         baseType;
    private final List<?>        resultValue;

    JdbcArray(JdbcConnection connection, String baseType, List<?> resultValue){
        this.connection = connection;
        this.baseType = baseType;
        this.resultValue = resultValue;
    }

    @Override
    public String getBaseTypeName() { return this.baseType; }

    @Override
    public int getBaseType() { return this.connection.typeSupport().getTypeNumber(this.baseType); }

    @Override
    public List<?> getArray() { return this.resultValue; }

    @Override
    public List<?> getArray(long index, int count) {
        int fromIndex = Math.toIntExact(index);
        int toIndex = Math.toIntExact(index + count);
        return this.resultValue.subList(fromIndex, toIndex);
    }

    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException("not support getArray(Map)");
    }

    @Override
    public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException("not support getObject(long,int,Map)");
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null; // TODO
    }

    @Override
    public ResultSet getResultSet(long index, int count) throws SQLException {
        return null; // TODO
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException("not support getResultSet(Map)");
    }

    @Override
    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException("not support getResultSet(long,int,Map)");
    }

    @Override
    public void free() {

    }
}
