package com.clougence.rdp.component.mcp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class McpProtocolBase {

    @JsonProperty("jsonrpc")
    protected String jsonrpc = "2.0";

    @JsonProperty("id")
    protected Object id;

}
