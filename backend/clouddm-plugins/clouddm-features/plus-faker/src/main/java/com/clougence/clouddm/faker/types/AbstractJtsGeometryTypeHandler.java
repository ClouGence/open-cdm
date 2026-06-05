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
package com.clougence.clouddm.faker.types;

import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.*;

import net.hasor.cobble.StringUtils;

/**
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractJtsGeometryTypeHandler<T> extends BaseTypeHandler<T> {

    protected static final GeometryFactory factory = new GeometryFactory();

    protected static String toWKT(byte[] wkb) throws SQLException {
        if (wkb == null) {
            return null;
        }
        try {
            Geometry object = new WKBReader(factory).read(wkb);
            return new WKTWriter().write(object);
        } catch (ParseException e) {
            throw new SQLException(e);
        }
    }

    protected static byte[] toWKB(String wkt) throws SQLException {
        if (StringUtils.isBlank(wkt)) {
            return null;
        }
        try {
            Geometry object = new WKTReader(factory).read(wkt);
            return new WKBWriter().write(object);
        } catch (ParseException e) {
            throw new SQLException(e);
        }
    }
}
