package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.anthropic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

/**
 * See more details <a href="https://docs.anthropic.com/claude/docs/models-overview">here</a>.
 */
public enum AnthropicChatModelName {

    CLAUDE_3_7_SONNET_20250219("claude-3-7-sonnet-20250219"),

    CLAUDE_3_5_SONNET_20241022("claude-3-5-sonnet-20241022"),
    CLAUDE_3_5_HAIKU_20241022("claude-3-5-haiku-20241022"),

    CLAUDE_3_5_SONNET_20240620("claude-3-5-sonnet-20240620"),

    CLAUDE_3_OPUS_20240229("claude-3-opus-20240229"),
    CLAUDE_3_SONNET_20240229("claude-3-sonnet-20240229"),
    CLAUDE_3_HAIKU_20240307("claude-3-haiku-20240307"),

    CLAUDE_2_1("claude-2.1"),
    CLAUDE_2("claude-2.0");

    private final String stringValue;

    AnthropicChatModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(AnthropicChatModelName.values().length);
    private static final String      DEFAULT_CONFIG;

    static {
        Set<AnthropicChatModelName> defaultModels = Collections.unmodifiableSet( //
                new HashSet<>(Collections.singletonList(CLAUDE_3_7_SONNET_20250219)));
        for (AnthropicChatModelName chatModelName : defaultModels) {
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
