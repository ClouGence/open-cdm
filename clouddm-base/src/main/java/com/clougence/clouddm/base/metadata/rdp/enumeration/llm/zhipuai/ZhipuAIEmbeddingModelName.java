package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.zhipuai;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

import lombok.Getter;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/4/10 14:43
 */
@Getter
public enum ZhipuAIEmbeddingModelName {

    EMBEDDING3("embedding-3", 2048);

    private final String  type;
    private final Integer defaultDimension;

    ZhipuAIEmbeddingModelName(String type, Integer defaultDimension){
        this.type = type;
        this.defaultDimension = defaultDimension;
    }

    public static ZhipuAIEmbeddingModelName getByType(String type) {
        for (ZhipuAIEmbeddingModelName value : ZhipuAIEmbeddingModelName.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }

        throw new RuntimeException("can not recognize embedding encode：" + type);
    }

    private static final Map<String, Integer> KNOWN_DIMENSION = new HashMap<>(ZhipuAIEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        for (ZhipuAIEmbeddingModelName embeddingModelName : ZhipuAIEmbeddingModelName.values()) {
            KNOWN_DIMENSION.put(embeddingModelName.type, embeddingModelName.getDefaultDimension());
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
