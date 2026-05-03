package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.hugginface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum HuggingFaceEmbeddingModelName {

    SENTENCE_TRANSFORMERS_ALL_MINI_LM_L6_V2("sentence-transformers/all-MiniLM-L6-v2", 384);

    private final String  stringValue;
    private final Integer dimension;

    HuggingFaceEmbeddingModelName(String stringValue, Integer dimension){
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

    private static final Map<String, Integer> KNOWN_DIMENSION = new HashMap<>(HuggingFaceEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        for (HuggingFaceEmbeddingModelName embeddingModelName : HuggingFaceEmbeddingModelName.values()) {
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
