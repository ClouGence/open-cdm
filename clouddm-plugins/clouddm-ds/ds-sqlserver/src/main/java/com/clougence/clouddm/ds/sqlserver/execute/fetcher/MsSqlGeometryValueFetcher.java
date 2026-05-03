package com.clougence.clouddm.ds.sqlserver.execute.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

public class MsSqlGeometryValueFetcher extends StringAsClobFetcher {

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            byte[] geometryBytes = rs.getBytes(columnName);
            if (geometryBytes == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                try {
                    com.microsoft.sqlserver.jdbc.Geometry parse = com.microsoft.sqlserver.jdbc.Geometry.deserialize(geometryBytes);
                    String wkt = parse.asTextZM();
                    byte[] wktBytes = wkt.getBytes();
                    fcd = StringValueFCD.ofInMemory(true, wkt.length(), wkt.length(), wkt, wktBytes);
                } catch (Exception e) {
                    String wkt = "WKB Error :" + e.getMessage();
                    byte[] wktBytes = wkt.getBytes();
                    fcd = StringValueFCD.ofInMemory(false, wkt.length(), wkt.length(), wkt, wktBytes);
                }
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }
}
