package com.clougence.clouddm.console.web.global.mcp.utils;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static java.time.format.DateTimeFormatter.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * A JSON codec implementation using Jackson for serialization and deserialization.
 * Provides methods to convert objects to their JSON representation and parse JSON strings into objects.
 * Customizes the behavior of the Jackson {@link ObjectMapper} to support serialization and deserialization
 * of Java 8 date/time types such as {@link LocalDate}, {@link LocalTime}, and {@link LocalDateTime}.
 * @param objectMapper
-- GETTER --
Returns the ObjectMapper instance used for JSON processing.
 */
public record JacksonJsonCodec(ObjectMapper objectMapper) implements Json.JsonCodec {

    private static ObjectMapper createObjectMapper() {

        SimpleModule module = new SimpleModule("llm-module");

        module.addSerializer(LocalDate.class, new StdSerializer<LocalDate>(LocalDate.class) {

            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(ISO_LOCAL_DATE));
            }
        });

        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {

            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = p.getCodec().readTree(p);
                if (node.isObject()) {
                    int year = node.get("year").asInt();
                    int month = node.get("month").asInt();
                    int day = node.get("day").asInt();
                    return LocalDate.of(year, month, day);
                } else {
                    return LocalDate.parse(node.asText(), ISO_LOCAL_DATE);
                }
            }
        });

        module.addSerializer(LocalTime.class, new StdSerializer<LocalTime>(LocalTime.class) {

            @Override
            public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(ISO_LOCAL_TIME));
            }
        });

        module.addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {

            @Override
            public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = p.getCodec().readTree(p);
                if (node.isObject()) {
                    int hour = node.get("hour").asInt();
                    int minute = node.get("minute").asInt();
                    int second = Optional.ofNullable(node.get("second")).map(JsonNode::asInt).orElse(0);
                    int nano = Optional.ofNullable(node.get("nano")).map(JsonNode::asInt).orElse(0);
                    return LocalTime.of(hour, minute, second, nano);
                } else {
                    return LocalTime.parse(node.asText(), ISO_LOCAL_TIME);
                }
            }
        });

        module.addSerializer(LocalDateTime.class, new StdSerializer<LocalDateTime>(LocalDateTime.class) {

            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(ISO_LOCAL_DATE_TIME));
            }
        });

        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = p.getCodec().readTree(p);
                if (node.isObject()) {
                    JsonNode date = node.get("date");
                    int year = date.get("year").asInt();
                    int month = date.get("month").asInt();
                    int day = date.get("day").asInt();
                    JsonNode time = node.get("time");
                    int hour = time.get("hour").asInt();
                    int minute = time.get("minute").asInt();
                    int second = Optional.ofNullable(time.get("second")).map(JsonNode::asInt).orElse(0);
                    int nano = Optional.ofNullable(time.get("nano")).map(JsonNode::asInt).orElse(0);
                    return LocalDateTime.of(year, month, day, hour, minute, second, nano);
                } else {
                    return LocalDateTime.parse(node.asText(), ISO_LOCAL_DATE_TIME);
                }
            }
        });

        // FAIL_ON_UNKNOWN_PROPERTIES is enabled by default
        // to prevent issues caused by LLM hallucinations
        return new ObjectMapper().setVisibility(FIELD, ANY).findAndRegisterModules().registerModule(module).enable(INDENT_OUTPUT);
    }

    /**
     * Constructs a JacksonJsonCodec instance with the provided ObjectMapper.
     *
     * @param objectMapper the ObjectMapper to use for JSON serialization and deserialization.
     */
    public JacksonJsonCodec{
    }

    /**
     * Constructs a JacksonJsonCodec instance with a default ObjectMapper.
     * The default ObjectMapper is configured with custom serializers and deserializers
     * for Java 8 date/time types such as LocalDate, LocalTime, and LocalDateTime.
     * It also registers other modules found on the classpath, enables formatted JSON output,
     * and throws exceptions for unknown properties to improve handling of unexpected input.
     */
    public JacksonJsonCodec(){
        this(createObjectMapper());
    }

    @Override
    public String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        try {
            return objectMapper.readValue(json, objectMapper.constructType(type));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
