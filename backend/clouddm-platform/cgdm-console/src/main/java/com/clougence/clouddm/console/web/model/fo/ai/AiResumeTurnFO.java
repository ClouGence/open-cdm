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

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public final class AiResumeTurnFO {
    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String sessionId;

    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String requestId;

    @Size(max = 128)
    private final String streamClientId;

    @JsonCreator
    public AiResumeTurnFO(@JsonProperty("sessionId") String sessionId, @JsonProperty("requestId") String requestId, @JsonProperty("streamClientId") String streamClientId){
        this.sessionId = sessionId;
        this.requestId = requestId;
        this.streamClientId = streamClientId;
    }
}
