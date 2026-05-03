package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class DecrRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;

    public DecrRedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.DECR; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // DECR key
        writer.append("DECR ");
        writer.append(fmtString(keyName));
    }
}
