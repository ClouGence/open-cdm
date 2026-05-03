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

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.*;

import com.clougence.drivers.adapter.lob.JdbcBob;
import com.clougence.drivers.adapter.lob.JdbcCob;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

class JdbcResultSet implements ResultSet, Closeable {

    private final AdapterCursor            cursor;
    private final JdbcStatement            statement;
    private boolean                        closed        = false;
    private boolean                        wasNull       = false;
    private boolean                        wasLast       = false;
    private int                            rowNumber;
    private final Map<Integer, String>     indexToName   = new LinkedHashMap<>();
    private final Map<String, Integer>     nameToIndex   = new LinkedHashMap<>();
    private final Map<String, String>      nameToType    = new LinkedHashMap<>();
    private final Map<String, TypeConvert> nameToConvert = new LinkedHashMap<>();

    JdbcResultSet(JdbcStatement statement, AdapterCursor cursor){
        this.statement = statement;
        this.cursor = cursor;
        List<JdbcColumn> columns = cursor.columns();
        for (int i = 0; i < columns.size(); i++) {
            String label = columns.get(i).name;
            String type = columns.get(i).type;
            int index = i + 1;

            this.nameToIndex.put(label, index);
            this.indexToName.put(index, label);
            this.nameToType.put(label, type);
        }
    }

    private Object columnValue(String name) throws SQLException {
        this.checkOpen();
        Integer columnIndex = this.nameToIndex.get(name);
        if (columnIndex == null) {
            throw new SQLException("Invalid column " + name);
        } else {
            return columnValue(columnIndex);
        }
    }

    private String columnLabel(int columnIndex) throws SQLException {
        this.checkOpen();
        if (columnIndex <= 0 || columnIndex > this.cursor.columns().size()) {
            throw new SQLException("Invalid column " + columnIndex);
        }
        return this.indexToName.get(columnIndex);
    }

    private Object columnValue(int columnIndex) throws SQLException {
        this.checkOpen();
        if (columnIndex <= 0 || columnIndex > this.cursor.columns().size()) {
            throw new SQLException("Invalid column " + columnIndex);
        }

        if (this.wasLast || this.rowNumber < 1) {
            throw new SQLException("No row available");
        }

        Object object;
        try {
            object = this.cursor.column(columnIndex);
        } catch (Exception iae) {
            throw new SQLException(iae.getMessage());
        }
        this.wasNull = (object == null);
        return object;
    }

    private <T> T convertTo(String columnLabel, Object value, Class<?> toType) {
        if (value == null) {
            return null;
        }

        TypeConvert convert = this.nameToConvert.computeIfAbsent(columnLabel, c -> {
            String typeName = this.nameToType.getOrDefault(c, AdapterType.Unknown);
            TypeSupport typeSupport = this.statement.jdbcConn.typeSupport();
            return typeSupport.findConvert(typeName, toType);
        });
        if (convert != null) {
            return (T) convert.convert(toType, value);
        } else {
            throw new ClassCastException("the type " + value.getClass().getName() + " cannot be as " + toType.getName());
        }
    }

    private Object convertTimeZone(Object value, Calendar cal) {
        if (cal != null) {
            if (value instanceof OffsetTime) {
                ZoneOffset zoneOffset = ZoneOffset.of(cal.getTimeZone().getID());
                value = ((OffsetTime) value).withOffsetSameInstant(zoneOffset);
            } else if (value instanceof OffsetDateTime) {
                ZoneOffset zoneOffset = ZoneOffset.of(cal.getTimeZone().getID());
                value = ((OffsetDateTime) value).withOffsetSameInstant(zoneOffset);
            }
        }
        return value;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        this.checkOpen();
        return this.statement.jdbcConn.adapterConnection().unwrap(iface, this);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        this.checkOpen();
        return this.statement.jdbcConn.adapterConnection().unwrap(iface, this) != null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkOpen();
        SQLWarning sqlWarning = null;
        for (String warning : this.cursor.warnings()) {
            if (sqlWarning == null) {
                sqlWarning = new SQLWarning(warning);
                continue;
            }
            sqlWarning.setNextWarning(new SQLWarning(warning));
        }

        return sqlWarning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkOpen();
        this.cursor.clearWarnings();
    }

    @Override
    public boolean isClosed() { return this.closed || this.statement.isClosed(); }

    @Override
    public void close() {
        if (!this.closed) {
            this.closed = true;
            IOUtils.closeQuietly(this.cursor);
        }
    }

    protected void checkOpen() throws SQLException {
        if (this.isClosed()) {
            throw new SQLException("ResultSet is closed", JdbcErrorCode.SQL_STATE_IS_CLOSED);
        }
    }

