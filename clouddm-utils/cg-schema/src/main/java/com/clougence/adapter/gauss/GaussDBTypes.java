package com.clougence.adapter.gauss;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

import com.clougence.adapter.gauss.driver.Oid;
import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;

public enum GaussDBTypes implements FieldType {

    SMALLINT("smallint", JDBCType.SMALLINT, Oid.INT2, false, false), // smallint、int2
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

    BIT("bit", JDBCType.BINARY, Oid.BIT, false, false),
    // modify by junyu.change JDBCType.OTHER to JDBCType.BIT
    BIT_VARYING("bit varying", JDBCType.BINARY, Oid.VARBIT, false, false), // varbit、bit varying
    BOOLEAN("boolean", JDBCType.BIT, Oid.BOOL, false, false), // bool、boolean

    XML("xml", JDBCType.SQLXML, Oid.XML, false, false),
    BYTEA("bytea", JDBCType.BINARY, Oid.BYTEA, false, false),
    REF_CURSOR("refcursor", JDBCType.REF_CURSOR, Oid.REF_CURSOR, false, false), // refcursor

    POINT("point", JDBCType.OTHER, Oid.POINT, false, false),
    LSEG("lseg", JDBCType.OTHER, null, false, false),
    BOX("box", JDBCType.OTHER, Oid.BOX, false, false),
    PATH("path", JDBCType.OTHER, null, false, false),
    POLYGON("polygon", JDBCType.OTHER, null, false, false),
    CIRCLE("circle", JDBCType.OTHER, null, false, false),
    GEOMETRY("geometry", JDBCType.OTHER, null, false, true),
    GEOGRAPHY("geography", JDBCType.OTHER, null, false, true),

    CIDR("cidr", JDBCType.OTHER, null, false, false),
    INET("inet", JDBCType.OTHER, null, false, false),
    MACADDR("macaddr", JDBCType.OTHER, null, false, false),
    MACADDR8("macaddr8", JDBCType.OTHER, null, false, false),
    HSTORE("hstore", JDBCType.OTHER, null, false, true),
    CITEXT("citext", JDBCType.OTHER, null, false, true),

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

    XID("xid", JDBCType.OTHER, null, false, false),
    TXID("txid", JDBCType.OTHER, null, false, false),
    TXID_SNAPSHOT("txid_snapshot", JDBCType.OTHER, null, false, false),
    PG_NODE_TREE("pg_node_tree", JDBCType.OTHER, null, false, false),
    REGTYPE("regtype", JDBCType.OTHER, null, false, false),
    ABSTIME("abstime", JDBCType.OTHER, null, false, false),
    ANYARRAY("anyarray", JDBCType.ARRAY, null, false, false),
    ACLITEM("aclitem", JDBCType.OTHER, null, false, false),

    // vector
    INT2VECTOR("int2vector", JDBCType.OTHER, null, false, false),
    OIDVECTOR("oidvector", JDBCType.OTHER, null, false, false),
    VECTOR("vector", JDBCType.OTHER, null, false, false),
    HALFVEC("halfvec", JDBCType.OTHER, null, false, false),

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
    LSEG_ARRAY("lseg[]", JDBCType.ARRAY, null, true, false), // 1018
    BOX_ARRAY("box[]", JDBCType.ARRAY, null, true, false), // 1020
    PATH_ARRAY("path[]", JDBCType.ARRAY, null, true, false), // 1019
    POLYGON_ARRAY("polygon[]", JDBCType.ARRAY, null, true, false), // 1027
    CIRCLE_ARRAY("circle[]", JDBCType.ARRAY, null, true, false), // 719
    GEOMETRY_ARRAY("geometry[]", JDBCType.ARRAY, null, true, true),
    GEOGRAPHY_ARRAY("geography[]", JDBCType.ARRAY, null, true, true),

    CIDR_ARRAY("cidr[]", JDBCType.ARRAY, null, true, false), // 651
    INET_ARRAY("inet[]", JDBCType.ARRAY, null, true, false), // 1041
    MACADDR_ARRAY("macaddr[]", JDBCType.ARRAY, null, true, false), // 1040
    MACADDR8_ARRAY("macaddr8[]", JDBCType.ARRAY, null, true, false), // 775
    HSTORE_ARRAY("hstore[]", JDBCType.ARRAY, null, true, true),
    CITEXT_ARRAY("citext[]", JDBCType.ARRAY, null, true, true),

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

    XID_ARRAY("xid[]", JDBCType.ARRAY, null, true, false),
    TXID_ARRAY("txid[]", JDBCType.ARRAY, null, true, false),
    TXID_SNAPSHOT_ARRAY("txid_snapshot[]", JDBCType.ARRAY, null, true, false), //2949
    PG_NODE_TREE_ARRAY("pg_node_tree[]", JDBCType.ARRAY, null, true, false), // 3221
    REGTYPE_ARRAY("regtype[]", JDBCType.ARRAY, null, true, false),
    ABSTIME_ARRAY("abstime[]", JDBCType.ARRAY, null, true, false),
    ANYARRAY_ARRAY("anyarray[]", JDBCType.ARRAY, null, true, false),
    ACLITEM_ARRAY("aclitem[]", JDBCType.ARRAY, null, true, false),
    INT2VECTOR_ARRAY("int2vector_array", JDBCType.ARRAY, null, true, false),
    OIDVECTOR_ARRAY("oidvector_array", JDBCType.ARRAY, null, true, false),;

