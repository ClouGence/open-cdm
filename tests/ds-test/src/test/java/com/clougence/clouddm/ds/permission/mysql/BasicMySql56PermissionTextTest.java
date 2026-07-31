package com.clougence.clouddm.ds.permission.mysql;

public abstract class BasicMySql56PermissionTextTest extends BasicMySqlPermissionTextTest {

    protected BasicMySql56PermissionTextTest(int shardCount, int shardId){
        super("5.6", "5.6.51", shardCount, shardId);
    }
}
