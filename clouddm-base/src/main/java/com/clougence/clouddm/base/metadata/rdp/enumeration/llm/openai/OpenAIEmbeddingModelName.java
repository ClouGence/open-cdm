package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum OpenAIEmbeddingModelName {

    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small", 1536),
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large", 3072),
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002", 1536);

    private final String  stringValue;
    private final Integer dimension;

    OpenAIEmbeddingModelName(String stringValue, Integer dimension){
        this.stringValue = stringValue;
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public Integer dimension() {
        return dimension;
    }

    private static final Map<String, Integer> KNOWN_DIMENSION = new HashMap<>(OpenAIEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        for (OpenAIEmbeddingModelName embeddingModelName : OpenAIEmbeddingModelName.values()) {
            KNOWN_DIMENSION.put(embeddingModelName.toString(), embeddingModelName.dimension());
        }
        DEFAULT_CONFIG = LLMConfigHelper.genDefaultEmbeddingConfig(KNOWN_DIMENSION);
    }

    public static Integer knownDimension(String modelName) {
        return KNOWN_DIMENSION.get(modelName);
    }

    public static Set<String> allModelNames() {
        return KNOWN_DIMENSION.keySet();
    }

    public static String defaultConfig() {
        return DEFAULT_CONFIG;
    }

}
