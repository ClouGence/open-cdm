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
package com.clougence.clouddm.ds.ai.openai;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.clougence.clouddm.ds.ai.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
class OpenAiStreamParser implements ChatStreamDecoder {
    private final Predicate<JsonNode>        contentFiltered;
    private final Supplier<ChatFinishReason> missingFinishReason;

    private final boolean                    showReasoning;
    private final ChatResponseState          state = new ChatResponseState();

    public ChatResponse parse(InputStream input, ChatStreamControl handle, ChatStreamListener listener) throws IOException {
        ChatStreamListener deltaSink = listener;
        if (!showReasoning) {
            deltaSink = new ChatStreamListener(listener.getText(), value -> {
            }, listener.getInterrupted());
        }
        BufferedInputStream buffered = new BufferedInputStream(input);
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        StringBuilder data = new StringBuilder();
        boolean skipLf = false;
        boolean errorEvent = false;
        int next;
        long started = System.nanoTime();
        try {
            while ((next = buffered.read()) != -1) {
                handle.checkActive();
                if (skipLf) {
                    skipLf = false;
                    if (next == '\n') {
                        continue;
                    }
                }
                if (next != '\n' && next != '\r') {
                    line.write(next);
                    continue;
                }
                skipLf = next == '\r';
                String text = decode(line.toByteArray());
                line.reset();
                if (text.isEmpty()) {
                    if (!data.isEmpty()) {
                        String frame = data.substring(0, data.length() - 1);
                        data.setLength(0);
                        if (errorEvent) {
                            throw ChatHttpErrorDecoder.decode(200, frame);
                        }
                        if ("[DONE]".equals(frame)) {
                            continue;
                        }
                        consume(state, frame, deltaSink);
                    }
                    errorEvent = false;
                } else if (text.startsWith("event:")) {
                    errorEvent = "error".equals(text.substring(6).strip());
                } else if (text.startsWith("data:")) {
                    String value = text.substring(5);
                    if (value.startsWith(" ")) {
                        value = value.substring(1);
                    }
                    data.append(value).append('\n');
                }
            }
            handle.checkActive();
            if (line.size() > 0) {
                String lastLine = decode(line.toByteArray());
                if (lastLine.startsWith("data:")) {
                    data.append(lastLine.substring(5).stripLeading()).append('\n');
                }
            }
            if (!data.isEmpty()) {
                if (errorEvent) {
                    throw ChatHttpErrorDecoder.decode(200, data.toString());
                }
                String frame = data.substring(0, data.length() - 1);
                if (!"[DONE]".equals(frame)) {
                    consume(state, frame, deltaSink);
                }
            }
            state.getContent().finish(deltaSink);
            ChatResponse response = finish(state);
            log.info("AI stream closed: finishReason={}, textCharacters={}, toolCalls={}, usage={}, elapsedMs={}", response.getFinishReason(), state.getText()
                .length(), response.getTools().size(), state.getUsage(), (System.nanoTime() - started) / 1_000_000);
            return response;
        } catch (ThirdPartyApiException | IOException e) {
            log.warn("AI stream parsing failed: finishReason={}, textCharacters={}, toolCalls={}, usage={}, pendingLineBytes={}, pendingFrameCharacters={}, elapsedMs={}, errorType={}", state
                .getFinishReason(), state.getText()
                    .length(), state.getTools().size(), state.getUsage(), line.size(), data.length(), (System.nanoTime() - started) / 1_000_000, e.getClass().getSimpleName(), e);
            throw e;
        }
    }

    @Override
    public ChatStreamSnapshot snapshot() {
        List<ChatPartialToolCall> tools = state.getTools()
            .entrySet()
            .stream()
            .map(entry -> new ChatPartialToolCall(entry.getKey(),
                entry.getValue().getId().toString(),
                entry.getValue().getName().toString(),
                entry.getValue().getArguments().toString()))
            .toList();
        return new ChatStreamSnapshot(state.getContent().textSnapshot(), state.getContent().reasoningSnapshot(), tools, state.getUsage());
    }

