package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql84LineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql84LineageTextTest(int shardCount, int shardId){
        super("8.4", "8.4.10", shardCount, shardId);
    }
}
