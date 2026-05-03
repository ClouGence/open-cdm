package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class WaitAOFRedisCmd extends AbstractRedisCmd {

    private final IntToken numLocal;
    private final IntToken numReplicas;
    private final IntToken timeout;

    public WaitAOFRedisCmd(IntToken numLocal, IntToken numReplicas, IntToken timeout){
        this.numLocal = numLocal;
        this.numReplicas = numReplicas;
        this.timeout = timeout;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.WAITAOF; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("WAITAOF");
        writer.append(" ");
        writer.append(fmtInt(numLocal));
        writer.append(" ");
        writer.append(fmtInt(numReplicas));
        writer.append(" ");
        writer.append(fmtInt(timeout));
    }
}
