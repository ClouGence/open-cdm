package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres17SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres17SplitTextTest(int shardCount, int shardId){
        super("17", PostgresVersion.POSTGRES_17, shardCount, shardId);
    }
}
final class Postgres17SplitShard1Test extends BasicPostgres17SplitTextTest { Postgres17SplitShard1Test(){ super(3, 0); } }
final class Postgres17SplitShard2Test extends BasicPostgres17SplitTextTest { Postgres17SplitShard2Test(){ super(3, 1); } }
final class Postgres17SplitShard3Test extends BasicPostgres17SplitTextTest { Postgres17SplitShard3Test(){ super(3, 2); } }
