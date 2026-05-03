package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.zhipuai;

import java.util.*;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMChatConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/4/10 14:44
 */
@Getter
@Slf4j
public enum ZhipuAIChatModelName {

    // Chat Model
    GLM_4_PLUS("glm-4-plus", 0.95, 0.7),
    GLM_4_AIRX("glm-4-airx", 0.95, 0.7),
    GLM_4_LONG("glm-4-long", 0.95, 0.7),
    GLM_4_FLASHX("glm-4-flashx", 0.95, 0.7),

    // Reasoner Model
    GLM_Z1_AIR("glm-z1-air", 0.95, 0.7),
    GLM_Z1_AIRX("glm-z1-airx", 0.95, 0.7),
    GLM_Z1_FLASH("glm-z1-flash", 0.95, 0.7),

    // Agent model
    GLM_4_ALLTOOLS("glm-4-alltools", 0.95, 0.7),
    GLM_4_ASSISTANT("glm-4-assistant", 0.95, 0.7),;

    private final String modelName;
    private final double defaultTemperature;
    private final double defaultTopP;

    ZhipuAIChatModelName(String modelName, double defaultTemperature, double defaultTopP){
        this.modelName = modelName;
        this.defaultTemperature = defaultTemperature;
        this.defaultTopP = defaultTopP;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(ZhipuAIChatModelName.values().length);
    private static String            DEFAULT_CONFIG;

    static {
        Set<ZhipuAIChatModelName> defaultModels = Collections.unmodifiableSet( //
                new HashSet<>(Arrays.asList(GLM_4_PLUS, GLM_4_AIRX, GLM_Z1_AIR, GLM_Z1_FLASH, GLM_4_ALLTOOLS)));
        try {
            Map<String, LLMChatConfig> cs = new HashMap<>();
            for (ZhipuAIChatModelName model : defaultModels) {
                KNOWN_MODELS.add(model.toString());
                LLMChatConfig llmChatConfig = new LLMChatConfig();
                llmChatConfig.setTemperature(model.getDefaultTemperature());
                llmChatConfig.setTopP(model.getDefaultTopP());
                cs.put(model.getModelName(), llmChatConfig);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            DEFAULT_CONFIG = objectMapper.writeValueAsString(cs);
        } catch (Exception e) {
            log.error("gen default embedding config failed,but ignore it.msg:", e);
            DEFAULT_CONFIG = null;
        }
    }

    public static Set<String> allModelNames() {
        return KNOWN_MODELS;
    }

    public static String defaultConfig() {
        return DEFAULT_CONFIG;
    }
}
