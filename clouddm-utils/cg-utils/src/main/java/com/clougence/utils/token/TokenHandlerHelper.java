package com.clougence.utils.token;

import java.util.function.Function;

import lombok.Getter;

@Getter
public class TokenHandlerHelper implements TokenHandler {

    private final String                   openToken;
    private final String                   closeToken;
    private final Function<String, String> applyContext;

    public TokenHandlerHelper(String openToken, String closeToken, Function<String, String> applyContext){
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.applyContext = applyContext;
    }

    @Override
    public String handleToken(String var1) {
        return this.applyContext.apply(var1);
    }
}
