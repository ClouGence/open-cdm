package com.clougence.clouddm.ds.lineage.postgres;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicPostgresLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicPostgresLineageTextTest(int shardCount, int shardId){
        super("lineage/postgres", shardCount, shardId);
    }
}
final class PostgresLineageShard1Test extends BasicPostgresLineageTextTest { PostgresLineageShard1Test(){ super(3, 0); } }
final class PostgresLineageShard2Test extends BasicPostgresLineageTextTest { PostgresLineageShard2Test(){ super(3, 1); } }
final class PostgresLineageShard3Test extends BasicPostgresLineageTextTest { PostgresLineageShard3Test(){ super(3, 2); } }
