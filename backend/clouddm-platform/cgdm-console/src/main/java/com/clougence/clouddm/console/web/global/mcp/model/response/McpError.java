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

import com.fasterxml.jackson.annotation.JsonProperty;

public record McpError(@JsonProperty("code") int code, @JsonProperty("message") String message) {

    public static final int METHOD_NOT_FOUND = -32601;

    public static final int INTERNAL_ERROR = -32603;

    public McpError(int code, String message){
        this.code = code;
        this.message = message;
    }
}
