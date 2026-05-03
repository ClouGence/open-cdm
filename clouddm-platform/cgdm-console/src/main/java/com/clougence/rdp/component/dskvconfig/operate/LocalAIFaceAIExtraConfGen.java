package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.localai.LocalAIEmbeddingModelName;
import com.clougence.rdp.component.dskvconfig.model.LLMExtraConfig;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class LocalAIFaceAIExtraConfGen extends LLMExtraConfGen {

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
        return LocalAIEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return null;
    }
}
