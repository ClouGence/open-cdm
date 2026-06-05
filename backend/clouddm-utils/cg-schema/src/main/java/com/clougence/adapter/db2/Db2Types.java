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
package com.clougence.adapter.db2;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;

/**
 * https://www.ibm.com/docs/zh/db2/9.7?topic=djr-sql-data-type-representation
 */
public enum Db2Types implements FieldType {

    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    INTEGER("INTEGER", JDBCType.INTEGER),
    BIGINT("BIGINT", JDBCType.BIGINT),

    REAL("REAL", JDBCType.REAL),
    FLOAT("FLOAT", JDBCType.FLOAT),
    DECFLOAT("DECFLOAT", JDBCType.FLOAT),
    DOUBLE("DOUBLE", JDBCType.DOUBLE),
    DECIMAL("DECIMAL", JDBCType.DECIMAL),

    CHARACTER("CHAR", JDBCType.CHAR),
    NCHARACTER("NCHAR", JDBCType.NCHAR),
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    DATALINK("DATALINK", JDBCType.DATALINK),
    NVARCHAR("NVARCHAR", JDBCType.NVARCHAR),
    LONG_VARCHAR("LONG VARCHAR", JDBCType.LONGVARCHAR),
    GRAPHIC("GRAPHIC", JDBCType.NCHAR),
    VARGRAPHIC("VARGRAPHIC", JDBCType.NVARCHAR),
    LONG_VARGRAPHIC("LONG VARGRAPHIC", JDBCType.LONGVARCHAR),
    CLOB("CLOB", JDBCType.CLOB),
    NCLOB("NCLOB", JDBCType.NCLOB),
    DBCLOB("DBCLOB", JDBCType.CLOB),
    XML("XML", JDBCType.SQLXML),
    DISTINCT("DISTINCT", JDBCType.DISTINCT),
    ROWID("ROWID", JDBCType.ROWID),

    // Binary
    BINARY("BINARY", JDBCType.BINARY),
    VARBINARY("VARBINARY", JDBCType.VARBINARY),
    CHAR_FOR_BIT_DATA("CHAR FOR BIT DATA", JDBCType.BINARY),
    VARCHAR_FOR_BIT_DATA("VARCHAR FOR BIT DATA", JDBCType.VARBINARY),
    LONG_VARCHAR_FOR_BIT_DATA("LONG VARCHAR FOR BIT DATA", JDBCType.LONGVARBINARY),
    BLOB("BLOB", JDBCType.BLOB),

    // date
    DATE("DATE", JDBCType.DATE),
    TIME("TIME", JDBCType.TIME),
    TIMESTAMP("TIMESTAMP", JDBCType.TIMESTAMP),

    BOOLEAN("BOOLEAN", JDBCType.BOOLEAN),;

    private final String   codeKey;
    private final JDBCType jdbcType;

    Db2Types(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static String fixTypeName(String typeName) {
        if (StringUtils.isEmpty(typeName)) {
            return null;
        }
        if (typeName.contains("()")) {
            return Arrays.stream(typeName.split(" ")).filter(e -> !e.equals("()")).collect(Collectors.joining(" "));
        }
        return typeName;
    }

    public static Db2Types valueOfCode(String code) {
        String fixedCode = fixTypeName(code);
        if (StringUtils.isNotBlank(fixedCode)) {
            fixedCode = fixedCode.toLowerCase();
            if (AliasNames.containsKey(fixedCode)) {
                fixedCode = AliasNames.get(fixedCode);
            }
            for (Db2Types tableType : Db2Types.values()) {
                if (tableType.codeKey.equalsIgnoreCase(fixedCode) || tableType.name().equalsIgnoreCase(fixedCode)) {
                    return tableType;
                }
            }
        }
        throw new UnsupportedOperationException("Unsupported db2 columnType " + code);
    }

    public static Db2Types valueOfJdbcCode(Integer jdbcCode) {
        for (Db2Types tableType : Db2Types.values()) {
            if (Objects.equals(tableType.getJdbcType(), jdbcCode)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported db2 jdbcCode " + jdbcCode);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Db2; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() {
        switch (this) {
            case REAL:
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
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case REAL:
            case DOUBLE:
            case DECIMAL:
            case TIMESTAMP:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBinary() {
        switch (this) {
            case CHAR_FOR_BIT_DATA:
            case VARCHAR_FOR_BIT_DATA:
            case LONG_VARCHAR_FOR_BIT_DATA:
            case BLOB:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isString() {
        switch (this) {
            case CHARACTER:
            case VARCHAR:
            case LONG_VARCHAR:
            case GRAPHIC:
            case VARGRAPHIC:
            case LONG_VARGRAPHIC:
            case CLOB:
            case DBCLOB:
            case XML:
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
        return this == Db2Types.CHARACTER;
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case TIMESTAMP:
            case TIME:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        return TIME == this;
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

    public static final Map<String, String> AliasNames = new HashMap<>();

    static {
        // IBM for i
        AliasNames.put("char", "character");
        AliasNames.put("character varying", "varchar");
        AliasNames.put("national character varying", "nvarchar");
        AliasNames.put("numeric", "decimal");
        AliasNames.put("national character large object", "nclob");
        AliasNames.put("character large object", "clob");
        AliasNames.put("binary large object", "blob");
        AliasNames.put("national character", "nchar");
        AliasNames.put("double precision", "double");
        AliasNames.put("timestmp", "timestamp");
        AliasNames.put("varg", "nvarchar");
        AliasNames.put("binary varying", "varbinary");
        AliasNames.put("varbin", "varbinary");
    }
}
