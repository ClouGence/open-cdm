package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql56LineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql56LineageTextTest(int shardCount, int shardId){
        super("5.6", "5.6.51", shardCount, shardId);
    }
}
