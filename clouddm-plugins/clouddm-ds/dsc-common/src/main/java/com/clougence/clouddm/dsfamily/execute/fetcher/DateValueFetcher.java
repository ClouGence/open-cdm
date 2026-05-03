package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;
import com.clougence.utils.format.WellKnowFormat;

public class DateValueFetcher extends AbstractValueFetcher {

    public DateValueFetcher(){
        super(ValueType.Date);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 8;// long epochDay
    }

    @Override
    public LocalDate getDate(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Date date = rs.getDate(columnName);
        return date == null ? null : date.toLocalDate();
    }

    // only for array. getSize untrustworthy
    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Date date = rs.getDate(columnName);
        return date == null ? null : getFormat(ctx).format(date.toLocalDate());
    }

    // only for array. getSize untrustworthy
    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Date date = rs.getDate(columnName);
        return date == null ? null : new StringReader(getFormat(ctx).format(date.toLocalDate()));
    }

    protected static WellKnowFormat getFormat(ValueFetcherContext ctx) {
        if (ctx == null) {
            return WellKnowFormat.WKF_DATE10;
        }
        if (ctx.getOptions() == null) {
            return WellKnowFormat.WKF_DATE10;
        }
        if (ctx.getOptions().getDataFormat() == null) {
            return WellKnowFormat.WKF_DATE10;
        }

        return ctx.getOptions().getDataFormat();
    }
}
