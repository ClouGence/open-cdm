package com.clougence.clouddm.ds.lineage.sql92;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicSql92LineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicSql92LineageTextTest(int shardCount, int shardId){
        super("lineage/sql92", shardCount, shardId);
    }
}
final class Sql92LineageShard1Test extends BasicSql92LineageTextTest { Sql92LineageShard1Test(){ super(3, 0); } }
final class Sql92LineageShard2Test extends BasicSql92LineageTextTest { Sql92LineageShard2Test(){ super(3, 1); } }
final class Sql92LineageShard3Test extends BasicSql92LineageTextTest { Sql92LineageShard3Test(){ super(3, 2); } }
