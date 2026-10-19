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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiGetSessionFO {
    @NotNull
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private String  sessionId;
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private String  beforeTurnId;
    @Pattern(regexp = AiSqlConstants.UUID_PATTERN)
    private String  requestId;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize = 20;
}
