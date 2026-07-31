package com.clougence.clouddm.ds.split.sql99;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicSql99SplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicSql99SplitTextTest(int shardCount, int shardId){
        super("split/sql99", shardCount, shardId);
    }
}

final class Sql99SplitShard1Test extends BasicSql99SplitTextTest { Sql99SplitShard1Test(){ super(3, 0); } }
final class Sql99SplitShard2Test extends BasicSql99SplitTextTest { Sql99SplitShard2Test(){ super(3, 1); } }
final class Sql99SplitShard3Test extends BasicSql99SplitTextTest { Sql99SplitShard3Test(){ super(3, 2); } }
