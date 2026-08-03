package com.clougence.clouddm.ds.lineage.clickhouse;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicClickHouseLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicClickHouseLineageTextTest(int shardCount, int shardId){
        super("lineage/clickhouse", shardCount, shardId);
    }
}
final class ClickHouseLineageShard1Test extends BasicClickHouseLineageTextTest { ClickHouseLineageShard1Test(){ super(3, 0); } }
final class ClickHouseLineageShard2Test extends BasicClickHouseLineageTextTest { ClickHouseLineageShard2Test(){ super(3, 1); } }
final class ClickHouseLineageShard3Test extends BasicClickHouseLineageTextTest { ClickHouseLineageShard3Test(){ super(3, 2); } }
