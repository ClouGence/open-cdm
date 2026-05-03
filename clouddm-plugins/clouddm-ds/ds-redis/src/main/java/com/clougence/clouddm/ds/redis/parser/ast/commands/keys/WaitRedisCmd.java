package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class WaitRedisCmd extends AbstractRedisCmd {

    private final IntToken numReplicas;
    private final IntToken timeout;

    public WaitRedisCmd(IntToken numReplicas, IntToken timeout){
        this.numReplicas = numReplicas;
        this.timeout = timeout;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.WAIT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // WAIT numreplicas timeout

        writer.append("WAIT");
        writer.append(" ");
        writer.append(fmtInt(numReplicas));
        writer.append(" ");
        writer.append(fmtInt(timeout));
    }
}
