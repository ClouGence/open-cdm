package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.deepseek.DeepSeekChatModelName;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class DeepSeekFaceAIExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return null;
    }

    @Override
    protected String llmChatModelConfig() {
        return DeepSeekChatModelName.defaultConfig();
    }
}
