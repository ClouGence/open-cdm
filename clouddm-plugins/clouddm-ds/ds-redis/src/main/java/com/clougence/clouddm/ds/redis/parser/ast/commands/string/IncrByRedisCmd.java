package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class IncrByRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken increment;

    public IncrByRedisCmd(StrToken keyName, IntToken increment){
        this.keyName = keyName;
        this.increment = increment;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.INCRBY; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // INCRBY key increment

        writer.append("INCRBY");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(increment));
    }
}
