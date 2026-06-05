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
package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractValueFetcher implements ValueFetcher {

    private final ValueType type;

    protected AbstractValueFetcher(ValueType type){
        this.type = type;
    }

    @Override
    public ValueType getType() { return type; }

    @Override
    public Boolean getBoolean(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Boolean tryWasNull(boolean value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public Byte getByte(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Byte tryWasNull(byte value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public Short getShort(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Short tryWasNull(short value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public Integer getInteger(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Integer tryWasNull(int value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public Long getLong(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Long tryWasNull(long value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public BigInteger getBigInteger(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public Float getFloat(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Float tryWasNull(float value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public Double getDouble(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static Double tryWasNull(double value, ResultSet record) throws SQLException {
        return record.wasNull() ? null : value;
    }

    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public byte[] getBytes(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public InputStream getInputStream(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public LocalDate getDate(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public LocalTime getTime(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public OffsetTime getTimeZ(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public LocalDateTime getDateTime(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    @Override
    public OffsetDateTime getDateTimeZ(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        throw new UnsupportedOperationException("Fetcher Unsupported.");
    }

    protected static File tmpFile(ValueFetcherContext ctx) throws IOException {
        String vfcId = ctx.getVfcId();
        String tempPath = ctx.getOptions().getTempPath();
        File vfcFile = new File(tempPath, vfcId + ".tmp");

        try {
            if (vfcFile.exists()) {
                for (int i = 0; i < 100; i++) {
                    if (!vfcFile.exists()) {
                        createTmpFile(vfcFile);
                        ctx.setTmpFile(vfcFile);
                        return vfcFile;
                    }
                    vfcFile = new File(tempPath, vfcId + "_" + i + ".tmp");
                }
                throw new IOException("Failed to create temporary file " + vfcFile.getAbsolutePath());
            } else {
                createTmpFile(vfcFile);
                ctx.setTmpFile(vfcFile);
                return vfcFile;
            }
        } catch (IOException e) {
            log.error("tempPath: " + tempPath + ", vfcId:" + vfcId + ", vfcFile: " + vfcFile + " " + e.getMessage(), e);
            throw e;
        }
    }

    private static boolean createTmpFile(File vfcFile) throws IOException {
        if (!vfcFile.getParentFile().exists()) {
            vfcFile.getParentFile().mkdirs();
        }
        return vfcFile.createNewFile();
    }
}
