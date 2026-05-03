package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.bedrock.BedrockChatModelName;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.bedrock.BedrockEmbeddingModelName;

/**
 * @author chunlin create time is 2025/5/9
 */
@Service
public class BedrockExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return BedrockEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return BedrockChatModelName.defaultConfig();
    }
}
