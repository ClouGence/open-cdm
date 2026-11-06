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
package com.clougence.clouddm.ds.iotdb.metadata;

import java.sql.JDBCType;
import java.util.Locale;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

public enum IotdbFieldType implements FieldType {

    BOOLEAN(0, JDBCType.BOOLEAN),
    INT32(1, JDBCType.INTEGER),
    INT64(2, JDBCType.BIGINT),
    FLOAT(3, JDBCType.REAL),
    DOUBLE(4, JDBCType.DOUBLE),
    TEXT(5, JDBCType.VARCHAR),
    TIMESTAMP(8, JDBCType.TIMESTAMP),
    DATE(9, JDBCType.DATE),
    BLOB(10, JDBCType.BLOB),
    STRING(11, JDBCType.VARCHAR);

    private final int      code;
    private final JDBCType jdbcType;

    IotdbFieldType(int code, JDBCType jdbcType){
        this.code = code;
        this.jdbcType = jdbcType;
    }

    public static IotdbFieldType valueOfCode(String value) {
        if (value == null) {
            return null;
        }
        try {
            return valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getCodeKey() { return name(); }

    @Override
    public DsType getDsType() { return DsType.IoTDB; }

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
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() { return this == INT32 || this == INT64 || hasApproximate(); }

    @Override
    public boolean isBinary() { return this == BLOB; }

    @Override
    public boolean isString() { return this == TEXT || this == STRING; }

    @Override
    public boolean isDataOrTime() { return hasDate(); }

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
        return this == TIMESTAMP;
    }

    @Override
    public boolean hasZone() {
        return false;
    }

    @Override
    public boolean isAccurateDecimal() { return false; }
}
