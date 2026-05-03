package com.clougence.adapter.sqlserver;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

/**
 * https://docs.microsoft.com/zh-cn/sql/t-sql/data-types/data-types-transact-sql?view=sql-server-ver16
 * https://learn.microsoft.com/en-us/sql/t-sql/data-types/ntext-text-and-image-transact-sql?view=sql-server-ver16
 */
public enum SqlServerTypes implements FieldType {

    BIT("BIT", JDBCType.BOOLEAN),
    DECIMAL("DECIMAL", JDBCType.DECIMAL),
    NUMERIC("NUMERIC", JDBCType.NUMERIC),
    SMALLINT("SMALLINT", JDBCType.SMALLINT),
    TINYINT("TINYINT", JDBCType.TINYINT),
    INT("INT", JDBCType.INTEGER),
    BIGINT("BIGINT", JDBCType.BIGINT),

    SMALLMONEY("SMALLMONEY", JDBCType.DECIMAL),
    MONEY("MONEY", JDBCType.DECIMAL),
    FLOAT("FLOAT", JDBCType.FLOAT),
    REAL("REAL", JDBCType.REAL),

    DATE("DATE", JDBCType.DATE),
    DATETIMEOFFSET("DATETIMEOFFSET", JDBCType.OTHER),
    DATETIME2("DATETIME2", JDBCType.TIMESTAMP),
    SMALLDATETIME("SMALLDATETIME", JDBCType.OTHER),
    DATETIME("DATETIME", JDBCType.TIMESTAMP),
    TIME("TIME", JDBCType.TIME),

    CHAR("CHAR", JDBCType.CHAR),
    VARCHAR("VARCHAR", JDBCType.VARCHAR),
    @Deprecated // use varchar(max)
    TEXT("TEXT", JDBCType.LONGNVARCHAR),
    NCHAR("NCHAR", JDBCType.NCHAR),
    NVARCHAR("NVARCHAR", JDBCType.NVARCHAR),
    @Deprecated // use nvarchar(max)
    NTEXT("NTEXT", JDBCType.LONGNVARCHAR),

    BINARY("BINARY", JDBCType.BINARY),
    VARBINARY("VARBINARY", JDBCType.VARBINARY),
    @Deprecated // use varbinary(max)
    IMAGE("IMAGE", JDBCType.BLOB),

    @Deprecated // will be deleted in the future, please use ROWVERSION instead (sqlserver 2022)
    TIMESTAMP("TIMESTAMP", JDBCType.BINARY),
    ROWVERSION("ROWVERSION", JDBCType.BINARY),
    HIERARCHYID("HIERARCHYID", JDBCType.OTHER),
    UNIQUEIDENTIFIER("UNIQUEIDENTIFIER", JDBCType.VARCHAR),
    SQL_VARIANT("SQL_VARIANT", JDBCType.OTHER),
    XML("XML", JDBCType.SQLXML),
    GEOMETRY("GEOMETRY", JDBCType.OTHER),
    GEOGRAPHY("GEOGRAPHY", JDBCType.OTHER),
    SYSNAME("sysname", JDBCType.VARCHAR),;

    private final String   codeKey;
    private final JDBCType jdbcType;

    SqlServerTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static SqlServerTypes valueOfCode(String code) {
        for (SqlServerTypes tableType : SqlServerTypes.values()) {
            if (tableType.codeKey.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported sqlserver columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.SqlServer; }

    @Override
    public boolean isReadOnly() {
        switch (this) {
            case TIMESTAMP:
            case ROWVERSION:
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
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() {
        switch (this) {
            case BIT:
            case DECIMAL:
            case NUMERIC:
            case SMALLINT:
            case TINYINT:
            case INT:
            case BIGINT:
            case SMALLMONEY:
            case MONEY:
            case FLOAT:
            case REAL:
            case TIMESTAMP:
            case ROWVERSION:
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
            case HIERARCHYID:
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
            case TEXT:
            case NVARCHAR:
            case NTEXT:
            case UNIQUEIDENTIFIER:
            case SQL_VARIANT:
            case SYSNAME:
            case XML:
            case HIERARCHYID:
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
            case DATETIMEOFFSET:
            case DATETIME2:
            case SMALLDATETIME:
            case DATETIME:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isGeometry() {
        switch (this) {
            case GEOGRAPHY:
            case GEOMETRY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isBoolean() {
        switch (this) {
            case BIT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasFixedChar() {
        return this == SqlServerTypes.CHAR;
    }

    @Override
    public boolean hasDate() {
        switch (this) {
            case DATE:
            case DATETIMEOFFSET:
            case DATETIME2:
            case SMALLDATETIME:
            case DATETIME:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasTime() {
        switch (this) {
            case TIME:
            case DATETIMEOFFSET:
            case DATETIME2:
            case SMALLDATETIME:
            case DATETIME:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean hasZone() {
        switch (this) {
            case DATETIMEOFFSET:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isAccurateDecimal() { return this == DECIMAL || this == NUMERIC; }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() {
        return this.jdbcType;
    }
}
