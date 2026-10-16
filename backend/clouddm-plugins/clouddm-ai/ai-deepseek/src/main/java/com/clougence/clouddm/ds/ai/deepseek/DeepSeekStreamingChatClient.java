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
package com.clougence.clouddm.ds.ai.deepseek;

import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.ai.ChatProtocolConstants;
import com.clougence.clouddm.ds.ai.ChatRequest;
import com.clougence.clouddm.ds.ai.openai.OpenAiStreamingChatClient;

public class DeepSeekStreamingChatClient extends OpenAiStreamingChatClient {
    @Override
    protected ChatRequest customizeRequest(ChatRequest request) {
        Map<String, Object> parameters = new LinkedHashMap<>(request.getParameters());
        String thinking = "disabled";
        if (request.isShowReasoning()) {
            thinking = "enabled";
        }
        parameters.put(ChatProtocolConstants.PARAMETER_THINKING, Map.of("type", thinking));
        return new ChatRequest(request
            .getModel(), request.getMessages(), request.getTools(), request.getToolChoice(), request.getMaxOutputTokens(), request.isShowReasoning(), parameters);
    }

    @Override
    protected boolean replayReasoningContent() {
        return true;
    }
}
