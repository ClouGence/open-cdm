package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.cohere.CohereEmbeddingModelName;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class CohereFaceAIExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return CohereEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return null;
    }
}
