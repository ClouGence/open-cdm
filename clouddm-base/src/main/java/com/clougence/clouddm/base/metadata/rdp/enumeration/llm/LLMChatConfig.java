package com.clougence.clouddm.base.metadata.rdp.enumeration.llm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LLMChatConfig {

    // Controls randomness (0.0 = deterministic, 1.0+ = highly creative)
    private Double  temperature;

    // Controls nucleus sampling (lower values make responses more focused)
    private Double  topP;

    // Limits the maximum length of generated responses
    private Integer maxTokens;

    // Encourages or discourages new topics (higher values = more novelty)
    private Double  presencePenalty;

    // Reduces repetition (higher values = fewer repeated words/phrases)
    private Double  frequencyPenalty;

    // Whether to display the reasoning chain (thought process) in the response
    private Boolean showReasoning;

    public LLMChatConfig(){
    }
}
