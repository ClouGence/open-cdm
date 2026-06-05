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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

class JdbcCallableStatement extends JdbcPreparedStatement implements CallableStatement {

    private static final Object[][] EMPTY         = new Object[0][0];
    private ResultSet               lastResultOut = null;

    JdbcCallableStatement(JdbcConnection jdbcConn, String sql){
        super(jdbcConn, sql);
    }

    private ResultSet getOutParameter() throws SQLException {
        this.checkOpen();
        return this.lastResultOut;
    }

    @Override
    protected void afterExecute(AdapterRequest request, AdapterContainer container) throws SQLException {
        super.afterExecute(request, container);
        AdapterCursor outParameters = container.getOutParameters();
        if (outParameters == null) {
            outParameters = new AdapterMemoryCursor(Collections.emptyList(), EMPTY);
        }
        this.lastResultOut = new JdbcResultSet(this, outParameters);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
        if (StringUtils.isNotBlank(typeName)) {
            this.setParameter(JdbcArgMode.Out, parameterName, typeName, null);
        } else {
            this.registerOutParameter(parameterName, sqlType);
        }
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
        this.registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
        if (sqlType != null) {
            if (StringUtils.isNotBlank(sqlType.getName())) {
                this.setParameter(JdbcArgMode.Out, parameterName, sqlType.getName(), null);
            }
            if (sqlType.getVendorTypeNumber() != null) {
                this.setParameter(JdbcArgMode.Out, parameterName, this.getTypeName(sqlType.getVendorTypeNumber()), null);
            }
        }

        throw new IllegalArgumentException("registerOutParameter need typeName.");
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        this.setParameter(JdbcArgMode.Out, parameterName, this.getTypeName(sqlType), null);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        this.setParameter(JdbcArgMode.Out, parameterName, this.getTypeName(sqlType), null);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        if (StringUtils.isNotBlank(typeName)) {
            this.setParameter(JdbcArgMode.Out, parameterName, typeName, null);
        } else {
            this.setParameter(JdbcArgMode.Out, parameterName, this.getTypeName(sqlType), null);
        }
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.registerOutParameter("arg" + parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.registerOutParameter("arg" + parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.registerOutParameter("arg" + parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.Out, "arg" + parameterIndex, this.getTypeName(sqlType), null);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.setParameter(JdbcArgMode.Out, "arg" + parameterIndex, this.getTypeName(sqlType), null);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        this.checkParameterIndex(parameterIndex);
        this.registerOutParameter("arg" + parameterIndex, sqlType, typeName);
    }

    @Override
    public boolean wasNull() throws SQLException {
        this.checkOpen();
        if (this.lastResultOut == null) {
            throw new SQLException("wasNull cannot be call before fetching a result.");
        } else {
            return this.lastResultOut.wasNull();
        }
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getBoolean("arg" + parameterIndex);
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getBoolean(parameterName);
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getByte("arg" + parameterIndex);
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getByte(parameterName);
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getShort("arg" + parameterIndex);
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getShort(parameterName);
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getInt("arg" + parameterIndex);
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getInt(parameterName);
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getInt("arg" + parameterIndex);
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getLong(parameterName);
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getFloat("arg" + parameterIndex);
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getFloat(parameterName);
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getDouble("arg" + parameterIndex);
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getDouble(parameterName);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getBigDecimal("arg" + parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getBigDecimal("arg" + parameterIndex, scale);
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getBigDecimal(parameterName);
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getBytes("arg" + parameterIndex);
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getBytes(parameterName);
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getString("arg" + parameterIndex);
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getString(parameterName);
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getNString("arg" + parameterIndex);
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getNString(parameterName);
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getDate("arg" + parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getDate("arg" + parameterIndex, cal);
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getDate(parameterName);
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getDate(parameterName, cal);
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getTime("arg" + parameterIndex);
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getTime("arg" + parameterIndex, cal);
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getTime(parameterName);
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getTime(parameterName, cal);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getTimestamp("arg" + parameterIndex);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getTimestamp("arg" + parameterIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getTimestamp(parameterName);
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getTimestamp(parameterName, cal);
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getClob("arg" + parameterIndex);
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getClob(parameterName);
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getNClob("arg" + parameterIndex);
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getNClob(parameterName);
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getBlob("arg" + parameterIndex);
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getBlob(parameterName);
    }

    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getURL("arg" + parameterIndex);
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getURL(parameterName);
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getSQLXML("arg" + parameterIndex);
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getSQLXML(parameterName);
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getRowId("arg" + parameterIndex);
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getRowId(parameterName);
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getNCharacterStream("arg" + parameterIndex);
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getNCharacterStream(parameterName);
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getCharacterStream("arg" + parameterIndex);
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getCharacterStream(parameterName);
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getRef("arg" + parameterIndex);
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getRef(parameterName);
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getArray("arg" + parameterIndex);
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getArray(parameterName);
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getObject("arg" + parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getObject("arg" + parameterIndex, map);
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        this.checkOpen();
        this.checkParameterIndex(parameterIndex);
        return this.getOutParameter().getObject("arg" + parameterIndex, type);
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getObject(parameterName);
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getObject(parameterName, map);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        this.checkOpen();
        return this.getOutParameter().getObject(parameterName, type);
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, this.getTypeName(sqlType), null);
    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        if (StringUtils.isNotBlank(typeName)) {
            this.setParameter(JdbcArgMode.In, parameterName, typeName, null);
        } else {
            this.setParameter(JdbcArgMode.In, parameterName, this.getTypeName(sqlType), null);
        }
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Boolean, x);
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Byte, x);
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Short, x);
    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Int, x);
    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Long, x);
    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Float, x);
    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Double, x);
    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.BigDecimal, x);
    }

    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        String value = val == null ? null : val.toString();
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
    }

    @Override
    public void setString(String parameterName, String value) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        this.checkOpen();
        try {
            String value = reader == null ? null : IOUtils.readToString(reader);
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        this.checkOpen();
        try {
            String value = reader == null ? null : IOUtils.readToString(reader, length);
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.String, value);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        this.setClob(parameterName, x == null ? null : x.getCharacterStream());
    }

    @Override
    public void setNClob(String parameterName, NClob x) throws SQLException {
        this.setClob(parameterName, x == null ? null : x.getCharacterStream());
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        this.setClob(parameterName, reader);
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        this.setClob(parameterName, reader, length);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        this.setClob(parameterName, reader);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        this.setClob(parameterName, reader, length);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        this.setClob(parameterName, reader, length);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader reader) throws SQLException {
        this.setClob(parameterName, reader);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        this.setClob(parameterName, reader, length);
    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, null);
        } else {
            this.checkOpen();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copyLarge(x, out);
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, out.toByteArray());
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        this.setBinaryStream(parameterName, x, (long) length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Bytes, null);
        } else {
            this.checkOpen();
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
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        this.setBinaryStream(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        this.setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        this.setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        this.setBinaryStream(parameterName, x == null ? null : x.getBinaryStream());
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        this.setBinaryStream(parameterName, inputStream);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        this.setBinaryStream(parameterName, inputStream, length);
    }

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlDate, x);
    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlDate, null);
        } else {
            if (cal == null) {
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlDate, x);
            } else {
                ZonedDateTime zonedDate = x.toLocalDate().atStartOfDay(cal.getTimeZone().toZoneId());
                OffsetDateTime offsetDateTime = zonedDate.toOffsetDateTime();
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.OffsetDateTime, offsetDateTime);
            }
        }
    }

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlTime, x);
    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlTime, null);
        } else {
            if (cal == null) {
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlTime, x);
            } else {
                ZonedDateTime zonedTime = x.toInstant().atZone(cal.getTimeZone().toZoneId());
                OffsetTime offsetTime = OffsetTime.of(zonedTime.toLocalTime(), zonedTime.getOffset());
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.OffsetTime, offsetTime);
            }
        }
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlTimestamp, x);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlTimestamp, null);
        } else {
            if (cal == null) {
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.SqlTimestamp, x);
            } else {
                ZonedDateTime zonedTime = x.toInstant().atZone(cal.getTimeZone().toZoneId());
                OffsetDateTime offsetDateTime = zonedTime.toOffsetDateTime();
                this.setParameter(JdbcArgMode.In, parameterName, AdapterType.OffsetDateTime, offsetDateTime);
            }
        }
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type RowId not supported");
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type SQLXML not supported");
    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        if (x == null) {
            this.setParameter(JdbcArgMode.In, parameterName, AdapterType.Unknown, null);
        } else {
            String typeName = this.jdbcConn.typeSupport().getTypeName(x.getClass());
            this.setParameter(JdbcArgMode.In, parameterName, typeName, x);
        }
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, this.getTypeName(targetSqlType), x);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        this.setParameter(JdbcArgMode.In, parameterName, this.getTypeName(targetSqlType), x);
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        if (targetSqlType != null) {
            if (StringUtils.isNotBlank(targetSqlType.getName())) {
                this.setParameter(JdbcArgMode.In, parameterName, targetSqlType.getName(), x);
            }
            if (targetSqlType.getVendorTypeNumber() != null) {
                this.setParameter(JdbcArgMode.In, parameterName, this.getTypeName(targetSqlType.getVendorTypeNumber()), x);
            }
        }

        throw new IllegalArgumentException("setObject need targetSqlType.");
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        this.setObject(parameterName, x, targetSqlType);
    }
}
