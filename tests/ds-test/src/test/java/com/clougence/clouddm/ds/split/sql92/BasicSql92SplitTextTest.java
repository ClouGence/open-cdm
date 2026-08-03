package com.clougence.clouddm.ds.split.sql92;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicSql92SplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicSql92SplitTextTest(int shardCount, int shardId){
        super("split/sql92", shardCount, shardId);
    }
}

final class Sql92SplitShard1Test extends BasicSql92SplitTextTest { Sql92SplitShard1Test(){ super(3, 0); } }
final class Sql92SplitShard2Test extends BasicSql92SplitTextTest { Sql92SplitShard2Test(){ super(3, 1); } }
final class Sql92SplitShard3Test extends BasicSql92SplitTextTest { Sql92SplitShard3Test(){ super(3, 2); } }
