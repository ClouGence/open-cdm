package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import lombok.Getter;

@Getter
public class KeyOptToken extends AbstractRedisAst {

    private final KeyOptionType optionType;

    public KeyOptToken(KeyOptionType optionType){
        this.optionType = optionType;
    }

    public static enum KeyOptionType {
        NX,
        XX,
        GT,
        LT
    }

}
