package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class ClusterFailOverRedisCmd extends AbstractRedisCmd {

    private Type type;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_FAILOVER; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER FAILOVER");
        if (type != null) {
            writer.append(" ");
            switch (type) {
                case FORCE:
                    writer.append("FORCE");
                    break;
                case TAKEOVER:
                    writer.append("TAKEOVER");
                    break;
            }
        }
    }

    public enum Type {
        FORCE,
        TAKEOVER
    }
}
