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

import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.console.web.model.vo.ai.AiUserInputRequestVO;

public record AiSqlResult(String status, String sql, String message, AiUserInputRequestVO userInputRequest) {
    public static AiSqlResult fromText(String text, List<String> statements) {
        String sql = null;
        if (!statements.isEmpty()) {
            sql = statements.stream().map(statement -> statement.replaceAll("^[\\r\\n]+|[\\r\\n]+$", "")).collect(Collectors.joining("\n"));
        }
        return new AiSqlResult(AiSqlConstants.RESULT_GENERATED, sql, text.strip(), null);
    }
}
