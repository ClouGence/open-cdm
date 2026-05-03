package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;
import com.clougence.utils.format.WellKnowFormat;

public class DateTimeValueFetcher extends AbstractValueFetcher {

    public DateTimeValueFetcher(){
        super(ValueType.DateTime);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 16;// long epochDay, long nanoOfDay
    }

    @Override
    public LocalDateTime getDateTime(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Timestamp date = rs.getTimestamp(columnName);
        return date == null ? null : date.toLocalDateTime();
    }

    // only for array. getSize untrustworthy
    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Timestamp date = rs.getTimestamp(columnName);
        return date == null ? null : getFormat(ctx).format(date.toLocalDateTime());
    }

    // only for array. getSize untrustworthy
    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Timestamp date = rs.getTimestamp(columnName);
        return date == null ? null : new StringReader(getFormat(ctx).format(date.toLocalDateTime()));
    }

    protected static WellKnowFormat getFormat(ValueFetcherContext ctx) {
        if (ctx == null) {
            return WellKnowFormat.WKF_DATE_TIME24_S9;
        }
        if (ctx.getOptions() == null) {
            return WellKnowFormat.WKF_DATE_TIME24_S9;
        }
        if (ctx.getOptions().getDataTimeFormat() == null) {
            return WellKnowFormat.WKF_DATE_TIME24_S9;
        }

        return ctx.getOptions().getDataTimeFormat();
    }
}
