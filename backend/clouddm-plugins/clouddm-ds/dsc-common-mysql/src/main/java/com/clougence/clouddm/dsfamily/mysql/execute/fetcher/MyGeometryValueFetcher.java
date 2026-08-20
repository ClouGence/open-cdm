/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.dsfamily.mysql.execute.fetcher;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKBReader;

import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyGeometryValueFetcher extends StringAsClobFetcher {

    private static final GeometryFactory           factory                   = new GeometryFactory();
    private static final Pattern                   AXIS_DIRECTION            = Pattern.compile("AXIS\\s*\\[\\s*\"[^\"]*\"\\s*,\\s*(NORTH|SOUTH|EAST|WEST)", Pattern.CASE_INSENSITIVE);
    private static final Map<Connection, String>   CONNECTION_KEYS           = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<String, Set<Integer>> LATITUDE_FIRST_SRS_CACHE = new ConcurrentHashMap<>();
    private static final CoordinateSequenceFilter  SWAP_XY_FILTER           = new CoordinateSequenceFilter() {

        @Override
        public void filter(CoordinateSequence sequence, int index) {
            double x = sequence.getX(index);
            sequence.setOrdinate(index, 0, sequence.getY(index));
            sequence.setOrdinate(index, 1, x);
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public boolean isGeometryChanged() {
            return true;
        }
    };

    public static void prepareSpatialReferenceAxes(Connection connection) {
        String connectionKey = connectionKey(connection);
        if (connectionKey == null) {
            return;
        }
        CONNECTION_KEYS.put(connection, connectionKey);
        if (LATITUDE_FIRST_SRS_CACHE.containsKey(connectionKey)) {
            return;
        }

        Set<Integer> latitudeFirstSrids = new HashSet<>();
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(//
                "SELECT SRS_ID, DEFINITION FROM information_schema.ST_SPATIAL_REFERENCE_SYSTEMS WHERE DEFINITION LIKE 'GEOGCS%'")) {
            while (resultSet.next()) {
                int srid = resultSet.getInt(1);
                if (isLatitudeFirst(resultSet.getString(2))) {
                    latitudeFirstSrids.add(srid);
                }
            }
        } catch (SQLException e) {
            log.debug("MySQL spatial reference axes are unavailable: {}", e.getMessage());
        }
        LATITUDE_FIRST_SRS_CACHE.putIfAbsent(connectionKey, Collections.unmodifiableSet(latitudeFirstSrids));
    }

    @Override
    protected StringValueFCD fetchState(String columnName, ResultSet rs, ValueFetcherContext ctx) throws SQLException {
        StringValueFCD fcd;
        if (ctx.getContext() == null || !(ctx.getContext() instanceof StringValueFCD)) {
            byte[] geometryBytes = rs.getBytes(columnName);
            if (geometryBytes == null) {
                fcd = StringValueFCD.ofInMemory(true, 0, 0, null, null);
            } else {
                try {
                    int srid = ByteBuffer.wrap(geometryBytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                    WKBReader wkbReader = new WKBReader(factory);
                    Geometry object = wkbReader.read(Arrays.copyOfRange(geometryBytes, 4, geometryBytes.length));
                    object.setSRID(srid);
                    if (isLatitudeFirst(rs, srid)) {
                        object.apply(SWAP_XY_FILTER);
                        object.geometryChanged();
                    }

                    String wkt = object.toText();
                    byte[] wktBytes = wkt.getBytes(StandardCharsets.UTF_8);
                    fcd = StringValueFCD.ofInMemory(true, wkt.length(), wkt.length(), wkt, wktBytes);
                } catch (Exception e) {
                    String wkt = "WKB Error :" + e.getMessage();
                    byte[] wktBytes = wkt.getBytes(StandardCharsets.UTF_8);
                    fcd = StringValueFCD.ofInMemory(false, wkt.length(), wkt.length(), wkt, wktBytes);
                }
            }
            ctx.setContext(fcd);
        } else {
            fcd = (StringValueFCD) ctx.getContext();
        }
        return fcd;
    }

    private static boolean isLatitudeFirst(ResultSet resultSet, int srid) {
        if (srid == 0) {
            return false;
        }
        try {
            String connectionKey = CONNECTION_KEYS.get(resultSet.getStatement().getConnection());
            if (connectionKey == null) {
                return false;
            }
            Set<Integer> latitudeFirstSrids = LATITUDE_FIRST_SRS_CACHE.get(connectionKey);
            return latitudeFirstSrids != null && latitudeFirstSrids.contains(srid);
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean isLatitudeFirst(String definition) {
        if (definition == null) {
            return false;
        }
        Matcher matcher = AXIS_DIRECTION.matcher(definition);
        if (!matcher.find()) {
            return false;
        }
        String firstDirection = matcher.group(1);
        if (!matcher.find()) {
            return false;
        }
        String secondDirection = matcher.group(1);
        return isNorthSouth(firstDirection) && isEastWest(secondDirection);
    }

    private static boolean isNorthSouth(String direction) {
        return "NORTH".equalsIgnoreCase(direction) || "SOUTH".equalsIgnoreCase(direction);
    }

    private static boolean isEastWest(String direction) {
        return "EAST".equalsIgnoreCase(direction) || "WEST".equalsIgnoreCase(direction);
    }

    private static String connectionKey(Connection connection) {
        try {
            return connection.getMetaData().getURL() + '\0' + connection.getMetaData().getUserName();
        } catch (SQLException e) {
            return null;
        }
    }
}
