package com.clougence.adapter.polar.porpg;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

import com.clougence.adapter.polar.porpg.driver.Oid;
import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;

/**
 * <li>https://www.postgresql.org/docs/13/index.html</li>
 * <li>org.postgresql.jdbc.TypeInfoCache</li>
 * @version : 2021-05-10
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBPgTypes implements FieldType {

    SMALLSERIAL("smallserial", JDBCType.SMALLINT, Oid.INT2, true, false, false), // smallserial、serial2
    SERIAL("serial", JDBCType.INTEGER, Oid.INT4, true, false, false), // serial、serial4
    BIGSERIAL("bigserial", JDBCType.BIGINT, Oid.INT8, true, false, false), // bigserial、serial8

    SMALLINT("smallint", JDBCType.SMALLINT, Oid.INT2, false, false, false), // smallint、int2
    INTEGER("integer", JDBCType.INTEGER, Oid.INT4, false, false), // integer、int、int4
    BIGINT("bigint", JDBCType.BIGINT, Oid.INT8, false, false), // bigint、int8
    OID("oid", JDBCType.BIGINT, Oid.OID, false, false),
    NUMERIC("numeric", JDBCType.NUMERIC, Oid.NUMERIC, false, false), // numeric、decimal
    REAL("real", JDBCType.REAL, Oid.FLOAT4, false, false), // real、float4
    DOUBLE_PRECISION("double precision", JDBCType.DOUBLE, Oid.FLOAT8, false, false), // double precision、float、float8

    MONEY("money", JDBCType.DOUBLE, Oid.MONEY, false, false),

    CHARACTER("character", JDBCType.CHAR, Oid.CHAR, false, false), // char、character
    BPCHAR("bpchar", JDBCType.CHAR, Oid.BPCHAR, false, false),
    CHARACTER_VARYING("character varying", JDBCType.VARCHAR, Oid.VARCHAR, false, false), // character varying、varchar
    TEXT("text", JDBCType.VARCHAR, Oid.TEXT, false, false),
    NAME("name", JDBCType.VARCHAR, Oid.NAME, false, false),

    TIMESTAMP_WITHOUT_TIME_ZONE("timestamp without time zone", JDBCType.TIMESTAMP, Oid.TIMESTAMP, false, false), // timestamp、timestamp without time zone
    TIMESTAMP_WITH_TIME_ZONE("timestamp with time zone", JDBCType.TIMESTAMP, Oid.TIMESTAMPTZ, false, false), // timestamptz、timestamp with time zone
    TIME_WITHOUT_TIME_ZONE("time without time zone", JDBCType.TIME, Oid.TIME, false, false), // time、time without time zone
    TIME_WITH_TIME_ZONE("time with time zone", JDBCType.TIME, Oid.TIMETZ, false, false), // timetz、time with time zone
    DATE("date", JDBCType.DATE, Oid.DATE, false, false),
    INTERVAL("interval", JDBCType.OTHER, Oid.INTERVAL, false, false),

    BIT("bit", JDBCType.BIT, Oid.BIT, false, false),
    //modify by junyu.change JDBCType.OTHER to JDBCType.BIT
    BIT_VARYING("bit varying", JDBCType.BIT, Oid.VARBIT, false, false), // varbit、bit varying
    BOOLEAN("boolean", JDBCType.BIT, Oid.BOOL, false, false), // bool、boolean

    XML("xml", JDBCType.SQLXML, Oid.XML, false, false),
    BYTEA("bytea", JDBCType.BINARY, Oid.BYTEA, false, false),
    REF_CURSOR("refcursor", JDBCType.REF_CURSOR, Oid.REF_CURSOR, false, false), // refcursor

    POINT("point", JDBCType.OTHER, Oid.POINT, false, false),
    LINE("line", JDBCType.OTHER, null, false, false),
    LSEG("lseg", JDBCType.OTHER, null, false, false),
    BOX("box", JDBCType.OTHER, Oid.BOX, false, false),
    PATH("path", JDBCType.OTHER, null, false, false),
    POLYGON("polygon", JDBCType.OTHER, null, false, false),
    CIRCLE("circle", JDBCType.OTHER, null, false, false),
    GEOMETRY("geometry", JDBCType.OTHER, null, false, true),

    CIDR("cidr", JDBCType.OTHER, null, false, false),
    INET("inet", JDBCType.OTHER, null, false, false),
    MACADDR("macaddr", JDBCType.OTHER, null, false, false),
    MACADDR8("macaddr8", JDBCType.OTHER, null, false, false),

    TSVECTOR("tsvector", JDBCType.OTHER, null, false, false),
    TSQUERY("tsquery", JDBCType.OTHER, null, false, false),

    UUID("uuid", JDBCType.OTHER, Oid.UUID, false, false),

    JSON("json", JDBCType.OTHER, Oid.JSON, false, false),
    JSONB("jsonb", JDBCType.OTHER, null, false, false),

    INT4RANGE("int4range", JDBCType.OTHER, null, false, false),
    INT8RANGE("int8range", JDBCType.OTHER, null, false, false),
    NUMRANGE("numrange", JDBCType.OTHER, null, false, false),
    TSRANGE("tsrange", JDBCType.OTHER, null, false, false),
    TSTZRANGE("tstzrange", JDBCType.OTHER, null, false, false),
    DATERANGE("daterange", JDBCType.OTHER, null, false, false),

    PG_LSN("pg_lsn", JDBCType.OTHER, null, false, false),
    TXID_SNAPSHOT("txid_snapshot", JDBCType.OTHER, null, false, false),

    // --------------------------------------------------------------------------------------------------------------------------
    SMALLINT_ARRAY("smallint[]", JDBCType.ARRAY, Oid.INT2_ARRAY, true, false), // smallint、int2
    INTEGER_ARRAY("integer[]", JDBCType.ARRAY, Oid.INT4_ARRAY, true, false), // integer、int、int4
    BIGINT_ARRAY("bigint[]", JDBCType.ARRAY, Oid.INT8_ARRAY, true, false), // bigint、int8、
    OID_ARRAY("oid[]", JDBCType.ARRAY, Oid.OID_ARRAY, true, false),
    NUMERIC_ARRAY("numeric[]", JDBCType.ARRAY, Oid.NUMERIC_ARRAY, true, false), // numeric、decimal
    REAL_ARRAY("real[]", JDBCType.ARRAY, Oid.FLOAT4_ARRAY, true, false), // real、float4
    DOUBLE_PRECISION_ARRAY("double precision[]", JDBCType.ARRAY, Oid.FLOAT8_ARRAY, true, false), // double precision、float、float8

    MONEY_ARRAY("money[]", JDBCType.ARRAY, Oid.MONEY_ARRAY, true, false),

    CHARACTER_ARRAY("character[]", JDBCType.ARRAY, Oid.CHAR_ARRAY, true, false), // char、character
    BPCHAR_ARRAY("bpchar[]", JDBCType.ARRAY, Oid.BPCHAR_ARRAY, true, false),
    CHARACTER_VARYING_ARRAY("character varying[]", JDBCType.ARRAY, Oid.VARCHAR_ARRAY, true, false), // character varying、varchar
    TEXT_ARRAY("text[]", JDBCType.ARRAY, Oid.TEXT_ARRAY, true, false),
    NAME_ARRAY("name[]", JDBCType.ARRAY, Oid.NAME_ARRAY, true, false),

    TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY("timestamp without time zone[]", JDBCType.ARRAY, Oid.TIMESTAMP_ARRAY, true, false), // timestamp、timestamp without time zone
    TIMESTAMP_WITH_TIME_ZONE_ARRAY("timestamp with time zone[]", JDBCType.ARRAY, Oid.TIMESTAMPTZ_ARRAY, true, false), // timestamptz、timestamp with time zone
    TIME_WITHOUT_TIME_ZONE_ARRAY("time without time zone[]", JDBCType.ARRAY, Oid.TIME_ARRAY, true, false), // time、time without time zone
    TIME_WITH_TIME_ZONE_ARRAY("time with time zone[]", JDBCType.ARRAY, Oid.TIMETZ_ARRAY, true, false), // timetz、time with time zone
    DATE_ARRAY("date[]", JDBCType.ARRAY, Oid.DATE_ARRAY, true, false),
    INTERVAL_ARRAY("interval[]", JDBCType.ARRAY, Oid.INTERVAL_ARRAY, true, false),

    BIT_ARRAY("bit[]", JDBCType.ARRAY, Oid.BIT_ARRAY, true, false),
    BIT_VARYING_ARRAY("bit varying[]", JDBCType.ARRAY, Oid.VARBIT_ARRAY, true, false), // varbit、bit varying
    BOOLEAN_ARRAY("boolean[]", JDBCType.ARRAY, Oid.BOOL_ARRAY, true, false), // bool、boolean

    XML_ARRAY("xml[]", JDBCType.ARRAY, Oid.XML_ARRAY, true, false),
    BYTEA_ARRAY("bytea[]", JDBCType.ARRAY, Oid.BYTEA_ARRAY, true, false),
    REF_CURSOR_ARRAY("refcursor[]", JDBCType.ARRAY, Oid.REF_CURSOR_ARRAY, true, false), // refcursor

    POINT_ARRAY("point[]", JDBCType.ARRAY, Oid.POINT_ARRAY, true, false),
    LINE_ARRAY("line[]", JDBCType.ARRAY, null, true, false), // 629
    LSEG_ARRAY("lseg[]", JDBCType.ARRAY, null, true, false), // 1018
    BOX_ARRAY("box[]", JDBCType.ARRAY, null, true, false), // 1020
    PATH_ARRAY("path[]", JDBCType.ARRAY, null, true, false), // 1019
    POLYGON_ARRAY("polygon[]", JDBCType.ARRAY, null, true, false), // 1027
    CIRCLE_ARRAY("circle[]", JDBCType.ARRAY, null, true, false), // 719
    GEOMETRY_ARRAY("geometry[]", JDBCType.ARRAY, null, true, true),

    CIDR_ARRAY("cidr[]", JDBCType.ARRAY, null, true, false), // 651
    INET_ARRAY("inet[]", JDBCType.ARRAY, null, true, false), // 1041
    MACADDR_ARRAY("macaddr[]", JDBCType.ARRAY, null, true, false), // 1040
    MACADDR8_ARRAY("macaddr8[]", JDBCType.ARRAY, null, true, false), // 775

    TSVECTOR_ARRAY("tsvector[]", JDBCType.ARRAY, null, true, false), // 3643
    TSQUERY_ARRAY("tsquery[]", JDBCType.ARRAY, null, true, false), // 3645

    UUID_ARRAY("uuid[]", JDBCType.ARRAY, Oid.UUID_ARRAY, true, false), // 2951

    JSON_ARRAY("json[]", JDBCType.ARRAY, Oid.JSON_ARRAY, true, false),
    JSONB_ARRAY("jsonb[]", JDBCType.ARRAY, Oid.JSONB_ARRAY, true, false),

    INT4RANGE_ARRAY("int4range[]", JDBCType.ARRAY, null, true, false), // 3905
    INT8RANGE_ARRAY("int8range[]", JDBCType.ARRAY, null, true, false), // 3927
    NUMRANGE_ARRAY("numrange[]", JDBCType.ARRAY, null, true, false), // 3907
    TSRANGE_ARRAY("tsrange[]", JDBCType.ARRAY, null, true, false), // 3909
    TSTZRANGE_ARRAY("tstzrange[]", JDBCType.ARRAY, null, true, false), // 3911
    DATERANGE_ARRAY("daterange[]", JDBCType.ARRAY, null, true, false), // 3913

    PG_LSN_ARRAY("pg_lsn[]", JDBCType.ARRAY, null, true, false), // 3221
    TXID_SNAPSHOT_ARRAY("txid_snapshot[]", JDBCType.ARRAY, null, true, false), //2949
    ;

    private final boolean  serial;
    private final boolean  array;
    private final String   codeKey;
    private final JDBCType jdbcType;
    private final Integer  pgOid;
    private final boolean  needFullName;

    PolarDBPgTypes(String codeKey, JDBCType jdbcType, Integer pgOid, boolean array, boolean needFullName){
        this(codeKey, jdbcType, pgOid, false, array, needFullName);
    }

    PolarDBPgTypes(String codeKey, JDBCType jdbcType, Integer pgOid, boolean serial, boolean array, boolean needFullName){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
        this.pgOid = pgOid;
        this.serial = serial;
        this.array = array;
        this.needFullName = needFullName;
    }

    public static PolarDBPgTypes valueOfCode(String code) {
        code = code.toLowerCase();
        if (AliasNames.containsKey(code)) {
            code = AliasNames.get(code);
        }
        for (PolarDBPgTypes tableType : PolarDBPgTypes.values()) {
            if (StringUtils.equalsIgnoreCase(tableType.codeKey, code)) {
                return tableType;
            }

            if (tableType.needFullName && StringUtils.endsWithIgnoreCase(code, tableType.codeKey)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres columnType " + code);
    }

    public static PolarDBPgTypes valueOfTypeOid(Number pgOid) {
        if (pgOid == null) {
            return null;
        }
        for (PolarDBPgTypes tableType : PolarDBPgTypes.values()) {
            if (!tableType.serial && tableType.pgOid != null && tableType.pgOid.equals(pgOid.intValue())) {
                return tableType;
            }
        }
        return null;
    }

    public boolean isSerial() { return this.serial; }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.PolarDBPostgre; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case REAL:
            case DOUBLE_PRECISION:
            case REAL_ARRAY:
            case DOUBLE_PRECISION_ARRAY:
                return true;
            default:
                return false;
        }
    }

    public boolean isArray() {
        switch (this) {
            case SMALLINT_ARRAY:
            case INTEGER_ARRAY:
            case BIGINT_ARRAY:
            case OID_ARRAY:
            case NUMERIC_ARRAY:
            case REAL_ARRAY:
            case DOUBLE_PRECISION_ARRAY:
            case MONEY_ARRAY:
            case CHARACTER_ARRAY:
            case BPCHAR_ARRAY:
            case CHARACTER_VARYING_ARRAY:
            case TEXT_ARRAY:
            case NAME_ARRAY:
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITH_TIME_ZONE_ARRAY:
            case DATE_ARRAY:
            case INTERVAL_ARRAY:
            case BIT_ARRAY:
            case BIT_VARYING_ARRAY:
            case BOOLEAN_ARRAY:
            case XML_ARRAY:
            case BYTEA_ARRAY:
            case REF_CURSOR_ARRAY:
            case POINT_ARRAY:
            case LINE_ARRAY:
            case LSEG_ARRAY:
            case BOX_ARRAY:
            case PATH_ARRAY:
            case POLYGON_ARRAY:
            case CIRCLE_ARRAY:
            case GEOMETRY_ARRAY:
            case CIDR_ARRAY:
            case INET_ARRAY:
            case MACADDR_ARRAY:
            case MACADDR8_ARRAY:
            case TSVECTOR_ARRAY:
            case TSQUERY_ARRAY:
            case UUID_ARRAY:
            case JSON_ARRAY:
            case JSONB_ARRAY:
            case INT4RANGE_ARRAY:
            case INT8RANGE_ARRAY:
            case NUMRANGE_ARRAY:
            case TSRANGE_ARRAY:
            case TSTZRANGE_ARRAY:
            case DATERANGE_ARRAY:
            case PG_LSN_ARRAY:
            case TXID_SNAPSHOT_ARRAY:
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
            case SMALLSERIAL:
            case SERIAL:
            case BIGSERIAL:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case OID:
            case NUMERIC:
            case MONEY:
            case BOOLEAN:
            case REAL:
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
            case BIT_VARYING:
            case BYTEA:
            case JSONB:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isString() {
        switch (this) {
            case CHARACTER:
            case BPCHAR:
            case CHARACTER_VARYING:
            case TEXT:
            case NAME:
            case INTERVAL:
            case XML:
            case CIDR:
            case INET:
            case MACADDR:
            case MACADDR8:
            case UUID:
            case JSON:
            case INT4RANGE:
            case INT8RANGE:
            case NUMRANGE:
            case TSRANGE:
            case TSTZRANGE:
            case DATERANGE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
        switch (this) {
            case DATE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case TIME_WITHOUT_TIME_ZONE:
            case TIME_WITH_TIME_ZONE:
            case DATE_ARRAY:
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITH_TIME_ZONE_ARRAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isGeometry() {
        switch (this) {
            case POINT:
            case LINE:
            case LSEG:
            case BOX:
            case PATH:
            case POLYGON:
            case CIRCLE:
            case GEOMETRY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBoolean() {
        switch (this) {
            case BOOLEAN:
            case BOOLEAN_ARRAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasFixedChar() {
        return this == PolarDBPgTypes.CHARACTER;
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case DATE_ARRAY:
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case TIME_WITHOUT_TIME_ZONE:
            case TIME_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
            case TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITHOUT_TIME_ZONE_ARRAY:
            case TIME_WITH_TIME_ZONE_ARRAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasZone() {
        switch (this) {
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIME_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE_ARRAY:
            case TIME_WITH_TIME_ZONE_ARRAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isAccurateDecimal() { return this == NUMERIC; }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }

    public Integer getPgOid() { return this.pgOid; }

    public static final Map<String, String> AliasNames = new HashMap<>();

    static {
        AliasNames.put("serial2", "smallserial");
        AliasNames.put("serial4", "serial");
        AliasNames.put("serial8", "bigserial");
        AliasNames.put("int", "integer");
        AliasNames.put("int2", "smallint");
        AliasNames.put("int4", "integer");
        AliasNames.put("int8", "bigint");
        AliasNames.put("decimal", "numeric");
        AliasNames.put("float4", "real");
        AliasNames.put("float", "double precision");
        AliasNames.put("float8", "double precision");
        AliasNames.put("char", "character");
        AliasNames.put("varchar", "character varying");
        AliasNames.put("timestamp", "timestamp without time zone");
        AliasNames.put("timestamptz", "timestamp with time zone");
        AliasNames.put("time", "time without time zone");
        AliasNames.put("timetz", "time with time zone");
        AliasNames.put("varbit", "bit varying");
        AliasNames.put("bool", "boolean");
        // https://www.postgresql.org/docs/13/datatype-oid.html
        AliasNames.put("regclass", "oid");
        AliasNames.put("regcollation", "oid");
        AliasNames.put("regconfig", "oid");
        AliasNames.put("regdictionary", "oid");
        AliasNames.put("regnamespace", "oid");
        AliasNames.put("regoper", "oid");
        AliasNames.put("regoperator", "oid");
        AliasNames.put("regproc", "oid");
        AliasNames.put("regprocedure", "oid");
        AliasNames.put("regrole", "oid");
        AliasNames.put("regtype", "oid");

        AliasNames.put("int[]", "integer[]");
        AliasNames.put("int2[]", "smallint[]");
        AliasNames.put("int4[]", "integer[]");
        AliasNames.put("int8[]", "bigint[]");
        AliasNames.put("decimal[]", "numeric[]");
        AliasNames.put("float4[]", "real[]");
        AliasNames.put("float[]", "double precision[]");
        AliasNames.put("float8[]", "double precision[]");
        AliasNames.put("char[]", "character[]");
        AliasNames.put("varchar[]", "character varying[]");
        AliasNames.put("timestamp[]", "timestamp without time zone[]");
        AliasNames.put("timestamptz[]", "timestamp with time zone[]");
        AliasNames.put("time[]", "time without time zone[]");
        AliasNames.put("timetz[]", "time with time zone[]");
        AliasNames.put("varbit[]", "bit varying[]");
        AliasNames.put("bool[]", "boolean[]");
    }
}
