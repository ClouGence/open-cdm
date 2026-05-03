package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class ClusterSetSlotRedisCmd extends AbstractRedisCmd {

    private IntToken slot;
    private Type     type;
    private StrToken nodeId;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_SETSLOT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER SETSLOT");
        writer.append(" ");
        writer.append(fmtInt(this.slot));
        switch (this.type) {
            case IMPORTING:
            case MIGRATING:
            case NODE:
                writer.append(" ");
                writer.append(this.type.name().toUpperCase());
                writer.append(" ");
                writer.append(fmtString(this.nodeId));
                break;
            case STABLE:
                writer.append(" ");
                writer.append("STABLE");
                break;
        }
    }

    public enum Type {
        IMPORTING,
        MIGRATING,
        NODE,
        STABLE
    }
}
