/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.rdp.dal.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 读写枚举类型，支持枚举实现 {@link EnumOfCode}、{@link EnumOfValue} 接口。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-11-29
 */
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E>       enumType;
    private final EnumOfCode<E>  ofCode;
    private final EnumOfValue<E> ofValue;

    public EnumTypeHandler(Class<E> enumType){
        if (enumType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enumType = enumType;
        Enum<E>[] enums = ((Class<Enum<E>>) enumType).getEnumConstants();
        if (enums.length > 0 && EnumOfCode.class.isAssignableFrom(enumType)) {
            this.ofCode = (EnumOfCode<E>) enums[0];
        } else {
            this.ofCode = null;
        }
        if (enums.length > 0 && EnumOfValue.class.isAssignableFrom(enumType)) {
            this.ofValue = (EnumOfValue<E>) enums[0];
        } else {
            this.ofValue = null;
        }
        if (this.ofCode != null && this.ofValue != null) {
            throw new IllegalArgumentException(enumType.getName() + " Type EnumOfCode and EnumOfValue cannot exist at the same time.");
        }
    }

    public Class<E> getEnumType() { return this.enumType; }

    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (parameter instanceof EnumOfCode && this.ofCode != null) {
            ps.setString(i, ((EnumOfCode<?>) parameter).codeName());
            return;
        }
        if (parameter instanceof EnumOfValue && this.ofValue != null) {
            ps.setInt(i, ((EnumOfValue<?>) parameter).codeValue());
            return;
        }
        //
        if (jdbcType == null) {
            ps.setString(i, parameter.name());
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (this.ofCode != null) {
            String s = rs.getString(columnName);
            return s == null ? null : valueByOfCode(s);
        }
        if (this.ofValue != null) {
            int s = rs.getInt(columnName);
            return rs.wasNull() ? null : valueByOfValue(s);
        }
        //
        String s = rs.getString(columnName);
        return s == null ? null : valueOf(s);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (this.ofCode != null) {
            String s = rs.getString(columnIndex);
            return s == null ? null : valueByOfCode(s);
        }
        if (this.ofValue != null) {
            int s = rs.getInt(columnIndex);
            return rs.wasNull() ? null : valueByOfValue(s);
        }
        //
        String s = rs.getString(columnIndex);
        return s == null ? null : valueOf(s);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (this.ofCode != null) {
            String s = cs.getString(columnIndex);
            return s == null ? null : valueByOfCode(s);
        }
        if (this.ofValue != null) {
            int s = cs.getInt(columnIndex);
            return cs.wasNull() ? null : valueByOfValue(s);
        }
        //
        String s = cs.getString(columnIndex);
        return s == null ? null : valueOf(s);
    }

    protected E valueByOfValue(int s) {
        return this.ofValue.valueOfCode(s);
    }

    protected E valueByOfCode(String s) {
        return this.ofCode.valueOfCode(s);
    }

    protected E valueOf(String enumDat) {
        if (this.ofCode != null) {
            return this.ofCode.valueOfCode(enumDat);
        } else {
            return Enum.valueOf(enumType, enumDat);
        }
    }
}
