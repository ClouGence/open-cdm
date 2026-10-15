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

import jakarta.validation.constraints.NotBlank;

public record AiUserInputOptionVO(@ChatToolParameter(name = AiSqlConstants.FIELD_ID, description = "Stable option ID.") @NotBlank String id,
                                  @ChatToolParameter(name = AiSqlConstants.FIELD_LABEL, description = "Short answer label.") @NotBlank String label,
                                  @ChatToolParameter(name = AiSqlConstants.FIELD_DESCRIPTION, description = "Optional brief explanation.", required = false) String description) {
}
