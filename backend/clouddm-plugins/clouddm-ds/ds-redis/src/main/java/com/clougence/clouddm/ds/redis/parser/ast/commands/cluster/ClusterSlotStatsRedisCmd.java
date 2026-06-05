/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
