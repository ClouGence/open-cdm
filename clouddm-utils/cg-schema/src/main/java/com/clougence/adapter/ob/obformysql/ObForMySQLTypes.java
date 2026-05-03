package com.clougence.adapter.ob.obformysql;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * reference:https://open.oceanbase.com/docs/community/oceanbase-database/V3.1.0/data-type
 *
 * @author wanshao
 */
public enum ObForMySQLTypes implements FieldType {

    // ---------------------------------------------------- numberic ------------------------------------------------------------------- //
    BIT("BIT", JDBCType.BIT),

    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN),

    BOOLEAN_UNSIGNED("BOOLEAN UNSIGNED", JDBCType.TINYINT),

    TINYINT("TINYINT", JDBCType.TINYINT),

    TINYINT_UNSIGNED("TINYINT UNSIGNED", JDBCType.SMALLINT),

    SMALLINT("SMALLINT", JDBCType.SMALLINT),

    SMALLINT_UNSIGNED("SMALLINT UNSIGNED", JDBCType.INTEGER),

    MEDIUMINT("MEDIUMINT", JDBCType.INTEGER),

    MEDIUMINT_UNSIGNED("MEDIUMINT UNSIGNED", JDBCType.INTEGER),

    INT("INT", JDBCType.INTEGER),

    INT_UNSIGNED("INT UNSIGNED", JDBCType.BIGINT),

    BIGINT("BIGINT", JDBCType.BIGINT),

    BIGINT_UNSIGNED("BIGINT UNSIGNED", JDBCType.VARCHAR),

    DECIMAL("DECIMAL", JDBCType.DECIMAL),

    NUMBER("NUMBER", JDBCType.NUMERIC),

    FLOAT("FLOAT", JDBCType.REAL),

    DOUBLE("DOUBLE", JDBCType.DOUBLE),

    // ---------------------------------------------------- time ------------------------------------------------------------------- //

    DATETIME("DATETIME", JDBCType.TIMESTAMP),

    TIMESTAMP("TIMESTAMP", JDBCType.TIMESTAMP),

    DATE("DATE", JDBCType.DATE),

    TIME("TIME", JDBCType.TIME),

    YEAR("YEAR", JDBCType.DATE),

    // ---------------------------------------------------- character ------------------------------------------------------------------- //
    // ob use utf8mb4 by default for character type

    JSON("JSON", JDBCType.LONGVARCHAR),

    /**
     * max is 256k
     */
    VARCHAR("VARCHAR", JDBCType.VARCHAR),

    VARBINARY("VARBINARY", JDBCType.VARBINARY),
    /**
     * max is 256 bytes
     */
    CHAR("CHAR", JDBCType.CHAR),

    BINARY("BINARY", JDBCType.BINARY),

    ENUM("ENUM", JDBCType.VARCHAR),

    SET("SET", JDBCType.VARCHAR),

    // ---------------------------------------------------- big object ------------------------------------------------------------------- //
    TINYTEXT("TINYTEXT", JDBCType.VARCHAR),

    TINYBLOB("TINYBLOB", JDBCType.BINARY),

    TEXT("TEXT", JDBCType.VARCHAR),

    BLOB("BLOB", JDBCType.BINARY),

    MEDIUMTEXT("MEDIUMTEXT", JDBCType.VARCHAR),

    MEDIUMBLOB("MEDIUMBLOB", JDBCType.BINARY),

    LONGTEXT("LONGTEXT", JDBCType.VARCHAR),

    LONGBLOB("LONGBLOB", JDBCType.BINARY),;

    private final String   codeKey;
    private final JDBCType jdbcType;

    ObForMySQLTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static ObForMySQLTypes valueOfCode(String code) {
        for (ObForMySQLTypes tableType : ObForMySQLTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
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
    public DsType getDsType() { return DsType.OceanBase; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isArray() { return false; }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case BOOLEAN:
            case BOOLEAN_UNSIGNED:
            case TINYINT:
            case TINYINT_UNSIGNED:
            case SMALLINT:
            case SMALLINT_UNSIGNED:
            case MEDIUMINT:
            case MEDIUMINT_UNSIGNED:
            case INT:
            case INT_UNSIGNED:
            case BIGINT:
            case BIGINT_UNSIGNED:
            case DECIMAL:
            case FLOAT:
            case DOUBLE:
            case NUMBER:
            case YEAR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case BIT:
            case VARBINARY:
            case BINARY:
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
            case VARCHAR:
            case CHAR:
            case ENUM:
            case SET:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
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
    public boolean isBoolean() {
        switch (this) {
            case BOOLEAN:
            case BOOLEAN_UNSIGNED:
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
    public boolean isAccurateDecimal() { return this == DECIMAL || this == NUMBER; }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }
}
