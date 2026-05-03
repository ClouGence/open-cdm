package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

public class ByteValueFetcher extends AbstractValueFetcher {

    public ByteValueFetcher(){
        super(ValueType.Byte);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 1;
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Byte byteValue = getByte(columnName, rs, ctx);
        return byteValue == null ? null : new BigDecimal(byteValue);
    }

    @Override
    public Byte getByte(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return tryWasNull(rs.getByte(columnName), rs);
    }

    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Byte b = tryWasNull(rs.getByte(columnName), rs);
        return b == null ? null : b.toString();
    }

    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Byte b = tryWasNull(rs.getByte(columnName), rs);
        return b == null ? null : new StringReader(b.toString());
    }
}
