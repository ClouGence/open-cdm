package com.clougence.clouddm.ds.lineage.sql99;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicSql99LineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicSql99LineageTextTest(int shardCount, int shardId){
        super("lineage/sql99", shardCount, shardId);
    }
}
final class Sql99LineageShard1Test extends BasicSql99LineageTextTest { Sql99LineageShard1Test(){ super(3, 0); } }
final class Sql99LineageShard2Test extends BasicSql99LineageTextTest { Sql99LineageShard2Test(){ super(3, 1); } }
final class Sql99LineageShard3Test extends BasicSql99LineageTextTest { Sql99LineageShard3Test(){ super(3, 2); } }
