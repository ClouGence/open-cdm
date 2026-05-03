package com.clougence.clouddm.dsfamily.mysql.execute.fetcher;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKBReader;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

public class MyGeometryValueFetcher extends StringAsClobFetcher {

    private static final GeometryFactory factory = new GeometryFactory();

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            byte[] geometryBytes = rs.getBytes(columnName);
            if (geometryBytes == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                try {
                    int srid = ByteBuffer.wrap(geometryBytes, 0, 4).asIntBuffer().get();
                    WKBReader wkbReader = new WKBReader(factory);
                    Geometry object = wkbReader.read(Arrays.copyOfRange(geometryBytes, 4, geometryBytes.length));
                    object.setSRID(srid);

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
