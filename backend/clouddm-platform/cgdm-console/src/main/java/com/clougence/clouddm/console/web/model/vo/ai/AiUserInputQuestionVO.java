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

import java.util.List;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.ai.AiUserInputQuestionType;
import com.clougence.clouddm.ds.ai.tool.ChatToolParameter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public final class AiUserInputQuestionVO {
    @ChatToolParameter(name = AiSqlConstants.FIELD_ID, description = "Stable question ID.")
    @NotBlank
    private final String                    id;

    @ChatToolParameter(name = AiSqlConstants.FIELD_LABEL, description = "Optional short section label.", required = false)
    private final String                    label;

    @ChatToolParameter(name = AiSqlConstants.FIELD_QUESTION, description = "Main question shown to the user.")
    @NotBlank
    private final String                    question;

    @ChatToolParameter(name = AiSqlConstants.FIELD_TYPE, description = "Question interaction type.")
    @NotNull
    private final AiUserInputQuestionType   type;

    @ChatToolParameter(name = AiSqlConstants.FIELD_OPTIONS, description = "Two or three distinct answers for choice questions. Omit for FREE_TEXT.", required = false)
    @Valid
    @Size(max = 3)
    private final List<AiUserInputOptionVO> options;

    @ChatToolParameter(name = AiSqlConstants.FIELD_ALLOW_CUSTOM, description = "Allow free-form text in addition to a choice. Defaults to true.", required = false)
    @JsonProperty(AiSqlConstants.FIELD_ALLOW_CUSTOM)
    private final Boolean                   allowCustom;

    @ChatToolParameter(name = AiSqlConstants.FIELD_REQUIRED, description = "Require an answer. Defaults to true.", required = false)
    private final Boolean                   required;

    @JsonCreator
    public AiUserInputQuestionVO(@JsonProperty("id") String id, @JsonProperty("label") String label, @JsonProperty("question") String question,
                                 @JsonProperty("type") AiUserInputQuestionType type, @JsonProperty("options") List<AiUserInputOptionVO> options,
                                 @JsonProperty(AiSqlConstants.FIELD_ALLOW_CUSTOM) Boolean allowCustom, @JsonProperty("required") Boolean required){
        this.id = id;
        this.label = label;
        this.question = question;
        this.type = type;
        this.options = options;
        this.allowCustom = allowCustom;
        this.required = required;
    }
}
