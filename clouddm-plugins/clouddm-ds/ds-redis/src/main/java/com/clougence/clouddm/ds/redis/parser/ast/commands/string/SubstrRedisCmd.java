package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class SubstrRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken start;
    private final IntToken end;

    public SubstrRedisCmd(StrToken keyName, IntToken start, IntToken end){
        this.keyName = keyName;
        this.start = start;
        this.end = end;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SUBSTR; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SUBSTR key start end

        writer.append("SUBSTR");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(start));
        writer.append(" ");
        writer.append(fmtInt(end));
    }
}
