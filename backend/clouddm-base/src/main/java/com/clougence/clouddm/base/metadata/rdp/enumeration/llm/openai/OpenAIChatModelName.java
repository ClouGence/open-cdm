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
package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum OpenAIChatModelName {

    GPT_3_5_TURBO("gpt-3.5-turbo"), // alias
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),

    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"), // alias

    GPT_4("gpt-4"), // alias
    GPT_4_0613("gpt-4-0613"),

    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"), // alias
    GPT_4_1106_PREVIEW("gpt-4-1106-preview"),
    GPT_4_0125_PREVIEW("gpt-4-0125-preview"),
    GPT_4_TURBO("gpt-4-turbo"), // alias
    GPT_4_TURBO_2024_04_09("gpt-4-turbo-2024-04-09"),

    GPT_4_32K("gpt-4-32k"), // alias
    GPT_4_32K_0613("gpt-4-32k-0613"),

    GPT_4_O("gpt-4o"), // alias
    GPT_4_O_2024_05_13("gpt-4o-2024-05-13"),
    GPT_4_O_2024_08_06("gpt-4o-2024-08-06"),
    GPT_4_O_2024_11_20("gpt-4o-2024-11-20"),

    GPT_4_O_MINI("gpt-4o-mini"), // alias
    GPT_4_O_MINI_2024_07_18("gpt-4o-mini-2024-07-18"),

    O1("o1"), // alias
    O1_2024_12_17("o1-2024-12-17"),

    O1_MINI("o1-mini"), // alias
    O1_MINI_2024_09_12("o1-mini-2024-09-12"),

    O1_PREVIEW("o1-preview"), // alias
    O1_PREVIEW_2024_09_12("o1-preview-2024-09-12"),

    O3_MINI("o3-mini"), // alias
    O3_MINI_2025_01_31("o3-mini-2025-01-31");

    private final String stringValue;

    OpenAIChatModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private static final Set<String> KNOWN_MODELS = new HashSet<>(OpenAIChatModelName.values().length);
    private static final String      DEFAULT_CONFIG;

    static {
        Set<OpenAIChatModelName> defaultModels = Collections.unmodifiableSet( //
                new HashSet<>(Arrays.asList(GPT_3_5_TURBO, GPT_4, O1, O3_MINI)));
        for (OpenAIChatModelName chatModelName : defaultModels) {
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
