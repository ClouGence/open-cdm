package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql56SplitTextTest extends BasicMySqlSplitTextTest {
    protected BasicMySql56SplitTextTest(int shardCount, int shardId){
        super("5.6", "5.6.51", shardCount, shardId);
    }
}
