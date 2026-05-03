package com.clougence.adapter.hologres;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * https://help.aliyun.com/zh/maxcompute/user-guide/data-types
 */
public enum HgSqlTypes implements FieldType {

    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN, null),

    BIT("BIT", JDBCType.BINARY, null),
    VARBIT("VARBIT", JDBCType.BINARY, null),
    BYTEA("BYTEA", JDBCType.BINARY, null),

    INTEGER("INTEGER", JDBCType.INTEGER, null),
    BIGINT("BIGINT", JDBCType.BIGINT, null),
    REAL("REAL", JDBCType.FLOAT, null),
    NUMERIC("NUMERIC", JDBCType.NUMERIC, null),
    DOUBLE_PRECISION("DOUBLE PRECISION", JDBCType.DOUBLE, null),
    SERIAL("SERIAL", JDBCType.INTEGER, null),
    SMALLINT("SMALLINT", JDBCType.SMALLINT, null),
    DECIMAL("DECIMAL", JDBCType.DECIMAL, null),

    TEXT("TEXT", JDBCType.VARCHAR, null),
    CHARACTER("CHARACTER", JDBCType.VARCHAR, Oid.CHAR),
    CHARACTER_VARYING("CHARACTER VARYING", JDBCType.VARCHAR, Oid.VARCHAR),

    DATE("DATE", JDBCType.DATE, null),
    TIME("TIME", JDBCType.TIME, Oid.TIME),
    TIMESTAMP_WITHOUT_TIME_ZONE("TIMESTAMP", JDBCType.TIMESTAMP, Oid.TIMESTAMP),
    TIME_WITHOUT_TIME_ZONE("TIME WITHOUT TIME ZONE", JDBCType.TIMESTAMP, Oid.TIMETZ),
    TIMESTAMP_WITH_TIME_ZONE("TIMESTAMP WITH TIME ZONE", JDBCType.TIMESTAMP_WITH_TIMEZONE, Oid.TIMESTAMPTZ),

    ROARING_BITMAP("ROARING_BITMAP", JDBCType.ARRAY, null),
    ROARING_BITMAP64("ROARING_BITMAP64", JDBCType.ARRAY, null),

    INTERVAL("INTERVAL", JDBCType.OTHER, null),
    OID("OID", JDBCType.INTEGER, null),
    INET("INET", JDBCType.OTHER, null),
    MONEY("MONEY", JDBCType.OTHER, null),
    JSON("JSON", JDBCType.OTHER, null),
    JSONB("JSONB", JDBCType.OTHER, null),
    UUID("UUID", JDBCType.OTHER, null),
    POINT("point", JDBCType.OTHER, null),
    AVG_FLOAT8_STATE("AVG_FLOAT8_STATE", JDBCType.OTHER, null),;

    private final String   codeKey;
    private final JDBCType jdbcType;
    private final Integer  oid;
    //    private final OdpsType odpsType;

    HgSqlTypes(String codeKey, JDBCType jdbcType, Integer oid){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
        this.oid = oid;
    }

    public static HgSqlTypes valueOfCode(String code) {
        for (HgSqlTypes tableType : HgSqlTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported Hologres columnType " + code);
    }

    public static HgSqlTypes valueOfTypeOid(Number pgOid) {
        if (pgOid == null) {
            return null;
        }
        for (HgSqlTypes tableType : HgSqlTypes.values()) {
            if (tableType.oid != null && tableType.oid.equals(pgOid.intValue())) {
                return tableType;
            }
        }
        return null;
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return this.ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Hologres; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case DOUBLE_PRECISION:
            case REAL:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isArray() {
        switch (this) {
            case ROARING_BITMAP:
            case ROARING_BITMAP64:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case BIGINT:
            case REAL:
            case DECIMAL:
            case SMALLINT:
            case INTEGER:
            case OID:
            case SERIAL:
            case DOUBLE_PRECISION:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case BIT:
            case VARBIT:
            case BYTEA:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isString() {
        switch (this) {
            case JSON:
            case TEXT:
            case CHARACTER:
            case CHARACTER_VARYING:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
        switch (this) {
            case TIME:
            case DATE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIME_WITHOUT_TIME_ZONE:
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
            case CHARACTER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIME:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case TIME_WITHOUT_TIME_ZONE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case TIME:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIME_WITHOUT_TIME_ZONE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasZone() {
        switch (this) {
            case TIME_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                return true;
            default:
                return false;
        }
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

}
