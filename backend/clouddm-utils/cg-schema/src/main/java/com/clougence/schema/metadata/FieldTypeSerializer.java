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

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaFramework;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public final class FieldTypeSerializer {

    public static class JacksonDeserializer extends JsonDeserializer<FieldType> {

        @Override
        public FieldType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return readFieldType(p.getText());
        }
    }

    public static class JacksonSerializer extends JsonSerializer<FieldType> {

        @Override
        public void serialize(FieldType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getStoreType());
        }
    }

    private final static Map<String, DsType> cacheDsType = new ConcurrentHashMap<>();

    private static FieldType readFieldType(String sqlType) {
        if (StringUtils.isBlank(sqlType)) {
            return null;
        }

        String[] sqlTypeSplit = sqlType.split(",");
        DsType dsType = cacheDsType.computeIfAbsent(sqlTypeSplit[0], DsType::valueOfCode);
        return Objects.requireNonNull(SchemaFramework.getFieldType(dsType, Integer.parseInt(sqlTypeSplit[1])), "passed FieldType failed.");
    }
}