    private final boolean  array;

    private final String   codeKey;

    private final JDBCType jdbcType;

    private final Integer  pgOid;

    private final boolean  needFullName;

    GaussDBTypes(String codeKey, JDBCType jdbcType, Integer pgOid, boolean array, boolean needFullName){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
        this.pgOid = pgOid;
        this.array = array;
        this.needFullName = needFullName;
    }

    public static GaussDBTypes valueOfCode(String code) {
        code = code.toLowerCase();
        if (AliasNames.containsKey(code)) {
            code = AliasNames.get(code);
        }
        for (GaussDBTypes tableType : GaussDBTypes.values()) {
            if (StringUtils.equalsIgnoreCase(tableType.codeKey, code)) {
                return tableType;
            }

            if (tableType.needFullName && StringUtils.endsWithIgnoreCase(code, tableType.codeKey)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported gaussdb columnType " + code);
    }

    public static GaussDBTypes valueOfTypeOid(Number pgOid) {
        if (pgOid == null) {
            return null;
        }
        for (GaussDBTypes tableType : GaussDBTypes.values()) {
            if (tableType.pgOid != null && tableType.pgOid.equals(pgOid.intValue())) {
                return tableType;
            }
        }
        return null;
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.GaussDB; }

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
            case LSEG_ARRAY:
            case BOX_ARRAY:
            case PATH_ARRAY:
            case POLYGON_ARRAY:
            case CIRCLE_ARRAY:
            case GEOMETRY_ARRAY:
            case GEOGRAPHY_ARRAY:
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
            case PG_NODE_TREE_ARRAY:
                return true;
            default:
                return false;
        }
    }

    /**
     *
     * @return key is array type codeKey,value is element type in it.
     *
     * !DO NOT CALL THIS METHOD REPEAT.
     */
    public static Map<String, GaussDBTypes> arrayEleTypesOfCodeKey() {
        Map<String, GaussDBTypes> re = new HashMap<>();
        for (GaussDBTypes colType : GaussDBTypes.values()) {
            if (!colType.isArray()) {
                continue;
            }

            String codeKey = colType.getCodeKey();
            String eTypeStr = codeKey.substring(0, codeKey.length() - 2);

            for (GaussDBTypes eType : GaussDBTypes.values()) {
                if (StringUtils.equalsIgnoreCase(eType.codeKey, eTypeStr)) {
                    re.put(codeKey, eType);
                }
            }
        }

        return re;
    }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case OID:
            case NUMERIC:
            case MONEY:
            case BOOLEAN:
            case REAL:
            case DOUBLE_PRECISION:
            case SMALLINT_ARRAY:
            case INTEGER_ARRAY:
            case BIGINT_ARRAY:
            case OID_ARRAY:
            case NUMERIC_ARRAY:
            case MONEY_ARRAY:
            case BOOLEAN_ARRAY:
            case REAL_ARRAY:
            case DOUBLE_PRECISION_ARRAY:
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
            case BIT_ARRAY:
            case BIT_VARYING_ARRAY:
            case BYTEA_ARRAY:
            case JSONB_ARRAY:
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
            case CHARACTER_ARRAY:
            case BPCHAR_ARRAY:
            case CHARACTER_VARYING_ARRAY:
            case TEXT_ARRAY:
            case NAME_ARRAY:
            case INTERVAL_ARRAY:
            case XML_ARRAY:
            case CIDR_ARRAY:
            case INET_ARRAY:
            case MACADDR_ARRAY:
            case MACADDR8_ARRAY:
            case UUID_ARRAY:
            case JSON_ARRAY:
            case INT4RANGE_ARRAY:
            case INT8RANGE_ARRAY:
            case NUMRANGE_ARRAY:
            case TSRANGE_ARRAY:
            case TSTZRANGE_ARRAY:
            case DATERANGE_ARRAY:
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
            case LSEG:
            case BOX:
            case PATH:
            case POLYGON:
            case CIRCLE:
            case GEOMETRY:
            case GEOGRAPHY:
            case POINT_ARRAY:
            case LSEG_ARRAY:
            case BOX_ARRAY:
            case PATH_ARRAY:
            case POLYGON_ARRAY:
            case CIRCLE_ARRAY:
            case GEOMETRY_ARRAY:
            case GEOGRAPHY_ARRAY:
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
        return this == GaussDBTypes.CHARACTER;
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
    public String templateOfSelectCol(Object[] args, String colName) {
        switch (this) {
            case GEOMETRY:
                return "ST_AsEWKT(" + colName + ")";
            default:
                return colName;
        }
    }

