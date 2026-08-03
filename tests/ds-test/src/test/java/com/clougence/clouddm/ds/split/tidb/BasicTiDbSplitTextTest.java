package com.clougence.clouddm.ds.split.tidb;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicTiDbSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicTiDbSplitTextTest(int shardCount, int shardId){
        super("split/tidb", shardCount, shardId);
    }
}

final class TiDbSplitShard1Test extends BasicTiDbSplitTextTest { TiDbSplitShard1Test(){ super(3, 0); } }
final class TiDbSplitShard2Test extends BasicTiDbSplitTextTest { TiDbSplitShard2Test(){ super(3, 1); } }
final class TiDbSplitShard3Test extends BasicTiDbSplitTextTest { TiDbSplitShard3Test(){ super(3, 2); } }
