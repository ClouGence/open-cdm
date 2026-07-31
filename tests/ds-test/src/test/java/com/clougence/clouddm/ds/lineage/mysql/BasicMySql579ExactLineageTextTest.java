package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql579ExactLineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql579ExactLineageTextTest(int shardCount, int shardId){
        super("5.7/exact-5.7.9", "5.7.9", shardCount, shardId);
    }
}
final class MySql579ExactLineageShard1Test extends BasicMySql579ExactLineageTextTest { MySql579ExactLineageShard1Test(){ super(3, 0); } }
final class MySql579ExactLineageShard2Test extends BasicMySql579ExactLineageTextTest { MySql579ExactLineageShard2Test(){ super(3, 1); } }
final class MySql579ExactLineageShard3Test extends BasicMySql579ExactLineageTextTest { MySql579ExactLineageShard3Test(){ super(3, 2); } }
