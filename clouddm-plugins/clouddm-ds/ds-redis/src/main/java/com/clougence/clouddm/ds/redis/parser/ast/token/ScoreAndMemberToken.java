package com.clougence.clouddm.ds.redis.parser.ast.token;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;

import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreAndMemberToken extends AbstractRedisAst {

    private com.clougence.clouddm.ds.redis.parser.ast.token.IntToken score;
    private com.clougence.clouddm.ds.redis.parser.ast.token.StrToken member;

    public ScoreAndMemberToken(IntToken score, StrToken member){
        this.score = score;
        this.member = member;
    }
}
