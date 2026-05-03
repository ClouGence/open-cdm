package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
public class ClusterMyShardIdRedisCmd extends AbstractRedisCmd {

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_MYSHARDID; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER MYSHARDID");
    }
}
