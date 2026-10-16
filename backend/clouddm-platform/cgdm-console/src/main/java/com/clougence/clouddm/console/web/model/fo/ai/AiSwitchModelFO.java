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
package com.clougence.clouddm.console.web.model.fo.ai;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public final class AiSwitchModelFO {
    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String  sessionId;

    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String  engineId;

    @NotNull
    @Min(1)
    private final Integer engineVersion;

    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String  previousEngineId;

    @NotNull
    @Min(1)
    private final Integer previousEngineVersion;

    @JsonCreator
    public AiSwitchModelFO(@JsonProperty("sessionId") String sessionId, @JsonProperty("engineId") String engineId, @JsonProperty("engineVersion") Integer engineVersion,
                           @JsonProperty("previousEngineId") String previousEngineId, @JsonProperty("previousEngineVersion") Integer previousEngineVersion){
        this.sessionId = sessionId;
        this.engineId = engineId;
        this.engineVersion = engineVersion;
        this.previousEngineId = previousEngineId;
        this.previousEngineVersion = previousEngineVersion;
    }
}
