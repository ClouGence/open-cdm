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

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record AiTurnFO(@NotNull @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String sessionId, @NotNull @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String requestId,
                       @Size(max = 128) String streamClientId, @NotBlank @Size(max = 16384) String prompt, @NotNull @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String engineId,
                       @NotNull @Min(1) Integer engineVersion, @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String regenerateTurnId,
                       @NotNull @Size(max = 16) List<@NotNull @Size(min = 1, max = 512) String> levels, @Valid AiSqlSelection selection, @Valid AiUserInputAnswerFO userInputAnswer,
                       @Size(max = 128) String timezone, Integer timezoneOffsetMin) {
}
