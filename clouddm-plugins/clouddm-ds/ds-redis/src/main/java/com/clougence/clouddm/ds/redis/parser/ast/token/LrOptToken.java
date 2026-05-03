package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import lombok.Getter;

@Getter
public class LrOptToken extends AbstractRedisAst {

    private final LrOptionType optionType;

    public LrOptToken(LrOptionType optionType){
        this.optionType = optionType;
    }

    public static enum LrOptionType {
        Left,
        Right,
    }

}
