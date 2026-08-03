package com.clougence.clouddm.ds.split.ob4ora;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicObOracleSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicObOracleSplitTextTest(int shardCount, int shardId){
        super("split/ob4ora", shardCount, shardId);
    }
}

final class ObOracleSplitShard1Test extends BasicObOracleSplitTextTest { ObOracleSplitShard1Test(){ super(3, 0); } }
final class ObOracleSplitShard2Test extends BasicObOracleSplitTextTest { ObOracleSplitShard2Test(){ super(3, 1); } }
final class ObOracleSplitShard3Test extends BasicObOracleSplitTextTest { ObOracleSplitShard3Test(){ super(3, 2); } }
