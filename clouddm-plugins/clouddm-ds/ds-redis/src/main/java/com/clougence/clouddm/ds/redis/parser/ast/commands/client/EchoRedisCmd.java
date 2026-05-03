package com.clougence.clouddm.ds.redis.parser.ast.commands.client;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EchoRedisCmd extends AbstractRedisCmd {

    private final StrToken message;

    public EchoRedisCmd(StrToken message){
        this.message = message;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ECHO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ECHO ");
        writer.append(fmtString(message));
    }
}
