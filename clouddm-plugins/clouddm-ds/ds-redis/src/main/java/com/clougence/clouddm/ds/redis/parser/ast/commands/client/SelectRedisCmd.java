package com.clougence.clouddm.ds.redis.parser.ast.commands.client;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectRedisCmd extends AbstractRedisCmd {

    private IntToken database;

    public SelectRedisCmd(IntToken database){
        this.database = database;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SELECT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SELECT ");
        writer.append(fmtInt(database));
    }
}
