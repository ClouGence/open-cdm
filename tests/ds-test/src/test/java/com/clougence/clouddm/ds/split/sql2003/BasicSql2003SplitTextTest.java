package com.clougence.clouddm.ds.split.sql2003;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicSql2003SplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicSql2003SplitTextTest(int shardCount, int shardId){
        super("split/sql2003", shardCount, shardId);
    }
}

final class Sql2003SplitShard1Test extends BasicSql2003SplitTextTest { Sql2003SplitShard1Test(){ super(3, 0); } }
final class Sql2003SplitShard2Test extends BasicSql2003SplitTextTest { Sql2003SplitShard2Test(){ super(3, 1); } }
final class Sql2003SplitShard3Test extends BasicSql2003SplitTextTest { Sql2003SplitShard3Test(){ super(3, 2); } }
