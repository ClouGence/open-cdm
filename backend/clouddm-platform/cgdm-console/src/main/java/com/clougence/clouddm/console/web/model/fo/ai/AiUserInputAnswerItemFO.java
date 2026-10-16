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

import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class AiUserInputAnswerItemFO {
    @JsonProperty(AiSqlConstants.FIELD_QUESTION_ID)
    private final String       questionId;

    private final String       question;

    @Size(max = 20)
    private final List<String> values;

    @JsonCreator
    public AiUserInputAnswerItemFO(@JsonProperty(AiSqlConstants.FIELD_QUESTION_ID) String questionId, @JsonProperty("question") String question,
                                   @JsonProperty("values") List<String> values){
        if (values == null) {
            values = List.of();
        }
        this.questionId = questionId;
        this.question = question;
        this.values = values;
    }
}
