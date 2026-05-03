package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZRankRedisCmd extends AbstractRedisCmd {

    private StrToken keyName;
    private StrToken member;
    private boolean  withScore;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZRANK; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZRANK");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(member));
        if (withScore) {
            writer.append(" WITHSCORE");
        }
    }
}
