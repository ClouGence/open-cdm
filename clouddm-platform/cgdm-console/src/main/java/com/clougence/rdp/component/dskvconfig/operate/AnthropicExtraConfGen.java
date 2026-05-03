package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.anthropic.AnthropicChatModelName;

/**
 * @author chunlin create time is 2025/5/9
 */
@Service
public class AnthropicExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return null;
    }

    @Override
    protected String llmChatModelConfig() {
        return AnthropicChatModelName.defaultConfig();
    }
}
