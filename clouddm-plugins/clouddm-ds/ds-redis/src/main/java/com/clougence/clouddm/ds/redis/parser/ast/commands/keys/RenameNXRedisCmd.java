package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class RenameNXRedisCmd extends AbstractRedisCmd {

    private final StrToken oldKey;
    private final StrToken newKey;

    public RenameNXRedisCmd(StrToken oldKey, StrToken newKey){
        this.oldKey = oldKey;
        this.newKey = newKey;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.RENAMENX; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // RENAMENX key newkey

        writer.append("RENAMENX");
        writer.append(" ");
        writer.append(fmtString(oldKey));
        writer.append(" ");
        writer.append(fmtString(newKey));
    }
}
