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
package com.clougence.drivers.adapter.lob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import com.clougence.drivers.adapter.JdbcErrorCode;

/**
 * The representation (mapping) in the JavaTM programming language of an SQL BLOB value. An SQL BLOB is a built-in type that stores a Binary Large Object
 * as a column value in a row of a database table. The driver implements Blob using an SQL locator(BLOB), which means that a Blob object contains a logical
 * pointer to the SQL BLOB data rather than the data itself. A Blob object is valid for the duration of the transaction in which is was created. Methods in
 * the interfaces ResultSet, CallableStatement, and PreparedStatement, such as getBlob and setBlob allow a programmer to access an SQL BLOB value. The Blob
 * interface provides methods for getting the length of an SQL BLOB (Binary Large Object) value, for materializing a BLOB value on the client, and for
 * determining the position of a pattern of bytes within a BLOB value. This class is new in the JDBC 2.0 API.
 */
public class JdbcBob implements Blob, JdbcOutputStreamWatcher {

    //
    // This is a real brain-dead implementation of BLOB. Once I add streamability to the I/O for MySQL this will be more efficiently implemented
    // (except for the position() method, ugh).
    //

    /** The binary data that makes up this BLOB */
    private byte[]  binaryData = null;
    private boolean isClosed   = false;

    /**
     * Creates a BLOB encapsulating the given binary data
     * @param data   data to fill the Blob
     */
    public JdbcBob(byte[] data){
        setBinaryData(data);
    }

    private synchronized byte[] getBinaryData() { return this.binaryData; }

    @Override
    public synchronized java.io.InputStream getBinaryStream() throws SQLException {
        checkClosed();
        return new ByteArrayInputStream(getBinaryData());
    }

    @Override
    public synchronized byte[] getBytes(long pos, int length) throws SQLException {
        checkClosed();
        if (pos < 1) {
            throw new SQLException("pos argument can not be < 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        pos--;
        if (pos > this.binaryData.length) {
            throw new SQLException("pos argument can not be larger than the BLOB's length", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
        if (pos + length > this.binaryData.length) {
            throw new SQLException("pos + length arguments can not be larger than the BLOB's length", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        byte[] newData = new byte[length];
        System.arraycopy(getBinaryData(), (int) (pos), newData, 0, length);
        return newData;
    }

    @Override
    public synchronized long length() throws SQLException {
        checkClosed();
        return getBinaryData().length;
    }

    @Override
    public synchronized long position(byte[] pattern, long start) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public synchronized long position(java.sql.Blob pattern, long start) throws SQLException {
        checkClosed();
        return position(pattern.getBytes(0, (int) pattern.length()), start);
    }

    private synchronized void setBinaryData(byte[] newBinaryData) { this.binaryData = newBinaryData; }

    @Override
    public synchronized OutputStream setBinaryStream(long indexToWriteAt) throws SQLException {
        checkClosed();
        if (indexToWriteAt < 1) {
            throw new SQLException("indexToWriteAt must be >= 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        JdbcWatchableOutputStream bytesOut = new JdbcWatchableOutputStream();
        bytesOut.setWatcher(this);
        bytesOut.write(this.binaryData, 0, (int) (indexToWriteAt - 1));

        return bytesOut;
    }

    @Override
    public synchronized int setBytes(long writeAt, byte[] bytes) throws SQLException {
        checkClosed();

        return setBytes(writeAt, bytes, 0, bytes.length);
    }

    @Override
    public synchronized int setBytes(long writeAt, byte[] bytes, int offset, int length) throws SQLException {
        checkClosed();
        OutputStream bytesOut = setBinaryStream(writeAt);

        try {
            bytesOut.write(bytes, offset, length);
        } catch (IOException ioEx) {
            SQLException sqlEx = new SQLException("IO Error while writing bytes to blob", JdbcErrorCode.SQL_STATE_GENERAL_ERROR, ioEx);
            throw sqlEx;
        } finally {
            try {
                bytesOut.close();
            } catch (IOException doNothing) {
                // do nothing
            }
        }

        return length;
    }

    @Override
    public synchronized void streamClosed(JdbcWatchableStream out) {
        int streamSize = out.size();

        if (streamSize < this.binaryData.length) {
            out.write(this.binaryData, streamSize, this.binaryData.length - streamSize);
        }

        this.binaryData = out.toByteArray();
    }

    @Override
    public synchronized void truncate(long len) throws SQLException {
        checkClosed();
        if (len < 0) {
            throw new SQLException("len argument can not be < 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
        if (len > this.binaryData.length) {
            throw new SQLException("len argument can not be larger than the BLOB's length", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        // TODO: Do this without copying byte[]s by maintaining some end pointer on the original data

        byte[] newData = new byte[(int) len];
        System.arraycopy(getBinaryData(), 0, newData, 0, (int) len);
        this.binaryData = newData;
    }

    @Override
    public synchronized void free() {
        this.binaryData = null;
        this.isClosed = true;
    }

    @Override
    public synchronized InputStream getBinaryStream(long pos, long length) throws SQLException {
        checkClosed();

        if (pos < 1) {
            throw new SQLException("pos argument can not be < 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        } else {
            pos--;
        }

        if (pos > this.binaryData.length) {
            throw new SQLException("len argument can not be larger than the BLOB's length", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
        if (pos + length > this.binaryData.length) {
            throw new SQLException("pos + length arguments can not be larger than the BLOB's length", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        return new ByteArrayInputStream(getBinaryData(), (int) pos, (int) length);
    }

    private synchronized void checkClosed() throws SQLException {
        if (this.isClosed) {
            throw new SQLException("Invalid operation on closed BLOB", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }
}
