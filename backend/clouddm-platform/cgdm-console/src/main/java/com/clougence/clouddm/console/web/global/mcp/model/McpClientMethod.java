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
package com.clougence.clouddm.console.web.global.mcp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum McpClientMethod {
    @JsonProperty("initialize")
    INITIALIZE,

    @JsonProperty("tools/call")
    TOOLS_CALL,

    @JsonProperty("tools/list")
    TOOLS_LIST,

    @JsonProperty("notifications/cancelled")
    NOTIFICATION_CANCELLED,

    @JsonProperty("notifications/initialized")
    NOTIFICATION_INITIALIZED,

    @JsonProperty("ping")
    PING,

    @JsonProperty("resources/list")
    RESOURCES_LIST,

    @JsonProperty("resources/read")
    RESOURCES_READ,

    @JsonProperty("resources/templates/list")
    RESOURCES_TEMPLATES_LIST,

    @JsonProperty("prompts/list")
    PROMPTS_LIST,

    @JsonProperty("prompts/get")
    PROMPTS_GET,

    @JsonProperty("notifications/roots/list_changed")
    NOTIFICATION_ROOTS_LIST_CHANGED
}
