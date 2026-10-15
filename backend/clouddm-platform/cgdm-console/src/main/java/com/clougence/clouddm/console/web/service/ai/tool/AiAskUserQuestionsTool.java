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

import java.util.*;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.ai.AiUserInputQuestionType;
import com.clougence.clouddm.console.web.model.vo.ai.AiUserInputOptionVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiUserInputQuestionVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiUserInputRequestVO;
import com.clougence.clouddm.ds.ai.tool.ChatTool;
import com.clougence.clouddm.ds.ai.tool.ChatToolParameter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class AiAskUserQuestionsTool {
    @ChatTool(name = AiSqlConstants.TOOL_ASK_USER_QUESTIONS, description = "Render a structured question card and pause the task for the user's answer. Use only when missing user input would materially change the result. Keep title, questions, and options concise. Call as the only tool in the response. After success, stop and wait; the returned questions are not a user answer.")
    public AiUserInputRequestVO askUserQuestions(@ChatToolParameter(name = AiSqlConstants.FIELD_TITLE, description = "Short question card title.", required = false) String title,
                                                 @ChatToolParameter(name = AiSqlConstants.FIELD_QUESTIONS, description = "Prefer one focused question; never more than three.") @NotNull @Size(min = 1, max = 3) List<@NotNull @Valid AiUserInputQuestionVO> questions,
                                                 @ChatToolParameter(name = AiSqlConstants.FIELD_SUBMIT_LABEL, description = "Optional submit button label.", required = false) String submitLabel) {
        List<AiUserInputQuestionVO> normalized = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (AiUserInputQuestionVO question : questions) {
            String id = question.id().strip();
            if (!ids.add(id)) {
                throw new IllegalArgumentException("Question IDs must be unique.");
            }
            List<AiUserInputOptionVO> options = List.of();
            if (question.type() != AiUserInputQuestionType.FREE_TEXT) {
                if (question.options() == null || question.options().size() < 2 || question.options().size() > 3) {
                    throw new IllegalArgumentException("Choice questions require 2-3 options.");
                }
                Set<String> optionIds = new HashSet<>();
                options = new ArrayList<>();
                for (AiUserInputOptionVO option : question.options()) {
                    String optionId = option.id().strip();
                    if (!optionIds.add(optionId)) {
                        throw new IllegalArgumentException("Option IDs must be unique.");
                    }
                    options.add(new AiUserInputOptionVO(optionId, option.label().strip(), option.description()));
                }
            }
            normalized.add(new AiUserInputQuestionVO(id,
                question.label(),
                question.question().strip(),
                question.type(),
                options,
                !Boolean.FALSE.equals(question.allowCustom()),
                !Boolean.FALSE.equals(question.required())));
        }
        return new AiUserInputRequestVO(2, UUID.randomUUID().toString(), title, normalized, submitLabel);
    }
}
