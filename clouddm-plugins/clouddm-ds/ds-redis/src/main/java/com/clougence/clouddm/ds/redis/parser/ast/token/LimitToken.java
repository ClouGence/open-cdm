package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import lombok.Getter;

@Getter
public class LimitToken extends AbstractRedisAst {

    private final com.clougence.clouddm.ds.redis.parser.ast.token.IntToken offsetToken;
    private final com.clougence.clouddm.ds.redis.parser.ast.token.IntToken countToken;

    public LimitToken(com.clougence.clouddm.ds.redis.parser.ast.token.IntToken offsetToken, IntToken countToken){
        this.offsetToken = offsetToken;
        this.countToken = countToken;
    }
}
