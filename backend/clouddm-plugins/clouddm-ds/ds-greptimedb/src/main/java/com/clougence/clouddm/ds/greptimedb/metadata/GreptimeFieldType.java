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

package com.clougence.clouddm.ds.greptimedb.metadata;

import java.sql.JDBCType;
import java.util.Locale;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

public enum GreptimeFieldType implements FieldType {

    BOOLEAN(1, JDBCType.BOOLEAN),
    TINYINT(2, JDBCType.TINYINT),
    SMALLINT(3, JDBCType.SMALLINT),
    INT(4, JDBCType.INTEGER),
    BIGINT(5, JDBCType.BIGINT),
    FLOAT(6, JDBCType.FLOAT),
    DOUBLE(7, JDBCType.DOUBLE),
    DECIMAL(8, JDBCType.DECIMAL),
    STRING(9, JDBCType.VARCHAR),
    BINARY(10, JDBCType.VARBINARY),
    DATE(11, JDBCType.DATE),
    TIMESTAMP(12, JDBCType.TIMESTAMP),
    TIME(13, JDBCType.TIME),
    INTERVAL(14, JDBCType.OTHER),
    JSON(15, JDBCType.OTHER),
    JSON2(16, JDBCType.OTHER),
    VECTOR(17, JDBCType.VARBINARY);

    private final int      code;
    private final JDBCType jdbcType;

    GreptimeFieldType(int code, JDBCType jdbcType){
        this.code = code;
        this.jdbcType = jdbcType;
    }

    public static GreptimeFieldType valueOfCode(String type) {
        String baseType = type.split("[ (]", 2)[0].toUpperCase(Locale.ROOT);
        try {
            return valueOf(baseType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getCodeKey() { return name(); }

    @Override
    public DsType getDsType() { return DsType.GreptimeDB; }

    @Override
    public int getCodeNum() { return code; }

    @Override
    public Integer getJdbcType() { return jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return jdbcType;
    }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        return this == FLOAT || this == DOUBLE;
    }

    @Override
    public boolean isArray() { return false; }

    @Override
    public boolean isStruct() { return this == JSON || this == JSON2; }

    @Override
    public boolean isNumber() {
        return switch (this) {
            case TINYINT, SMALLINT, INT, BIGINT, FLOAT, DOUBLE, DECIMAL -> true;
            default -> false;
        };
    }

    @Override
    public boolean isBinary() { return jdbcType == JDBCType.VARBINARY; }

    @Override
    public boolean isString() { return this == STRING; }

    @Override
    public boolean isDataOrTime() { return hasDate() || hasTime(); }

    @Override
    public boolean isGeometry() { return false; }

    @Override
    public boolean isBoolean() { return this == BOOLEAN; }

    @Override
    public boolean hasFixedChar() {
        return false;
    }

    @Override
    public boolean hasDate() {
        return this == DATE || this == TIMESTAMP;
    }

    @Override
    public boolean hasTime() {
        return this == TIME || this == TIMESTAMP;
    }

    @Override
    public boolean hasZone() {
        return false;
    }

    @Override
    public boolean isAccurateDecimal() { return this == DECIMAL; }
}
