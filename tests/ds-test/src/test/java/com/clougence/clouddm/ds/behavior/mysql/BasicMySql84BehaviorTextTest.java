package com.clougence.clouddm.ds.behavior.mysql;

public abstract class BasicMySql84BehaviorTextTest extends BasicMySqlBehaviorTextTest {

    protected BasicMySql84BehaviorTextTest(int shardCount, int shardId){
        super("8.4", "8.4.10", shardCount, shardId);
    }
}
