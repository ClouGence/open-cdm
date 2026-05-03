package com.clougence.utils.token;

import java.util.Map;

import lombok.Getter;

public class MapVariableTokenHandler implements TokenHandler {

    private final Map<String, Object> variables;
    @Getter
    public final String               openToken;
    @Getter
    public final String               closeToken;

    public MapVariableTokenHandler(String openToken, String closeToke, Map<String, Object> variables){
        this.variables = variables;
        this.openToken = openToken;
        this.closeToken = closeToke;
    }

    @Override
    public String handleToken(String content) {
        if (variables != null && variables.containsKey(content)) {
            return variables.get(content).toString();
        }
        return openToken + content + closeToken;
    }
}
