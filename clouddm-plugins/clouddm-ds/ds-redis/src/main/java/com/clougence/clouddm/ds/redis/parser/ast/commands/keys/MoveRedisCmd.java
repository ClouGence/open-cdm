package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class MoveRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken destDatabase;

    public MoveRedisCmd(StrToken keyName, IntToken destDatabase){
        this.keyName = keyName;
        this.destDatabase = destDatabase;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.MOVE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // MOVE key db

        writer.append("MOVE ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(destDatabase));
    }
}
