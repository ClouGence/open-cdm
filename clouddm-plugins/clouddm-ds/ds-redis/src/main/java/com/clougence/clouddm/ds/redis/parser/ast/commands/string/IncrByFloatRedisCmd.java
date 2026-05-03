package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.FloatToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class IncrByFloatRedisCmd extends AbstractRedisCmd {

    private final StrToken   keyName;
    private final FloatToken increment;

    public IncrByFloatRedisCmd(StrToken keyName, FloatToken increment){
        this.keyName = keyName;
        this.increment = increment;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.INCRBYFLOAT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // INCRBYFLOAT key increment

        writer.append("INCRBYFLOAT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtFloat(increment));
    }
}
