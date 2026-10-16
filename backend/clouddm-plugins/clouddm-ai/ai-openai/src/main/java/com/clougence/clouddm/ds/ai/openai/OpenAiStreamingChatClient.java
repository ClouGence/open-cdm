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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.ai.*;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OpenAiStreamingChatClient implements StreamingChatClient {
    private final ChatHttpTransport transport = new ChatHttpTransport();

    protected ChatRequest customizeRequest(ChatRequest request) {
        return request;
    }

    protected boolean replayReasoningContent() {
        return false;
    }

    protected boolean isContentFiltered(JsonNode response) {
        return false;
    }

    protected ChatFinishReason finishReasonWhenMissing() {
        return null;
    }

    @Override
    public ChatResponse stream(ChatEndpoint endpoint, ChatRequest request, ChatStreamControl control, ChatStreamListener listener) throws IOException {
        ChatRequest input = customizeRequest(OpenAiModelCompatibilityHelper.normalizeParameters(request));
        List<Map<String, Object>> messages = input.getMessages().stream().map(message -> {
            if (!"assistant".equals(message.get("role"))) {
                return message;
            }
            Map<String, Object> adapted = new LinkedHashMap<>(message);
            if (adapted.containsKey(ChatProtocolConstants.FIELD_TOOL_CALLS)) {
                adapted.put(ChatProtocolConstants.FIELD_TOOL_CALLS, ChatToolArguments
                    .replayToolCalls((List<Map<String, Object>>) adapted.get(ChatProtocolConstants.FIELD_TOOL_CALLS)));
            }
            if (!adapted.containsKey("tool_calls") && adapted.get("content") == null) {
                adapted.put("content", "");
            }
            if (replayReasoningContent()) {
                // ReAkh also sends an empty reasoning field for assistant messages without thinking.
                adapted.putIfAbsent(ChatProtocolConstants.FIELD_REASONING_CONTENT, "");
            } else {
                adapted.remove(ChatProtocolConstants.FIELD_REASONING_CONTENT);
            }
            return adapted;
        }).toList();

        Map<String, Object> body = new LinkedHashMap<>(input.getParameters());
        body.put("model", input.getModel());
        body.put("messages", messages);
        body.put("stream", true);
        body.put("stream_options", Map.of("include_usage", true));
        if (input.getMaxOutputTokens() != null) {
            if (OpenAiModelCompatibilityHelper.usesMaxCompletionTokens(input.getModel())) {
                body.putIfAbsent(ChatProtocolConstants.PARAMETER_MAX_COMPLETION_TOKENS, input.getMaxOutputTokens());
            } else {
                body.put("max_tokens", input.getMaxOutputTokens());
            }
        }
        if (!input.getTools().isEmpty()) {
            body.put("tools", input.getTools());
        }
        body.put("tool_choice", input.getToolChoice().getValue());
        Request httpRequest = new Request.Builder().url(endpoint.getUrl())
            .header("Authorization", "Bearer " + endpoint.getApiKey())
            .header("Accept", "text/event-stream")
            .post(RequestBody.create(JsonUtils.toJson(body), MediaType.get("application/json; charset=utf-8")))
            .build();
        OpenAiStreamParser parser = new OpenAiStreamParser(this::isContentFiltered, this::finishReasonWhenMissing, input.isShowReasoning());
        return transport.stream(httpRequest, control, parser, listener);
    }

    @Override
    public void close() {
        transport.close();
    }
}
