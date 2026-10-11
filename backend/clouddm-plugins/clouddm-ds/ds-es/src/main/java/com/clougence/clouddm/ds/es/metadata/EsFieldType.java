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
package com.clougence.clouddm.ds.es.metadata;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

public enum EsFieldType implements FieldType {
    Text("text", JDBCType.VARCHAR),
    Keyword("keyword", JDBCType.VARCHAR),
    Long("long", JDBCType.BIGINT),
    Integer("integer", JDBCType.INTEGER),
    Short("short", JDBCType.SMALLINT),
    Byte("byte", JDBCType.TINYINT),
    Double("double", JDBCType.DOUBLE),
    Float("float", JDBCType.REAL),
    HalfFloat("half_float", JDBCType.REAL),
    ScaledFloat("scaled_float", JDBCType.DECIMAL),
    UnsignedLong("unsigned_long", JDBCType.DECIMAL),
    Boolean("boolean", JDBCType.BOOLEAN),
    Date("date", JDBCType.TIMESTAMP),
    DateNanos("date_nanos", JDBCType.TIMESTAMP),
    Binary("binary", JDBCType.VARBINARY),
    Object("object", JDBCType.STRUCT),
    Nested("nested", JDBCType.STRUCT),
    Flattened("flattened", JDBCType.STRUCT),
    Alias("alias", JDBCType.OTHER),
    Ip("ip", JDBCType.VARCHAR),
    Version("version", JDBCType.VARCHAR),
    Wildcard("wildcard", JDBCType.VARCHAR),
    ConstantKeyword("constant_keyword", JDBCType.VARCHAR),
    MatchOnlyText("match_only_text", JDBCType.VARCHAR),
    SearchAsYouType("search_as_you_type", JDBCType.VARCHAR),
    Completion("completion", JDBCType.OTHER),
    TokenCount("token_count", JDBCType.INTEGER),
    GeoPoint("geo_point", JDBCType.OTHER),
    GeoShape("geo_shape", JDBCType.OTHER),
    Point("point", JDBCType.OTHER),
    Shape("shape", JDBCType.OTHER),
    DenseVector("dense_vector", JDBCType.ARRAY),
    SparseVector("sparse_vector", JDBCType.OTHER),
    RankFeature("rank_feature", JDBCType.REAL),
    RankFeatures("rank_features", JDBCType.OTHER),
    Histogram("histogram", JDBCType.OTHER),
    AggregateMetricDouble("aggregate_metric_double", JDBCType.OTHER),
    IntegerRange("integer_range", JDBCType.OTHER),
    LongRange("long_range", JDBCType.OTHER),
    FloatRange("float_range", JDBCType.OTHER),
    DoubleRange("double_range", JDBCType.OTHER),
    DateRange("date_range", JDBCType.OTHER),
    IpRange("ip_range", JDBCType.OTHER),
    Percolator("percolator", JDBCType.OTHER),
    SemanticText("semantic_text", JDBCType.OTHER),
    Lookup("lookup", JDBCType.OTHER),
    Composite("composite", JDBCType.STRUCT),
    Unknown("unknown", JDBCType.OTHER);

    private final String   codeKey;
    private final JDBCType jdbcType;

    EsFieldType(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static EsFieldType valueOfCode(String code) {
        for (EsFieldType type : values()) {
            if (type.codeKey.equals(code)) {
                return type;
            }
        }
        return Unknown;
    }

    @Override
    public String getCodeKey() { return codeKey; }

    // Persisted metadata stores these ids: add new constants only at the end.
    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.ElasticSearch; }

    @Override
    public Integer getJdbcType() { return jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return jdbcType;
    }

    @Override
    public boolean isReadOnly() { return true; }

    @Override
    public boolean hasApproximate() {
        return jdbcType == JDBCType.DOUBLE || jdbcType == JDBCType.REAL || this == ScaledFloat;
    }

    @Override
    public boolean isArray() { return jdbcType == JDBCType.ARRAY; }

    @Override
    public boolean isStruct() { return jdbcType == JDBCType.STRUCT; }

    @Override
    public boolean isNumber() {
        return switch (jdbcType) {
            case BIGINT, INTEGER, SMALLINT, TINYINT, DOUBLE, REAL, DECIMAL -> true;
            default -> false;
        };
    }

    @Override
    public boolean isBinary() { return this == Binary; }

    @Override
    public boolean isString() { return jdbcType == JDBCType.VARCHAR; }

    @Override
    public boolean isDataOrTime() { return this == Date || this == DateNanos; }

    @Override
    public boolean isGeometry() { return this == GeoPoint || this == GeoShape || this == Point || this == Shape; }

    @Override
    public boolean isBoolean() { return this == Boolean; }

    @Override
    public boolean hasFixedChar() {
        return false;
    }

    @Override
    public boolean hasDate() {
        return isDataOrTime();
    }

    @Override
    public boolean hasTime() {
        return isDataOrTime();
    }

    @Override
    public boolean hasZone() {
        return false;
    }

    @Override
    public boolean isAccurateDecimal() { return this == UnsignedLong; }
}
