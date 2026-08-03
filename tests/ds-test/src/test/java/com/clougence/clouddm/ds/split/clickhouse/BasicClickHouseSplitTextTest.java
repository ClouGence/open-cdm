package com.clougence.clouddm.ds.split.clickhouse;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicClickHouseSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicClickHouseSplitTextTest(int shardCount, int shardId){
        super("split/clickhouse", shardCount, shardId);
    }
}

final class ClickHouseSplitShard1Test extends BasicClickHouseSplitTextTest { ClickHouseSplitShard1Test(){ super(3, 0); } }
final class ClickHouseSplitShard2Test extends BasicClickHouseSplitTextTest { ClickHouseSplitShard2Test(){ super(3, 1); } }
final class ClickHouseSplitShard3Test extends BasicClickHouseSplitTextTest { ClickHouseSplitShard3Test(){ super(3, 2); } }
