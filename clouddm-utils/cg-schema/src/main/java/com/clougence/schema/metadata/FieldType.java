package com.clougence.schema.metadata;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 数据实际类型。
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@JsonSerialize(using = FieldTypeSerializer.JacksonSerializer.class)
@JsonDeserialize(using = FieldTypeSerializer.JacksonDeserializer.class)
public interface FieldType {

    String name();

    String getCodeKey();

    DsType getDsType();

    default String getStoreType() { return this.getDsType().getShortName() + "," + this.getCodeNum(); }

    boolean isReadOnly();

    boolean hasApproximate();

    boolean isArray();

    boolean isStruct();

    boolean isNumber();

    boolean isBinary();

    boolean isString();

    boolean isDataOrTime();

    boolean isGeometry();

    boolean isBoolean();

    boolean hasFixedChar();

    boolean hasDate();

    boolean hasTime();

    boolean hasZone();

    default boolean isLob() {
        JDBCType jdbcType = toJDBCType();
        switch (jdbcType) {
            case BLOB:
            case CLOB:
            case NCLOB:
                return true;
        }
        return false;
    }

    /**
     *  isNumber() = isInteger() || isDecimal()
     *  isInteger include bigint/int/...
     *  isDecimal() = isAccurateDecimal() || hasApproximate()
     *  isAccurateDecimal include decimal/numeric
     *  hasApproximate include float/double
     *  todo isInteger,isDecimal
     * @return
     */
    boolean isAccurateDecimal();

    default String templateOfSelectCol(Object[] args, String colName) {
        return colName;
    }

    default String templateOfInsert(Object[] args) {
        return "?";
    }

    default String templateOfSetCol(Object[] args, String colName) {
        return colName;
    }

    default String templateOfSetValue(Object[] args) {
        return "?";
    }

    default String templateOfWhereCol(Object[] args, String colName) {
        return colName;
    }

    default String templateOfWhereValue(Object[] args) {
        return "?";
    }

    int getCodeNum();

    Integer getJdbcType();

    JDBCType toJDBCType();

}
