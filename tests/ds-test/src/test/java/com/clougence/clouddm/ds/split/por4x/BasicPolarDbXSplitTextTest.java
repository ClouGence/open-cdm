package com.clougence.clouddm.ds.split.por4x;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicPolarDbXSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicPolarDbXSplitTextTest(int shardCount, int shardId){
        super("split/por4x", shardCount, shardId);
    }
}

final class PolarDbXSplitShard1Test extends BasicPolarDbXSplitTextTest { PolarDbXSplitShard1Test(){ super(3, 0); } }
final class PolarDbXSplitShard2Test extends BasicPolarDbXSplitTextTest { PolarDbXSplitShard2Test(){ super(3, 1); } }
final class PolarDbXSplitShard3Test extends BasicPolarDbXSplitTextTest { PolarDbXSplitShard3Test(){ super(3, 2); } }
