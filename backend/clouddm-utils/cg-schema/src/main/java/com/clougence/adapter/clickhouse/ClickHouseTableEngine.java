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
package com.clougence.adapter.clickhouse;

import com.clougence.utils.StringUtils;

import lombok.Getter;

/**
 * @author bucketli 2021/8/14 05:37
 */
public enum ClickHouseTableEngine {

    // merge tree
    MergeTree("MergeTree"),

    CollapsingMergeTree("CollapsingMergeTree"),

    SummingMergeTree("SummingMergeTree"),

    AggregatingMergeTree("AggregatingMergeTree"),

    VersionedCollapsingMergeTree("CollapsingMergeTree"),

    GraphiteMergeTree("GraphiteMergeTree"),

    ReplacingMergeTree("ReplacingMergeTree"),

    ReplicatedMergeTree("ReplicatedMergeTree"),

    ReplicatedReplacingMergeTree("ReplicatedReplacingMergeTree"),

    // log
    Log("Log"),

    TinyLog("TinyLog"),

    StripeLog("StripeLog"),

    // integration
    Kafka("Kafka"),

    MySQL("MySQL"),

    ODBC("ODBC"),

    JDBC("JDBC"),

    HDFS("HDFS"),

    // other
    Distributed("Distributed"),

    MaterializedView("MaterializedView"),

    Dictionary("Dictionary"),

    Merge("Merge"),

    File("File"),

    Null("Null"),

    Set("Set"),

    Join("Join"),

    URL("URL"),

    View("View"),

    Memory("Memory"),

    Buffer("Buffer"),

    // ali yun
    SharedMergeTree("SharedMergeTree"),

    SharedCollapsingMergeTree("SharedCollapsingMergeTree"),

    SharedSummingMergeTree("SharedSummingMergeTree"),

    SharedAggregatingMergeTree("SharedAggregatingMergeTree"),

    SharedVersionedCollapsingMergeTree("SharedCollapsingMergeTree"),

    SharedGraphiteMergeTree("SharedGraphiteMergeTree"),

    SharedReplacingMergeTree("SharedReplacingMergeTree"),

    SharedReplicatedMergeTree("SharedReplicatedMergeTree"),

    SharedReplicatedReplacingMergeTree("SharedReplicatedReplacingMergeTree"),;

    @Getter
    private final String expr;

    ClickHouseTableEngine(String expr){
        this.expr = expr;
    }

    public static ClickHouseTableEngine valueOfCode(String engineType) {
        for (ClickHouseTableEngine engineItem : ClickHouseTableEngine.values()) {
            if (StringUtils.equalsIgnoreCase(engineItem.expr, engineType)) {
                return engineItem;
            }
        }
        return null;
    }

    public static boolean isSupportedSelectFinal(String engineType) {
        if (StringUtils.isBlank(engineType)) {
            return false;
        }
        return ReplacingMergeTree.expr.equalsIgnoreCase(engineType) || SummingMergeTree.expr.equalsIgnoreCase(engineType) || AggregatingMergeTree.expr.equalsIgnoreCase(engineType)
               || CollapsingMergeTree.expr.equalsIgnoreCase(engineType) || VersionedCollapsingMergeTree.expr.equalsIgnoreCase(engineType);
    }
}
