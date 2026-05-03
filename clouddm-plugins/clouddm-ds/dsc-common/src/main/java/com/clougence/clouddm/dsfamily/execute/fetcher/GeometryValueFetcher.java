package com.clougence.clouddm.dsfamily.execute.fetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKBReader;

import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;
import com.clougence.utils.HexadecimalUtils;

public class GeometryValueFetcher extends StringAsClobFetcher {

    private static final GeometryFactory factory = new GeometryFactory();

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            String str = rs.getString(columnName);
            if (str == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                try {
                    byte[] geometryBytes = HexadecimalUtils.hex2bytes(str);
                    Geometry object = new WKBReader(factory).read(geometryBytes);
                    String wkt = object.toText();
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
