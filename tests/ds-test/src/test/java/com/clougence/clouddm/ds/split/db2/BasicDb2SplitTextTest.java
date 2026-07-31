package com.clougence.clouddm.ds.split.db2;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicDb2SplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicDb2SplitTextTest(int shardCount, int shardId){
        super("split/db2", shardCount, shardId);
    }
}

final class Db2SplitShard1Test extends BasicDb2SplitTextTest { Db2SplitShard1Test(){ super(3, 0); } }
final class Db2SplitShard2Test extends BasicDb2SplitTextTest { Db2SplitShard2Test(){ super(3, 1); } }
final class Db2SplitShard3Test extends BasicDb2SplitTextTest { Db2SplitShard3Test(){ super(3, 2); } }
