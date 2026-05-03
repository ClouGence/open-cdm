package com.clougence.clouddm.ds.redis.parser.ast.commands.set;

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
public class SMoveRedisCmd extends AbstractRedisCmd {

    private StrToken src;
    private StrToken dst;
    private StrToken member;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SMOVE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SMOVE");
        writer.append(" ");
        writer.append(fmtString(this.src));
        writer.append(" ");
        writer.append(fmtString(this.dst));
        writer.append(" ");
        writer.append(fmtString(this.member));
    }
}
