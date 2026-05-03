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
     *  -- P 的范围是 [1,27]， S 的范围 [0,9]， 整数位数字范围是[1,18]。另外，P 必须要大于等于 S 的取值。默认的 S 取值为 0
     */
    DECIMAL("DECIMAL", JDBCType.DECIMAL),
    /**
     * DECIMALV3(M[,D]) 相对DECIMAL可表示的范围更大，整数部分支持大于18位
     * 高精度定点数，M 代表一共有多少个有效数字(precision)，D 代表小数位有多少数字(scale)，
     * 有效数字 M 的范围是 [1, 38]，小数位数字数量 D 的范围是 [0, precision]。
     * 默认值为 DECIMALV3(9, 0)。
     */
    DECIMALV3("DECIMALV3", JDBCType.DECIMAL),
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
    TEXT("TEXT", JDBCType.LONGVARCHAR),

    // ---------------------------------------------------- time ------------------------------------------------------------------- //

    /** 日期类型，目前的取值范围是 ['0000-01-01', '9999-12-31']，默认的打印形式是 'YYYY-MM-DD' */
    DATE("DATE", JDBCType.DATE),
    /** 日期时间类型，取值范围是 ['0000-01-01 00:00:00', '9999-12-31 23:59:59'] */
    DATETIME("DATETIME", JDBCType.TIMESTAMP),
    /** 日期类型，目前的取值范围是 ['0000-01-01', '9999-12-31']，默认的打印形式是 'YYYY-MM-DD' */
    DATEV2("DATEV2", JDBCType.DATE),
    /** 日期时间类型，取值范围是 ['0000-01-01 00:00:00', '9999-12-31 23:59:59'] */
    DATETIMEV2("DATETIMEV2", JDBCType.TIMESTAMP),

    // ---------------------------------------------------- extra ------------------------------------------------------------------- //

    /** ARRAY<type> */
    ARRAY("ARRAY", JDBCType.ARRAY),
    /** JSONB */
    JSONB("JSONB", JDBCType.OTHER),
    JSON("JSON", JDBCType.OTHER),
    /** QUANTILE_STATE */
    QUANTILE_STATE("QUANTILE_STATE", JDBCType.OTHER),

    /** 统计值，用于近似去重 */
    HLL("HLL", JDBCType.BIGINT),
    /** 统计值，常用来加速 count distinct 的去重计数使用 */
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
