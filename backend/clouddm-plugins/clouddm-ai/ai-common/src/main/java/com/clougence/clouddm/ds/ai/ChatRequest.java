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
package com.clougence.clouddm.ds.ai;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/** Provider-neutral chat input; extension parameters cannot override the streaming contract. */
@Getter
public final class ChatRequest {
    private final String                    model;

    private final List<Map<String, Object>> messages;

    private final List<Map<String, Object>> tools;

    private final ChatToolChoice            toolChoice;

    private final Integer                   maxOutputTokens;

    private final boolean                   showReasoning;

    private final Map<String, Object>       parameters;

    @JsonCreator
    public ChatRequest(@JsonProperty("model") String model, @JsonProperty("messages") List<Map<String, Object>> messages, @JsonProperty("tools") List<Map<String, Object>> tools,
                       @JsonProperty("toolChoice") ChatToolChoice toolChoice, @JsonProperty("maxOutputTokens") Integer maxOutputTokens,
                       @JsonProperty("showReasoning") boolean showReasoning, @JsonProperty("parameters") Map<String, Object> parameters){
        Objects.requireNonNull(toolChoice);
        if (model == null || model.isBlank() || messages.isEmpty() || (maxOutputTokens != null && maxOutputTokens <= 0)
            || (toolChoice == ChatToolChoice.REQUIRED && tools.isEmpty()) || parameters.keySet().stream().anyMatch(RESERVED::contains)) {
            throw new IllegalArgumentException("Invalid chat request");
        }
        messages = List.copyOf(messages);
        tools = List.copyOf(tools);
        parameters = Map.copyOf(parameters);
        this.model = model;
        this.messages = messages;
        this.tools = tools;
        this.toolChoice = toolChoice;
        this.maxOutputTokens = maxOutputTokens;
        this.showReasoning = showReasoning;
        this.parameters = parameters;
    }

    private static final Set<String> RESERVED = Set.of("model", "messages", "tools", "tool_choice", "stream", "stream_options", "max_tokens", "n");
}
