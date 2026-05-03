package com.clougence.adapter.mc;

import java.sql.JDBCType;

import com.clougence.adapter.mc.odps.OdpsType;
import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * https://help.aliyun.com/zh/maxcompute/user-guide/data-types
 */
public enum McSqlTypes implements FieldType {

    // v1
    BIGINT("BIGINT", JDBCType.BIGINT),
    DOUBLE("DOUBLE", JDBCType.DECIMAL),
    DECIMAL("DECIMAL", JDBCType.DECIMAL),
    STRING("STRING", JDBCType.VARCHAR),
    DATETIME("DATETIME", JDBCType.TIMESTAMP),
    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN),
    // v2
    TINYINT("TINYINT", JDBCType.TINYINT),
    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    INT("INT", JDBCType.INTEGER),
    BINARY("BINARY", JDBCType.BINARY),
    FLOAT("FLOAT", JDBCType.FLOAT),
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    CHAR("CHAR", JDBCType.CHAR),
    DATE("DATE", JDBCType.DATE),
    TIMESTAMP("TIMESTAMP", JDBCType.TIMESTAMP),
    TIMESTAMP_NTZ("TIMESTAMP_NTZ", JDBCType.TIMESTAMP),
    INTERVAL_YEAR_MONTH("YEAR TO MONTH", JDBCType.TIMESTAMP),
    INTERVAL_DAY_TIME("DAY TO SECOND", JDBCType.TIMESTAMP),
    ARRAY("ARRAY", JDBCType.ARRAY),
    MAP("MAP", JDBCType.STRUCT),
    STRUCT("STRUCT", JDBCType.STRUCT),
    JSON("JSON", JDBCType.CLOB),;

    private final String   codeKey;
    private final JDBCType jdbcType;
    //    private final OdpsType odpsType;

    McSqlTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static McSqlTypes valueOfCode(String code) {
        for (McSqlTypes tableType : McSqlTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported MaxCompute columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return this.ordinal(); }

    @Override
    public DsType getDsType() { return DsType.MaxCompute; }

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
    public boolean isArray() {
        switch (this) {
            case ARRAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isStruct() {
        switch (this) {
            case MAP:
            case STRUCT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isNumber() {
        switch (this) {
            case BIGINT:
            case DOUBLE:
            case DECIMAL:
            case TINYINT:
            case SMALLINT:
            case INT:
            case FLOAT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case BINARY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isString() {
        switch (this) {
            case STRING:
            case VARCHAR:
            case CHAR:
            case JSON:
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
            case TIMESTAMP:
            case TIMESTAMP_NTZ:
            case INTERVAL_YEAR_MONTH:
            case INTERVAL_DAY_TIME:
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
        switch (this) {
            case CHAR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATETIME:
            case DATE:
            case TIMESTAMP:
            case TIMESTAMP_NTZ:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case DATETIME:
            case TIMESTAMP:
            case TIMESTAMP_NTZ:
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
    public boolean isAccurateDecimal() {
        switch (this) {
            case DECIMAL:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }

    public static McSqlTypes toMcType(String typeName) {
        try {
            switch (OdpsType.valueOf(typeName)) {
                case BIGINT:
                    return BIGINT;
                case DOUBLE:
                    return DOUBLE;
                case BOOLEAN:
                    return BOOLEAN;
                case DATETIME:
                    return DATETIME;
                case STRING:
                    return STRING;
                case DECIMAL:
                    return DECIMAL;
                case MAP:
                    return MAP;
                case ARRAY:
                    return ARRAY;
                case TINYINT:
                    return TINYINT;
                case SMALLINT:
                    return SMALLINT;
                case INT:
                    return INT;
                case FLOAT:
                    return FLOAT;
                case CHAR:
                    return CHAR;
                case VARCHAR:
                    return VARCHAR;
                case DATE:
                    return DATE;
                case TIMESTAMP:
                    return TIMESTAMP;
                case BINARY:
                    return BINARY;
                case INTERVAL_DAY_TIME:
                    return INTERVAL_DAY_TIME;
                case INTERVAL_YEAR_MONTH:
                    return INTERVAL_YEAR_MONTH;
                case STRUCT:
                    return STRUCT;
                case JSON:
                    return JSON;
            }
        } catch (Exception ignored) {
        }
        throw new UnsupportedOperationException("Unsupported MaxCompute tableType " + typeName);
    }
}
