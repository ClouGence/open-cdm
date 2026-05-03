package com.clougence.utils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.SneakyThrows;

/**
 * @author wanshao create time is 2021/1/29
 **/
public class JsonUtils {

    private static final ObjectMapper            objectMapper;
    private static final Map<Class<?>, JavaType> listTypeCache = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Map<Class<?>, JavaType> objTypeCache  = new java.util.concurrent.ConcurrentHashMap<>();

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // refer to:https://stackoverflow.com/questions/69811740/how-do-i-enable-the-jsr310-support-for-localdate-using-jackson
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper defaultObjectMapper() {
        return objectMapper;
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toObj(String jsonStr, Class<T> clz) {
        return objectMapper.readValue(jsonStr, clz);
    }

    @SneakyThrows
    public static <T> T toObj(String jsonStr, JavaType clz) {
        return objectMapper.readValue(jsonStr, clz);
    }

    @SneakyThrows
    public static <T> T toList(String jsonStr, TypeReference<T> clz) {
        return objectMapper.readValue(jsonStr, clz);
    }

    @SneakyThrows
    public static <T> List<T> toListUseType(String jsonStr, Class<T> clz) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }

        JavaType javaType;
        if (!listTypeCache.containsKey(clz)) {
            javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clz);
            listTypeCache.put(clz, javaType);
        } else {
            javaType = listTypeCache.get(clz);
        }

        return objectMapper.readValue(jsonStr, javaType);
    }

    @SneakyThrows
    public static <T> T toObjUseType(String jsonStr, Class<T> clz) {
        JavaType javaType;
        if (!objTypeCache.containsKey(clz)) {
            javaType = objectMapper.getTypeFactory().constructType(clz);
            objTypeCache.put(clz, javaType);
        } else {
            javaType = objTypeCache.get(clz);
        }

        return objectMapper.readValue(jsonStr, javaType);
    }

    @SneakyThrows
    public static List<Map<String, Object>> toMapList(String jsonStr) {
        return objectMapper.readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    @SneakyThrows
    public static Map<String, String> toMap(String jsonStr) {
        return objectMapper.readValue(jsonStr, new TypeReference<Map<String, String>>() {
        });
    }

    @SneakyThrows
    public static JavaType toJavaType(Parameter parameter) {
        return objectMapper.getTypeFactory().constructType(parameter.getParameterizedType());
    }

    @SneakyThrows
    public static JavaType toJavaType(Type parameter) {
        return objectMapper.getTypeFactory().constructType(parameter);
    }
}
