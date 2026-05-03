package com.clougence.adapter.dameng;

import java.sql.JDBCType;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * https://eco.dameng.com/document/dm/zh-cn/pm/dm8_sql-data-types-operators.html
 *
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DmSqlTypes implements FieldType {

    CHAR("CHAR", Types.CHAR),
    NCHAR("NCHAR", Types.CHAR),
    VARCHAR("VARCHAR", Types.VARCHAR),
    NVARCHAR2("NVARCHAR2", Types.VARCHAR),
    TEXT("TEXT", Types.LONGVARCHAR),
    CLOB("CLOB", Types.CLOB), // strings of letters and numbers
    //
    NUMERIC("NUMERIC", Types.DECIMAL),
    TINYINT("TINYINT", Types.TINYINT),
    SMALLINT("SMALLINT", Types.SMALLINT),
    INT("INT", Types.INTEGER),
    BIGINT("BIGINT", Types.BIGINT),
    FLOAT("FLOAT", Types.FLOAT),
    REAL("REAL", Types.REAL),
    BIT("BIT", Types.BIT),
    //
    BINARY("BINARY", Types.BINARY),
    VARBINARY("VARBINARY", Types.VARBINARY),
    IMAGE("IMAGE", Types.LONGVARBINARY),
    BFILE("BFILE", Types.VARCHAR), // from jdbc driver query result
    //
    DATE("DATE", Types.DATE),
    TIME("TIME", Types.TIME),
    TIME_WITH_TIME_ZONE("TIME WITH TIME ZONE", 94),
    TIMESTAMP("TIMESTAMP", Types.TIMESTAMP),
    TIMESTAMP_WITH_TIME_ZONE("TIMESTAMP WITH TIME ZONE", 95),
    TIMESTAMP_WITH_LOCAL_TIME_ZONE("TIMESTAMP WITH LOCAL TIME ZONE", Types.TIMESTAMP),

    INTERVAL_YEAR("INTERVAL YEAR", 101),
    INTERVAL_YEAR_TO_MONTH("INTERVAL YEAR TO MONTH", 107),
    INTERVAL_MONTH("INTERVAL MONTH", 102),
    INTERVAL_DAY("INTERVAL DAY", 103),
    INTERVAL_DAY_TO_HOUR("INTERVAL DAY TO HOUR", 108),
    INTERVAL_DAY_TO_MINUTE("INTERVAL DAY TO MINUTE", 109),
    INTERVAL_DAY_TO_SECOND("INTERVAL DAY TO SECOND", 110),
    INTERVAL_HOUR("INTERVAL HOUR", 104),
    INTERVAL_HOUR_TO_MINUTE("INTERVAL HOUR TO MINUTE", 111),
    INTERVAL_HOUR_TO_SECOND("INTERVAL HOUR TO SECOND", 112),
    INTERVAL_MINUTE("INTERVAL MINUTE", 105),
    INTERVAL_MINUTE_TO_SECOND("INTERVAL MINUTE TO SECOND", 113),
    INTERVAL_SECOND("INTERVAL SECOND", 106),
    //
    INDTAB("INDTAB", Types.OTHER),
    CLASS("CLASS", Types.OTHER),
    ROWID("ROWID", Types.OTHER),;

    private final String   codeKey;
    private final JDBCType jdbcType;
    private final Integer  jdbcTypeNum;

    DmSqlTypes(String codeKey, Integer jdbcType){
        JDBCType tmpJdbcType;
        this.codeKey = codeKey;
        try {
            tmpJdbcType = jdbcType == null ? null : JDBCType.valueOf(jdbcType);
        } catch (Exception e) {
            tmpJdbcType = null;
        }
        this.jdbcType = tmpJdbcType;
        this.jdbcTypeNum = jdbcType;
    }

    public static DmSqlTypes valueOfCode(String code) {
        code = code.toUpperCase();
        if (AliasNames.containsKey(code)) {
            code = AliasNames.get(code);
        }

        for (DmSqlTypes tableType : DmSqlTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported oracle columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Dameng; }

    @Override
    public boolean isReadOnly() {
        switch (this) {
            case ROWID:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case FLOAT:
            case REAL:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isArray() { return false; }

    @Override
    public boolean isStruct() {
        switch (this) {
            case CLASS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isNumber() {
        switch (this) {
            case NUMERIC:
            case TINYINT:
            case SMALLINT:
            case INT:
            case BIGINT:
            case FLOAT:
            case REAL:
            case BIT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case BINARY:
            case VARBINARY:
            case IMAGE:
            case BFILE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isString() {
        switch (this) {
            case CHAR:
            case NCHAR:
            case VARCHAR:
            case NVARCHAR2:
            case TEXT:
            case CLOB:
            case ROWID:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
        switch (this) {
            case TIME_WITH_TIME_ZONE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
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
        switch (this) {
            case CHAR:
            case NCHAR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case TIME:
            case TIME_WITH_TIME_ZONE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasZone() {
        switch (this) {
            case TIME_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isAccurateDecimal() {
        switch (this) {
            case NUMERIC:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Integer getJdbcType() { return this.jdbcTypeNum; }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }

    public static DmSqlTypes toDmdbType(String dataType) {
        dataType = dataType.toUpperCase();
        if (AliasNames.containsKey(dataType)) {
            dataType = AliasNames.get(dataType);
        }

        //        if (dataType.startsWith("xxx")) {
        //            return DmSqlTypes.NUMBER_DECIMAL;
        //        }
        //
        for (DmSqlTypes type : DmSqlTypes.values()) {
            if (type.getCodeKey().equalsIgnoreCase(dataType)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("Unsupported DM tableType " + dataType);
    }

    public boolean isInterval() {
        switch (this) {
            case INTERVAL_YEAR:
            case INTERVAL_YEAR_TO_MONTH:
            case INTERVAL_MONTH:
            case INTERVAL_DAY:
            case INTERVAL_DAY_TO_HOUR:
            case INTERVAL_DAY_TO_MINUTE:
            case INTERVAL_DAY_TO_SECOND:
            case INTERVAL_HOUR:
            case INTERVAL_HOUR_TO_MINUTE:
            case INTERVAL_HOUR_TO_SECOND:
            case INTERVAL_MINUTE:
            case INTERVAL_MINUTE_TO_SECOND:
            case INTERVAL_SECOND:
                return true;
            default:
                return false;
        }
    }

    public static final Map<String, String> AliasNames = new HashMap<>();

    static {
        AliasNames.put("CHARACTER", "CHAR");
        AliasNames.put("NATIONAL CHARACTER", "NCHAR");
        AliasNames.put("NATIONAL CHAR", "NCHAR");
        AliasNames.put("VARCHAR2", "VARCHAR");
        AliasNames.put("CHARACTER VARYING", "VARCHAR");
        AliasNames.put("CHAR VARYING", "VARCHAR");
        AliasNames.put("NCHAR VARYING", "NVARCHAR2");
        AliasNames.put("NATIONAL CHAR VARYING", "NVARCHAR2");
        AliasNames.put("NATIONAL CHARACTER VARYING", "NVARCHAR2");
        AliasNames.put("LONG", "TEXT");
        AliasNames.put("LONGVARCHAR", "TEXT");
        AliasNames.put("NCLOB", "CLOB");
        AliasNames.put("DECIMAL", "NUMERIC");
        AliasNames.put("DEC", "NUMERIC");
        AliasNames.put("NUMBER", "NUMERIC");
        AliasNames.put("BYTE", "TINYINT");
        AliasNames.put("INTEGER", "INT");
        AliasNames.put("DOUBLE", "FLOAT");
        AliasNames.put("DOUBLE PRECISION", "FLOAT");
        AliasNames.put("BLOB", "IMAGE");
        AliasNames.put("LONGVARBINARY", "IMAGE");
        AliasNames.put("RAW", "VARBINARY");
        AliasNames.put("DATETIME", "TIMESTAMP");
        AliasNames.put("DATETIME WITH TIME ZONE", "TIMESTAMP WITH TIME ZONE");
    }

}
