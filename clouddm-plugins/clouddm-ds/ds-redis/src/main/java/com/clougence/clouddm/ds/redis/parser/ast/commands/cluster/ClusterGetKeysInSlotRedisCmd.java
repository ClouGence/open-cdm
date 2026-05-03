package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class ClusterGetKeysInSlotRedisCmd extends AbstractRedisCmd {

    private IntToken slot;
    private IntToken count;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_GETKEYSINSLOT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER GETKEYSINSLOT");
        writer.append(" ");
        writer.append(fmtInt(this.slot));
        writer.append(" ");
        writer.append(fmtInt(this.count));
    }
}
