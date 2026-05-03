package com.clougence.clouddm.base.metadata.rdp.enumeration.llm;

public enum LLMProvider {

    HuggingFace,

    Cohere,

    OpenAI,

    /**
     * aliyun model api service.
     */
    DashScope,

    DeepSeek,

    LocalAI,

    ZhipuAI,

    Ollama,

    Anthropic,

    Bedrock;

    public static LLMProvider valueOfCode(String code) {
        for (LLMProvider llmProvider : LLMProvider.values()) {
            if (llmProvider.name().equalsIgnoreCase(code)) {
                return llmProvider;
            }
        }

        throw new IllegalArgumentException("Unsupported LLM:" + code);
    }

}
