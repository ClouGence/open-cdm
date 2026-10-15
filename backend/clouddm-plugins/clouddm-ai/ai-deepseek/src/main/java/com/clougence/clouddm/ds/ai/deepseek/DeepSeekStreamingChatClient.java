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
        Map<String, Object> parameters = new LinkedHashMap<>(request.parameters());
        String thinking = "disabled";
        if (request.showReasoning()) {
            thinking = "enabled";
        }
        parameters.put(ChatProtocolConstants.PARAMETER_THINKING, Map.of("type", thinking));
        return new ChatRequest(request.model(), request.messages(), request.tools(), request.toolChoice(), request.maxOutputTokens(), request.showReasoning(), parameters);
    }

    @Override
    protected boolean replayReasoningContent() {
        return true;
    }
}
