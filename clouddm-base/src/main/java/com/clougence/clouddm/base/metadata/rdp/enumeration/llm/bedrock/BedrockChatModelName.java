package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.bedrock;

import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

/**
 * See more details <a href="https://docs.anthropic.com/claude/docs/models-overview">here</a>.
 */
public enum BedrockChatModelName {

    AWS_NOVA_MICRO_V1_0("us.amazon.nova-micro-v1:0"),

    AWS_NOVA_LITE_V1_0("us.amazon.nova-lite-v1:0"),

    AWS_NOVA_PRO_V1_0("us.amazon.nova-pro-v1:0"),;

    private final String stringValue;

    BedrockChatModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(BedrockChatModelName.values().length);
    private static final String      DEFAULT_CONFIG;

    static {
        for (BedrockChatModelName chatModelName : BedrockChatModelName.values()) {
            KNOWN_MODELS.add(chatModelName.toString());
        }
        DEFAULT_CONFIG = LLMConfigHelper.genDefaultChatConfig(KNOWN_MODELS);
    }

    public static Set<String> allModelNames() {
        return KNOWN_MODELS;
    }

    public static String defaultConfig() {
        return DEFAULT_CONFIG;
    }
}
