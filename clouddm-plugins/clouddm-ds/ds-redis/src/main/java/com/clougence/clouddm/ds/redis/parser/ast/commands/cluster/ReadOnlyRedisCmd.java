package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

/**
 * @Author: Ekko
 * @Date: 2023-06-06 14:34
 */
public class ReadOnlyRedisCmd extends AbstractRedisCmd {

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.READONLY; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("READONLY");
    }
}
