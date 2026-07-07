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
package com.clougence.adapter.doris;

import java.sql.JDBCType;
import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 * reference: <a href="https://doris.apache.org/zh-CN/docs/table-design/data-type">Doris Data Types</a>
 *
 * @author wanshao
 */
public enum DorisTypes implements FieldType {

    // ---------------------------------------------------- numberic ------------------------------------------------------------------- //
    /** 1 Byte symbol integer, range [128, 127] */
    TINYINT("TINYINT", JDBCType.TINYINT),
    /** 2 Byte symbol integer, range [32768, 32767] */
    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    /** 4 Byte symbol integer, range [-2147483648, 2147483647] */
    INT("INT", JDBCType.INTEGER),
    /** 8 Byte symbol integer, range [-9223372036854775808, 9223372036854775807] */
    BIGINT("BIGINT", JDBCType.BIGINT),
    /** 16 Byte symbol integer, range [-2^127 + 1 ~ 2^127-1] */
    LARGEINT("LARGEINT", JDBCType.BIGINT),
    /**
     * DECIMAL(P [, S])
     *  -- The P range is [1,27], the S range [0,9], and the integer numerical range [1,18]. Also, the P must be greater than the value equal to S. Default S value 0
     */
    DECIMAL("DECIMAL", JDBCType.DECIMAL),
    /**
     * DECIMALV3(M[,D]) has a wider representable range than DECIMAL, and the integer part supports more than 18 digits
     * High-precision fixed points, M, how many valid numbers are there? D, how many decimals are there?
     * The valid M range is [1,38] and the decimal number D range is [0, recognition].
     * The default value is DECIMALV3(9,0).
     */
    DECIMALV3("DECIMALV3", JDBCType.DECIMAL),
    /** 8 byte floating points */
    DOUBLE("DOUBLE", JDBCType.DOUBLE),
    /** 4 byte float points */
    FLOAT("FLOAT", JDBCType.FLOAT),
    /** BOOL, BOLEAN, like TINYINT, 0 for false, 1 for true */
    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN),

    // ---------------------------------------------------- character ------------------------------------------------------------------- //

    /** CHAR(M), fixed string, M represents the length of the fixed string. M range is 1 ~ 255. */
    CHAR("CHAR", JDBCType.CHAR),
    /** VARCHAR(M) long string, and M represents the length of the longer string. M ranges 1~1048576, default value 1. Starting with 2.1 version, the M range is 1~1048576; the M range before 2.1 is 1~6553 */
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    /** String, maximum length 65533 bytes */
    STRING("STRING", JDBCType.LONGVARCHAR),
    TEXT("TEXT", JDBCType.LONGVARCHAR),

    // ---------------------------------------------------- time ------------------------------------------------------------------- //

    /** Date type, current range of values ['000-01-01', '9999-12-31'] and default print form 'YYYY-MM-DD' */
    DATE("DATE", JDBCType.DATE),
    /** Date-time type, value range ['000-01-00:00', '9999-12-31 23:59:59'] */
    DATETIME("DATETIME", JDBCType.TIMESTAMP),
    /** Date type, current range of values ['000-01-01', '9999-12-31'] and default print form 'YYYY-MM-DD' */
    DATEV2("DATEV2", JDBCType.DATE),
    /** Date-time type, value range ['000-01-00:00', '9999-12-31 23:59:59'] */
    DATETIMEV2("DATETIMEV2", JDBCType.TIMESTAMP),

    // ---------------------------------------------------- extra ------------------------------------------------------------------- //

    /** ARRAY<type> */
    ARRAY("ARRAY", JDBCType.ARRAY),
    /** JSONB */
    JSONB("JSONB", JDBCType.OTHER),
    JSON("JSON", JDBCType.OTHER),
    /** QUANTILE_STATE */
    QUANTILE_STATE("QUANTILE_STATE", JDBCType.OTHER),

    /** Statistical value for approximate weight removal */
    HLL("HLL", JDBCType.BIGINT),
    /** Statistical value, often used to accelerate the count recount */
    BITMAP("BITMAP", JDBCType.BIGINT),;

    private final String   codeKey;
    private final JDBCType jdbcType;

    DorisTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static DorisTypes valueOfCode(String code) {
        // extract key
        String param = StringUtils.trim(code);
        int index = param.indexOf("(");
        if (index != -1) {
            param = param.substring(0, index);
        }
        String realCode = ALIAS_NAMES_MAP.get(param);
        if (StringUtils.isBlank(realCode)) {
            realCode = param;
        }
        for (DorisTypes dorisType : DorisTypes.values()) {
            if (dorisType.codeKey.equalsIgnoreCase(realCode)) {
                return dorisType;
            }
        }
        throw new UnsupportedOperationException("Unsupported code:" + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Doris; }

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
    public boolean isNumber() {
        switch (this) {
            case BIGINT:
            case LARGEINT:
            case SMALLINT:
            case TINYINT:
            case BOOLEAN:
            case DECIMAL:
            case DECIMALV3:
            case INT:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isAccurateDecimal() { return this == DECIMAL || this == DECIMALV3; }

    @Override
    public boolean isBinary() { return false; }

    @Override
    public boolean isString() {
        switch (this) {
            case CHAR:
            case VARCHAR:
            case STRING:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
        switch (this) {
            case DATETIME:
            case DATE:
            case DATEV2:
            case DATETIMEV2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isGeometry() { return false; }

    @Override
    public boolean isBoolean() {
        switch (this) {
            case BOOLEAN:
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
        switch (this) {
            case DATE:
            case DATETIME:
            case DATEV2:
            case DATETIMEV2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        return this == DorisTypes.DATETIME;
    }

    @Override
    public boolean hasZone() {
        return false;
    }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }

    public static final Map<String, String> ALIAS_NAMES_MAP = new LinkedCaseInsensitiveMap<>();

    static {
        ALIAS_NAMES_MAP.put("bigint unsigned", "LARGEINT");
        ALIAS_NAMES_MAP.put("unknown", "QUANTILE_STATE");
    }
}
