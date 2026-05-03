package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.bedrock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum BedrockEmbeddingModelName {

    TITAN_EMBED_TEXT_V1("amazon.titan-embed-text-v1", 1536),
    TITAN_EMBED_TEXT_V2("amazon.titan-embed-text-v2:0", 256);

    private final String  stringValue;
    private final Integer dimension;

    BedrockEmbeddingModelName(String stringValue, Integer dimension){
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

    private static final Map<String, Integer> KNOWN_DIMENSION = new HashMap<>(BedrockEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        for (BedrockEmbeddingModelName embeddingModelName : BedrockEmbeddingModelName.values()) {
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
