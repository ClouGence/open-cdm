package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

public class FloatValueFetcher extends AbstractValueFetcher {

    public FloatValueFetcher(){
        super(ValueType.Float);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 4;
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Float floatValue = getFloat(columnName, rs, ctx);
        return floatValue == null ? null : new BigDecimal(floatValue);
    }

    @Override
    public Float getFloat(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return tryWasNull(rs.getFloat(columnName), rs);
    }

    // only for array. getSize untrustworthy
    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Float b = tryWasNull(rs.getFloat(columnName), rs);
        return b == null ? null : b.toString();
    }

    // only for array. getSize untrustworthy
    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Float b = tryWasNull(rs.getFloat(columnName), rs);
        return b == null ? null : new StringReader(b.toString());
    }
}
