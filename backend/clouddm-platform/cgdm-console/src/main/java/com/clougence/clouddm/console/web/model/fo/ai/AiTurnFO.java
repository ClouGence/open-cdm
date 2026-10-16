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

import java.util.List;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.ai.AiSqlSelection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public final class AiTurnFO {
    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String                                          sessionId;

    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String                                          requestId;

    @Size(max = 128)
    private final String                                          streamClientId;

    @NotBlank
    @Size(max = 16384)
    private final String                                          prompt;

    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String                                          engineId;

    @NotNull
    @Min(1)
    private final Integer                                         engineVersion;

    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String                                          regenerateTurnId;

    @NotNull
    @Size(max = 16)
    private final List<@NotNull @Size(min = 1, max = 512) String> levels;

    @Valid
    private final AiSqlSelection                                  selection;

    @Valid
    private final AiUserInputAnswerFO                             userInputAnswer;

    @Size(max = 128)
    private final String                                          timezone;

    private final Integer                                         timezoneOffsetMin;

    @JsonCreator
    public AiTurnFO(@JsonProperty("sessionId") String sessionId, @JsonProperty("requestId") String requestId, @JsonProperty("streamClientId") String streamClientId,
                    @JsonProperty("prompt") String prompt, @JsonProperty("engineId") String engineId, @JsonProperty("engineVersion") Integer engineVersion,
                    @JsonProperty("regenerateTurnId") String regenerateTurnId, @JsonProperty("levels") List<@NotNull @Size(min = 1, max = 512) String> levels,
                    @JsonProperty("selection") AiSqlSelection selection, @JsonProperty("userInputAnswer") AiUserInputAnswerFO userInputAnswer,
                    @JsonProperty("timezone") String timezone, @JsonProperty("timezoneOffsetMin") Integer timezoneOffsetMin){
        this.sessionId = sessionId;
        this.requestId = requestId;
        this.streamClientId = streamClientId;
        this.prompt = prompt;
        this.engineId = engineId;
        this.engineVersion = engineVersion;
        this.regenerateTurnId = regenerateTurnId;
        this.levels = levels;
        this.selection = selection;
        this.userInputAnswer = userInputAnswer;
        this.timezone = timezone;
        this.timezoneOffsetMin = timezoneOffsetMin;
    }
}