    @Override
    public String templateOfInsert(Object[] args) {
        switch (this) {
            case BIT:
            case BIT_ARRAY:
            case BIT_VARYING:
            case BIT_VARYING_ARRAY:
            case UUID:
            case INTERVAL:
            case POINT:
            case LSEG:
            case BOX:
            case PATH:
            case POLYGON:
            case CIRCLE:
            case GEOMETRY:
            case UUID_ARRAY:
            case INTERVAL_ARRAY:
            case POINT_ARRAY:
            case LSEG_ARRAY:
            case BOX_ARRAY:
            case PATH_ARRAY:
            case POLYGON_ARRAY:
            case CIRCLE_ARRAY:
            case GEOMETRY_ARRAY:
                if (args != null && args.length > 0) {
                    return "?::" + this.codeKey + "(" + args[0] + ")";
                } else {
                    return "?::" + this.codeKey;
                }
            case MONEY:
                return "?::numeric::money";
            case MONEY_ARRAY:
                return "?::numeric[]::money[]";
            default:
                return "?";
        }
    }

    @Override
    public String templateOfSetValue(Object[] args) {
        switch (this) {
            case BIT:
            case BIT_ARRAY:
            case BIT_VARYING:
            case BIT_VARYING_ARRAY:
            case UUID:
            case INTERVAL:
            case POINT:
            case LSEG:
            case BOX:
            case PATH:
            case POLYGON:
            case CIRCLE:
            case GEOMETRY:
            case UUID_ARRAY:
            case INTERVAL_ARRAY:
            case POINT_ARRAY:
            case LSEG_ARRAY:
            case BOX_ARRAY:
            case PATH_ARRAY:
            case POLYGON_ARRAY:
            case CIRCLE_ARRAY:
            case GEOMETRY_ARRAY:
                if (args != null && args.length > 0) {
                    return "?::" + this.codeKey + "(" + args[0] + ")";
                } else {
                    return "?::" + this.codeKey;
                }
            case MONEY:
                return "?::numeric::money";
            case MONEY_ARRAY:
                return "?::numeric[]::money[]";
            default:
                return "?";
        }
    }

    @Override
    public String templateOfWhereValue(Object[] args) {
        switch (this) {
            case BIT:
            case BIT_ARRAY:
            case BIT_VARYING:
            case BIT_VARYING_ARRAY:
                if (args != null && args.length > 0) {
                    return "?::" + this.codeKey + "(" + args[0] + ")";
                } else {
                    return "?::" + this.codeKey;
                }
            case MONEY:
                return "?::numeric::money";
            case MONEY_ARRAY:
                return "?::numeric[]::money[]";
            case UUID:
                return "?::uuid";
            case INTERVAL:
                return "?::interval";
            default:
                return "?";
        }
    }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }

    public Integer getPgOid() { return this.pgOid; }

    public static final Map<String, String> AliasNames = new HashMap<>();

    static {
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

        AliasNames.put("_int", "integer[]");
        AliasNames.put("_int2", "smallint[]");
        AliasNames.put("_int4", "integer[]");
        AliasNames.put("_int8", "bigint[]");
        AliasNames.put("_numeric", "numeric[]");
        AliasNames.put("_float4", "real[]");
        AliasNames.put("_float8", "double precision[]");
        AliasNames.put("_money", "money[]");
        AliasNames.put("_bpchar", "character[]");
        AliasNames.put("_varchar", "character varying[]");
        AliasNames.put("_text", "text[]");
        AliasNames.put("_timestamp", "timestamp without time zone[]");
        AliasNames.put("_timestamptz", "timestamp with time zone[]");
        AliasNames.put("_date", "date[]");
        AliasNames.put("_time", "time without time zone[]");
        AliasNames.put("_timetz", "time with time zone[]");
        AliasNames.put("_interval", "interval[]");
        AliasNames.put("_bool", "boolean[]");
        AliasNames.put("_point", "point[]");
        AliasNames.put("_lseg", "lseg[]");
        AliasNames.put("_box", "box[]");
        AliasNames.put("_path", "path[]");
        AliasNames.put("_polygon", "polygon[]");
        AliasNames.put("_circle", "circle[]");
        AliasNames.put("_cidr", "cidr[]");
        AliasNames.put("_inet", "inet[]");
        AliasNames.put("_macaddr", "macaddr[]");
        AliasNames.put("_macaddr8", "macaddr8[]");
        AliasNames.put("_bit", "bit[]");
        AliasNames.put("_varbit", "bit varying[]");
        AliasNames.put("_bytea", "bytea[]");
        AliasNames.put("_tsvector", "tsvector[]");
        AliasNames.put("_tsquery", "tsquery[]");
        AliasNames.put("_uuid", "uuid[]");
        AliasNames.put("_xml", "xml[]");
        AliasNames.put("_json", "json[]");
        AliasNames.put("_jsonb", "jsonb[]");
        AliasNames.put("_int4range", "int4range[]");
        AliasNames.put("_int8range", "int8range[]");
        AliasNames.put("_numrange", "numrange[]");
        AliasNames.put("_tsrange", "tsrange[]");
        AliasNames.put("_tstzrange", "tstzrange[]");
        AliasNames.put("_daterange", "daterange[]");
        AliasNames.put("_oid", "oid[]");
        AliasNames.put("_name", "name[]");
    }
}
