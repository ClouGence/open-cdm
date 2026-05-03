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
