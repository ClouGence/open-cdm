package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres16SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres16SplitTextTest(int shardCount, int shardId){
        super("16", PostgresVersion.POSTGRES_16, shardCount, shardId);
    }
}
final class Postgres16SplitShard1Test extends BasicPostgres16SplitTextTest { Postgres16SplitShard1Test(){ super(3, 0); } }
final class Postgres16SplitShard2Test extends BasicPostgres16SplitTextTest { Postgres16SplitShard2Test(){ super(3, 1); } }
final class Postgres16SplitShard3Test extends BasicPostgres16SplitTextTest { Postgres16SplitShard3Test(){ super(3, 2); } }
