package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres14SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres14SplitTextTest(int shardCount, int shardId){
        super("14", PostgresVersion.POSTGRES_14, shardCount, shardId);
    }
}
final class Postgres14SplitShard1Test extends BasicPostgres14SplitTextTest { Postgres14SplitShard1Test(){ super(3, 0); } }
final class Postgres14SplitShard2Test extends BasicPostgres14SplitTextTest { Postgres14SplitShard2Test(){ super(3, 1); } }
final class Postgres14SplitShard3Test extends BasicPostgres14SplitTextTest { Postgres14SplitShard3Test(){ super(3, 2); } }
