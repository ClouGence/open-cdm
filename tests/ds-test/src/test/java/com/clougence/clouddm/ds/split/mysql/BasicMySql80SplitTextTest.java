package com.clougence.clouddm.ds.split.mysql;

public abstract class BasicMySql80SplitTextTest extends BasicMySqlSplitTextTest {
    protected BasicMySql80SplitTextTest(int shardCount, int shardId){
        super("8.0", "8.0.46", shardCount, shardId);
    }
}
