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

import java.util.Map;

import com.clougence.clouddm.platform.dal.model.ai.AiTurnStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiRunEventType {
    START(AiSqlConstants.EVENT_STARTED),
    THINKING(AiSqlConstants.EVENT_PHASE),
    PROGRESS_TEXT(AiSqlConstants.EVENT_TEXT_DELTA),
    TOOL_REQUESTED(AiSqlConstants.EVENT_TOOL_STARTED),
    TOOL_EXECUTED(AiSqlConstants.EVENT_TOOL_COMPLETED),
    RESPONSE_GENERATED(AiSqlConstants.EVENT_RESULT),
    ERROR(AiSqlConstants.EVENT_ERROR),
    CHAT_COMPLETED(AiSqlConstants.EVENT_DONE),
    CHAT_CANCELLED(AiSqlConstants.EVENT_DONE),
    CHAT_FAILED(AiSqlConstants.EVENT_DONE),
    // Draft replacements are journaled so replay reconstructs the same visible output.
    SNAPSHOT(AiSqlConstants.EVENT_SNAPSHOT);

    private final String wireType;

    public static AiRunEventType fromWire(String type, Object data) {
        if (AiSqlConstants.EVENT_DONE.equals(type)) {
            String status = String.valueOf(((Map<?, ?>) data).get(AiSqlConstants.FIELD_STATUS));
            if (AiTurnStatus.CANCELLED.name().equals(status)) {
                return CHAT_CANCELLED;
            }
            if (AiTurnStatus.FAILED.name().equals(status) || AiTurnStatus.EXPIRED.name().equals(status)) {
                return CHAT_FAILED;
            }
            return CHAT_COMPLETED;
        }
        for (AiRunEventType candidate : values()) {
            if (candidate.wireType.equals(type)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Unknown AI event type: " + type);
    }
}
