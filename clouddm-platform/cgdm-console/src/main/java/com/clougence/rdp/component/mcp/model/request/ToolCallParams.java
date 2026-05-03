package com.clougence.rdp.component.mcp.model.request;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ToolCallParams {

    @JsonProperty("name")
    private String              name;

    @JsonProperty("arguments")
    private Map<String, Object> arguments;
}
