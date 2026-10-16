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
package com.clougence.clouddm.console.web.model.vo.ai;

import java.time.Instant;

import com.clougence.clouddm.platform.dal.model.ai.AiPreset;
import com.clougence.clouddm.platform.dal.model.ai.AiVerificationStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class AiEngineVO {
    private final String               id;

    private final String               name;

    private final AiPreset             preset;

    private final String               model;

    private final String               baseUrl;

    private final int                  version;

    private final AiVerificationStatus verificationStatus;

    private final Integer              verifiedVersion;

    private final Instant              verifiedAt;

    private final boolean              hasApiKey;

    private final Instant              createdAt;

    private final Instant              modifiedAt;

    @JsonCreator
    public AiEngineVO(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("preset") AiPreset preset, @JsonProperty("model") String model,
                      @JsonProperty("baseUrl") String baseUrl, @JsonProperty("version") int version, @JsonProperty("verificationStatus") AiVerificationStatus verificationStatus,
                      @JsonProperty("verifiedVersion") Integer verifiedVersion, @JsonProperty("verifiedAt") Instant verifiedAt, @JsonProperty("hasApiKey") boolean hasApiKey,
                      @JsonProperty("createdAt") Instant createdAt, @JsonProperty("modifiedAt") Instant modifiedAt){
        this.id = id;
        this.name = name;
        this.preset = preset;
        this.model = model;
        this.baseUrl = baseUrl;
        this.version = version;
        this.verificationStatus = verificationStatus;
        this.verifiedVersion = verifiedVersion;
        this.verifiedAt = verifiedAt;
        this.hasApiKey = hasApiKey;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
