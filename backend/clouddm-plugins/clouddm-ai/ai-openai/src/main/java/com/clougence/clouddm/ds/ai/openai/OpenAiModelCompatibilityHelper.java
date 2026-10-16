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
package com.clougence.clouddm.ds.ai.openai;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.clougence.clouddm.ds.ai.ChatProtocolConstants;
import com.clougence.clouddm.ds.ai.ChatRequest;

final class OpenAiModelCompatibilityHelper {

    private static final double GPT_5_DEFAULT_TEMPERATURE = 1.0D;

    private static final String REASONING_NONE            = "none";

    private static final String REASONING_MINIMAL         = "minimal";

    private static final String REASONING_LOW             = "low";

    private static final String REASONING_MEDIUM          = "medium";

    private static final String REASONING_HIGH            = "high";

    private static final String REASONING_XHIGH           = "xhigh";

    private OpenAiModelCompatibilityHelper(){
    }

    private static Double normalizeTemperature(String modelName, Double temperature, String reasoningEffort) {
        String normalizedReasoningEffort = normalizeReasoningEffort(modelName, reasoningEffort);
        if (!shouldDisableSampling(modelName, normalizedReasoningEffort)) {
            return temperature;
        }
        if (isSearchModel(modelName)) {
            return null;
        }
        if (isLegacyGpt5Model(modelName) || isProModel(modelName)) {
            return GPT_5_DEFAULT_TEMPERATURE;
        }
        return null;
    }

    private static Double normalizeTopP(String modelName, Double topP, String reasoningEffort) {
        String normalizedReasoningEffort = normalizeReasoningEffort(modelName, reasoningEffort);
        if (!shouldDisableSampling(modelName, normalizedReasoningEffort)) {
            return topP;
        }
        return null;
    }

    private static String normalizeReasoningEffort(String modelName, String reasoningEffort) {
        String normalized = normalizeText(reasoningEffort);
        if (normalized == null || !isGpt5Model(modelName)) {
            return normalized;
        }

        if (isProModel(modelName)) {
            return REASONING_HIGH;
        }

        if (isGpt51OrLaterModel(modelName)) {
            if (REASONING_MINIMAL.equals(normalized)) {
                return REASONING_LOW;
            }
            if (isSupportedGpt51OrLaterReasoning(normalized)) {
                return normalized;
            }
            return null;
        }

        if (REASONING_NONE.equals(normalized)) {
            return REASONING_MINIMAL;
        }
        if (REASONING_XHIGH.equals(normalized)) {
            return REASONING_HIGH;
        }
        if (isSupportedLegacyGpt5Reasoning(normalized)) {
            return normalized;
        }
        return null;
    }

    static ChatRequest normalizeParameters(ChatRequest request) {
        Map<String, Object> parameters = new LinkedHashMap<>(request.getParameters());
        String effort = normalizeReasoningEffort(request.getModel(), (String) parameters.get(ChatProtocolConstants.PARAMETER_REASONING_EFFORT));
        Number temperature = (Number) parameters.get(ChatProtocolConstants.PARAMETER_TEMPERATURE);
        Number topP = (Number) parameters.get(ChatProtocolConstants.PARAMETER_TOP_P);
        Double temperatureValue = null;
        Double topPValue = null;
        if (temperature != null) {
            temperatureValue = temperature.doubleValue();
        }
        if (topP != null) {
            topPValue = topP.doubleValue();
        }
        parameters.put(ChatProtocolConstants.PARAMETER_REASONING_EFFORT, effort);
        parameters.put(ChatProtocolConstants.PARAMETER_TEMPERATURE, normalizeTemperature(request.getModel(), temperatureValue, effort));
        parameters.put(ChatProtocolConstants.PARAMETER_TOP_P, normalizeTopP(request.getModel(), topPValue, effort));
        parameters.values().removeIf(Objects::isNull);
        return new ChatRequest(request
            .getModel(), request.getMessages(), request.getTools(), request.getToolChoice(), request.getMaxOutputTokens(), request.isShowReasoning(), parameters);
    }

    static boolean usesMaxCompletionTokens(String modelName) {
        return isGpt5Model(modelName);
    }

    private static boolean isGpt5Model(String modelName) {
        String normalized = normalizeText(modelName);
        return normalized != null && normalized.startsWith("gpt-5");
    }

    private static boolean isSearchModel(String modelName) {
        String normalized = normalizeText(modelName);
        return normalized != null && normalized.contains("-search");
    }

    private static boolean shouldDisableSampling(String modelName, String normalizedReasoningEffort) {
        return isGpt5Model(modelName)
               && (isLegacyGpt5Model(modelName) || isProModel(modelName) || (normalizedReasoningEffort != null && !REASONING_NONE.equals(normalizedReasoningEffort)));
    }

    private static boolean isLegacyGpt5Model(String modelName) {
        return isGpt5Model(modelName) && !isProModel(modelName) && !isGpt51OrLaterModel(modelName);
    }

    private static boolean isGpt51OrLaterModel(String modelName) {
        Integer minorVersion = parseGpt5MinorVersion(modelName);
        return minorVersion != null && minorVersion >= 1;
    }

    private static boolean isProModel(String modelName) {
        String normalized = normalizeText(modelName);
        return normalized != null && normalized.contains("-pro");
    }

    private static boolean isSupportedLegacyGpt5Reasoning(String reasoningEffort) {
        return REASONING_MINIMAL.equals(reasoningEffort) || REASONING_LOW.equals(reasoningEffort) || REASONING_MEDIUM.equals(reasoningEffort)
               || REASONING_HIGH.equals(reasoningEffort);
    }

    private static boolean isSupportedGpt51OrLaterReasoning(String reasoningEffort) {
        return REASONING_NONE.equals(reasoningEffort) || REASONING_LOW.equals(reasoningEffort) || REASONING_MEDIUM.equals(reasoningEffort) || REASONING_HIGH.equals(reasoningEffort)
               || REASONING_XHIGH.equals(reasoningEffort);
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }

    private static Integer parseGpt5MinorVersion(String modelName) {
        String normalized = normalizeText(modelName);
        if (normalized == null || !normalized.startsWith("gpt-5.")) {
            return null;
        }

        int start = "gpt-5.".length();
        int end = start;
        while (end < normalized.length() && Character.isDigit(normalized.charAt(end))) {
            end++;
        }
        if (end == start) {
            return null;
        }
        return Integer.parseInt(normalized.substring(start, end));
    }
}
