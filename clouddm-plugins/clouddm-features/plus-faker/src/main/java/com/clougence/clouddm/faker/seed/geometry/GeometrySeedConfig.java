/*
 * Copyright 2015-2022 the original author or authors.
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

import org.apache.ibatis.type.StringTypeHandler;
import org.apache.ibatis.type.TypeHandler;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.faker.types.JtsGeometryWktAsWkbTypeHandler;
import com.clougence.clouddm.faker.types.TypeHandlerRegistryUtils;
import com.clougence.clouddm.faker.utils.RandomRatio;

/**
 * 几何图形 SeedConfig
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class GeometrySeedConfig extends SeedConfig {

    private GeometryType                  geometryType;
    private FormatType                    formatType;
    private final RandomRatio<SpaceRange> range = new RandomRatio<>(); //图形将在这个矩形范围
    private int                           precision;
    private int                           minPointSize;
    private int                           maxPointSize;

    public final SeedType getSeedType() { return SeedType.Geometry; }

    @Override
    protected TypeHandler<?> defaultTypeHandler() {
        return TypeHandlerRegistryUtils.getTypeHandler(String.class);
    }

    public GeometryType getGeometryType() { return this.geometryType; }

    public void setGeometryType(GeometryType geometryType) { this.geometryType = geometryType; }

    public FormatType getFormatType() { return formatType; }

    public void setFormatType(FormatType formatType) {
        if (formatType == FormatType.WKB) {
            setTypeHandler(new JtsGeometryWktAsWkbTypeHandler());
        } else if (formatType == FormatType.WKT) {
            setTypeHandler(new StringTypeHandler());
        } else {
            throw new UnsupportedOperationException(formatType + " Unsupported.");
        }
        this.formatType = formatType;
    }

    public void addRange(double axisXofA, double axisYofA, double axisXofB, double axisYofB) {
        this.addRange(50, axisXofA, axisYofA, axisXofB, axisYofB);
    }

    public void addRange(int ratio, double axisXofA, double axisYofA, double axisXofB, double axisYofB) {
        this.range.addRatio(ratio, new SpaceRange(axisXofA, axisYofA, axisXofB, axisYofB));
    }

    public RandomRatio<SpaceRange> getRange() { return this.range; }

    public int getPrecision() { return precision; }

    public void setPrecision(int precision) { this.precision = precision; }

    public int getMinPointSize() { return minPointSize; }

    public void setMinPointSize(int minPointSize) { this.minPointSize = minPointSize; }

    public int getMaxPointSize() { return maxPointSize; }

    public void setMaxPointSize(int maxPointSize) { this.maxPointSize = maxPointSize; }
}
