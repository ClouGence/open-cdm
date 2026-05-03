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
