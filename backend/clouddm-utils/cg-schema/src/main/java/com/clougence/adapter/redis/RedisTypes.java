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
package com.clougence.adapter.redis;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * @version : 2021-06-24
 * @author 赵永春 (zyc@hasor.net)
 */
public enum RedisTypes implements FieldType {

    String("String", JDBCType.VARCHAR),
    Integer("Integer", JDBCType.INTEGER),
    Decimal("Decimal", JDBCType.DECIMAL),
    Boolean("Boolean", JDBCType.BOOLEAN),
    serializedValue("serializedValue", JDBCType.VARCHAR),
    List("List", JDBCType.ARRAY),
    Set("Set", JDBCType.ARRAY),;

    private final String   codeKey;
    private final JDBCType jdbcType;

    RedisTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static RedisTypes valueOfCode(String code) {
        for (RedisTypes tableType : RedisTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported Redis columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Redis; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        return false;
    }

    @Override
    public boolean isArray() { return this == List || this == Set; }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case Integer:
            case Decimal:
            case Boolean:
            case serializedValue:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() { return false; }

    @Override
    public boolean isString() { return this == RedisTypes.String; }

    @Override
    public boolean isDataOrTime() { return false; }

    @Override
    public boolean isGeometry() { return false; }

    @Override
    public boolean isBoolean() {
        switch (this) {
            case Boolean:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasFixedChar() {
        return false;
    }

    @Override
    public boolean hasDate() {
        return false;
    }

    @Override
    public boolean hasTime() {
        return false;
    }

    @Override
    public boolean hasZone() {
        return false;
    }

    @Override
    public boolean isAccurateDecimal() { return this == Decimal; }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }
}
