package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-07 10:56
 */
@Getter
@Setter
public abstract class ArgToken extends AbstractRedisAst {

    private long argIndex;

    public ArgToken(long argIndex){
        this.argIndex = argIndex;
    }

    public boolean isArg() { return argIndex >= 0; }

    public abstract Object getValue();
}
