package com.clougence.clouddm.ds.behavior.mysql;

public abstract class BasicMySql56BehaviorTextTest extends BasicMySqlBehaviorTextTest {

    protected BasicMySql56BehaviorTextTest(int shardCount, int shardId){
        super("5.6", "5.6.51", shardCount, shardId);
    }
}
