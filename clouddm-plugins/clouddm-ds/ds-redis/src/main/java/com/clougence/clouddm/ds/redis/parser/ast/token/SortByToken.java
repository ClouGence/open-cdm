package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import com.clougence.clouddm.ds.redis.parser.ast.token.OrderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortByToken extends AbstractRedisAst {

    private com.clougence.clouddm.ds.redis.parser.ast.token.OrderType optionType;

    public SortByToken(OrderType optionType){
        this.optionType = optionType;
    }

}
