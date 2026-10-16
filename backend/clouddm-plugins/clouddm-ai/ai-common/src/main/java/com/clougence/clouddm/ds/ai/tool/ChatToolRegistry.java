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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import com.clougence.utils.JsonUtils;

import jakarta.validation.Validator;

/** Model-visible specifications and executable tools are registered together per conversation run. */
public final class ChatToolRegistry {
    private final Validator                          validator;
    private final Map<String, ChatToolSpecification> specifications = new LinkedHashMap<>();
    private final Map<String, ChatToolExecutor>      executors      = new LinkedHashMap<>();

    public ChatToolRegistry(Validator validator){
        this.validator = validator;
    }

    public static List<Map<String, Object>> definitionsFrom(Class<?> type) {
        return Arrays.stream(type.getMethods())
            .filter(method -> method.isAnnotationPresent(ChatTool.class))
            .sorted(Comparator.comparing(Method::getName))
            .map(ChatToolSchemas::specification)
            .map(ChatToolSpecification::definition)
            .toList();
    }

    public void register(Object target) {
        List<Method> methods = Arrays.stream(target.getClass().getMethods())
            .filter(method -> method.isAnnotationPresent(ChatTool.class))
            .sorted(Comparator.comparing(Method::getName))
            .toList();
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
                throw new IllegalArgumentException("Tool class must be public.");
            }
            ChatToolSpecification specification = ChatToolSchemas.specification(method);
            if (specifications.putIfAbsent(specification.getName(), specification) != null) {
                throw new IllegalArgumentException("Duplicate tool name: " + specification.getName());
            }
            executors.put(specification.getName(), new ChatToolExecutor(target, method, validator));
        }
    }

    public ChatToolExecutor executor(String name) {
        return executors.get(name);
    }

    public List<Map<String, Object>> definitions() {
        return specifications.values().stream().map(ChatToolSpecification::definition).toList();
    }

    public String resultText(Object result) {
        if (result == null) {
            return ChatToolConstants.SUCCESS;
        }
        if (result instanceof String text) {
            return text;
        }
        return JsonUtils.toJson(result);
    }
}
