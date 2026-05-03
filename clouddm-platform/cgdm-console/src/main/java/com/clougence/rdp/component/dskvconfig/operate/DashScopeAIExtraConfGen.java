package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.dashscope.DashScopeChatModelName;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.dashscope.DashScopeEmbeddingModelName;

/**
 * @author bucketli 2022/8/10 10:00:22
 */
@Service
public class DashScopeAIExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return DashScopeEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return DashScopeChatModelName.defaultConfig();
    }
}
