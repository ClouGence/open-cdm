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
package com.clougence.clouddm.faker.seed.geometry;

import static com.clougence.utils.RandomUtils.*;
import static java.math.BigDecimal.ROUND_DOWN;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.clouddm.faker.utils.RandomRatio;
import com.clougence.utils.StringUtils;

/**
 * 几何信息 SeedFactory
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class GeometrySeedFactory implements SeedFactory<GeometrySeedConfig> {

    private static final GeometryFactory factory = new GeometryFactory();

    protected static byte[] toWKB(String wkt) {
        try {
            Geometry object = new WKTReader(factory).read(wkt);
            return new WKBWriter().write(object);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GeometrySeedConfig newConfig(SeedConfig contextType) {
        return new GeometrySeedConfig();
    }

    @Override
    public Supplier<Serializable> createSeed(GeometrySeedConfig seedConfig) {
        GeometryType geometryType = seedConfig.getGeometryType();
        RandomRatio<SpaceRange> range = seedConfig.getRange();
        int precision = Math.max(0, seedConfig.getPrecision());
        int minPointSize = Math.max(1, seedConfig.getMinPointSize());
        int maxPointSize = Math.max(1, seedConfig.getMaxPointSize());
        FormatType fmt = seedConfig.getFormatType();

        boolean allowNullable = seedConfig.isAllowNullable();
        Float nullableRatio = seedConfig.getNullableRatio();
        if (allowNullable && nullableRatio == null) {
            throw new IllegalStateException("allowNullable is true but, nullableRatio missing.");
        }

        return () -> {
            if (allowNullable && nextFloat(0, 100) < nullableRatio) {
                return null;
            } else {
                BigInteger pointCount = nextBigInteger(BigInteger.valueOf(minPointSize), BigInteger.valueOf(maxPointSize));
                return randomGeometry(geometryType, range.getByRandom(), precision, pointCount, fmt);
            }
        };
    }

    private static final GeometryType[] allTypes = GeometryType.values();

    private Serializable randomGeometry(GeometryType type, SpaceRange range, int precision, BigInteger pointCount, FormatType fmt) {
        while (type == GeometryType.Random) {
            type = allTypes[(int) nextLong(0, allTypes.length)];
        }

        switch (type) {
            case Point: {
                Point point = randomPoint(range.getPointA(), range.getPointB(), precision);
                return fmtResult(fmt, "(" + fmtPoint(point) + ")");
            }
            case Line: {
                // ax + by + c = 0
                Point pointA = randomPoint(range.getPointA(), range.getPointB(), precision);
                Point pointB = randomPoint(range.getPointA(), range.getPointB(), precision);
                BigDecimal sum = pointA.getX().add(pointB.getY()).negate();
                return fmtResult(fmt, "{" + pointA.getX().toPlainString() + "," + pointB.getY().toPlainString() + "," + sum.toPlainString() + "}");
            }
            case Lseg:
            case Box: {
                Point pointA = randomPoint(range.getPointA(), range.getPointB(), precision);
                Point pointB = randomPoint(range.getPointA(), range.getPointB(), precision);
                return fmtResult(fmt, "((" + fmtPoint(pointA) + "),(" + fmtPoint(pointB) + "))");
            }
            case Path: {
                String[] pathStr = new String[pointCount.intValue()];
                for (int i = 0; i < pathStr.length; i++) {
                    pathStr[i] = fmtPoint(randomPoint(range.getPointA(), range.getPointB(), precision));
                }
                return fmtResult(fmt, "((" + StringUtils.join(pathStr, "),(") + "))");
            }
            case Polygon: {
                String[] points = nextPolygon(range, precision, pointCount, this::fmtPoint);
                return fmtResult(fmt, "((" + StringUtils.join(points, "),(") + "))");
            }
            case MultiPolygon: {
                String[] points = nextPolygon(range, precision, pointCount, this::fmtMultiPolygon);
                String[] forMulti = new String[points.length + 1];
                System.arraycopy(points, 0, forMulti, 0, points.length);
                forMulti[points.length] = points[0];

                String multiPolygon = "((" + StringUtils.join(forMulti, ",") + "))";
                return fmtResult(fmt, "MULTIPOLYGON(" + multiPolygon + ")"); // 生成的图形是没有挖洞的
            }
            case Circle: {
                Point point = randomPoint(range.getPointA(), range.getPointB(), precision);
                BigDecimal radius = nextRadius(point, range, precision);
                return fmtResult(fmt, "((" + fmtPoint(point) + ")," + radius.toPlainString() + ")");
            }
            default: {
                throw new UnsupportedOperationException("unsupported GeometryType " + type);
            }
        }
    }

    private Serializable fmtResult(FormatType fmt, String wkt) {
        if (StringUtils.isBlank(wkt)) {
            return null;
        }
        return fmt == FormatType.WKB ? toWKB(wkt) : wkt;
    }

    private Point randomPoint(Point formBorder, Point toBorder, int precision) {
        BigDecimal randomX = nextDecimal(formBorder.getX(), toBorder.getX(), precision);
        BigDecimal randomY = nextDecimal(formBorder.getY(), toBorder.getY(), precision);
        return new Point(randomX, randomY);
    }

    private String fmtPoint(Point point) {
        return point.getX().toPlainString() + "," + point.getY().toPlainString();
    }

    private String fmtMultiPolygon(Point point) {
        return point.getX().toPlainString() + " " + point.getY().toPlainString();
    }

    private String[] nextPolygon(SpaceRange range, int precision, BigInteger count, Function<Point, String> fmtPoint) {
        int pointCount = Math.max(3, count.intValue());
        Point centrePoint = randomPoint(range.getPointA(), range.getPointB(), precision);
        String[] pathStr = new String[pointCount];
        double unitAngle = 360d / (double) pathStr.length;
        for (int i = 0; i < pathStr.length; i++) {
            // 角度和半径
            double curAngle = i * unitAngle;
            BigDecimal curRadius = nextRadius(centrePoint, curAngle, range, precision);
            // 多边形上的点
            BigDecimal x = centrePoint.getX().add(curRadius.multiply(BigDecimal.valueOf(Math.sin(curAngle)))).setScale(precision, ROUND_DOWN);
            BigDecimal y = centrePoint.getY().add(curRadius.multiply(BigDecimal.valueOf(Math.cos(curAngle)))).setScale(precision, ROUND_DOWN);
            pathStr[i] = fmtPoint.apply(new Point(x, y));
        }
        return pathStr;
    }

    private BigDecimal nextRadius(Point point, double dipAngle, SpaceRange range, int precision) {
        // TODO point 是矩形 range 中的一个点；dipAngle 是 point 为圆心的一个角；point 和 dipAngle 共同决定了一个射线；该射线最终会与 range 的一个边相交。
        //    - 求：point 到 range 相交点之间的长度 len
        //    - 返回 precision 表示能够表示的最小值 到 len 之间的一个随机数。结果如果有小数则最终精度按照 precision 来限定

        // 没有实现上面的逻辑，最多影响是 几何图形随机性不足或可能越出 range 的范围，不影响测试数据
        BigDecimal minRadius = BigDecimal.ONE.divide(new BigDecimal("1" + StringUtils.repeat("0", precision)));
        BigDecimal maxRadius = BigDecimal.valueOf(100);
        return nextDecimal(minRadius, maxRadius, precision);
    }

    private BigDecimal nextRadius(Point point, SpaceRange range, int precision) {
        // TODO point 是矩形 range 中的一个点；point 到 range 的边有无数的交点。
        //    - 求：point 到 交点中距离最短的那个长度 len
        //    - 返回 precision 表示能够表示的最小值 到 len 之间的一个随机数。结果如果有小数则最终精度按照 precision 来限定

        // 没有实现上面的逻辑，最多影响是 几何图形随机性不足或可能越出 range 的范围，不影响测试数据
        BigDecimal minRadius = BigDecimal.ONE.divide(new BigDecimal("1" + StringUtils.repeat("0", precision)));
        BigDecimal maxRadius = BigDecimal.valueOf(100);
        return nextDecimal(minRadius, maxRadius, precision);
    }
}
