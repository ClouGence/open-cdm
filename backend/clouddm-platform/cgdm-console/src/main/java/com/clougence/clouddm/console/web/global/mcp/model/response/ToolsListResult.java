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

import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.global.mcp.McpI18nProxy;
import com.clougence.clouddm.console.web.global.mcp.json.JsonSchemaElement;
import com.clougence.clouddm.console.web.global.mcp.utils.JsonSchemaElementUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolsListResult {

    @JsonProperty("tools")
    private List<ToolDesc> tools;

    @JsonProperty("nextCursor")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String         nextCursor;

    public ToolsListResult(){
    }

    public ToolsListResult(List<ToolDesc> tools, String nextCursor){
        this.tools = tools;
        this.nextCursor = nextCursor;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ToolDesc {

        @JsonProperty("name")
        private String              name;

        @JsonProperty("description")
        private String              description;

        @JsonProperty("inputSchema")
        private Map<String, Object> inputSchema;

        public ToolDesc(){
        }

        public ToolDesc(String name, String description, Parameter input, McpI18nProxy i18nProxy){
            this.name = name;
            this.description = description;
            if (input != null) {
                JsonSchemaElement paramsSchema = JsonSchemaElementUtils
                    .jsonSchemaElementFrom(input.getType(), input.getParameterizedType(), null, false, new LinkedHashMap<>(), i18nProxy);
                this.inputSchema = JsonSchemaElementUtils.toMap(paramsSchema, false /* strict */);
            } else {
                this.inputSchema = null;
            }
        }
    }
}
