package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.token.ArgToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrIntToken extends ArgToken {

    private String value;

    public StrIntToken(String value){
        super(-1);
        this.value = value;
    }

    public StrIntToken(long index){
        super(index);
    }

    public static StrIntToken ofArg(long index) {
        return new StrIntToken(index);
    }

    public static StrIntToken of(String value) {
        return new StrIntToken(value);
    }
}
