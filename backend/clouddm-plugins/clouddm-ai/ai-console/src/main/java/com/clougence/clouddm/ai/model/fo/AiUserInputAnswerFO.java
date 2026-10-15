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
package com.clougence.clouddm.ai.model.fo;

import java.util.List;

import com.clougence.clouddm.ai.model.AiSqlConstants;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AiUserInputAnswerFO(@JsonProperty(AiSqlConstants.FIELD_SCHEMA_VERSION) Integer schemaVersion,
                                  @JsonProperty(AiSqlConstants.FIELD_USER_INPUT_REQUEST_ID) @NotNull @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String requestId,
                                  @JsonProperty(AiSqlConstants.FIELD_USER_INPUT_HISTORY_ID) @NotNull @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String requestHistoryId,
                                  @JsonProperty(AiSqlConstants.FIELD_ANSWER_TEXT) @Size(max = 16384) String answerText,
                                  @Valid @Size(max = 20) List<AiUserInputAnswerItemFO> answers) {
    public AiUserInputAnswerFO{
        if (schemaVersion == null) {
            schemaVersion = 2;
        }
        if (answers == null) {
            answers = List.of();
        }
    }
}
