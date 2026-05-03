package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyAndStringToken extends AbstractRedisAst {

    private com.clougence.clouddm.ds.redis.parser.ast.token.StrToken keyName;
    private com.clougence.clouddm.ds.redis.parser.ast.token.StrToken stringValue;

    public KeyAndStringToken(com.clougence.clouddm.ds.redis.parser.ast.token.StrToken keyName, StrToken stringValue){
        this.keyName = keyName;
        this.stringValue = stringValue;
    }
}
