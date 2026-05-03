package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.token.ArgToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrToken extends ArgToken {

    private String  value;
    private boolean quotes;

    private StrToken(long argIndex){
        super(argIndex);
    }

    private StrToken(String value, boolean quotes){
        super(-1);
        this.value = value;
        this.quotes = quotes;
    }

    public static StrToken ofArg(long index) {
        return new StrToken(index);
    }

    public static StrToken of(String value, boolean quotes) {
        return new StrToken(value, quotes);
    }
}
