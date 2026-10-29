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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

/** Parse at tool execution time so argument errors can be returned to the model. */
public final class ChatToolArguments {
    private static final ObjectReader JSON = JsonUtils.defaultObjectMapper().reader().with(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature());

    private ChatToolArguments(){
    }

    public static JsonNode parse(String arguments) {
        if (arguments == null || arguments.isBlank()) {
            return JsonUtils.defaultObjectMapper().createObjectNode();
        }
        try {
            String normalized = arguments.strip();
            if (normalized.startsWith("\"") && normalized.endsWith("\"")) {
                JsonNode quoted = JSON.readTree(normalized);
                if (quoted.isTextual()) {
                    normalized = quoted.textValue();
                }
            }
            int firstBrace = normalized.indexOf('{');
            int lastBrace = normalized.lastIndexOf('}');
            if (firstBrace >= 0 && lastBrace > firstBrace) {
                normalized = normalized.substring(firstBrace, lastBrace + 1);
            }
            JsonNode value = JSON.readTree(normalized);
            if (value == null || !value.isObject()) {
                throw new IllegalArgumentException("Tool arguments must be a JSON object.");
            }
            return value;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Tool arguments contain invalid JSON.", e);
        }
    }

    public static String normalizeForReplay(String arguments) {
        if (arguments != null && !arguments.isBlank()) {
            try {
                JsonNode value = JSON.readTree(arguments);
                if (value != null && value.isObject()) {
                    return value.toString();
                }
            } catch (JsonProcessingException ignored) {
                // ReAkh replays invalid arguments as an empty object, alongside the tool error result.
            }
        }
        return "{}";
    }

    public static List<Map<String, Object>> replayToolCalls(List<Map<String, Object>> calls) {
        List<Map<String, Object>> normalized = new ArrayList<>();
        for (Map<String, Object> call : calls) {
            Map<String, Object> adapted = new LinkedHashMap<>(call);
            Map<String, Object> function = new LinkedHashMap<>((Map<String, Object>) call.get(ChatProtocolConstants.FIELD_FUNCTION));
            function.put(ChatProtocolConstants.FIELD_ARGUMENTS, normalizeForReplay((String) function.get(ChatProtocolConstants.FIELD_ARGUMENTS)));
            adapted.put(ChatProtocolConstants.FIELD_FUNCTION, function);
            normalized.add(adapted);
        }
        return normalized;
    }
}
