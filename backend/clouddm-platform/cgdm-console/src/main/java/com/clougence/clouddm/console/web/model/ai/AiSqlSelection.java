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
package com.clougence.clouddm.console.web.model.ai;

import org.hibernate.validator.constraints.CodePointLength;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** SQL text selected by the user, never executed by the assistant. */
@Getter
@EqualsAndHashCode
public final class AiSqlSelection {
    @NotBlank
    @CodePointLength(max = 512)
    private final String title;

    @NotBlank
    private final String sql;

    @Min(1)
    private final int    startLine;

    @Min(1)
    private final int    endLine;

    @JsonCreator
    public AiSqlSelection(@JsonProperty("title") String title, @JsonProperty("sql") String sql, @JsonProperty("startLine") int startLine, @JsonProperty("endLine") int endLine){
        this.title = title;
        this.sql = sql;
        this.startLine = startLine;
        this.endLine = endLine;
    }
}
