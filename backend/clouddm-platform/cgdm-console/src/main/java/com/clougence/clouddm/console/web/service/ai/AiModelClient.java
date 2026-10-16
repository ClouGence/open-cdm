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
package com.clougence.clouddm.console.web.service.ai;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.ds.ai.*;
import com.clougence.clouddm.ds.ai.dashscope.DashScopeStreamingChatClient;
import com.clougence.clouddm.ds.ai.deepseek.DeepSeekStreamingChatClient;
import com.clougence.clouddm.ds.ai.kimi.KimiStreamingChatClient;
import com.clougence.clouddm.ds.ai.minimax.MiniMaxStreamingChatClient;
import com.clougence.clouddm.ds.ai.openai.OpenAiStreamingChatClient;
import com.clougence.clouddm.platform.dal.model.ai.AiPreset;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

/** CloudDM adapter for model selection, credentials and provider calls. */
@Component
@Slf4j
public class AiModelClient {
    private final Map<AiPreset, StreamingChatClient> clients = new EnumMap<>(AiPreset.class);

    public AiModelClient(){
        clients.put(AiPreset.CUSTOM, new OpenAiStreamingChatClient());
        clients.put(AiPreset.DEEPSEEK, new DeepSeekStreamingChatClient());
        clients.put(AiPreset.KIMI, new KimiStreamingChatClient());
        clients.put(AiPreset.MINIMAX, new MiniMaxStreamingChatClient());
        clients.put(AiPreset.DASHSCOPE, new DashScopeStreamingChatClient());
    }

    public ChatResponse stream(AiEngineConfigVO engine, List<Map<String, Object>> messages, List<Map<String, Object>> tools, Integer maxOutputTokens, AiRunHandle handle,
                               Consumer<String> deltas) {
        handle.checkActive();
        HttpUrl url = HttpUrl.get(engine.getBaseUrl());
        if (engine.getPreset() != AiPreset.DASHSCOPE) {
            url = url.newBuilder().addPathSegments("chat/completions").build();
        }
        String key = engine.getAccessToken();
        ChatEndpoint endpoint = new ChatEndpoint(url, key);
        ChatToolChoice choice = ChatToolChoice.AUTO;
        if (tools.isEmpty()) {
            choice = ChatToolChoice.NONE;
        }
        ChatRequest request = new ChatRequest(engine.getModel(), messages, tools, choice, maxOutputTokens, false, Map.of());
        long started = System.nanoTime();
        log.info("AI model request started: engineId={}, preset={}, model={}, messageCount={}, toolCount={}, maxOutputTokens={}, remainingMs={}", engine.getId(), engine
            .getPreset(), engine.getModel(), messages.size(), tools.size(), maxOutputTokens, handle.remainingMillis());
        try {
            ChatResponse reply = clients.get(engine.getPreset()).stream(endpoint, request, handle.getStreamControl(), deltas);
            handle.checkActive();
            log.info("AI model request completed: engineId={}, model={}, finishReason={}, textCharacters={}, toolCalls={}, usage={}, elapsedMs={}", engine.getId(), engine
                .getModel(), reply.getFinishReason(), reply.getText().length(), reply.getTools().size(), reply.getUsage(), (System.nanoTime() - started) / 1_000_000);
            return reply;
        } catch (ThirdPartyApiException e) {
            handle.checkActive();
            log.error("AI model request failed: engineId={}, model={}, error={}, elapsedMs={}", engine.getId(), engine.getModel(), e
                .getMessageKey(), (System.nanoTime() - started) / 1_000_000, e);
            throw e;
        } catch (IOException e) {
            handle.checkActive();
            log.error("AI model transport failed: engineId={}, model={}, errorType={}, elapsedMs={}", engine.getId(), engine.getModel(), e.getClass()
                .getSimpleName(), (System.nanoTime() - started) / 1_000_000, e);
            throw ThirdPartyApiException.as().with(e, ChatProtocolConstants.ERROR_PROVIDER_UNAVAILABLE);
        } catch (CancellationException e) {
            handle.checkActive();
            throw e;
        }
    }

    @PreDestroy
    public void shutdown() {
        clients.values().forEach(StreamingChatClient::close);
    }
}
