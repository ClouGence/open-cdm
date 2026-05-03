package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.deepseek;

import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

import lombok.Getter;

@Getter
public enum DeepSeekChatModelName {

    DEEP_SEEK_CHAT("deepseek-chat"),
    DEEP_SEEK_REASONER("deepseek-reasoner");

    private final String stringValue;

    DeepSeekChatModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(DeepSeekChatModelName.values().length);
    private static final String      DEFAULT_CONFIG;

    static {
        for (DeepSeekChatModelName chatModelName : DeepSeekChatModelName.values()) {
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
