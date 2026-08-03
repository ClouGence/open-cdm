package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql5710ExactSplitTextTest extends BasicMySqlSplitTextTest {

    protected BasicMySql5710ExactSplitTextTest(int shardCount, int shardId){
        super("5.7/exact-5.7.10", "5.7.10", shardCount, shardId);
    }
}

final class MySql5710ExactSplitShard1Test extends BasicMySql5710ExactSplitTextTest { MySql5710ExactSplitShard1Test(){ super(3, 0); } }
final class MySql5710ExactSplitShard2Test extends BasicMySql5710ExactSplitTextTest { MySql5710ExactSplitShard2Test(){ super(3, 1); } }
final class MySql5710ExactSplitShard3Test extends BasicMySql5710ExactSplitTextTest { MySql5710ExactSplitShard3Test(){ super(3, 2); } }
