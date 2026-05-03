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
