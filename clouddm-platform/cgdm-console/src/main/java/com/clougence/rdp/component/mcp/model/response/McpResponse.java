package com.clougence.rdp.component.mcp.model.response;

import com.clougence.rdp.component.mcp.model.McpProtocolBase;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McpResponse extends McpProtocolBase {

    @JsonProperty("result")
    private Object result;

    public McpResponse(){
    }

    public McpResponse(Object id, Object result){
        this.id = id;
        this.result = result;
    }

}
