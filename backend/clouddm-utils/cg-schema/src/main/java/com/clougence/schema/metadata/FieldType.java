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
