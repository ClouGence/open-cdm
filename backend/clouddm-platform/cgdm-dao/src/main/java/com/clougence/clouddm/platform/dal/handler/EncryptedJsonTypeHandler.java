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
package com.clougence.clouddm.platform.dal.handler;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.ds.SecretField;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JavaType;

public class EncryptedJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final Class<T> type;
    private final JavaType javaType;

    public EncryptedJsonTypeHandler(Class<T> type){
        this.type = type;
        this.javaType = JsonUtils.defaultObjectMapper().getTypeFactory().constructType(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        T storage = JsonUtils.toObj(JsonUtils.toJson(parameter), this.javaType);
        process(storage, true, new IdentityHashMap<>());
        ps.setString(i, JsonUtils.toJson(storage));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return read(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return read(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return read(cs.getString(columnIndex));
    }

    private T read(String value) {
        T result = JsonUtils.toObj(value, this.javaType);
        process(result, false, new IdentityHashMap<>());
        return result;
    }

    private void process(Object value, boolean encrypt, IdentityHashMap<Object, Boolean> visited) {
        if (value == null || isSimpleValue(value.getClass()) || visited.containsKey(value)) {
            return;
        }
        visited.put(value, Boolean.TRUE);

        if (value instanceof Collection<?> collection) {
            collection.forEach(item -> process(item, encrypt, visited));
            return;
        }
        if (value instanceof Map<?, ?> map) {
            map.values().forEach(item -> process(item, encrypt, visited));
            return;
        }
        for (Field field : value.getClass().getDeclaredFields()) {
            processField(value, field, encrypt, visited);
        }
    }

    private void processField(Object owner, Field field, boolean encrypt, IdentityHashMap<Object, Boolean> visited) {
        try {
            field.setAccessible(true);
            Object value = field.get(owner);
            if (field.isAnnotationPresent(SecretField.class) && value instanceof String text && StringUtils.isNotBlank(text)) {
                field.set(owner, encrypt ? CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(text) : CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(text));
                return;
            }
            process(value, encrypt, visited);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to process encrypted json field: " + this.type.getName() + "." + field.getName(), e);
        }
    }

    private boolean isSimpleValue(Class<?> clazz) {
        return clazz.isPrimitive() ||                       //
               clazz.isEnum() ||                            //
               CharSequence.class.isAssignableFrom(clazz) ||//
               Number.class.isAssignableFrom(clazz) ||      //
               Boolean.class == clazz ||                    //
               Character.class == clazz;
    }
}
