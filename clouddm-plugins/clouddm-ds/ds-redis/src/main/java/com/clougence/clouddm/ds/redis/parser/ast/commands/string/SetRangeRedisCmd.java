package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class SetRangeRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken offset;
    private final StrToken value;

    public SetRangeRedisCmd(StrToken keyName, IntToken offset, StrToken value){
        this.keyName = keyName;
        this.offset = offset;
        this.value = value;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SETRANGE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SETRANGE key offset value

        writer.append("SETRANGE");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(offset));
        writer.append(" ");
        writer.append(fmtString(value));
    }
}
