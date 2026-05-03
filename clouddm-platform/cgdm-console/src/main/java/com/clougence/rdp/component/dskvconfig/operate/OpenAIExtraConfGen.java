package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai.OpenAIChatModelName;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai.OpenAIEmbeddingModelName;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class OpenAIExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return OpenAIEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return OpenAIChatModelName.defaultConfig();
    }
}
