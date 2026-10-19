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

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.ds.ai.tool.ChatToolParameter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public final class AiUserInputOptionVO {
    @ChatToolParameter(name = AiSqlConstants.FIELD_ID, description = "Stable option ID.")
    @NotBlank
    private final String id;

    @ChatToolParameter(name = AiSqlConstants.FIELD_LABEL, description = "Short answer label.")
    @NotBlank
    private final String label;

    @ChatToolParameter(name = AiSqlConstants.FIELD_DESCRIPTION, description = "Optional brief explanation.", required = false)
    private final String description;

    @JsonCreator
    public AiUserInputOptionVO(@JsonProperty("id") String id, @JsonProperty("label") String label, @JsonProperty("description") String description){
        this.id = id;
        this.label = label;
        this.description = description;
    }
}
