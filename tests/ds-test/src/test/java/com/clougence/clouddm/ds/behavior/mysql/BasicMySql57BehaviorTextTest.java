package com.clougence.clouddm.ds.behavior.mysql;

public abstract class BasicMySql57BehaviorTextTest extends BasicMySqlBehaviorTextTest {

    protected BasicMySql57BehaviorTextTest(int shardCount, int shardId){
        super("5.7", "5.7.44", shardCount, shardId);
    }
}
