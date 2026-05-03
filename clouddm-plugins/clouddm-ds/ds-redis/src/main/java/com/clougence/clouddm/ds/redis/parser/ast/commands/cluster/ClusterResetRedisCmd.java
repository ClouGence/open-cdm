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
public class ClusterResetRedisCmd extends AbstractRedisCmd {

    private Type type;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_RESET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER RESET");
        if (this.type != null) {
            writer.append(" ");
            writer.append(this.type.name().toUpperCase());
        }
    }

    public enum Type {
        HARD,
        SOFT,
    }

}
