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
package com.clougence.clouddm.console.web.global.mcp.model.response;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ToolCallResult {

    @JsonProperty("content")
    private final List<ContentBlock> content;

    private ToolCallResult(List<ContentBlock> content){
        this.content = content;
    }

    public static ToolCallResult fromText(String text) {
        return new ToolCallResult(Collections.singletonList(new ContentBlock(text)));
    }

    @Getter
    public static class ContentBlock {

        @JsonProperty("type")
        private final String type = "text";

        @JsonProperty("text")
        private final String text;

        public ContentBlock(String text){
            this.text = text;
        }
    }
}
