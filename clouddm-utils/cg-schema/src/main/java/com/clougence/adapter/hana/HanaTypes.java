package com.clougence.adapter.hana;

import java.sql.JDBCType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 * reference: https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20a1569875191014b507cf392724b7eb.html?q=offset
 *
 * @author hlz
 */
public enum HanaTypes implements FieldType {

    // com.sap.db.jdbc.packet.DataType
    // ---------------------------------------------------- numberic ------------------------------------------------------------------- //
    /** [0,255]The TINYINT data type stores an 8-bit unsigned integer. The minimum value is 0 and the maximum value is 255.*/
    TINYINT("TINYINT", JDBCType.TINYINT),
    /** [-32768, 32767]The SMALLINT data type stores a 16-bit signed integer. The minimum value is -32,768 and the maximum value is 32,767.  */
    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    /** [-2147483648, 2147483647] The INTEGER data type stores a 32-bit signed integer. The minimum value is -2,147,483,648 and the maximum value is 2,147,483,647. */
    INTEGER("INTEGER", JDBCType.INTEGER),
    /** [-9223372036854775808, 9223372036854775807] The BIGINT data type stores a 64-bit signed integer. The minimum value is -9,223,372,036,854,775,808 and the maximum value is 9,223,372,036,854,775,807. */
    BIGINT("BIGINT", JDBCType.BIGINT),
    /**
     * DECIMAL[38,38]
     * DECIMAL(<p>, <s>) is the SQL standard notation for fixed-point decimals. <p> specifies precision, or the number of total digits (the sum of whole digits and fractional digits). <s> denotes scale, or the number of fractional digits.
     * Precision can range from 1 to 38. The scale can range from 0 to <p>. If the scale is not specified, then it defaults to 0.
     * If precision and scale are not specified, then DECIMAL becomes a floating-point decimal number. In this case, precision and scale can vary within the range of 1 to 34 for precision and -6,111 to 6,176 for scale, depending on the stored value.
     */
    DECIMAL("DECIMAL", JDBCType.DECIMAL),

    /**
     * SMALLDECIMAL[16,16]
     * The SMALLDECIMAL data type is a floating-point decimal number.
     * The precision and scale can vary within the range 1-16 for precision and -369-368 for scale, depending on the stored value.
     * SMALLDECIMAL is supported on row and column store.
     */
    SMALLDECIMAL("SMALLDECIMAL", JDBCType.DECIMAL),

    /** The REAL data type specifies a single-precision, 32-bit floating-point number. **/
    REAL("REAL", JDBCType.REAL),
    /**
     * The DOUBLE data type specifies a double-precision, 64-bit floating-point number.
     * The minimum value is -1.7976931348623157E308 and the maximum value is 1.7976931348623157E308 .
     * The smallest positive DOUBLE value is 2.2250738585072014E-308 and the largest negative DOUBLE value is -2.2250738585072014E-308.
     */
    DOUBLE("DOUBLE", JDBCType.DOUBLE),

