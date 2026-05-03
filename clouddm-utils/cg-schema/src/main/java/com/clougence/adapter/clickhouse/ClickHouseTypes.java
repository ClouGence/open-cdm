package com.clougence.adapter.clickhouse;

import java.sql.JDBCType;

import com.clougence.adapter.clickhouse.driver.ChDriverDataType;
import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * <li>https://clickhouse.com/docs/en/sql-reference/data-types/</li>
 * @version : 2021-06-24
 * @author 赵永春 (zyc@hasor.net)
 */
public enum ClickHouseTypes implements FieldType {

    Bool("Bool", JDBCType.BOOLEAN),

    Int8("Int8", ChDriverDataType.Int8),
    Int16("Int16", ChDriverDataType.Int16),
    Int32("Int32", ChDriverDataType.Int32),
    Int64("Int64", ChDriverDataType.Int64),
    Int128("Int128", ChDriverDataType.Int128),
    Int256("Int256", ChDriverDataType.Int256),

    UInt8("UInt8", ChDriverDataType.UInt8),
    UInt16("UInt16", ChDriverDataType.UInt16),
    UInt32("UInt32", ChDriverDataType.UInt32),
    UInt64("UInt64", ChDriverDataType.UInt64),
    UInt128("UInt128", ChDriverDataType.UInt128),
    UInt256("UInt256", ChDriverDataType.UInt256),

    Float32("Float32", ChDriverDataType.Float32),
    Float64("Float64", ChDriverDataType.Float64),

    Decimal32("Decimal32", ChDriverDataType.Decimal32),
    Decimal64("Decimal64", ChDriverDataType.Decimal64),
    Decimal128("Decimal128", ChDriverDataType.Decimal128),
    Decimal256("Decimal256", ChDriverDataType.Decimal256),
    Decimal("Decimal", ChDriverDataType.Decimal),

    String("String", ChDriverDataType.String),
    FixedString("FixedString", ChDriverDataType.FixedString),
    UUID("UUID", ChDriverDataType.UUID),
    Enum("Enum", ChDriverDataType.Enum8),
    Enum8("Enum8", ChDriverDataType.Enum8),
    Enum16("Enum16", ChDriverDataType.Enum16),
    IPv4("IPv4", ChDriverDataType.IPv4),
    IPv6("IPv6", ChDriverDataType.IPv6),

    Date("Date", ChDriverDataType.Date),
    Date32("Date32", ChDriverDataType.Date),
    DateTime("DateTime", ChDriverDataType.DateTime),
    DateTime32("DateTime32", ChDriverDataType.DateTime64),
    DateTime64("DateTime64", ChDriverDataType.DateTime64),

    // Interval data type values can’t be stored in tables.
    IntervalSecond("INTERVAL ? SECOND", ChDriverDataType.IntervalSecond), //IntervalSecond
    IntervalMinute("INTERVAL ? MINUTE", ChDriverDataType.IntervalMinute), //IntervalMinute
    IntervalHour("INTERVAL ? HOUR", ChDriverDataType.IntervalHour), //IntervalHour
    IntervalDay("INTERVAL ? DAY", ChDriverDataType.IntervalDay), //IntervalDay
    IntervalWeek("INTERVAL ? WEEK", ChDriverDataType.IntervalWeek), //IntervalWeek
    IntervalMonth("INTERVAL ? MONTH", ChDriverDataType.IntervalMonth), //IntervalMonth
    IntervalQuarter("INTERVAL ? QUARTER", ChDriverDataType.IntervalQuarter), //IntervalQuarter
    IntervalYear("INTERVAL ? YEAR", ChDriverDataType.IntervalYear), //IntervalYear

    Nothing("Nothing", ChDriverDataType.Nothing),
    Nested("Nested", ChDriverDataType.Nested),
    Tuple("Tuple", ChDriverDataType.Tuple),
    Array("Array", ChDriverDataType.Array),
    Map("Map", ChDriverDataType.Map),
    Nullable("Nullable", JDBCType.NULL),
    AggregateFunction("AggregateFunction", ChDriverDataType.AggregateFunction),
    Unknown("Unknown", ChDriverDataType.Unknown),
    SimpleAggregateFunction("SimpleAggregateFunction", ChDriverDataType.AggregateFunction),
    //

    // this type is experimental, ck config need set allow_experimental_geo_types = 1
    Point("Point", JDBCType.VARCHAR),
    Polygon("Polygon", JDBCType.VARCHAR),
    MultiPolygon("MultiPolygon", JDBCType.VARCHAR),
    LowCardinality("LowCardinality", JDBCType.VARCHAR),
    Ring("Ring", JDBCType.VARCHAR),;

    //    LowCardinality("LowCardinality", Fmt.of("LowCardinality(?)")),
    private final String           codeKey;
    private final JDBCType         jdbcType;
    private final ChDriverDataType driverType;

    ClickHouseTypes(String codeKey, ChDriverDataType driverType){
        this.codeKey = codeKey;
        this.jdbcType = driverType.getJdbcType();
        this.driverType = driverType;
    }

    ClickHouseTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
        this.driverType = null;
    }

    public static ClickHouseTypes valueOfCode(String code) {
        for (ClickHouseTypes tableType : ClickHouseTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported ClickHouse columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.ClickHouse; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case Float32:
            case Float64:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isArray() {
        switch (this) {
            case Array:
            case Tuple:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isStruct() { return this == Map; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case Int8:
            case Int16:
            case Int32:
            case Int64:
            case Int128:
            case Int256:
            case UInt8:
            case UInt16:
            case UInt32:
            case UInt64:
            case UInt128:
            case UInt256:
            case Decimal:
            case Decimal32:
            case Decimal64:
            case Decimal128:
            case Decimal256:
            case Float32:
            case Float64:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() { return false; }

    @Override
    public boolean isString() {
        switch (this) {
            case String:
            case FixedString:
            case UUID:
            case Enum8:
            case Enum16:
            case IPv4:
            case IPv6:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
        switch (this) {
            case Date:
            case DateTime:
            case DateTime32:
            case DateTime64:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isGeometry() {
        switch (this) {
            case Point:
            case Ring:
            case Polygon:
            case MultiPolygon:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBoolean() {
        switch (this) {
            case Bool:
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
            case Date:
            case DateTime:
            case DateTime32:
            case DateTime64:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case DateTime:
            case DateTime32:
            case DateTime64:
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
            case Decimal:
            case Decimal32:
            case Decimal64:
            case Decimal128:
            case Decimal256:
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

    public ChDriverDataType getDriverType() { return this.driverType; }

    public boolean isUnsigned() {
        switch (this) {
            case UInt8:
            case UInt16:
            case UInt32:
            case UInt64:
            case UInt128:
            case UInt256:
                return true;
            default:
                return false;
        }
    }

}
