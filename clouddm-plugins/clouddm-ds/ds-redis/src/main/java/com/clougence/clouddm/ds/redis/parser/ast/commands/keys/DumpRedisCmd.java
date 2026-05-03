package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class DumpRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;

    public DumpRedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.DUMP; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("DUMP ");
        writer.append(fmtString(keyName));
    }
}
