package com.clougence.adapter.tidb;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * https://docs.pingcap.com/zh/tidb/stable/data-type-overview
 *
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net) */
public enum TiDBTypes implements FieldType {

    /** BIT[(M)]
     *
     * A bit-value type. M indicates the number of bits per value, from 1 to 64. The default is 1 if M is omitted. */
    BIT("BIT", JDBCType.BIT),
    /**
     * TINYINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A very small integer. The signed range is -128 to 127. The unsigned range is 0 to 255.
     */
    TINYINT("TINYINT", JDBCType.TINYINT),
    /**
     * SMALLINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A small integer. The signed range is -32768 to 32767. The unsigned range is 0 to 65535.
     */
    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    /**
     * MEDIUMINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A medium-sized integer. The signed range is -8388608 to 8388607. The unsigned range is 0 to 16777215.
     */
    MEDIUMINT("MEDIUMINT", JDBCType.INTEGER),
    /**
     * INT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A normal-size integer. The signed range is -2147483648 to 2147483647. The unsigned range is 0 to 4294967295.
     */
    INT("INT", JDBCType.INTEGER),
    /**
     * BIGINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A large integer. The signed range is -9223372036854775808 to 9223372036854775807. The unsigned range is 0 to 18446744073709551615.
     */
    BIGINT("BIGINT", JDBCType.BIGINT),
    /**
     * DECIMAL[(M[,D])] [UNSIGNED] [ZEROFILL]
     * A packed “exact” fixed-point number. M is the total number of digits (the precision) and D is the number of digits after the decimal point (the scale).
     * The decimal point and (for negative numbers) the - sign are not counted in M. If D is 0, values have no decimal point or fractional part.
     * The maximum number of digits (M) for DECIMAL is 65.
     * The maximum number of supported decimals (D) is 30.
     * If D is omitted, the default is 0. If M is omitted,
     * the default is 10. (There is also a limit on how long
     * the text of DECIMAL literals can be; see Section 12.22.3, “Expression Handling”.)
     */
    DECIMAL("DECIMAL", JDBCType.DECIMAL),
    /**
     * FLOAT[(M,D)] [UNSIGNED] [ZEROFILL]
     *
     * A small (single-precision) floating-point number. Permissible values are -3.402823466E+38 to -1.175494351E-38, 0, and 1.175494351E-38 to 3.402823466E+38.
     * These are the theoretical limits, based on the IEEE standard. The actual range might be slightly smaller depending on your hardware or operating system.
     *
     * M is the total number of digits and D is the number of digits following the decimal point.
     * If M and D are omitted, values are stored to the limits permitted by the hardware.
     * A single-precision floating-point number is accurate to approximately 7 decimal places.
     */
    FLOAT("FLOAT", JDBCType.REAL),
    /**
     * DOUBLE[(M,D)] [UNSIGNED] [ZEROFILL]
     *
     * A normal-size (double-precision) floating-point number. Permissible values are -1.7976931348623157E+308 to -2.2250738585072014E-308, 0, and 2.2250738585072014E-308 to 1.7976931348623157E+308.
     * These are the theoretical limits, based on the IEEE standard. The actual range might be slightly smaller depending on your hardware or operating system.
     *
     * M is the total number of digits and D is the number of digits following the decimal point. If M and D are omitted, values are stored to the limits permitted by the hardware.
     * A double-precision floating-point number is accurate to approximately 15 decimal places.
     */
    DOUBLE("DOUBLE", JDBCType.DOUBLE),
    /**
     * DATE
     *
     * A date. The supported range is '1000-01-01' to '9999-12-31'.
     * MySQL displays DATE values in 'YYYY-MM-DD' format, but permits assignment of values to DATE columns using either strings or numbers.
     */
    DATE("DATE", JDBCType.DATE),
    /**
     * DATETIME[(fsp)]
     *
     * A date and time combination. The supported range is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999'.
     * MySQL displays DATETIME values in 'YYYY-MM-DD hh:mm:ss[.fraction]' format, but permits assignment of values to DATETIME columns using either strings or numbers.
     *
     * An optional fsp value in the range from 0 to 6 may be given to specify fractional seconds precision.
     * A value of 0 signifies that there is no fractional part. If omitted, the default precision is 0.
     */
    DATETIME("DATETIME", JDBCType.TIMESTAMP),
    /**
     * TIMESTAMP[(fsp)]
     *
     * A timestamp. The range is '1970-01-01 00:00:01.000000' UTC to '2038-01-19 03:14:07.999999' UTC.
     * TIMESTAMP values are stored as the number of seconds since the epoch ('1970-01-01 00:00:00' UTC).
     * A TIMESTAMP cannot represent the value '1970-01-01 00:00:00' because that is equivalent to 0 seconds
     * from the epoch and the value 0 is reserved for representing '0000-00-00 00:00:00', the “zero” TIMESTAMP value.
     *
     * An optional fsp value in the range from 0 to 6 may be given to specify fractional seconds precision.
     * A value of 0 signifies that there is no fractional part. If omitted, the default precision is 0.
     */
    TIMESTAMP("TIMESTAMP", JDBCType.TIMESTAMP),
    /**
     * TIME[(fsp)]
     *
     * A time. The range is '-838:59:59.000000' to '838:59:59.000000'.
     * MySQL displays TIME values in 'hh:mm:ss[.fraction]' format, but permits assignment of values to TIME columns using either strings or numbers.
     *
     * An optional fsp value in the range from 0 to 6 may be given to specify fractional seconds precision.
     * A value of 0 signifies that there is no fractional part. If omitted, the default precision is 0.
     */
    TIME("TIME", JDBCType.TIME),
    /**
     * YEAR[(4)]
     *
     * A year in 4-digit format. MySQL displays YEAR values in YYYY format,
     * but permits assignment of values to YEAR columns using either strings or numbers. Values display as 1901 to 2155, or 0000.
     */
    YEAR("YEAR", JDBCType.DATE),
    /**
     * The CHAR and VARCHAR types are similar, but differ in the way they are stored and retrieved.
     * They also differ in maximum length and in whether trailing spaces are retained.
     */
    CHAR("CHAR", JDBCType.CHAR),
    /**
     * The CHAR and VARCHAR types are similar, but differ in the way they are stored and retrieved. They also differ in maximum length and in whether trailing spaces are retained.
     *
     * VARCHAR is shorthand for CHARACTER VARYING.
     */
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    /**
     * BINARY[(M)]
     *
     * The VARBINARY type is similar to the VARCHAR type, but stores binary byte strings rather than nonbinary character strings.
     * M represents the maximum column length in bytes.
     */
    BINARY("BINARY", JDBCType.BINARY),
    /**
     * VARBINARY(M)
     *
     * The VARBINARY type is similar to the VARCHAR type, but stores binary byte strings rather than nonbinary character strings.
     * M represents the maximum column length in bytes.
     */
    VARBINARY("VARBINARY", JDBCType.VARBINARY),
    /**
     * TINYBLOB(M)
     *
     * A BLOB column with a maximum length of 255 (28 − 1) bytes. Each TINYBLOB value is stored using a 1-byte length prefix that indicates the number of bytes in the value.
     */
    TINYBLOB("TINYBLOB", JDBCType.VARBINARY),
    /**
     * BLOB[(M)]
     *
     * A BLOB column with a maximum length of 65,535 (216 − 1) bytes. Each BLOB value is stored using a 2-byte length prefix that indicates the number of bytes in the value.
     *
     * An optional length M can be given for this type. If this is done, MySQL creates the column as the smallest BLOB type large enough to hold values M bytes long.
     */
    BLOB("BLOB", JDBCType.LONGVARBINARY),
    /**
     * MEDIUMBLOB
     *
     * A BLOB column with a maximum length of 16,777,215 (224 − 1) bytes. Each MEDIUMBLOB value is stored using a 3-byte length prefix that indicates the number of bytes in the value.
     */
    MEDIUMBLOB("MEDIUMBLOB", JDBCType.LONGVARBINARY),
    /**
     * LONGBLOB
     *
     * A BLOB column with a maximum length of 4,294,967,295 or 4GB (232 − 1) bytes.
     * The effective maximum length of LONGBLOB columns depends on the configured maximum packet size in the client/server protocol and available memory.
     * Each LONGBLOB value is stored using a 4-byte length prefix that indicates the number of bytes in the value.
     */
    LONGBLOB("LONGBLOB", JDBCType.LONGVARBINARY),
    /**
     * TINYTEXT [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * A TEXT column with a maximum length of 255 (28 − 1) characters. The effective maximum length is less if the value contains multibyte characters.
     * Each TINYTEXT value is stored using a 1-byte length prefix that indicates the number of bytes in the value.
     */
    TINYTEXT("TINYTEXT", JDBCType.VARCHAR),
    /**
     * TEXT [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * A TEXT column with a maximum length of 65,535 (216 − 1) characters. The effective maximum length is less if the value contains multibyte characters.
     * Each TEXT value is stored using a 2-byte length prefix that indicates the number of bytes in the value.
     *
     * An optional length M can be given for this type. If this is done, MySQL creates the column as the smallest TEXT type large enough to hold values M characters long.
     */
    TEXT("TEXT", JDBCType.LONGVARCHAR),
    /**
     * MEDIUMTEXT [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * A TEXT column with a maximum length of 16,777,215 (224 − 1) characters. The effective maximum length is less if the value contains multibyte characters.
     * Each MEDIUMTEXT value is stored using a 3-byte length prefix that indicates the number of bytes in the value.
     */
    MEDIUMTEXT("MEDIUMTEXT", JDBCType.LONGVARCHAR),
    /**
     * LONGTEXT [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * A TEXT column with a maximum length of 4,294,967,295 or 4GB (232 − 1) characters. The effective maximum length is less if the value contains multibyte characters.
     * The effective maximum length of LONGTEXT columns also depends on the configured maximum packet size in the client/server protocol and available memory.
     * Each LONGTEXT value is stored using a 4-byte length prefix that indicates the number of bytes in the value.
     */
    LONGTEXT("LONGTEXT", JDBCType.LONGVARCHAR),
    /**
     * ENUM('value1','value2',...) [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * An enumeration. A string object that can have only one value, chosen from the list of values 'value1', 'value2', ..., NULL or the special '' error value. ENUM values are represented internally as integers.
     *
     * An ENUM column can have a maximum of 65,535 distinct elements.
     *
     * The maximum supported length of an individual ENUM element is M <= 255 and (M x w) <= 1020, where M is the element literal length and w is the number of bytes required for the maximum-length character in the character set.
     */
    ENUM("ENUM", JDBCType.CHAR),
    /**
     * SET('value1','value2',...) [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * A set. A string object that can have zero or more values, each of which must be chosen from the list of values 'value1', 'value2', ... SET values are represented internally as integers.
     *
     * A SET column can have a maximum of 64 distinct members.
     *
     * The maximum supported length of an individual SET element is M <= 255 and (M x w) <= 1020, where M is the element literal length and w is the number of bytes required for the maximum-length character in the character set.
     */
    SET("SET", JDBCType.CHAR),
    /**
     * MySQL supports a native JSON data type defined by RFC 7159 that enables efficient access to data in JSON (JavaScript Object Notation) documents.
     * The JSON data type provides these advantages over storing JSON-format strings in a string column:
     */
    JSON("JSON", JDBCType.LONGVARCHAR);

    private final String   codeKey;
    private final JDBCType jdbcType;

    TiDBTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static TiDBTypes valueOfCode(String code) {
        for (TiDBTypes tableType : TiDBTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.TiDB; }

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
            case TINYINT:
            case SMALLINT:
            case MEDIUMINT:
            case INT:
            case BIGINT:
            case DECIMAL:
            case YEAR:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case BIT:
            case BINARY:
            case VARBINARY:
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isString() {
        switch (this) {
            case CHAR:
            case VARCHAR:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case ENUM:
            case SET:
            case JSON:
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
            case DATETIME:
            case TIMESTAMP:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isGeometry() { return false; }

    @Override
    public boolean isBoolean() { return false; }

    @Override
    public boolean hasFixedChar() {
        return false;
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case DATETIME:
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
            case DATETIME:
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
    public boolean isAccurateDecimal() { return this == DECIMAL; }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }
}
