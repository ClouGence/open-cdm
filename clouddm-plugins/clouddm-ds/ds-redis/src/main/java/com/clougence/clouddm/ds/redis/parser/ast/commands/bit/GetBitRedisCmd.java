package com.clougence.clouddm.ds.redis.parser.ast.commands.bit;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class GetBitRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken offset;

    public GetBitRedisCmd(StrToken keyName, IntToken offset){
        this.keyName = keyName;
        this.offset = offset;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.GETBIT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("GETBIT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(offset));
    }
}
