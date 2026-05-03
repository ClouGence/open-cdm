package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.token.ArgToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoolToken extends ArgToken {

    private boolean bool;

    private BoolToken(long argIndex){
        super(argIndex);
    }

    private BoolToken(boolean bool){
        super(-1);
        this.bool = bool;
    }

    public static BoolToken ofArg(long index) {
        return new BoolToken(index);
    }

    public static BoolToken of(boolean value) {
        return new BoolToken(value);
    }

    @Override
    public Object getValue() { return this.bool; }
}