    private String decode(byte[] bytes) {
        try {
            return StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes))
                .toString();
        } catch (CharacterCodingException e) {
            throw ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
    }

    private void consume(ChatResponseState state, String frame, ChatStreamListener deltaSink) throws IOException {
        JsonNode root = JsonUtils.defaultObjectMapper().readTree(frame);
        if (root != null && root.has("error")) {
            throw ChatHttpErrorDecoder.decode(200, frame);
        }
        if (root == null || !root.isObject()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        if (contentFiltered.test(root)) {
            state.setContentFiltered(true);
        }
        JsonNode usage = root.get("usage");
        if (usage != null && !usage.isNull()) {
            JsonNode input = usage.get("prompt_tokens");
            JsonNode output = usage.get("completion_tokens");
            if (input == null || output == null || !input.canConvertToLong() || !output.canConvertToLong() || !input.isIntegralNumber() || !output.isIntegralNumber()
                || input.longValue() < 0 || output.longValue() < 0) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            Long cached = null;
            Long thinking = null;
            if (usage.path(ChatProtocolConstants.FIELD_PROMPT_TOKEN_DETAILS).hasNonNull(ChatProtocolConstants.FIELD_CACHED_TOKENS)) {
                cached = usage.path(ChatProtocolConstants.FIELD_PROMPT_TOKEN_DETAILS).path(ChatProtocolConstants.FIELD_CACHED_TOKENS).longValue();
            }
            if (usage.path(ChatProtocolConstants.FIELD_COMPLETION_TOKEN_DETAILS).hasNonNull(ChatProtocolConstants.FIELD_REASONING_TOKENS)) {
                thinking = usage.path(ChatProtocolConstants.FIELD_COMPLETION_TOKEN_DETAILS).path(ChatProtocolConstants.FIELD_REASONING_TOKENS).longValue();
            }
            state.setUsage(new ChatUsage(input.longValue(), output.longValue(), cached, thinking));
        }
        JsonNode choices = root.get("choices");
        if (choices == null || choices.isNull()) {
            return;
        }
        if (!choices.isArray()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        if (choices.isEmpty() || choices.get(0).isNull()) {
            return;
        }
        JsonNode choice = choices.get(0);
        JsonNode reason = choice.get("finish_reason");
        if (reason != null && !reason.isNull()) {
            if (!reason.isTextual()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            state.setFinishReason(reason.textValue());
        }
        JsonNode delta = choice.get("delta");
        if (delta == null || delta.isNull()) {
            delta = choice.get("message");
        }
        if (delta == null || delta.isNull()) {
            return;
        }
        if (!delta.isObject()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        JsonNode reasoning = delta.get(ChatProtocolConstants.FIELD_REASONING_CONTENT);
        if (reasoning != null && !reasoning.isNull()) {
            if (!reasoning.isTextual()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            state.getReasoningContent().append(reasoning.textValue());
            deltaSink.getReasoning().accept(reasoning.textValue());
        }
        JsonNode text = delta.get("content");
        if (text != null && !text.isNull()) {
            if (!text.isTextual()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            if (!text.textValue().isEmpty()) {
                state.getContent().append(text.textValue(), deltaSink);
            }
        }
        JsonNode calls = delta.get("tool_calls");
        if (calls != null && !calls.isNull()) {
            if (!calls.isArray()) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            for (JsonNode call : calls) {
                appendTool(state, call);
            }
        }
    }

    private void appendTool(ChatResponseState state, JsonNode call) {
        JsonNode id = call.get("id");
        JsonNode function = call.get("function");
        if (function != null && !function.isNull() && !function.isObject()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        String idChunk = stringChunk(id);
        String nameChunk = null;
        String argsChunk = null;
        if (function != null && !function.isNull()) {
            nameChunk = stringChunk(function.get("name"));
            argsChunk = stringChunk(function.get("arguments"));
        }
        if ((idChunk == null || idChunk.isBlank()) && (nameChunk == null || nameChunk.isBlank()) && argsChunk == null) {
            return;
        }
        JsonNode type = call.get("type");
        if (type != null && !type.isNull() && !"function".equals(type.asText())) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        JsonNode indexNode = call.get("index");
        int index = state.getFallbackToolCallIndex();
        if (indexNode != null && !indexNode.isNull()) {
            if (!indexNode.isIntegralNumber() || !indexNode.canConvertToInt() || indexNode.intValue() < 0) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            index = indexNode.intValue();
        }
        ChatToolCallBuffer buffer = state.getTools().computeIfAbsent(index, key -> new ChatToolCallBuffer());
        if ((indexNode == null || indexNode.isNull()) && idChunk != null && !buffer.getId().isEmpty() && !buffer.getId().toString().equals(idChunk)) {
            index = state.getFallbackToolCallIndex() + 1;
            state.setFallbackToolCallIndex(index);
            buffer = state.getTools().computeIfAbsent(index, key -> new ChatToolCallBuffer());
        }
        if (idChunk != null) {
            buffer.getId().append(idChunk);
        }
        if (nameChunk != null) {
            buffer.getName().append(nameChunk);
        }
        if (argsChunk != null) {
            buffer.getArguments().append(argsChunk);
        }
    }

    private String stringChunk(JsonNode value) {
        if (value == null || value.isNull()) {
            return null;
        }
        if (!value.isTextual()) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        return value.textValue();
    }

    private ChatResponse finish(ChatResponseState state) {
        ChatFinishReason reason;
        if (state.isContentFiltered()) {
            reason = ChatFinishReason.CONTENT_FILTER;
        } else if (state.getFinishReason() == null) {
            reason = missingFinishReason.get();
        } else {
            reason = ChatFinishReason.fromValue(state.getFinishReason());
            if (reason == ChatFinishReason.OTHER) {
                reason = missingFinishReason.get();
            }
        }
        // A truncated tool batch is never executable, including unfinished argument JSON.
        if (reason == ChatFinishReason.LENGTH || reason == ChatFinishReason.CONTENT_FILTER || state.getTools().isEmpty()) {
            return new ChatResponse(state.getText().toString(), state.getReasoningContent().toString(), List.of(), state.getUsage(), reason);
        }
        List<ChatToolCall> tools = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (ChatToolCallBuffer buffer : state.getTools().values()) {
            if (buffer.getId().toString().isBlank() || buffer.getName().toString().isBlank() || !ids.add(buffer.getId().toString())) {
                throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
            }
            tools.add(new ChatToolCall(buffer.getId().toString(), buffer.getName().toString(), buffer.getArguments().toString()));
        }
        return new ChatResponse(state.getText().toString(), state.getReasoningContent().toString(), tools, state.getUsage(), ChatFinishReason.TOOL_CALLS);
    }
}
