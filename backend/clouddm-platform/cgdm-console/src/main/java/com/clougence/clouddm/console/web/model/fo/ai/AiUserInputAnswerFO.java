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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class AiUserInputAnswerFO {
    @JsonProperty(AiSqlConstants.FIELD_SCHEMA_VERSION)
    private final Integer                       schemaVersion;

    @JsonProperty(AiSqlConstants.FIELD_USER_INPUT_REQUEST_ID)
    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String                        requestId;

    @JsonProperty(AiSqlConstants.FIELD_USER_INPUT_HISTORY_ID)
    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private final String                        requestHistoryId;

    @JsonProperty(AiSqlConstants.FIELD_ANSWER_TEXT)
    @Size(max = 16384)
    private final String                        answerText;

    @Valid
    @Size(max = 20)
    private final List<AiUserInputAnswerItemFO> answers;

    @JsonCreator
    public AiUserInputAnswerFO(@JsonProperty(AiSqlConstants.FIELD_SCHEMA_VERSION) Integer schemaVersion, @JsonProperty(AiSqlConstants.FIELD_USER_INPUT_REQUEST_ID) String requestId,
                               @JsonProperty(AiSqlConstants.FIELD_USER_INPUT_HISTORY_ID) String requestHistoryId, @JsonProperty(AiSqlConstants.FIELD_ANSWER_TEXT) String answerText,
                               @JsonProperty("answers") List<AiUserInputAnswerItemFO> answers){
        if (schemaVersion == null) {
            schemaVersion = 2;
        }
        if (answers == null) {
            answers = List.of();
        }
        this.schemaVersion = schemaVersion;
        this.requestId = requestId;
        this.requestHistoryId = requestHistoryId;
        this.answerText = answerText;
        this.answers = answers;
    }
}
