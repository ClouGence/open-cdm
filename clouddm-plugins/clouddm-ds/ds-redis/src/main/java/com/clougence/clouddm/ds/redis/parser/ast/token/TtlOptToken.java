package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TtlOptToken extends AbstractRedisAst {

    private com.clougence.clouddm.ds.redis.parser.ast.token.IntToken value;
    private TtlType                                          ttlType;

    public TtlOptToken(IntToken value, TtlType ttlType){
        this.value = value;
        this.ttlType = ttlType;
    }

    public static enum TtlType {
        EX,
        PX,
        EXAT,
        PXAT,
    }
}