    /**
     *  The FLOAT(<n>) data type specifies a 32-bit or 64-bit real number,
     *  where <n> specifies the number of significant bits and can range between 1 and 53.
     * If you use the FLOAT(<n>) data type, and <n> is smaller than 25, then the 32-bit REAL data type is used instead.
     * If <n> is greater than or equal to 25, or if <n> is not declared, then the 64-bit DOUBLE data type is used.
     */
    FLOAT("FLOAT", JDBCType.DOUBLE),
    /**
     * The BOOLEAN data type stores boolean values, which are TRUE, FALSE, and UNKNOWN, where UNKNOWN is a synonym of NULL.
     * When the client does not support a boolean type, it returns 1 for TRUE and 0 for FALSE.
     */
    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN),

    // ---------------------------------------------------- character ------------------------------------------------------------------- //

    /**
     * The SAP HANA database does not officially support the CHAR and NCHAR datatypes.
     * Even though they are available for use, they are only for legacy support and consistent behavior is not
     * guaranteed for values of these types between a row table and a column table. Use VARCHAR and NVARCHAR instead.
     */
    CHAR("CHAR", JDBCType.CHAR),

    NCHAR("NCHAR", JDBCType.NCHAR),
    /**
     * max 5000
     * The VARCHAR(<n>) data type specifies a variable-length character string, where <n> indicates the maximum length in bytes and is an integer between 1 and 5000.
     * If the length is not specified in DDL statements, then the default of 1 is used.
     * If the VARCHAR(<n>) data type is used in a DML query, for example CAST (A as VARCHAR(n)),
     * then <n> indicates the maximum length of the string in characters. If the length is not specified,
     * then the default of 5000 is used. For non 7-bit ASCII character-based strings, use NVARCHAR.
     * */
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    /**
     * he NVARCHAR(<n>) data type specifies a variable-length Unicode character set string, where <n> indicates the maximum length in characters and is an integer between 1 and 5000. If the length is not specified in DDL statements, then the default of 1 is used.
     * If the NVARCHAR(<n>) data type is used in a DML query, for example CAST (A as NVARCHAR(n)), then <n> indicates the maximum length of the string in characters. If the length is not specified, then the default of 5000 is used. For 7-bit ASCII character-based strings only, use VARCHAR.
     */
    NVARCHAR("NVARCHAR", JDBCType.NVARCHAR),
    /**
     * The ALPHANUM(<n>) data type specifies a variable-length character string that contains alpha-numeric characters, where <n> indicates the maximum length and is an integer between 1 and 127.
     * Sorting among values of type ALPHANUM is performed in alpha-representation. In the case of a purely numeric value, this means that the value can be considered as an alpha value with leading zeros.
     */
    ALPHANUM("ALPHANUM", JDBCType.NVARCHAR),

    /**
     * The SHORTTEXT(<n>) data type specifies a variable-length character string that supports text search features and string search features. This data type can be defined for column tables but not for row tables. This is not a standalone SQL type. Selecting a SHORTTEXT(<n>) column yields a column of type NVARCHAR(<n>).
     */
    SHORTTEXT("SHORTTEXT", JDBCType.NVARCHAR),

    // ---------------------------------------------------- time ------------------------------------------------------------------- //

    /**
     * The DATE data type consists of year, month, and day information to represent a date value.
     * The default format for the DATE data type is YYYY-MM-DD. YYYY represents the year,
     * MM represents the month, and DD represents the day. The range of the date value is
     * between 0001-01-01 and 9999-12-31.DATE is a synonym for DAYDATE.
     */
    DATE("DATE", JDBCType.DATE),
    /**
     * The TIME data type consists of hour, minute, and second information to represent a time value.
     * The default format for the TIME data type is HH24:MI:SS. HH24 represents the hour from 0 to 24,
     * MI represents the minute from 0 to 59, and SS represents the second from 0 to 59.
     */
    TIME("TIME", JDBCType.TIME),
    /**
     * The SECONDDATE data type consists of year, month, day, hour, minute and second information to represent
     * a date with a time value. The default format for the SECONDDATE data type is YYYY-MM-DD HH24:MI:SS.
     * YYYY represents the year, MM represents the month, DD represents the day, HH24 represents hours,
     * MI represents minutes, and SS represents seconds. The range of the date value is between 0001-01-01 00:00:01 and 9999-12-31 24:00:00.
     */
    SECONDDATE("SECONDDATE", JDBCType.TIMESTAMP),

    /**
     * The TIMESTAMP data type consists of date and time information.
     * Its default format is YYYY-MM-DD HH24:MI:SS.FF7. FF<n> represents the fractional seconds
     * where <n> indicates the number of digits in fractional part. The range of the time stamp value is
     * between 0001-01-01 00:00:00.0000000 and 9999-12-31 23:59:59.9999999. LONGDATE is a synonym of TIMESTAMP.
     */
    TIMESTAMP("TIMESTAMP", JDBCType.TIMESTAMP),

    // ---------------------------------------------------- extra ------------------------------------------------------------------- //

    /**
     * The BLOB data type is used to store large amounts of binary data. BLOB values can be converted to VARBINARY.
     */
    BLOB("BLOB", JDBCType.BLOB),

    /**
     * The BINARY(<n>) data type is used to store binary data of a specified maximum length in bytes,
     * where <n> indicates the maximum length and is an integer between 1 and 2000.
     * If the length is not specified, then the default is 1.
     */
    BINARY("BINARY", JDBCType.BINARY),

    /**
     * The VARBINARY(<n>) data type is used to store binary data of a specified maximum length in bytes,
     * where <n> indicates the maximum length and is an integer between 1 and 5000.
     * If the length is not specified, then the default is 1.
     */
    VARBINARY("VARBINARY", JDBCType.VARBINARY),

    /**
     * The CLOB data type is used to store large amounts of 7-bit ASCII character data. CLOB values can be converted to VARCHAR.
     */
    CLOB("CLOB", JDBCType.CLOB),
    /**
     * The NCLOB data type is used to store a large Unicode character object. NCLOB values can be converted to NVARCHAR.
     */
    NCLOB("NCLOB", JDBCType.NCLOB),

    /**
     * The TEXT data type enables text search features. This data type can be defined for column tables,
     * but not for row tables. This is not a standalone SQL type. Selecting a TEXT column yields a
     * column of type NCLOB.A value of type TEXT cannot be converted implicitly to a value of
     * type (N)VARCHAR , and string functions (UPPER, LOWER, and so on) cannot be applied directly
     * to a value of type TEXT. Explicit conversion from a value of type TEXT to a value of type
     * (N)VARCHAR is allowed. String functions can therefore be applied to the converted value.
     * For columns of type TEXT, the LIKE predicate is not supported.
     */
    TEXT("TEXT", JDBCType.NCLOB),

    /**
     * The BINTEXT data type is similar to the TEXT data type and supports text search features,
     * but it is possible to insert binary data. This data type can be defined for column tables,
     * but not for row tables. This is not a standalone SQL type.
     * Selecting a BINTEXT column yields a column of type NCLOB.
     * For values of type BINTEXT, the same restrictions apply as for values of type TEXT.
     */
    BINTEXT("BINTEXT", JDBCType.NCLOB),

    /**
     * The ARRAY type is used to store collections of values sharing the same data type where each element is associated with exactly one ordinal position. Arrays can contain NULL values as elements to indicate the absence of a value. Arrays are immutable: adding, removing, or changing elements is not possible. Arrays with the WITHOUT DUPLICATES constraint cannot have the same non-NULL value more than once.
     */
    ARRAY("ARRAY", JDBCType.ARRAY),

    /**
     * Spatial data types are used to store values that contain spatial data, such as points, lines, or polygons.
     * The following column types are supported in column tables only: ST_Point, ST_Geometry.
     */
    ST_POINT("ST_POINT", JDBCType.LONGVARBINARY),
    ST_GEOMETRY("ST_GEOMETRY", JDBCType.LONGVARBINARY);

    private final String   codeKey;
    private final JDBCType jdbcType;

    HanaTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static HanaTypes valueOfCode(String code) {
        String param = StringUtils.trim(code);
        String realCode = ALIAS_NAMES_MAP.get(param);
        if (StringUtils.isBlank(realCode)) {
            realCode = param;
        }
        for (HanaTypes t : HanaTypes.values()) {
            if (t.codeKey.equalsIgnoreCase(realCode)) {
                return t;
            }
        }
        throw new UnsupportedOperationException("Unsupported code:" + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Hana; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        return this == FLOAT || this == DOUBLE || this == REAL;
    }

    @Override
    public boolean isArray() { return this == ARRAY; }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case SMALLDECIMAL:
            case DECIMAL:
            case REAL:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isAccurateDecimal() { return this == DECIMAL; }

    @Override
    public boolean isBinary() { return this == VARBINARY || this == BLOB || this == BINARY; }

    @Override
    public boolean isString() {
        switch (this) {
            case CHAR:
            case NCHAR:
            case VARCHAR:
            case NVARCHAR:
            case ALPHANUM:
            case SHORTTEXT:
            case CLOB:
            case NCLOB:
            case TEXT:
            case BINTEXT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
        switch (this) {
            case DATE:
            case TIME:
            case SECONDDATE:
            case TIMESTAMP:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isGeometry() { return this == ST_GEOMETRY || this == ST_POINT; }

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
        switch (this) {
            case CHAR:
            case NCHAR:
            case NVARCHAR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case SECONDDATE:
            case TIMESTAMP:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case TIME:
            case SECONDDATE:
            case TIMESTAMP:
                return true;
            default:
                return false;
        }
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
        ALIAS_NAMES_MAP.put("INT", "INTEGER");
        ALIAS_NAMES_MAP.put("LONGDATE", "TIMESTAMP");
    }

    // hana support column/row table, row table fit tp, column table fit ap
    public static final List<HanaTypes> ONLY_SUPPORT_COLUMN_TABLE_LIST = new LinkedList<>();

    static {
        ONLY_SUPPORT_COLUMN_TABLE_LIST.add(ST_POINT);
        ONLY_SUPPORT_COLUMN_TABLE_LIST.add(ST_GEOMETRY);
        ONLY_SUPPORT_COLUMN_TABLE_LIST.add(SHORTTEXT);
        ONLY_SUPPORT_COLUMN_TABLE_LIST.add(TEXT);
        ONLY_SUPPORT_COLUMN_TABLE_LIST.add(BINTEXT);
    }
}
