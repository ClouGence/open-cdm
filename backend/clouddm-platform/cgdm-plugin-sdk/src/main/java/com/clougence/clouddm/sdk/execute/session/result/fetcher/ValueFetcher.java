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
package com.clougence.clouddm.sdk.execute.session.result.fetcher;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public interface ValueFetcher {

    ValueType getType();

    long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Boolean getBoolean(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Byte getByte(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Short getShort(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Integer getInteger(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Long getLong(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    BigInteger getBigInteger(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Float getFloat(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Double getDouble(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    byte[] getBytes(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    LocalDate getDate(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    LocalTime getTime(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    OffsetTime getTimeZ(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    LocalDateTime getDateTime(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    OffsetDateTime getDateTimeZ(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;

    InputStream getInputStream(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException;
}
