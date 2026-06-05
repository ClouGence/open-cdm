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

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

class JdbcPreparedStatement extends JdbcStatement implements PreparedStatement {

    private final Map<String, JdbcArg> parameters     = new LinkedHashMap<>();
    private final Map<String, JdbcArg> parametersKeep = new LinkedHashMap<>();
    private final String               query;

    JdbcPreparedStatement(JdbcConnection jdbcConn, String query){
        super(jdbcConn);
        this.query = query;
    }

    @Override
    protected void configRequest(AdapterRequest request) {
        super.configRequest(request);
        request.setArgMap(new HashMap<>(this.parameters));
    }

    @Override
    protected void beforeExecute(AdapterRequest request, AdapterContainer container) throws SQLException {
        super.beforeExecute(request, container);
        this.parametersKeep.clear();
        this.parametersKeep.putAll(this.parameters);
        this.parameters.clear();
    }

    protected void checkParameterIndex(int parameterIndex) throws SQLException {
        if (parameterIndex < 1) {
            throw new SQLException("Parameter index starts from 1, but got " + parameterIndex + ".", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    protected void setParameter(JdbcArgMode mode, String parameterName, String typeName, Object object) throws SQLException {
        this.checkOpen();
        switch (mode) {
            case In:
            case Out:
                break;
            case InOut:
            default:
                throw new SQLException("parameter '" + parameterName + "' of type '" + typeName + "' only support In/Out.");
        }

        JdbcArg jdbcArg = this.parameters.computeIfAbsent(parameterName, s -> new JdbcArg(s, mode));
        if (mode.isIn()) {
            jdbcArg.setType(typeName);
            jdbcArg.setValue(object);
        } else {
            jdbcArg.setType(typeName);
        }

        if ((jdbcArg.getMode().isIn() && mode.isOut()) || (jdbcArg.getMode().isOut() && mode.isIn())) {
            jdbcArg.setMode(JdbcArgMode.InOut);
        }
    }

    private void setStringReader(String parameterName, Reader reader) throws SQLException {
        try {
            String value = reader == null ? null : IOUtils.readToString(reader);
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    private void setStringReader(String parameterName, Reader reader, long length) throws SQLException {
        try {
            String value = reader == null ? null : IOUtils.readToString(reader, length);
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    private void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, null);
        } else {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copyLarge(x, out);
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, out.toByteArray());
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }
    }

    private void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, null);
        } else {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copyLarge(x, out, 0, length);
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, out.toByteArray());
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        this.checkOpen();
        this.parameters.clear();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        this.checkOpen();
        if (this.parameters.isEmpty()) {
            return new JdbcParameterMetaData(this, this.parametersKeep);
        } else {
            return new JdbcParameterMetaData(this, this.parameters);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, this.getTypeName(sqlType), null);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (StringUtils.isNotBlank(typeName)) {
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, typeName, null);
        } else {
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, this.getTypeName(sqlType), null);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Boolean, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Byte, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Short, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Int, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Long, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Float, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Double, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.BigDecimal, x);
    }

    @Override
    public void setURL(int parameterIndex, URL val) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        String value = val == null ? null : val.toString();
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.String, value);
    }

    @Override
    public void setString(int parameterIndex, String value) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.String, value);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.String, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader, length);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, x == null ? null : x.getCharacterStream());
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, x == null ? null : x.getCharacterStream());
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader, length);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (x == null) {
            this.setStringReader("arg" + parameterIndex, null, length);
        } else {
            this.setStringReader("arg" + parameterIndex, new InputStreamReader(x, StandardCharsets.UTF_8), length);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setStringReader("arg" + parameterIndex, reader, length);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Bytes, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x, length);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, x == null ? null : x.getBinaryStream());
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, inputStream);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setBinaryStream("arg" + parameterIndex, inputStream, length);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlDate, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (x == null) {
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlDate, null);
        } else {
            if (cal == null) {
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlDate, x);
            } else {
                ZonedDateTime zonedDate = x.toLocalDate().atStartOfDay(cal.getTimeZone().toZoneId());
                OffsetDateTime offsetDateTime = zonedDate.toOffsetDateTime();
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.OffsetDateTime, offsetDateTime);
            }
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlTime, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (x == null) {
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlTime, null);
        } else {
            if (cal == null) {
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlTime, x);
            } else {
                ZonedDateTime zonedTime = x.toInstant().atZone(cal.getTimeZone().toZoneId());
                OffsetTime offsetTime = OffsetTime.of(zonedTime.toLocalTime(), zonedTime.getOffset());
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.OffsetTime, offsetTime);
            }
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlTimestamp, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (x == null) {
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlTimestamp, null);
        } else {
            if (cal == null) {
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.SqlTimestamp, x);
            } else {
                ZonedDateTime zonedTime = x.toInstant().atZone(cal.getTimeZone().toZoneId());
                OffsetDateTime offsetDateTime = zonedTime.toOffsetDateTime();
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.OffsetDateTime, offsetDateTime);
            }
        }
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        throw new SQLFeatureNotSupportedException("type RowId not supported");
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        throw new SQLFeatureNotSupportedException("type SQLXML not supported");
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        throw new SQLFeatureNotSupportedException("type Ref not supported");
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Array, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (x == null) {
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, AdapterType.Unknown, null);
        } else {
            String typeName = this.jdbcConn.typeSupport().getTypeName(x.getClass());
            this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, typeName, x);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, this.getTypeName(targetSqlType), x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        this.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        if (targetSqlType != null) {
            if (StringUtils.isNotBlank(targetSqlType.getName())) {
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, targetSqlType.getName(), x);
            }
            if (targetSqlType.getVendorTypeNumber() != null) {
                this.setParameter(JdbcArgMode.In, "arg" + parameterIndex, this.getTypeName(targetSqlType.getVendorTypeNumber()), x);
            }
        }

        throw new IllegalArgumentException("setObject need targetSqlType.");
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        this.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public int executeUpdate() throws SQLException {
        try {
            return this.executeUpdate(this.query);
        } finally {
            this.clearParameters();
        }
    }

    @Override
    public boolean execute() throws SQLException {
        try {
            return super.execute(this.query);
        } finally {
            this.clearParameters();
        }
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        try {
            return super.executeLargeUpdate(this.query);
        } finally {
            this.clearParameters();
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            return super.executeQuery(this.query);
        } finally {
            this.clearParameters();
        }
    }

    @Override
    public void addBatch() throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("addBatch not supported");
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkResultSet();

        AdapterResponse result = this.container.firstResult();
        if (result == null) {
            throw new SQLException("No results were returned by the query.", JdbcErrorCode.SQL_STATE_QUERY_EMPTY);
        } else {
            if (result.isResult()) {
                return new JdbcResultSetMetaData(this.getResultSet(), this, result.getColumnList());
            } else {
                throw new SQLException("current result is updateCount.", JdbcErrorCode.SQL_STATE_QUERY_IS_UPDATE_COUNT);
            }
        }
    }
}
