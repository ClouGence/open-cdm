/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
