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
package com.clougence.clouddm.ds.ai.dashscope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.ds.ai.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class DashScopeStreamParser implements ChatStreamDecoder {
    private final boolean                       showReasoning;
    private final StringBuilder                 text      = new StringBuilder();
    private final StringBuilder                 reasoning = new StringBuilder();
    private final ChatTextAccumulator           content   = new ChatTextAccumulator(text, reasoning);
    private final List<DashScopeToolCallBuffer> calls     = new ArrayList<>();
    private DashScopeToolCallBuffer             currentCall;
    private String                              finishReason;
    private ChatUsage                           usage;

    public ChatResponse parse(InputStream input, ChatStreamControl control, ChatStreamListener deltas) throws IOException {
        if (!showReasoning) {
            deltas = new ChatStreamListener(deltas.getText(), value -> {
            }, deltas.getInterrupted());
        }
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(input, StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT)));
        StringBuilder data = new StringBuilder();
        String event = null;
        String line;
        while ((line = reader.readLine()) != null) {
            control.checkActive();
            if (line.isEmpty()) {
                if (!data.isEmpty()) {
                    String frame = data.substring(0, data.length() - 1);
                    if (!"done".equals(event) && !"[DONE]".equals(frame)) {
                        consume(frame, "error".equals(event), deltas);
                    }
                    data.setLength(0);
                }
                event = null;
            } else if (line.startsWith("event:")) {
                event = line.substring(6).strip();
            } else if (line.startsWith("data:")) {
                data.append(line.substring(5).stripLeading()).append('\n');
            }
        }
        control.checkActive();
        if (!data.isEmpty()) {
            String frame = data.substring(0, data.length() - 1);
            if (!"done".equals(event) && !"[DONE]".equals(frame)) {
                consume(frame, "error".equals(event), deltas);
            }
        }
        content.finish(deltas);
        ChatFinishReason terminal = ChatFinishReason.STOP;
        if (finishReason != null) {
            terminal = ChatFinishReason.fromValue(finishReason);
        }
        List<ChatToolCall> tools = new ArrayList<>();
        if (terminal != ChatFinishReason.LENGTH && terminal != ChatFinishReason.CONTENT_FILTER) {
            Set<String> ids = new HashSet<>();
            for (DashScopeToolCallBuffer call : calls) {
                if (call.getId() == null || call.getId().isBlank() || call.getName().toString().isBlank() || !ids.add(call.getId())) {
                    throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
                }
                tools.add(new ChatToolCall(call.getId(), call.getName().toString(), call.getArguments().toString()));
            }
            if (!tools.isEmpty()) {
                terminal = ChatFinishReason.TOOL_CALLS;
            }
        }
        log.info("DashScope stream parsed: finishReason={}, textCharacters={}, toolCalls={}, usage={}", terminal, text.length(), tools.size(), usage);
        return new ChatResponse(text.toString(), reasoning.toString(), tools, usage, terminal);
    }

    @Override
    public ChatStreamSnapshot snapshot() {
        List<ChatPartialToolCall> tools = calls.stream()
            .map(call -> new ChatPartialToolCall(call.getIndex(), call.getId(), call.getName().toString(), call.getArguments().toString()))
            .toList();
        return new ChatStreamSnapshot(content.textSnapshot(), content.reasoningSnapshot(), tools, usage);
    }

    @Override
    public ChatResponse contentFiltered() {
        return new ChatResponse("", "", List.of(), usage, ChatFinishReason.CONTENT_FILTER);
    }

    private void consume(String data, boolean errorEvent, ChatStreamListener deltas) throws IOException {
        JsonNode result = JsonUtils.defaultObjectMapper().readTree(data);
        if (result == null || !result.isObject()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        String code = result.path("code").asText();
        if (errorEvent || !code.isEmpty()) {
            if (ChatProtocolConstants.ERROR_DATA_INSPECTION_FAILED.equals(code) || ChatProtocolConstants.ERROR_CONTENT_FILTER.equals(code)) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_CONTENT_FILTERED);
            }
            if (code.contains("ApiKey") || code.contains("AccessDenied")) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_PROVIDER_AUTH);
            }
            if (code.contains("Throttling")) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_PROVIDER_RATE_LIMIT);
            }
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        JsonNode tokens = result.get("usage");
        if (tokens != null && tokens.has("input_tokens") && tokens.has("output_tokens")) {
            Long cached = null;
            Long thinking = null;
            JsonNode inputDetails = tokens.path(ChatProtocolConstants.FIELD_INPUT_TOKEN_DETAILS);
            if (!inputDetails.hasNonNull(ChatProtocolConstants.FIELD_CACHED_TOKENS)) {
                inputDetails = tokens.path(ChatProtocolConstants.FIELD_PROMPT_TOKEN_DETAILS);
            }
            if (inputDetails.hasNonNull(ChatProtocolConstants.FIELD_CACHED_TOKENS)) {
                cached = inputDetails.path(ChatProtocolConstants.FIELD_CACHED_TOKENS).longValue();
            }
            if (tokens.path(ChatProtocolConstants.FIELD_OUTPUT_TOKEN_DETAILS).hasNonNull(ChatProtocolConstants.FIELD_REASONING_TOKENS)) {
                thinking = tokens.path(ChatProtocolConstants.FIELD_OUTPUT_TOKEN_DETAILS).path(ChatProtocolConstants.FIELD_REASONING_TOKENS).longValue();
            }
            usage = new ChatUsage(tokens.path("input_tokens").asLong(), tokens.path("output_tokens").asLong(), cached, thinking);
        }
        JsonNode output = result.path("output");
        String topFinish = output.path("finish_reason").asText();
        if (!topFinish.isBlank() && !"null".equals(topFinish)) {
            finishReason = topFinish;
        }
        String outputText = output.path("text").asText("");
        if (!outputText.isBlank()) {
            content.append(outputText, deltas);
            return;
        }
        JsonNode choices = output.path("choices");
        if (choices.isMissingNode() || choices.isEmpty()) {
            return;
        }
        if (!choices.isArray()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        JsonNode choice = choices.get(0);
        if (choice.isNull()) {
            return;
        }
        String reason = choice.path("finish_reason").asText();
        if (!reason.isBlank() && !"null".equals(reason)) {
            finishReason = reason;
        }
        JsonNode message = choice.path("message");
        JsonNode thought = message.get(ChatProtocolConstants.FIELD_REASONING_CONTENT);
        if (thought != null && !thought.isNull()) {
            if (!thought.isTextual()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            reasoning.append(thought.textValue());
            deltas.getReasoning().accept(thought.textValue());
        }
        JsonNode textNode = message.get("content");
        if (textNode != null && !textNode.isNull()) {
            if (textNode.isTextual()) {
                content.append(textNode.textValue(), deltas);
            } else if (textNode.isArray()) {
                for (JsonNode part : textNode) {
                    if (part.path("text").isTextual()) {
                        content.append(part.path("text").textValue(), deltas);
                    }
                }
            } else {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
        }
        JsonNode tools = message.get("tool_calls");
        if (tools != null && !tools.isNull()) {
            if (!tools.isArray()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            for (JsonNode tool : tools) {
                appendTool(tool);
            }
        }
    }

    private void appendTool(JsonNode tool) {
        Integer index = null;
        if (tool.hasNonNull("index")) {
            JsonNode indexNode = tool.path("index");
            if (indexNode.isIntegralNumber() && indexNode.canConvertToInt()) {
                index = indexNode.intValue();
            } else if (indexNode.isTextual()) {
                try {
                    index = Integer.valueOf(indexNode.textValue());
                } catch (NumberFormatException ignored) {
                    // Native SDK treats an unparseable index as an unindexed fragment.
                }
            }
        }
        String id = tool.path("id").asText("");
        DashScopeToolCallBuffer call = resolveCall(index, id);
        if (call == null) {
            return;
        }
        if (index != null) {
            call.setIndex(index);
        }
        if (!id.isBlank()) {
            call.setId(id);
        }
        JsonNode function = tool.path("function");
        if (function.hasNonNull("name")) {
            if (!function.path("name").isTextual()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            call.getName().append(function.path("name").textValue());
        }
        if (function.hasNonNull("arguments")) {
            if (!function.path("arguments").isTextual()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            call.getArguments().append(function.path("arguments").textValue());
        }
    }

    private DashScopeToolCallBuffer resolveCall(Integer index, String id) {
        if (index != null) {
            for (DashScopeToolCallBuffer call : calls) {
                if (index.equals(call.getIndex())) {
                    currentCall = call;
                    return call;
                }
            }
        }
        if (!id.isBlank()) {
            for (DashScopeToolCallBuffer call : calls) {
                if (id.equals(call.getId())) {
                    currentCall = call;
                    return call;
                }
            }
        }
        if (index != null || !id.isBlank()) {
            currentCall = new DashScopeToolCallBuffer();
            calls.add(currentCall);
        }
        return currentCall;
    }
}
