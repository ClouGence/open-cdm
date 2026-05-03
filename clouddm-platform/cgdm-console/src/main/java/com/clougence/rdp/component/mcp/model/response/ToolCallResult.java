package com.clougence.rdp.component.mcp.model.response;

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
