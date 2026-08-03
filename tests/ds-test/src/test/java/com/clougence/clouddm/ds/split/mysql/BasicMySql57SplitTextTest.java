package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql57SplitTextTest extends BasicMySqlSplitTextTest {
    protected BasicMySql57SplitTextTest(int shardCount, int shardId){
        super("5.7", "5.7.44", shardCount, shardId);
    }
}
