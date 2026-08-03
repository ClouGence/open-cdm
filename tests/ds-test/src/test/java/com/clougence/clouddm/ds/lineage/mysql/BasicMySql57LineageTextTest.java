package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql57LineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql57LineageTextTest(int shardCount, int shardId){
        super("5.7", "5.7.44", shardCount, shardId);
    }
}
