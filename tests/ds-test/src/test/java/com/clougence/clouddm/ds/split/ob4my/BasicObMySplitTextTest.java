package com.clougence.clouddm.ds.split.ob4my;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicObMySplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicObMySplitTextTest(int shardCount, int shardId){
        super("split/ob4my", shardCount, shardId);
    }
}

final class ObMySplitShard1Test extends BasicObMySplitTextTest { ObMySplitShard1Test(){ super(3, 0); } }
final class ObMySplitShard2Test extends BasicObMySplitTextTest { ObMySplitShard2Test(){ super(3, 1); } }
final class ObMySplitShard3Test extends BasicObMySplitTextTest { ObMySplitShard3Test(){ super(3, 2); } }
