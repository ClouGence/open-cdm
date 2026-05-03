package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

public class LongValueFetcher extends AbstractValueFetcher {

    public LongValueFetcher(){
        super(ValueType.Long);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 8;
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Long longValue = getLong(columnName, rs, ctx);
        return longValue == null ? null : new BigDecimal(longValue);
    }

    @Override
    public Long getLong(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return tryWasNull(rs.getLong(columnName), rs);
    }

    // only for array. getSize untrustworthy
    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Long b = tryWasNull(rs.getLong(columnName), rs);
        return b == null ? null : b.toString();
    }

    // only for array. getSize untrustworthy
    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Long b = tryWasNull(rs.getLong(columnName), rs);
        return b == null ? null : new StringReader(b.toString());
    }
}
