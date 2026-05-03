package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class RandomKeyRedisCmd extends AbstractRedisCmd {

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.RANDOMKEY; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // RANDOMKEY

        writer.append("RANDOMKEY");
    }
}
