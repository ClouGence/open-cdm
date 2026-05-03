package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.FloatToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HIncrByRedisCmd extends AbstractRedisCmd {

    private StrToken   keyName;
    private StrToken   filedName;
    private FloatToken floatValue;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HINCRBY; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HINCRBY");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        writer.append(" ");
        writer.append(fmtString(this.filedName));
        writer.append(" ");
        writer.append(fmtFloat(this.floatValue));
    }
}
