package com.clougence.clouddm.ds.behavior.mysql;

public abstract class BasicMySql80BehaviorTextTest extends BasicMySqlBehaviorTextTest {

    protected BasicMySql80BehaviorTextTest(int shardCount, int shardId){
        super("8.0", "8.0.46", shardCount, shardId);
    }
}
