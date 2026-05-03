package com.clougence.clouddm.ds.redis.parser.ast.commands.bit;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.BoolToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class SetBitRedisCmd extends AbstractRedisCmd {

    private final StrToken  keyName;
    private final IntToken  offset;
    private final BoolToken value;

    public SetBitRedisCmd(StrToken keyName, IntToken offset, BoolToken value){
        this.keyName = keyName;
        this.offset = offset;
        this.value = value;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SETBIT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SETBIT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(offset));
        writer.append(" ");
        writer.append(fmtBool(value));
    }
}
