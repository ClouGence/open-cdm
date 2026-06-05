/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.adapter.oracle;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

import com.clougence.adapter.oracle.driver.OracleType;
import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * https://docs.oracle.com/en/database/oracle/oracle-database/21/sqlrf/Data-Types.html 参考 ojdbc8-19.8.0.0.jar
 *
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum OracleSqlTypes implements FieldType {

    CHAR("CHAR", OracleType.CHAR),
    NCHAR("NCHAR", OracleType.NCHAR),
    VARCHAR2("VARCHAR2", OracleType.VARCHAR2),
    NVARCHAR("NVARCHAR", OracleType.NVARCHAR),
    NVARCHAR2("NVARCHAR2", OracleType.NVARCHAR),
    LONG("LONG", OracleType.LONG),
    //
    // NUMBER("NUMBER", OracleType.NUMBER),
    NUMBER_BIGINT("NUMBER BIGINT", OracleType.NUMBER),
    NUMBER_DECIMAL("NUMBER DECIMAL", OracleType.NUMBER),
    FLOAT("FLOAT", OracleType.FLOAT),
    BINARY_FLOAT("BINARY FLOAT", OracleType.BINARY_FLOAT),
    BINARY_DOUBLE("BINARY DOUBLE", OracleType.BINARY_DOUBLE),
    //
    CLOB("CLOB", OracleType.CLOB),
    NCLOB("NCLOB", OracleType.NCLOB),
    BLOB("BLOB", OracleType.BLOB),
    BFILE("BFILE", OracleType.BFILE),
    //
    DATE("DATE", OracleType.DATE),
    TIMESTAMP("TIMESTAMP", OracleType.TIMESTAMP),
    TIMESTAMP_WITH_TIME_ZONE("TIMESTAMP WITH TIME ZONE", OracleType.TIMESTAMP_WITH_TIME_ZONE),
    TIMESTAMP_WITH_LOCAL_TIME_ZONE("TIMESTAMP WITH LOCAL TIME ZONE", OracleType.TIMESTAMP_WITH_LOCAL_TIME_ZONE),
    INTERVAL_YEAR_TO_MONTH("INTERVAL YEAR TO MONTH", OracleType.INTERVAL_YEAR_TO_MONTH),
    INTERVAL_DAY_TO_SECOND("INTERVAL DAY TO SECOND", OracleType.INTERVAL_DAY_TO_SECOND),
    //
    RAW("RAW", OracleType.RAW),
    LONG_RAW("LONG RAW", OracleType.LONG_RAW),
    //
    ROWID("ROWID", OracleType.ROWID),
    UROWID("UROWID", OracleType.UROWID),
    //
    OBJECT("OBJECT", OracleType.OBJECT),
    REF("REF", OracleType.REF),
    VARRAY("VARRAY", OracleType.VARRAY),
    NESTED_TABLE("NESTED_TABLE", OracleType.NESTED_TABLE),
    //
    PLSQL_BOOLEAN("PLSQL_BOOLEAN", OracleType.PLSQL_BOOLEAN),
    ANYTYPE("ANYTYPE", OracleType.ANYTYPE),
    ANYDATA("ANYDATA", OracleType.ANYDATA),
    ANYDATASET("ANYDATASET", OracleType.ANYDATASET),
    //
    XMLTYPE("XMLTYPE", OracleType.XMLTYPE),
    HTTPURITYPE("HTTPURITYPE", OracleType.HTTPURITYPE),
    XDBURITYPE("XDBURITYPE", OracleType.XDBURITYPE),
    DBURITYPE("DBURITYPE", OracleType.DBURITYPE),
    //
    SDO_GEOMETRY("SDO_GEOMETRY", OracleType.SDO_GEOMETRY),
    SDO_TOPO_GEOMETRY("SDO_TOPO_GEOMETRY", OracleType.SDO_TOPO_GEOMETRY),
    SDO_GEORASTER("SDO_GEORASTER", OracleType.SDO_GEORASTER),
    //
    ORDAUDIO("ORDAUDIO", OracleType.ORDAUDIO),
    ORDDICOM("ORDDICOM", OracleType.ORDDICOM),
    ORDDOC("ORDDOC", OracleType.ORDDOC),
    ORDIMAGE("ORDIMAGE", OracleType.ORDIMAGE),
    ORDVIDEO("ORDVIDEO", OracleType.ORDVIDEO),
    SI_AVERAGE_COLOR("SI_AVERAGECOLOR", OracleType.SI_AVERAGE_COLOR),
    SI_COLOR("SI_COLOR", OracleType.SI_COLOR),
    SI_COLOR_HISTOGRAM("SI_COLORHISTOGRAM", OracleType.SI_COLOR_HISTOGRAM),
    SI_FEATURE_LIST("SI_FEATURELIST", OracleType.SI_FEATURE_LIST),
    SI_POSITIONAL_COLOR("SI_POSITIONALCOLOR", OracleType.SI_POSITIONAL_COLOR),
    SI_STILL_IMAGE("SI_STILLIMAGE", OracleType.SI_STILL_IMAGE),
    SI_TEXTURE("SI_TEXTURE", OracleType.SI_TEXTURE);

    private final String   codeKey;
    private final JDBCType jdbcType;
    private final Integer  jdbcTypeNum;

    OracleSqlTypes(String codeKey, OracleType oracleType){
        this.codeKey = codeKey;
        this.jdbcType = oracleType.toJdbcType();
        this.jdbcTypeNum = oracleType.getVendorTypeNumber();
    }

    public static OracleSqlTypes valueOfCode(String code) {
        code = code.toUpperCase();
        if (AliasNames.containsKey(code)) {
            code = AliasNames.get(code);
        }

        for (OracleSqlTypes tableType : OracleSqlTypes.values()) {
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
    public DsType getDsType() { return DsType.Oracle; }

    @Override
    public boolean isReadOnly() {
        switch (this) {
            case ROWID:
            case UROWID:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case FLOAT:
            case BINARY_FLOAT:
            case BINARY_DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isArray() { return this == VARRAY; }

    @Override
    public boolean isStruct() {
        switch (this) {
            case OBJECT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isNumber() {
        switch (this) {
            case NUMBER_BIGINT:
            case NUMBER_DECIMAL:
            case PLSQL_BOOLEAN:
            case FLOAT:
            case BINARY_FLOAT:
            case BINARY_DOUBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case BLOB:
            case BFILE:
            case RAW:
            case LONG_RAW:
            case ORDAUDIO:
            case ORDDICOM:
            case ORDDOC:
            case ORDIMAGE:
            case ORDVIDEO:
            case SI_COLOR:
            case SI_AVERAGE_COLOR:
            case SI_COLOR_HISTOGRAM:
            case SI_FEATURE_LIST:
            case SI_POSITIONAL_COLOR:
            case SI_STILL_IMAGE:
            case SI_TEXTURE:
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
            case VARCHAR2:
            case NVARCHAR:
            case NVARCHAR2:
            case LONG:
            case CLOB:
            case NCLOB:
            case ROWID:
            case UROWID:
            case REF:
            case XMLTYPE:
            case HTTPURITYPE:
            case XDBURITYPE:
            case DBURITYPE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isDataOrTime() {
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
    public boolean isGeometry() {
        switch (this) {
            case SDO_GEOMETRY:
            case SDO_TOPO_GEOMETRY:
            case SDO_GEORASTER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBoolean() {
        switch (this) {
            case PLSQL_BOOLEAN:
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
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isAccurateDecimal() { return this == NUMBER_DECIMAL; }

    @Override
    public Integer getJdbcType() { return this.jdbcTypeNum; }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }

    public static OracleSqlTypes toOracleType(String dataType, Integer numPrecision, Integer numScale) {
        dataType = dataType.toUpperCase();
        if (AliasNames.containsKey(dataType)) {
            dataType = AliasNames.get(dataType);
        }

        if (dataType.startsWith("NUMBER")) {
            if (numPrecision != null && numPrecision > 0 && (numScale == null || numScale == 0)) {
                return OracleSqlTypes.NUMBER_BIGINT;
            } else if (numPrecision == null && numScale != null && numScale == 0) {
                return OracleSqlTypes.NUMBER_BIGINT;
            } else {
                return OracleSqlTypes.NUMBER_DECIMAL;
            }
        } else if (dataType.startsWith("TIMESTAMP")) {
            if (dataType.contains("ZONE")) {
                if (dataType.contains("LOCAL")) {
                    return TIMESTAMP_WITH_LOCAL_TIME_ZONE;
                }
                return TIMESTAMP_WITH_TIME_ZONE;
            }
            return TIMESTAMP;
        } else if (dataType.startsWith("INTERVAL")) {
            if (dataType.contains("DAY")) {
                return INTERVAL_DAY_TO_SECOND;
            }
            if (dataType.contains("YEAR")) {
                return INTERVAL_YEAR_TO_MONTH;
            }
        } else if (dataType.startsWith("BINARY_FLOAT")) {
            return BINARY_FLOAT;
        } else if (dataType.startsWith("BINARY_DOUBLE")) {
            return BINARY_DOUBLE;
        }
        //
        for (OracleSqlTypes type : OracleSqlTypes.values()) {
            if (type.getCodeKey().equalsIgnoreCase(dataType)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("Unsupported oracle columnType " + dataType);
    }

    public static final Map<String, String> AliasNames = new HashMap<>();

    static {
        AliasNames.put("INT", NUMBER_BIGINT.getCodeKey());
        AliasNames.put("INTEGER", NUMBER_BIGINT.getCodeKey());
        AliasNames.put("VARCHAR", VARCHAR2.getCodeKey());
        AliasNames.put("BINARY_FLOAT", BINARY_FLOAT.getCodeKey());
        AliasNames.put("BINARY_DOUBLE", BINARY_DOUBLE.getCodeKey());
        AliasNames.put("INTERVAL YEAR", INTERVAL_YEAR_TO_MONTH.getCodeKey());
        AliasNames.put("INTERVAL DAY", INTERVAL_DAY_TO_SECOND.getCodeKey());
        AliasNames.put("CHARACTER", CHAR.getCodeKey());
        AliasNames.put("NATIONAL CHARACTER", NCHAR.getCodeKey());
        AliasNames.put("NATIONAL CHAR", NCHAR.getCodeKey());
        AliasNames.put("CHARACTER VARYING", VARCHAR2.getCodeKey());
        AliasNames.put("CHAR VARYING", VARCHAR2.getCodeKey());
        AliasNames.put("NATIONAL CHARACTER VARYING", NVARCHAR2.getCodeKey());
        AliasNames.put("NATIONAL CHAR VARYING", NVARCHAR2.getCodeKey());
        AliasNames.put("NCHAR VARYING", NVARCHAR2.getCodeKey());
        AliasNames.put("NUMERIC", NUMBER_DECIMAL.getCodeKey());
        AliasNames.put("DECIMAL", NUMBER_DECIMAL.getCodeKey());
        AliasNames.put("SMALLINT", NUMBER_DECIMAL.getCodeKey());
        AliasNames.put("DOUBLE PRECISION", FLOAT.getCodeKey());
        AliasNames.put("REAL", FLOAT.getCodeKey());
    }
}
