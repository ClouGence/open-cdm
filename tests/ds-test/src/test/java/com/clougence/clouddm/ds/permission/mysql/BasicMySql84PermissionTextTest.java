package com.clougence.clouddm.ds.permission.mysql;

public abstract class BasicMySql84PermissionTextTest extends BasicMySqlPermissionTextTest {

    protected BasicMySql84PermissionTextTest(int shardCount, int shardId){
        super("8.4", "8.4.10", shardCount, shardId);
    }
}
