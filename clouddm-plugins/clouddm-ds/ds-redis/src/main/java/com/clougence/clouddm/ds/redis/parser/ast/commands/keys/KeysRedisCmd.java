package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class KeysRedisCmd extends AbstractRedisCmd {

    private final StrToken pattern;

    public KeysRedisCmd(StrToken pattern){
        this.pattern = pattern;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.KEYS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // KEYS pattern
        writer.write("KEYS ");
        writer.write(fmtString(pattern));
    }
}