    @Override
    public Statement getStatement() throws SQLException {
        checkOpen();
        return this.statement;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        checkOpen();
        return new JdbcResultSetMetaData(this, this.statement, this.cursor.columns());
    }

    @Override
    public String getCursorName() throws SQLException {
        checkOpen();
        return null;
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        checkOpen();
        Integer index = this.nameToIndex.get(columnLabel);
        if (index == null) {
            throw new SQLException("invalid column label [" + columnLabel + "]");
        } else {
            return index;
        }
    }

    @Override
    public boolean next() throws SQLException {
        checkOpen();
        if (this.cursor.next()) {
            this.rowNumber++;
            return true;
        }
        this.wasLast = true;
        return false;
    }

    //

    @Override
    public int getType() throws SQLException {
        checkOpen();
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        checkOpen();
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getHoldability() throws SQLException {
        checkOpen();
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public int getRow() { return this.rowNumber; }

    @Override
    public boolean isBeforeFirst() { return (this.rowNumber == 0); }

    @Override
    public boolean isAfterLast() { return (this.rowNumber > 0 && this.wasLast); }

    @Override
    public boolean isFirst() { return (this.rowNumber == 1); }

    @Override
    public boolean isLast() throws SQLException {
        throw new SQLFeatureNotSupportedException("isLast not supported");
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        checkOpen();
        if (rows < 0) {
            throw new SQLException("Rows is negative");
        }
        if (rows != getFetchSize()) {
            throw new SQLException("Fetch size cannot be changed");
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        checkOpen();
        return this.cursor.batchSize();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        checkOpen();
        if (direction != ResultSet.FETCH_FORWARD) {
            throw new SQLException("Fetch direction must be FETCH_FORWARD");
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        checkOpen();
        return ResultSet.FETCH_FORWARD;
    }

    //

    @Override
    public boolean wasNull() throws SQLException {
        this.checkOpen();
        return this.wasNull;
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        Boolean res = this.getObject(columnIndex, Boolean.class);
        return res != null && res;
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        Boolean res = this.getObject(columnLabel, Boolean.class);
        return res != null && res;
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        Byte res = this.getObject(columnIndex, Byte.class);
        return res == null ? 0 : res;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        Byte res = this.getObject(columnLabel, Byte.class);
        return res == null ? 0 : res;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        Short res = this.getObject(columnIndex, Short.class);
        return res == null ? 0 : res;
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        Short res = this.getObject(columnLabel, Short.class);
        return res == null ? 0 : res;
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        Integer res = this.getObject(columnIndex, Integer.class);
        return res == null ? 0 : res;
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        Integer res = this.getObject(columnLabel, Integer.class);
        return res == null ? 0 : res;
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        Long res = this.getObject(columnIndex, Long.class);
        return res == null ? 0 : res;
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        Long res = this.getObject(columnLabel, Long.class);
        return res == null ? 0 : res;
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        Float res = this.getObject(columnIndex, Float.class);
        return res == null ? 0.0f : res;
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        Float res = this.getObject(columnLabel, Float.class);
        return res == null ? 0.0f : res;
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        Double res = this.getObject(columnIndex, Double.class);
        return res == null ? 0.0d : res;
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        Double res = this.getObject(columnLabel, Double.class);
        return res == null ? 0.0d : res;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        BigDecimal res = this.getBigDecimal(columnIndex);
        return res == null ? null : res.setScale(scale, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        BigDecimal res = this.getBigDecimal(columnLabel);
        return res == null ? null : res.setScale(scale, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return this.getObject(columnIndex, BigDecimal.class);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return this.getObject(columnLabel, BigDecimal.class);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return this.getDate(columnIndex, null);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return this.getDate(columnLabel, null);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        this.checkOpen();
        String label = this.columnLabel(columnIndex);
        Object value = this.columnValue(columnIndex);
        if (value == null) {
            return null;
        } else {
            return this.convertTo(label, this.convertTimeZone(value, cal), Date.class);
        }
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnLabel);
        if (value == null) {
            return null;
        } else {
            return this.convertTo(columnLabel, this.convertTimeZone(value, cal), Date.class);
        }
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return this.getTime(columnIndex, null);
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return this.getTime(columnLabel, null);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        this.checkOpen();
        String label = this.columnLabel(columnIndex);
        Object value = this.columnValue(columnIndex);
        if (value == null) {
            return null;
        } else {
            return this.convertTo(label, this.convertTimeZone(value, cal), Time.class);
        }
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnLabel);
        if (value == null) {
            return null;
        } else {
            return this.convertTo(columnLabel, this.convertTimeZone(value, cal), Time.class);
        }
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return this.getTimestamp(columnIndex, null);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return this.getTimestamp(columnLabel, null);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        this.checkOpen();
        String label = this.columnLabel(columnIndex);
        Object value = this.columnValue(columnIndex);
        if (value == null) {
            return null;
        } else {
            return this.convertTo(label, this.convertTimeZone(value, cal), Timestamp.class);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnLabel);
        if (value == null) {
            return null;
        } else {
            return this.convertTo(columnLabel, this.convertTimeZone(value, cal), Timestamp.class);
        }
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return this.getObject(columnIndex, String.class);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return this.getObject(columnLabel, String.class);
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return this.getObject(columnIndex, String.class);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        return this.getObject(columnLabel, String.class);
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        String str = this.getObject(columnIndex, String.class);
        return str == null ? null : new StringReader(str);
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        String str = this.getObject(columnLabel, String.class);
        return str == null ? null : new StringReader(str);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        String str = this.getObject(columnIndex, String.class);
        return str == null ? null : new StringReader(str);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        String str = this.getObject(columnLabel, String.class);
        return str == null ? null : new StringReader(str);
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        String str = this.getObject(columnIndex, String.class);
        return str == null ? null : new JdbcCob(str);
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        String str = this.getObject(columnLabel, String.class);
        return str == null ? null : new JdbcCob(str);
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        String str = this.getObject(columnIndex, String.class);
        return str == null ? null : new JdbcCob(str);
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        String str = this.getObject(columnLabel, String.class);
        return str == null ? null : new JdbcCob(str);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return this.getObject(columnIndex, byte[].class);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return this.getObject(columnLabel, byte[].class);
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        byte[] res = this.getObject(columnIndex, byte[].class);
        return res == null ? null : new JdbcBob(res);
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        byte[] res = this.getObject(columnLabel, byte[].class);
        return res == null ? null : new JdbcBob(res);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return this.getBinaryStream(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return this.getBinaryStream(columnLabel);
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return this.getBinaryStream(columnIndex);
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return this.getBinaryStream(columnLabel);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        byte[] res = this.getObject(columnIndex, byte[].class);
        return res == null ? null : new ByteArrayInputStream(res);
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        byte[] res = this.getObject(columnLabel, byte[].class);
        return res == null ? null : new ByteArrayInputStream(res);
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        String urlStr = this.getString(columnIndex);
        if (StringUtils.isBlank(urlStr)) {
            return null;
        }
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        String urlStr = this.getString(columnLabel);
        if (StringUtils.isBlank(urlStr)) {
            return null;
        }
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return this.columnValue(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return this.columnValue(columnLabel);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        this.checkOpen();
        String label = this.columnLabel(columnIndex);
        Object value = this.columnValue(columnIndex);
        return this.convertTo(label, value, Objects.requireNonNull(type, "the to type is null."));
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnLabel);
        return this.convertTo(columnLabel, value, Objects.requireNonNull(type, "the to type is null."));
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        this.checkOpen();
        String label = this.columnLabel(columnIndex);
        Object value = this.columnValue(columnIndex);
        if (value == null) {
            return null;
        }
        // 该方法是为了数据库自定义类型而设计，基本上涉及 SQL STRUCT、REF 或 ARRAY 类型。目前属于不支持的状态。
        // map 参数中是用来表示数据库自定义类型字段的 java 映射类型。
        throw new SQLFeatureNotSupportedException("not support getObject(String,Map)");
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnLabel);
        if (value == null) {
            return null;
        }
        // 该方法是为了数据库自定义类型而设计，基本上涉及 SQL STRUCT、REF 或 ARRAY 类型。目前属于不支持的状态。
        // map 参数中是用来表示数据库自定义类型字段的 java 映射类型。
        throw new SQLFeatureNotSupportedException("not support getObject(String,Map)");
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type Ref not supported");
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type Ref not supported");
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnIndex);
        if (value == null) {
            return null;
        }
        if (value instanceof Array) {
            return (Array) value;
        } else {
            throw new SQLFeatureNotSupportedException("column value is not java.sql.Array");
        }
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        this.checkOpen();
        Object value = this.columnValue(columnLabel);
        if (value == null) {
            return null;
        }
        if (value instanceof Array) {
            return (Array) value;
        } else {
            throw new SQLFeatureNotSupportedException("column value is not java.sql.Array");
        }
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type SQLXML not supported");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type SQLXML not supported");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type SQLXML not supported");
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        this.checkOpen();
        throw new SQLFeatureNotSupportedException("type RowId not supported");
    }

    //

    @Override
    public void beforeFirst() throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public void afterLast() throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean first() throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean last() throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean previous() throws SQLException {
        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s:row %d:cursor size %d:%s", //
                getClass().getSimpleName(), //
                this.rowNumber,             //
                this.cursor.batchSize(),    //
                this.cursor.columns()       //
        );
    }
}
