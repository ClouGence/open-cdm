package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres15SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres15SplitTextTest(int shardCount, int shardId){
        super("15", PostgresVersion.POSTGRES_15, shardCount, shardId);
    }
}
final class Postgres15SplitShard1Test extends BasicPostgres15SplitTextTest { Postgres15SplitShard1Test(){ super(3, 0); } }
final class Postgres15SplitShard2Test extends BasicPostgres15SplitTextTest { Postgres15SplitShard2Test(){ super(3, 1); } }
final class Postgres15SplitShard3Test extends BasicPostgres15SplitTextTest { Postgres15SplitShard3Test(){ super(3, 2); } }
