package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql97LineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql97LineageTextTest(int shardCount, int shardId){
        super("9.7", "9.7.1", shardCount, shardId);
    }
}
