package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;

public class BooleanValueFetcher extends AbstractValueFetcher {

    public BooleanValueFetcher(){
        super(ValueType.Boolean);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 1;
    }

    @Override
    public Boolean getBoolean(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return tryWasNull(rs.getBoolean(columnName), rs);
    }

    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Boolean b = tryWasNull(rs.getBoolean(columnName), rs);
        return b == null ? null : (b ? "true" : "false");
    }

    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        Boolean b = tryWasNull(rs.getBoolean(columnName), rs);
        return b == null ? null : new StringReader(b ? "true" : "false");
    }
}
