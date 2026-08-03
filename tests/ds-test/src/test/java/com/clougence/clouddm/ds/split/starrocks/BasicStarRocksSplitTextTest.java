package com.clougence.clouddm.ds.split.starrocks;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicStarRocksSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicStarRocksSplitTextTest(int shardCount, int shardId){
        super("split/starrocks", shardCount, shardId);
    }
}

final class StarRocksSplitShard1Test extends BasicStarRocksSplitTextTest { StarRocksSplitShard1Test(){ super(3, 0); } }
final class StarRocksSplitShard2Test extends BasicStarRocksSplitTextTest { StarRocksSplitShard2Test(){ super(3, 1); } }
final class StarRocksSplitShard3Test extends BasicStarRocksSplitTextTest { StarRocksSplitShard3Test(){ super(3, 2); } }
