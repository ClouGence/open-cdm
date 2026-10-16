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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.base.HalfDuplexParamBase;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.protocol.*;
import com.alibaba.dashscope.tools.ToolBase;
import com.alibaba.dashscope.tools.ToolFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.clougence.clouddm.ds.ai.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/** Uses DashScope's native SDK request format and native SSE envelope. */
public class DashScopeStreamingChatClient implements StreamingChatClient {
    private final ChatHttpTransport transport = new ChatHttpTransport();

    @Override
    public ChatResponse stream(ChatEndpoint endpoint, ChatRequest input, ChatStreamControl control, ChatStreamListener listener) throws IOException {
        control.checkActive();
        if (input.getToolChoice() == ChatToolChoice.REQUIRED) {
            throw ThirdPartyApiException.as().with(ChatProtocolConstants.ERROR_MODEL_PROTOCOL);
        }
        boolean multimodal = DashScopeConstants.isMultimodal(input.getModel());
        HalfDuplexRequest sdkRequest = sdkRequest(endpoint, input, multimodal);
        HttpRequest sdkHttpRequest;
        try {
            sdkHttpRequest = sdkRequest.getHttpRequest();
        } catch (NoApiKeyException e) {
            throw ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_PROVIDER_AUTH);
        }
        String path = DashScopeConstants.GENERATION_PATH;
        if (multimodal) {
            path = DashScopeConstants.MULTIMODAL_PATH;
        }
        Request httpRequest = new Request.Builder().url(endpoint.getUrl().newBuilder().addPathSegments(path).build())
            .headers(Headers.of(sdkHttpRequest.getHeaders()))
            .header("Accept", "text/event-stream")
            .post(RequestBody.create(sdkHttpRequest.getBody().toString(), MediaType.get("application/json; charset=utf-8")))
            .build();
        DashScopeStreamParser parser = new DashScopeStreamParser(input.isShowReasoning());
        return transport.stream(httpRequest, control, parser, listener);
    }

    private HalfDuplexRequest sdkRequest(ChatEndpoint endpoint, ChatRequest input, boolean multimodal) {
        List<ToolBase> tools = new ArrayList<>();
        for (Map<String, Object> tool : input.getTools()) {
            tools.add(JsonUtils.fromJson(JsonUtils.toJson(tool), ToolFunction.class));
        }
        List<Map<String, Object>> messages = new ArrayList<>();
        for (Map<String, Object> original : input.getMessages()) {
            Map<String, Object> message = new LinkedHashMap<>(original);
            if ("assistant".equals(message.get("role"))) {
                if (message.containsKey(ChatProtocolConstants.FIELD_TOOL_CALLS)) {
                    message.put(ChatProtocolConstants.FIELD_TOOL_CALLS, ChatToolArguments
                        .replayToolCalls((List<Map<String, Object>>) message.get(ChatProtocolConstants.FIELD_TOOL_CALLS)));
                }
                if (message.get("content") == null) {
                    message.put("content", "");
                }
                if (input.isShowReasoning() && message.containsKey("tool_calls")) {
                    message.putIfAbsent(ChatProtocolConstants.FIELD_REASONING_CONTENT, "");
                } else if (!input.isShowReasoning()) {
                    message.remove(ChatProtocolConstants.FIELD_REASONING_CONTENT);
                }
            }
            messages.add(message);
        }
        HalfDuplexParamBase param;
        String task = DashScopeConstants.GENERATION_TASK;
        if (multimodal) {
            List<Object> multimodalMessages = new ArrayList<>();
            for (Map<String, Object> original : messages) {
                Map<String, Object> message = new LinkedHashMap<>(original);
                message.put("content", List.of(Map.of("text", original.get("content"))));
                multimodalMessages.add(message);
            }
            var builder = MultiModalConversationParam.builder()
                .apiKey(endpoint.getApiKey())
                .model(input.getModel())
                .messages(multimodalMessages)
                .tools(tools)
                .toolChoice(input.getToolChoice().getValue())
                .maxTokens(input.getMaxOutputTokens())
                .enableThinking(input.isShowReasoning())
                .incrementalOutput(true);
            input.getParameters().forEach(builder::parameter);
            param = builder.build();
            task = DashScopeConstants.MULTIMODAL_TASK;
        } else {
            List<Message> generationMessages = new ArrayList<>();
            for (Map<String, Object> message : messages) {
                Message sdkMessage = JsonUtils.fromJson(JsonUtils.toJson(message), Message.class);
                // SDK deserialization does not map these wire names to its camel-case fields.
                sdkMessage.setToolCallId((String) message.get("tool_call_id"));
                sdkMessage.setReasoningContent((String) message.get(ChatProtocolConstants.FIELD_REASONING_CONTENT));
                generationMessages.add(sdkMessage);
            }
            var builder = GenerationParam.builder()
                .apiKey(endpoint.getApiKey())
                .model(input.getModel())
                .messages(generationMessages)
                .tools(tools)
                .toolChoice(input.getToolChoice().getValue())
                .maxTokens(input.getMaxOutputTokens())
                .enableThinking(input.isShowReasoning())
                .incrementalOutput(true)
                .resultFormat("message");
            input.getParameters().forEach(builder::parameter);
            param = builder.build();
        }
        ApiServiceOption options = ApiServiceOption.builder()
            .protocol(Protocol.HTTP)
            .httpMethod(HttpMethod.POST)
            .streamingMode(StreamingMode.OUT)
            .isSSE(true)
            .taskGroup("aigc")
            .task(task)
            .function("generation")
            .build();
        return new HalfDuplexRequest(param, options);
    }

    @Override
    public void close() {
        transport.close();
    }
}
