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
public class ClusterMeetRedisCmd extends AbstractRedisCmd {

    private StrToken ip;
    private IntToken port;
    private IntToken clusterBusPort;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_MEET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER MEET");
        writer.append(" ");
        writer.append(fmtString(this.ip));
        writer.append(" ");
        writer.append(fmtInt(this.port));
        if (this.clusterBusPort != null) {
            writer.append(" ");
            writer.append(fmtInt(this.clusterBusPort));
        }
    }
}
