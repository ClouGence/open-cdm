package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.cohere;

import java.util.*;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum CohereEmbeddingModelName {

    EMBED_ENGLISH_V3_0("embed-english-v3.0", 1024),
    EMBED_MULTILINGUAL_V3_0("embed-multilingual-v3.0", 1024),
    EMBED_ENGLISH_LIGHT_V3_0("embed-english-light-v3.0", 384),
    EMBED_MULTILINGUAL_LIGHT_V3_0("embed-multilingual-light-v3.0", 384),
    EMBED_ENGLISH_V2_0("embed-english-v2.0", 4096),
    EMBED_ENGLISH_LIGHT_V2_0("embed-english-light-v2.0", 1024),
    EMBED_MULTILINGUAL_V2_0("embed-multilingual-v2.0", 768);

    private final String  stringValue;
    private final Integer dimension;

    CohereEmbeddingModelName(String stringValue, Integer dimension){
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

    private static final Map<String, Integer> KNOWN_DIMENSION = new HashMap<>(CohereEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        Set<CohereEmbeddingModelName> defaultModels = Collections.unmodifiableSet( //
                new HashSet<>(Arrays.asList(EMBED_ENGLISH_LIGHT_V3_0, EMBED_MULTILINGUAL_LIGHT_V3_0)));
        for (CohereEmbeddingModelName embeddingModelName : defaultModels) {
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
