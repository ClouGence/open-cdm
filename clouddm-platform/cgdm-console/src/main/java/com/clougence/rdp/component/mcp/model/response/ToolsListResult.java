package com.clougence.rdp.component.mcp.model.response;

import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.rdp.component.mcp.McpI18nProxy;
import com.clougence.rdp.component.mcp.json.JsonSchemaElement;
import com.clougence.rdp.component.mcp.utils.JsonSchemaElementUtils;
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
