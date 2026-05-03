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
