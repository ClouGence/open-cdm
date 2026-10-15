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
package com.clougence.clouddm.ds.ai.tool;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

final class ChatToolSchemas {
    private ChatToolSchemas(){
    }

    static ChatToolSpecification specification(Method method) {
        ChatTool tool = method.getAnnotation(ChatTool.class);
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            ChatToolParameter contract = parameter.getAnnotation(ChatToolParameter.class);
            if (contract == null) {
                throw new IllegalArgumentException("Tool parameter requires @ChatToolParameter: " + method.getName());
            }
            Map<String, Object> schema = schema(parameter.getAnnotatedType());
            schema.put(ChatToolConstants.DESCRIPTION, contract.description());
            if (properties.putIfAbsent(contract.name(), schema) != null) {
                throw new IllegalArgumentException("Duplicate tool parameter: " + contract.name());
            }
            if (contract.required()) {
                required.add(contract.name());
            }
        }
        return new ChatToolSpecification(tool.name(),
            tool.description(),
            Map.of(ChatToolConstants.TYPE, ChatToolConstants.OBJECT, ChatToolConstants.PROPERTIES, properties, ChatToolConstants.REQUIRED, required));
    }

    private static Map<String, Object> schema(AnnotatedType annotated) {
        Type type = annotated.getType();
        Class<?> raw;
        if (type instanceof ParameterizedType parameterized) {
            raw = (Class<?>) parameterized.getRawType();
        } else {
            raw = (Class<?>) type;
        }
        Map<String, Object> schema = new LinkedHashMap<>();
        String kind;
        if (raw == String.class || raw.isEnum()) {
            kind = ChatToolConstants.STRING;
            if (raw.isEnum()) {
                schema.put(ChatToolConstants.ENUM, Arrays.stream(raw.getEnumConstants()).map(value -> ((Enum<?>) value).name()).toList());
            }
        } else if (Collection.class.isAssignableFrom(raw) || raw.isArray()) {
            kind = ChatToolConstants.ARRAY;
            AnnotatedType item;
            if (annotated instanceof AnnotatedParameterizedType parameterized) {
                item = parameterized.getAnnotatedActualTypeArguments()[0];
            } else if (annotated instanceof AnnotatedArrayType array) {
                item = array.getAnnotatedGenericComponentType();
            } else {
                throw new IllegalArgumentException("Tool collection must declare its item type.");
            }
            schema.put(ChatToolConstants.ITEMS, schema(item));
        } else if (raw == Boolean.class || raw == boolean.class) {
            kind = ChatToolConstants.BOOLEAN;
        } else if (raw == Integer.class || raw == int.class || raw == Long.class || raw == long.class) {
            kind = ChatToolConstants.INTEGER;
        } else if (Number.class.isAssignableFrom(raw) || raw == double.class || raw == float.class) {
            kind = ChatToolConstants.NUMBER;
        } else if (raw.isRecord()) {
            kind = ChatToolConstants.OBJECT;
            Map<String, Object> properties = new LinkedHashMap<>();
            List<String> required = new ArrayList<>();
            for (RecordComponent component : raw.getRecordComponents()) {
                ChatToolParameter contract = component.getAnnotation(ChatToolParameter.class);
                if (contract == null) {
                    throw new IllegalArgumentException("Tool record component requires @ChatToolParameter: " + component.getName());
                }
                Map<String, Object> property = schema(component.getAnnotatedType());
                property.put(ChatToolConstants.DESCRIPTION, contract.description());
                properties.put(contract.name(), property);
                if (contract.required()) {
                    required.add(contract.name());
                }
            }
            schema.put(ChatToolConstants.PROPERTIES, properties);
            schema.put(ChatToolConstants.REQUIRED, required);
        } else {
            throw new IllegalArgumentException("Register an explicit tool specification for type: " + raw.getName());
        }
        schema.put(ChatToolConstants.TYPE, kind);
        Size size = annotated.getAnnotation(Size.class);
        if (size != null) {
            String minimum = ChatToolConstants.MIN_LENGTH;
            String maximum = ChatToolConstants.MAX_LENGTH;
            if (kind.equals(ChatToolConstants.ARRAY)) {
                minimum = ChatToolConstants.MIN_ITEMS;
                maximum = ChatToolConstants.MAX_ITEMS;
            }
            schema.put(minimum, size.min());
            if (size.max() < Integer.MAX_VALUE) {
                schema.put(maximum, size.max());
            }
        }
        if (annotated.isAnnotationPresent(NotEmpty.class) || annotated.isAnnotationPresent(NotBlank.class)) {
            String minimum = ChatToolConstants.MIN_LENGTH;
            if (kind.equals(ChatToolConstants.ARRAY)) {
                minimum = ChatToolConstants.MIN_ITEMS;
            }
            schema.put(minimum, Math.max(1, ((Number) schema.getOrDefault(minimum, 0)).intValue()));
        }
        Pattern pattern = annotated.getAnnotation(Pattern.class);
        if (pattern != null) {
            schema.put(ChatToolConstants.PATTERN, pattern.regexp());
        }
        return schema;
    }
}
