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
package com.clougence.clouddm.console.web.component.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Component
@Getter
public class AiProperties {
    @Value("${clouddm.ai.max-active:16}")
    private int    maxActive;
    @Value("${clouddm.ai.max-per-engine:4}")
    private int    maxPerEngine;
    @Value("${clouddm.ai.max-probes:2}")
    private int    maxProbes;
    @Value("${clouddm.ai.max-requests:141}")
    private int    maxRequests;
    @Value("${clouddm.ai.max-tool-calls:140}")
    private int    maxToolCalls;
    @Value("${clouddm.ai.turn-seconds:3600}")
    private int    turnSeconds;
    @Value("${clouddm.ai.metadata-seconds:10}")
    private int    metadataSeconds;

    @Value("${clouddm.ai.context-tokens:256000}")
    private int    contextTokens;

    @Value("${STREAMING_THINK_ONLY_RETRY_PROMPT:}")
    private String thinkOnlyRetryPrompt;
    @Value("${STREAMING_TOOL_LIMIT_FAILURE_MESSAGE_PROMPT:}")
    private String toolLimitFailureMessagePrompt;
    @Value("${STREAMING_TOOL_WARNING_PROMPT:}")
    private String toolWarningPrompt;
    @Value("${STREAMING_FINAL_ANSWER_DIRECTIVE_PROMPT:}")
    private String finalAnswerDirectivePrompt;
    @Value("${STREAMING_TOOL_LIMIT_REACHED_PROMPT:}")
    private String toolLimitReachedPrompt;

    @PostConstruct
    public void initialize() {
        validateLimit("max-active", maxActive);
        validateLimit("max-per-engine", maxPerEngine);
        validateLimit("max-probes", maxProbes);
        validateLimit("max-requests", maxRequests);
        validateLimit("max-tool-calls", maxToolCalls);
        validateLimit("turn-seconds", turnSeconds);
        validateLimit("metadata-seconds", metadataSeconds);
        if (contextTokens < 8192 || contextTokens > 1048576) {
            throw new IllegalArgumentException("Invalid AI limit: context-tokens");
        }
    }

    private static void validateLimit(String name, int value) {
        if (value < 1) {
            throw new IllegalArgumentException("Invalid AI limit: " + name);
        }
    }
}
