package com.clougence.clouddm.ds.behavior.mysql;

public abstract class BasicMySql97BehaviorTextTest extends BasicMySqlBehaviorTextTest {

    protected BasicMySql97BehaviorTextTest(int shardCount, int shardId){
        super("9.7", "9.7.1", shardCount, shardId);
    }
}
