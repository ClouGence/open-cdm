package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres13SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres13SplitTextTest(int shardCount, int shardId){
        super("13", PostgresVersion.POSTGRES_13, shardCount, shardId);
    }
}
final class Postgres13SplitShard1Test extends BasicPostgres13SplitTextTest { Postgres13SplitShard1Test(){ super(3, 0); } }
final class Postgres13SplitShard2Test extends BasicPostgres13SplitTextTest { Postgres13SplitShard2Test(){ super(3, 1); } }
final class Postgres13SplitShard3Test extends BasicPostgres13SplitTextTest { Postgres13SplitShard3Test(){ super(3, 2); } }
