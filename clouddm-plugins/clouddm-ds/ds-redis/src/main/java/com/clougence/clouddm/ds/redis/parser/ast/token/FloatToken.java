package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.token.ArgToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloatToken extends ArgToken {

    private Double floatValue;
    private String floatValueStr;

    FloatToken(long argIndex){
        super(argIndex);
    }

    FloatToken(Double value, String string){
        super(-1);
        this.floatValue = value;
        this.floatValueStr = string;
    }

    public static FloatToken ofArg(long index) {
        return new FloatToken(index);
    }

    public static FloatToken of(Double value, String string) {
        return new FloatToken(value, string);
    }

    @Override
    public Object getValue() { return this.floatValue; }
}
