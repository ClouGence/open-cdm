package com.clougence.rdp.component.mcp.model.request;

import javax.validation.constraints.NotNull;

import com.clougence.rdp.component.mcp.model.McpClientMethod;
import com.clougence.rdp.component.mcp.model.McpProtocolBase;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpRequest extends McpProtocolBase {

    @JsonProperty("method")
    @NotNull
    private McpClientMethod method;

    @JsonProperty("params")
    private Object          params;
}
