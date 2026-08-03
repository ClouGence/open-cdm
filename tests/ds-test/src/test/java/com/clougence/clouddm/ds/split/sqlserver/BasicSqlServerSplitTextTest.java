package com.clougence.clouddm.ds.split.sqlserver;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicSqlServerSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicSqlServerSplitTextTest(int shardCount, int shardId){
        super("split/sqlserver", shardCount, shardId);
    }
}

final class SqlServerSplitShard1Test extends BasicSqlServerSplitTextTest { SqlServerSplitShard1Test(){ super(3, 0); } }
final class SqlServerSplitShard2Test extends BasicSqlServerSplitTextTest { SqlServerSplitShard2Test(){ super(3, 1); } }
final class SqlServerSplitShard3Test extends BasicSqlServerSplitTextTest { SqlServerSplitShard3Test(){ super(3, 2); } }
