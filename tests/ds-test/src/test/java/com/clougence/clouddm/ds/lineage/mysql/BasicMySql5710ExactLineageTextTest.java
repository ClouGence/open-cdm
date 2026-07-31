package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql5710ExactLineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql5710ExactLineageTextTest(int shardCount, int shardId){
        super("5.7/exact-5.7.10", "5.7.10", shardCount, shardId);
    }
}
final class MySql5710ExactLineageShard1Test extends BasicMySql5710ExactLineageTextTest { MySql5710ExactLineageShard1Test(){ super(3, 0); } }
final class MySql5710ExactLineageShard2Test extends BasicMySql5710ExactLineageTextTest { MySql5710ExactLineageShard2Test(){ super(3, 1); } }
final class MySql5710ExactLineageShard3Test extends BasicMySql5710ExactLineageTextTest { MySql5710ExactLineageShard3Test(){ super(3, 2); } }
