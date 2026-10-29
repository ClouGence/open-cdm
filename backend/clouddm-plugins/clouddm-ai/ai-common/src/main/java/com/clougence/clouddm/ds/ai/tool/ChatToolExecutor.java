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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public final class ChatToolExecutor {
    private final Object    target;
    private final Method    method;
    private final Validator validator;

    public ChatToolExecutor(Object target, Method method, Validator validator){
        this.target = target;
        this.method = method;
        this.validator = validator;
    }

    public Object execute(JsonNode arguments) {
        if (!arguments.isObject()) {
            throw new IllegalArgumentException("Tool arguments must be an object.");
        }
        Parameter[] parameters = method.getParameters();
        Object[] values = new Object[parameters.length];
        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            ChatToolParameter contract = parameter.getAnnotation(ChatToolParameter.class);
            JsonNode value = arguments.get(contract.name());
            if (contract.required() && (value == null || value.isNull())) {
                throw new IllegalArgumentException("Missing required parameter: " + contract.name());
            }
            if (value != null && !value.isNull()) {
                values[index] = JsonUtils.defaultObjectMapper().convertValue(value, JsonUtils.toJavaType(parameter));
            }
        }
        Set<ConstraintViolation<Object>> violations = validator.forExecutables().validateParameters(target, method, values);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(
                violations.stream().map(violation -> violation.getPropertyPath() + ": " + violation.getMessage()).sorted().findFirst().orElseThrow());
        }
        try {
            return method.invoke(target, values);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtime) {
                throw runtime;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException("Tool invocation failed.", cause);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Tool method must be public.", e);
        }
    }
}
