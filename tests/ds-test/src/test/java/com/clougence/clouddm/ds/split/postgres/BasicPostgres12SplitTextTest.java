package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres12SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres12SplitTextTest(int shardCount, int shardId){
        super("12", PostgresVersion.POSTGRES_12, shardCount, shardId);
    }
}
final class Postgres12SplitShard1Test extends BasicPostgres12SplitTextTest { Postgres12SplitShard1Test(){ super(3, 0); } }
final class Postgres12SplitShard2Test extends BasicPostgres12SplitTextTest { Postgres12SplitShard2Test(){ super(3, 1); } }
final class Postgres12SplitShard3Test extends BasicPostgres12SplitTextTest { Postgres12SplitShard3Test(){ super(3, 2); } }
