package com.clougence.clouddm.ds.split.gauss;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicGaussSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicGaussSplitTextTest(int shardCount, int shardId){
        super("split/gauss", shardCount, shardId);
    }
}

final class GaussSplitShard1Test extends BasicGaussSplitTextTest { GaussSplitShard1Test(){ super(3, 0); } }
final class GaussSplitShard2Test extends BasicGaussSplitTextTest { GaussSplitShard2Test(){ super(3, 1); } }
final class GaussSplitShard3Test extends BasicGaussSplitTextTest { GaussSplitShard3Test(){ super(3, 2); } }
