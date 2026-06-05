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
package com.clougence.adapter.starrocks;

import java.sql.JDBCType;
import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 * reference: https://docs.starrocks.io/zh-cn/latest/sql-reference/sql-statements/data-types/BIGINT
 * @author mode
 */
public enum StarRocksTypes implements FieldType {

    //json
    // ---------------------------------------------------- numberic ------------------------------------------------------------------- //
    /** 1 字节有符号整数，范围 [-128, 127] */
    TINYINT("TINYINT", JDBCType.TINYINT),
    /** 2 字节有符号整数，范围 [-32768, 32767] */
    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    /** 4 字节有符号整数，范围 [-2147483648, 2147483647] */
    INT("INT", JDBCType.INTEGER),
    /** 8 字节有符号整数，范围 [-9223372036854775808, 9223372036854775807] */
    BIGINT("BIGINT", JDBCType.BIGINT),
    /** 16 字节有符号整数，范围 [-2^127 + 1 ~ 2^127 - 1] */
    LARGEINT("LARGEINT", JDBCType.BIGINT),
    /**
     * DECIMAL(P [, S])
     *  -- P 的范围是 [1,27]， S 的范围 [0, 9]。另外，P 必须要大于等于 S 的取值。默认的 S 取值为 0
     *  -- StarRocks-1.18 版本开始起 ,P 的范围是 [1,38]，S 的范围 [0, P]。默认的 S 取值为 0。
     */
    DECIMAL("DECIMAL", JDBCType.DECIMAL),

    DECIMALV2("DECIMALV2", JDBCType.DECIMAL),

    DECIMAL32("DECIMAL32", JDBCType.DECIMAL),

    DECIMAL64("DECIMAL64", JDBCType.DECIMAL),

    DECIMAL128("DECIMAL128", JDBCType.DECIMAL),

    /** 8 字节浮点数 */
    DOUBLE("DOUBLE", JDBCType.DOUBLE),
    /** 4 字节浮点数 */
    FLOAT("FLOAT", JDBCType.FLOAT),
    /** BOOL, BOOLEAN 与 TINYINT 一样，0 代表 false，1 代表 true */
    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN),

    // ---------------------------------------------------- character ------------------------------------------------------------------- //

    /** CHAR(M),定长字符串，M 代表的是定长字符串的长度。M 的范围是 1~255 */
    CHAR("CHAR", JDBCType.CHAR),
    /** VARCHAR(M) 变长字符串，M 代表的是变长字符串的长度。M 的范围是 1~1048576，默认取值 1。自 2.1 版本开始，M 的范围为 1~1048576；2.1 之前的版本的 M 范围为 1~65533 */
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    /** 字符串，最大长度 65533 字节 */
    STRING("STRING", JDBCType.LONGVARCHAR),
    /** JSON 类型 */
    JSON("JSON", JDBCType.LONGVARCHAR),

    // ---------------------------------------------------- time ------------------------------------------------------------------- //

    /** 日期类型，目前的取值范围是 ['0000-01-01', '9999-12-31']，默认的打印形式是 'YYYY-MM-DD' */
    DATE("DATE", JDBCType.DATE),
    /** 日期时间类型，取值范围是 ['0000-01-01 00:00:00', '9999-12-31 23:59:59'] */
    DATETIME("DATETIME", JDBCType.TIMESTAMP),

    // ---------------------------------------------------- extra ------------------------------------------------------------------- //
    /** ARRAY<type> */
    ARRAY("ARRAY", JDBCType.ARRAY),
    /** 统计值，用于近似去重 */
    HLL("HLL", JDBCType.BIGINT),
    /** 统计值，常用来加速 count distinct 的去重计数使用 */
    BITMAP("BITMAP", JDBCType.BIGINT),

    /**
     * Since v3.0, StarRocks supports the BINARY/VARBINARY data type, which is used to store binary data.
     * The maximum supported length is the same as VARCHAR (1~1048576). The unit is byte.
     * If M is not specified, 1048576 is used by default. Binary data types contain byte strings while character
     * data types contain character strings. BINARY is an alias of VARBINARY. The usage is the same as VARBINARY.
     * note: data_type and column_type value is unknown from information.columns!!! can not parse varbinary
     */
    VARBINARY("VARBINARY", JDBCType.VARBINARY),

    /** StarRocks supports MAP data types starting from version 3.1. You can define MAP columns during table creation,
     *  import MAP data into the table, and query MAP data.
     */
    MAP("MAP", JDBCType.OTHER),

    /** StarRocks supports the STRUCT data type starting from version 3.1. You can define the STRUCT column when creating a table,
     *  import STRUCT data into the table, and query STRUCT data.
     */
    STRUCT("STRUCT", JDBCType.STRUCT);

    public static final Map<String, String> AliasNames = new LinkedCaseInsensitiveMap<>();

    static {
        AliasNames.put("bigint unsigned", "LARGEINT");
        AliasNames.put("binary", "VARBINARY");
        AliasNames.put("unknown", "ARRAY");//database itself does not recognize array
    }

    private final String   codeKey;
    private final JDBCType jdbcType;

    StarRocksTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static StarRocksTypes valueOfCode(String code) {
        String realCode = AliasNames.get(code);
        if (StringUtils.isBlank(realCode)) {
            realCode = code;
        }
        for (StarRocksTypes tableType : StarRocksTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(realCode)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.StarRocks; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        return this == FLOAT || this == DOUBLE;
    }

    @Override
    public boolean isArray() { return this == ARRAY; }

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
            case DECIMALV2:
            case DECIMAL32:
            case DECIMAL64:
            case DECIMAL128:
            case INT:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() { return this == VARBINARY; }

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
            case DATE:
            case DATETIME:
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
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        return this == StarRocksTypes.DATETIME;
    }

    @Override
    public boolean hasZone() {
        return false;
    }

    @Override
    public boolean isAccurateDecimal() {
        return this == DECIMAL //
               || this == DECIMALV2 //
               || this == DECIMAL32 //
               || this == DECIMAL64 //
               || this == DECIMAL128;
    }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }
}
