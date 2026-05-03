package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.OrderType;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class ClusterSlotStatsRedisCmd extends AbstractRedisCmd {

    // type 1
    private IntToken   startNum;
    private IntToken   endNum;

    // type 2
    private IntToken   limit;
    private MetricType metric;
    private OrderType  order;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_SLOT_STATS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER SLOT-STATS");
        if (startNum != null && endNum != null) {
            writer.append(" SLOTSRANGE ");
            writer.append(fmtInt(startNum));
            writer.append(" ");
            writer.append(fmtInt(endNum));
        } else {
            writer.append(" ORDERBY ");
            writer.append(metric.name());
            if (limit != null) {
                writer.append(" LIMIT ");
                writer.append(fmtInt(limit));
            }
            if (order != null) {
                writer.append(" ");
                writer.append(order.name());
            }
        }
    }

    public static enum MetricType {
        SLOT,
        MIGRATING,
        IMPORTING,
        STABLE
    }
}
