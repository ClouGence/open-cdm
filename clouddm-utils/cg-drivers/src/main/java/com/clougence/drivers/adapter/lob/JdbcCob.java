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

import java.io.*;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;

import com.clougence.drivers.adapter.JdbcErrorCode;

public class JdbcCob implements Clob, NClob, JdbcOutputStreamWatcher, JdbcWriterWatcher {

    protected String charData;

    public JdbcCob(String charDataInit){
        this.charData = charDataInit;
    }

    @Override
    public InputStream getAsciiStream() {
        if (this.charData != null) {
            return new ByteArrayInputStream(this.charData.getBytes());
        }

        return null;
    }

    @Override
    public Reader getCharacterStream() {
        if (this.charData != null) {
            return new StringReader(this.charData);
        }

        return null;
    }

    @Override
    public String getSubString(long startPos, int length) throws SQLException {
        if (startPos < 1) {
            throw new SQLException("CLOB start position can not be < 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        int adjustedStartPos = (int) startPos - 1;
        int adjustedEndIndex = adjustedStartPos + length;
        if (this.charData != null) {
            if (adjustedEndIndex > this.charData.length()) {
                throw new SQLException("CLOB start position + length can not be > length of CLOB", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
            return this.charData.substring(adjustedStartPos, adjustedEndIndex);
        } else {
            return null;
        }
    }

    @Override
    public long length() {
        if (this.charData != null) {
            return this.charData.length();
        } else {
            return 0;
        }
    }

    @Override
    public long position(java.sql.Clob arg0, long arg1) throws SQLException {
        return position(arg0.getSubString(1L, (int) arg0.length()), arg1);
    }

    @Override
    public long position(String stringToFind, long startPos) throws SQLException {
        if (startPos < 1) {
            throw new SQLException("Illegal starting position for search, '" + startPos + "'", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        if (this.charData != null) {
            if ((startPos - 1) > this.charData.length()) {
                throw new SQLException("Starting position for search is past end of CLOB", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
            }
            int pos = this.charData.indexOf(stringToFind, (int) (startPos - 1));
            return (pos == -1) ? (-1) : (pos + 1);
        } else {
            return -1;
        }
    }

    @Override
    public OutputStream setAsciiStream(long indexToWriteAt) throws SQLException {
        if (indexToWriteAt < 1) {
            throw new SQLException("indexToWriteAt must be >= 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        JdbcWatchableOutputStream bytesOut = new JdbcWatchableOutputStream();
        bytesOut.setWatcher(this);
        bytesOut.write(this.charData.getBytes(), 0, (int) (indexToWriteAt - 1));
        return bytesOut;
    }

    @Override
    public Writer setCharacterStream(long indexToWriteAt) throws SQLException {
        if (indexToWriteAt < 1) {
            throw new SQLException("indexToWriteAt must be >= 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        JdbcWatchableWriter writer = new JdbcWatchableWriter();
        writer.setWatcher(this);
        //
        // Don't call write() if nothing to write...
        //
        if (indexToWriteAt > 1) {
            writer.write(this.charData, 0, (int) (indexToWriteAt - 1));
        }
        return writer;
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        if (pos < 1) {
            throw new SQLException("Starting position can not be < 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
        if (str == null) {
            throw new SQLException("String to set can not be NULL", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        StringBuilder charBuf = new StringBuilder(this.charData);
        pos--;
        int strLength = str.length();
        charBuf.replace((int) pos, (int) (pos + strLength), str);
        this.charData = charBuf.toString();
        return strLength;
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        if (pos < 1) {
            throw new SQLException("Starting position can not be < 1", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
        if (str == null) {
            throw new SQLException("String to set can not be NULL", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        StringBuilder charBuf = new StringBuilder(this.charData);
        pos--;
        try {
            String replaceString = str.substring(offset, offset + len);
            charBuf.replace((int) pos, (int) (pos + replaceString.length()), replaceString);
        } catch (StringIndexOutOfBoundsException e) {
            throw new SQLException(e.getMessage(), JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT, e);
        }

        this.charData = charBuf.toString();
        return len;
    }

    @Override
    public void truncate(long length) throws SQLException {
        if (length > this.charData.length()) {
            throw new SQLException("Cannot truncate CLOB of length " + this.charData.length() + " to length of " + length);
        }

        this.charData = this.charData.substring(0, (int) length);
    }

    @Override
    public void writerClosed(JdbcWatchableWriter out) {
        int dataLength = out.size();
        if (dataLength < this.charData.length()) {
            out.write(this.charData, dataLength, this.charData.length() - dataLength);
        }

        this.charData = out.toString();
    }

    @Override
    public void streamClosed(JdbcWatchableStream out) {
        int streamSize = out.size();
        if (streamSize < this.charData.length()) {
            out.write(this.charData.getBytes(), streamSize, this.charData.length() - streamSize);
        }

        byte[] byteArray = out.toByteArray();
        char[] charArray = new char[byteArray.length];
        int readpoint = 0;
        int readLength = byteArray.length;
        for (int i = 0; i < readLength; i++) {
            charArray[i] = (char) byteArray[readpoint];
            readpoint++;
        }

        this.charData = new String(charArray);
    }

    @Override
    public void free() {
        this.charData = null;
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        return new StringReader(getSubString(pos, (int) length));
    }
}
