package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueType;
import com.clougence.utils.format.WellKnowFormat;

public class TimeWithZoneValueFetcher extends AbstractValueFetcher {

    public TimeWithZoneValueFetcher(){
        super(ValueType.TimeZ);
    }

    @Override
    public long getSize(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        return 12;// long nanoOfDay, int zoneOffsetTotalSeconds
    }

    @Override
    public OffsetTime getTimeZ(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        try {
            return rs.getObject(columnName, OffsetTime.class);
        } catch (SQLException e1) {
            try {
                OffsetDateTime date = rs.getObject(columnName, OffsetDateTime.class);
                return date == null ? null : date.toOffsetTime();
            } catch (SQLException e2) {
                java.sql.Timestamp date = rs.getTimestamp(columnName);
                return date == null ? null : OffsetTime.of(date.toLocalDateTime().toLocalTime(), ZoneOffset.UTC);
            }
        }
    }

    // only for array. getSize untrustworthy
    @Override
    public String getString(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Time date = rs.getTime(columnName);
        return date == null ? null : getFormat(ctx).format(date.toLocalTime());
    }

    // only for array. getSize untrustworthy
    @Override
    public Reader getReader(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        java.sql.Time date = rs.getTime(columnName);
        return date == null ? null : new StringReader(getFormat(ctx).format(date.toLocalTime()));
    }

    private static WellKnowFormat getFormat(ValueFetcherContext ctx) {
        if (ctx == null) {
            return WellKnowFormat.WKF_OFFSET_TIME24_S9;
        }
        if (ctx.getOptions() == null) {
            return WellKnowFormat.WKF_OFFSET_TIME24_S9;
        }
        if (ctx.getOptions().getTimeWithZoneFormat() == null) {
            return WellKnowFormat.WKF_OFFSET_TIME24_S9;
        }

        return ctx.getOptions().getTimeWithZoneFormat();
    }
}
