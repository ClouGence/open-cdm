package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.dashscope;

import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

import lombok.Getter;

@Getter
public enum DashScopeChatModelName {

    QWEN_PLUS("qwen-plus"),
    QWEN_MAX("qwen-max"),
    QWEN_TURBO("qwen-turbo"),

    ;

    private final String stringValue;

    DashScopeChatModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(DashScopeChatModelName.values().length);
    private static final String      DEFAULT_CONFIG;

    static {
        for (DashScopeChatModelName chatModelName : DashScopeChatModelName.values()) {
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
