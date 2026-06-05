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
package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.ollama.OllamaChatModelName;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.ollama.OllamaEmbeddingModelName;
import com.clougence.rdp.component.dskvconfig.model.LLMExtraConfig;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class OllamaExtraConfGen extends LLMExtraConfGen {

    @Override
    public LLMExtraConfig newDsExtraConfig() {
        LLMExtraConfig config = new LLMExtraConfig();
        config.setLlmEmbedding(llmEmbeddingModelConfig());
        config.setLlmChat(llmChatModelConfig());
        config.setHttpsEnabled(false);
        return config;
    }

    @Override
    protected String llmEmbeddingModelConfig() {
        return OllamaEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return OllamaChatModelName.defaultConfig();
    }
}
