package com.clougence.rdp.component.mcp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class InitializeResult {

    @JsonProperty("protocolVersion")
    private final String       protocolVersion;

    @JsonProperty("capabilities")
    private final Capabilities capabilities;

    @JsonProperty("serverInfo")
    private final ServerInfo   serverInfo;

    public InitializeResult(String protocolVersion, Capabilities capabilities, ServerInfo serverInfo){
        this.protocolVersion = protocolVersion;
        this.capabilities = capabilities;
        this.serverInfo = serverInfo;
    }

    @Getter
    public static class ServerInfo {

        @JsonProperty("name")
        private final String name;

        @JsonProperty("version")
        private final String version;

        public ServerInfo(String name, String version){
            this.name = name;
            this.version = version;
        }
    }

    @Getter
    public static class Capabilities {

        @JsonProperty("tools")
        private final Tools tools;

        public Capabilities(Tools tools){
            this.tools = tools;
        }
    }

    @Getter
    public static class Tools {

        @JsonProperty("listChanged")
        private final boolean listChanged;

        public Tools(boolean listChanged){
            this.listChanged = listChanged;
        }
    }
}
