package com.clougence.clouddm.ds.redis.parser.ast.token;

import java.math.BigInteger;

import com.clougence.clouddm.ds.redis.parser.ast.token.ArgToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntToken extends ArgToken {

    private BigInteger bigInteger;

    private IntToken(long argIndex){
        super(argIndex);
    }

    private IntToken(BigInteger value){
        super(-1);
        this.bigInteger = value;
    }

    public static IntToken ofArg(long index) {
        return new IntToken(index);
    }

    public static IntToken of(BigInteger value) {
        return new IntToken(value);
    }

    @Override
    public Object getValue() { return this.bigInteger; }
}
