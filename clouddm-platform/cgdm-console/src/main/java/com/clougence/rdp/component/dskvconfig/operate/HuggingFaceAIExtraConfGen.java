package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.hugginface.HuggingFaceEmbeddingModelName;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class HuggingFaceAIExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return HuggingFaceEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return null;
    }
}
