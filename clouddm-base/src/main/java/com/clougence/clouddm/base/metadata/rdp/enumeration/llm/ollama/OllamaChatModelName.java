package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.ollama;

import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum OllamaChatModelName {

    LLAMA_3_2("llama3.2");

    private final String stringValue;

    OllamaChatModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(OllamaChatModelName.values().length);
    private static final String      DEFAULT_CONFIG;

    static {
        for (OllamaChatModelName chatModelName : OllamaChatModelName.values()) {
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
