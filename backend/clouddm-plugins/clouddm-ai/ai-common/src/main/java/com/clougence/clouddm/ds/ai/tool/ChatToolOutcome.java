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
package com.clougence.clouddm.ds.ai.tool;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class ChatToolOutcome {
    private final boolean success;

    private final String  text;

    private final Object  value;

    @JsonCreator
    public ChatToolOutcome(@JsonProperty("success") boolean success, @JsonProperty("text") String text, @JsonProperty("value") Object value){
        this.success = success;
        this.text = text;
        this.value = value;
    }

    public static ChatToolOutcome error(String message) {
        return new ChatToolOutcome(false, ChatToolConstants.ERROR_PREFIX + message, null);
    }
}
