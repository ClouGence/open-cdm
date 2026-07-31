package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public abstract class BasicPostgres18SplitTextTest extends BasicPostgresSplitTextTest {
    protected BasicPostgres18SplitTextTest(int shardCount, int shardId){
        super("18", PostgresVersion.POSTGRES_18, shardCount, shardId);
    }
}
final class Postgres18SplitShard1Test extends BasicPostgres18SplitTextTest { Postgres18SplitShard1Test(){ super(3, 0); } }
final class Postgres18SplitShard2Test extends BasicPostgres18SplitTextTest { Postgres18SplitShard2Test(){ super(3, 1); } }
final class Postgres18SplitShard3Test extends BasicPostgres18SplitTextTest { Postgres18SplitShard3Test(){ super(3, 2); } }
