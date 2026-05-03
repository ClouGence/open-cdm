package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.ollama;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum OllamaEmbeddingModelName {

    LLAMA_3_2("llama3.2", 3072),
    ALL_MINILM("all-minilm", 384);

    private final String  stringValue;
    private final Integer dimension;

    OllamaEmbeddingModelName(String stringValue, Integer dimension){
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

    private static final Map<String, Integer> KNOWN_DIMENSION = new HashMap<>(OllamaEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        for (OllamaEmbeddingModelName embeddingModelName : OllamaEmbeddingModelName.values()) {
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
