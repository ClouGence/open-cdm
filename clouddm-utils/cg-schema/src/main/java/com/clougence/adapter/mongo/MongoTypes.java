package com.clougence.adapter.mongo;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * @author bucketli 2022/5/11 15:02:25
 */
public enum MongoTypes implements FieldType {

    String("String", JDBCType.VARCHAR),
    Integer("Integer", JDBCType.INTEGER),
    Long("Long", JDBCType.BIGINT),
    Double("Double", JDBCType.DOUBLE, true, false),
    Boolean("Boolean", JDBCType.BOOLEAN),
    Array("Array", JDBCType.ARRAY),
    Decimal("Decimal", JDBCType.DECIMAL),
    Date("Date", JDBCType.TIMESTAMP, false, true),
    Binary("Binary", JDBCType.BINARY),

    //subdocument
    Object("Object", JDBCType.OTHER),
    ObjectId("ObjectId", JDBCType.VARCHAR),;

    private final String   codeKey;
    private final JDBCType jdbcType;
    private final boolean  approximate;
    private final boolean  date;

    MongoTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
        this.approximate = false;
        this.date = false;
    }

    MongoTypes(String codeKey, JDBCType jdbcType, boolean approximate, boolean date){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
        this.approximate = approximate;
        this.date = date;
    }

    public static MongoTypes valueOfCode(String code) {
        for (MongoTypes tableType : MongoTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported MongoDB columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.MongoDB; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        return this.approximate;
    }

    @Override
    public boolean isArray() { return false; }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case Integer:
            case Long:
            case Decimal:
            case Double:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() { return false; }

    @Override
    public boolean isString() { return this == MongoTypes.String; }

    @Override
    public boolean isDataOrTime() { return this == MongoTypes.Date; }

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
        return this.date;
    }

    @Override
    public boolean hasTime() {
        return this.date;
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
