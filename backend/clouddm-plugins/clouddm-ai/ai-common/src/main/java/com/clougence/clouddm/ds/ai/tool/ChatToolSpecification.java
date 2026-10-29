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

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class ChatToolSpecification {
    private final String              name;

    private final String              description;

    private final Map<String, Object> parameters;

    @JsonCreator
    public ChatToolSpecification(@JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("parameters") Map<String, Object> parameters){
        parameters = Map.copyOf(parameters);
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public Map<String, Object> definition() {
        Map<String, Object> function = new LinkedHashMap<>();
        function.put(ChatToolConstants.NAME, name);
        function.put(ChatToolConstants.DESCRIPTION, description);
        function.put(ChatToolConstants.PARAMETERS, parameters);
        return Map.of(ChatToolConstants.TYPE, ChatToolConstants.FUNCTION, ChatToolConstants.FUNCTION, Map.copyOf(function));
    }
}
