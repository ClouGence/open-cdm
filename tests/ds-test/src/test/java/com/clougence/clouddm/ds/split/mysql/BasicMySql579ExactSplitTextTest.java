package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql579ExactSplitTextTest extends BasicMySqlSplitTextTest {

    protected BasicMySql579ExactSplitTextTest(int shardCount, int shardId){
        super("5.7/exact-5.7.9", "5.7.9", shardCount, shardId);
    }
}

final class MySql579ExactSplitShard1Test extends BasicMySql579ExactSplitTextTest {
    MySql579ExactSplitShard1Test(){
        super(3, 0);
    }
}

final class MySql579ExactSplitShard2Test extends BasicMySql579ExactSplitTextTest {
    MySql579ExactSplitShard2Test(){
        super(3, 1);
    }
}

final class MySql579ExactSplitShard3Test extends BasicMySql579ExactSplitTextTest {
    MySql579ExactSplitShard3Test(){
        super(3, 2);
    }
}
