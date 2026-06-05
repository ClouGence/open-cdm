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
package com.clougence.schema.umi.serializer;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public final class SerializerRoot extends Serializer<Object> {

    public static class JacksonDeserializer extends AbstractJsonDeserializer<Object> {

        public JacksonDeserializer(){
            super(new SerializerRoot());
        }
    }

    public static class JacksonSerializer extends AbstractJsonSerializer<Object> {

        public JacksonSerializer(){
            super(new SerializerRoot());
        }
    }

    @SneakyThrows
    public String serialize(Object object) {
        Serializer<Object> serializer = SerializerRegistry.lookSerializer(object.getClass().getSimpleName());
        return serializer.serialize(object);
    }

    @SneakyThrows
    public Object deserialize(String jsonData) {
        Map<String, Object> readValue = new ObjectMapper().readValue(jsonData, new TypeReference<Map<String, Object>>() {
        });

        String aClassName = (String) readValue.get(KEY_CLASS);
        Serializer<Object> deserialize = SerializerRegistry.lookSerializer(aClassName);
        return deserialize.deserialize(jsonData);
    }
}
