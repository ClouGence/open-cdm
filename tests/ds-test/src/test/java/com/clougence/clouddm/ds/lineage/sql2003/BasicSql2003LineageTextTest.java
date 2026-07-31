package com.clougence.clouddm.ds.lineage.sql2003;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicSql2003LineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicSql2003LineageTextTest(int shardCount, int shardId){
        super("lineage/sql2003", shardCount, shardId);
    }
}
final class Sql2003LineageShard1Test extends BasicSql2003LineageTextTest { Sql2003LineageShard1Test(){ super(3, 0); } }
final class Sql2003LineageShard2Test extends BasicSql2003LineageTextTest { Sql2003LineageShard2Test(){ super(3, 1); } }
final class Sql2003LineageShard3Test extends BasicSql2003LineageTextTest { Sql2003LineageShard3Test(){ super(3, 2); } }
