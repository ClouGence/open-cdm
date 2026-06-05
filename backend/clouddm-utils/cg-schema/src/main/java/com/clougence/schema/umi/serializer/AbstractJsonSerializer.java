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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public abstract class AbstractJsonSerializer<T> extends JsonSerializer<T> {

    private Serializer<T> serializer = null;

    public AbstractJsonSerializer(Serializer<T> serializer){
        this.serializer = serializer;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String apply = this.serializer.serialize(value);
        if (apply.startsWith("{") && apply.endsWith("}")) {
            gen.writeStartObject();
            gen.writeRaw(apply.substring(1, apply.length() - 1));
            gen.writeEndObject();
        } else if (apply.startsWith("[") && apply.endsWith("]")) {
            gen.writeStartArray();
            gen.writeRaw(apply.substring(1, apply.length() - 1));
            gen.writeEndArray();
        }
    }
}
