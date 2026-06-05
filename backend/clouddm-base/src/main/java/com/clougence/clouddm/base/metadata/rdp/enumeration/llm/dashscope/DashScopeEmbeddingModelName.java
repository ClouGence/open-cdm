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
package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.dashscope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMConfigHelper;

public enum DashScopeEmbeddingModelName {

    TEXT_EMBEDDING_V3("text-embedding-v3", 1024, 10),
    TEXT_EMBEDDING_V2("text-embedding-v2", 1536, 25),
    TEXT_EMBEDDING_V1("text-embedding-v1", 1536, 25),
    TEXT_EMBEDDING_ASYNC_V2("text-embedding-async-v2", 1536, 100000),
    TEXT_EMBEDDING_ASYNC_V1("text-embedding-async-v1", 1536, 100000);

    private final String  stringValue;
    private final Integer dimension;
    private final Integer maxSegmentsPerBatch;

    DashScopeEmbeddingModelName(String stringValue, Integer dimension, Integer maxSegmentsPerBatch){
        this.stringValue = stringValue;
        this.dimension = dimension;
        this.maxSegmentsPerBatch = maxSegmentsPerBatch;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public Integer dimension() {
        return dimension;
    }

    public Integer maxSegmentsPerBatch() {
        return maxSegmentsPerBatch;
    }

    private static final Map<String, Integer> KNOWN_DIMENSION              = new HashMap<>(DashScopeEmbeddingModelName.values().length);
    private static final Map<String, Integer> KNOWN_MAX_SEGMENTS_PER_BATCH = new HashMap<>(DashScopeEmbeddingModelName.values().length);
    private static final String               DEFAULT_CONFIG;

    static {
        for (DashScopeEmbeddingModelName embeddingModelName : DashScopeEmbeddingModelName.values()) {
            KNOWN_DIMENSION.put(embeddingModelName.toString(), embeddingModelName.dimension());
            KNOWN_MAX_SEGMENTS_PER_BATCH.put(embeddingModelName.toString(), embeddingModelName.maxSegmentsPerBatch());
        }
        DEFAULT_CONFIG = LLMConfigHelper.genDefaultEmbeddingConfig(KNOWN_DIMENSION);
    }

    public static Integer knownDimension(String modelName) {
        return KNOWN_DIMENSION.get(modelName);
    }

    public static Set<String> allModelNames() {
        return KNOWN_DIMENSION.keySet();
    }

    public static Integer knownMaxSegmentsPerBatch(String modelName) {
        return KNOWN_MAX_SEGMENTS_PER_BATCH.get(modelName);
    }

    public static String defaultConfig() {
        return DEFAULT_CONFIG;
    }
}
