package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class GetSetRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final StrToken value;

    public GetSetRedisCmd(StrToken keyName, StrToken value){
        this.keyName = keyName;
        this.value = value;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.GETSET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // GETSET key value

        writer.append("GETSET");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(value));
    }
}
