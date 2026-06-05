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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LLMConfigHelper {

    public static String genDefaultEmbeddingConfig(Map<String, Integer> model2Dimension) {
        try {
            Map<String, LLMEmbeddingConfig> cs = new HashMap<>();
            for (Map.Entry<String, Integer> entry : model2Dimension.entrySet()) {
                String modelName = entry.getKey();
                Integer dimension = entry.getValue();
                cs.put(modelName, new LLMEmbeddingConfig(dimension));
            }

            return new ObjectMapper().writeValueAsString(cs);
        } catch (Exception e) {
            log.error("gen default embedding config failed,but ignore it.msg:", e);
            return null;
        }
    }

    public static String genDefaultChatConfig(Set<String> modelNames) {
        // Temperature: Balanced creativity and determinism
        // TopP: Allows reasonable diversity without too much randomness
        return genDefaultChatConfig(modelNames, 0.7, 0.9, false);
    }

    public static String genDefaultChatConfig(Set<String> modelNames, double defaultTemperature, double defaultTopP, Boolean showReasoning) {
        try {
            Map<String, LLMChatConfig> cs = new HashMap<>();
            for (String modelName : modelNames) {
                LLMChatConfig llmChatConfig = new LLMChatConfig();
                llmChatConfig.setTemperature(defaultTemperature);
                llmChatConfig.setTopP(defaultTopP);
                llmChatConfig.setShowReasoning(showReasoning);
                cs.put(modelName, llmChatConfig);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper.writeValueAsString(cs);
        } catch (Exception e) {
            log.error("gen default embedding config failed,but ignore it.msg:", e);
            return null;
        }
    }
}
