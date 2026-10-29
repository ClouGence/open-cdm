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
package com.clougence.clouddm.console.web.service.ai.tool;

import java.util.Map;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.ds.ai.tool.ChatTool;
import com.clougence.clouddm.ds.ai.tool.ChatToolParameter;

import jakarta.validation.constraints.NotBlank;

public final class AiGenerateSqlTool {
    @ChatTool(name = AiSqlConstants.TOOL_GENERATE_SQL, description = "Deliver generated SQL to the user as a structured SQL artifact for review. The sql argument is the actual SQL text, not a request or explanation. Never execute or validate it against a database. Each successful call returns {sql} and appends its SQL to the current turn's SQL artifact. For long output, deliver one batch of complete statements per response and wait for the tool result before continuing with the next batch in the current turn. Do not repeat a successfully delivered batch or put its SQL in the final answer.")
    public Map<String, String> generateSql(@ChatToolParameter(name = AiSqlConstants.FIELD_SQL, description = "Complete SQL statements to deliver, without Markdown fences. For a long script, provide a batch of complete statements and continue with subsequent batches in later calls.") @NotBlank String sql) {
        return Map.of(AiSqlConstants.FIELD_SQL, sql);
    }
}
