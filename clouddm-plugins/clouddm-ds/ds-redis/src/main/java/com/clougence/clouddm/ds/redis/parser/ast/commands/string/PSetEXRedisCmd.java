package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class PSetEXRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken timeoutMs;
    private final StrToken value;

    public PSetEXRedisCmd(StrToken keyName, IntToken timeoutMs, StrToken value){
        this.keyName = keyName;
        this.timeoutMs = timeoutMs;
        this.value = value;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PSETEX; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // PSETEX key milliseconds value

        writer.append("PSETEX");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(timeoutMs));
        writer.append(" ");
        writer.append(fmtString(value));
    }
}
