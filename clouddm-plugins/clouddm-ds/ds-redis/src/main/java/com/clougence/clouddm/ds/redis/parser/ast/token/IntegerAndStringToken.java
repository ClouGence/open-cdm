package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegerAndStringToken extends AbstractRedisAst {

    private final com.clougence.clouddm.ds.redis.parser.ast.token.IntToken intValue;
    private final com.clougence.clouddm.ds.redis.parser.ast.token.StrToken stringValue;

    public IntegerAndStringToken(IntToken intValue, StrToken stringValue){
        this.intValue = intValue;
        this.stringValue = stringValue;
    }
}
