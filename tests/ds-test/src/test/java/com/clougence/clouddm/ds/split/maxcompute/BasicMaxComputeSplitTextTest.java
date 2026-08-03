package com.clougence.clouddm.ds.split.maxcompute;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicMaxComputeSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicMaxComputeSplitTextTest(int shardCount, int shardId){
        super("split/maxcompute", shardCount, shardId);
    }
}

final class MaxComputeSplitShard1Test extends BasicMaxComputeSplitTextTest { MaxComputeSplitShard1Test(){ super(3, 0); } }
final class MaxComputeSplitShard2Test extends BasicMaxComputeSplitTextTest { MaxComputeSplitShard2Test(){ super(3, 1); } }
final class MaxComputeSplitShard3Test extends BasicMaxComputeSplitTextTest { MaxComputeSplitShard3Test(){ super(3, 2); } }
