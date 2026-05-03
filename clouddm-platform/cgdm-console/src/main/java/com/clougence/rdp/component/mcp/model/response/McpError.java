package com.clougence.rdp.component.mcp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class McpError {

    public static final int METHOD_NOT_FOUND = -32601;

    public static final int INTERNAL_ERROR   = -32603;

    @JsonProperty("code")
    private final int       code;

    @JsonProperty("message")
    private final String    message;

    public McpError(int code, String message){
        this.code = code;
        this.message = message;
    }
}
