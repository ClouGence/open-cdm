package com.clougence.clouddm.ds.lineage.mysql;

public abstract class BasicMySql80LineageTextTest extends BasicMySqlLineageTextTest {

    protected BasicMySql80LineageTextTest(int shardCount, int shardId){
        super("8.0", "8.0.46", shardCount, shardId);
    }
}
