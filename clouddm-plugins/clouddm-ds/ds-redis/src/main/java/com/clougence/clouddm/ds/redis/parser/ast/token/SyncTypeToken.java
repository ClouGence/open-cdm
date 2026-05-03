package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyncTypeToken extends AbstractRedisAst {

    private SyncOptionType optionType;

    public SyncTypeToken(SyncOptionType optionType){
        this.optionType = optionType;
    }

    public static enum SyncOptionType {
        Sync,
        Async,
    }
}
