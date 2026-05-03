package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class DecrByRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken decrement;

    public DecrByRedisCmd(StrToken keyName, IntToken decrement){
        this.keyName = keyName;
        this.decrement = decrement;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.DECRBY; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // DECRBY key decrement

        writer.append("DECRBY");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(decrement));
    }
}
