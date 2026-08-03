package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql97SplitTextTest extends BasicMySqlSplitTextTest {
    protected BasicMySql97SplitTextTest(int shardCount, int shardId){
        super("9.7", "9.7.1", shardCount, shardId);
    }
}
