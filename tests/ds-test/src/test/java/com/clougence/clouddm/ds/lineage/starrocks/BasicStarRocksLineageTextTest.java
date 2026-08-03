package com.clougence.clouddm.ds.lineage.starrocks;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicStarRocksLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicStarRocksLineageTextTest(int shardCount, int shardId){
        super("lineage/starrocks", shardCount, shardId);
    }
}
final class StarRocksLineageShard1Test extends BasicStarRocksLineageTextTest { StarRocksLineageShard1Test(){ super(3, 0); } }
final class StarRocksLineageShard2Test extends BasicStarRocksLineageTextTest { StarRocksLineageShard2Test(){ super(3, 1); } }
final class StarRocksLineageShard3Test extends BasicStarRocksLineageTextTest { StarRocksLineageShard3Test(){ super(3, 2); } }
