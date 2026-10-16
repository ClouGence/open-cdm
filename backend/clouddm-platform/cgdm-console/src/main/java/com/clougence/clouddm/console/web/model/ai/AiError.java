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

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;

public enum AiError {
    INVALID_REQUEST,
    NOT_FOUND,
    SESSION_MODEL_CHANGED,
    FORBIDDEN,
    ENGINE_VERSION_CONFLICT,
    ENGINE_NAME_MODEL_CONFLICT,
    ENGINE_UNAVAILABLE,
    STREAM_UNAVAILABLE,
    REQUEST_CONFLICT,
    SESSION_BUSY,
    AI_BUSY,
    CONTEXT_COMPACTION_FAILED,
    CONTEXT_REVOKED,
    METADATA_FAILED,
    METADATA_TIMEOUT,
    MODEL_OUTPUT_TRUNCATED,
    TURN_EXPIRED,
    INTERNAL_ERROR;

    public String message() {
        String key = name();
        if (!key.startsWith("AI_")) {
            key = "AI_" + key;
        }
        return DmI18nUtils.getMessage(key);
    }

    public ErrorMessageException exception() {
        return new ErrorMessageException(name(), message());
    }
}
