package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.zhipuai.ZhipuAIChatModelName;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.zhipuai.ZhipuAIEmbeddingModelName;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/4/10 14:36
 */
@Service
public class ZhipuAIExtraConfGen extends LLMExtraConfGen {

    @Override
    protected String llmEmbeddingModelConfig() {
        return ZhipuAIEmbeddingModelName.defaultConfig();
    }

    @Override
    protected String llmChatModelConfig() {
        return ZhipuAIChatModelName.defaultConfig();
    }
}
