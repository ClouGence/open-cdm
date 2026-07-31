package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql84SplitTextTest extends BasicMySqlSplitTextTest {
    protected BasicMySql84SplitTextTest(int shardCount, int shardId){
        super("8.4", "8.4.10", shardCount, shardId);
    }
}
