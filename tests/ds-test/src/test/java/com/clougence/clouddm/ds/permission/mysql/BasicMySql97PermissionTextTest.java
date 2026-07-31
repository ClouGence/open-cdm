package com.clougence.clouddm.ds.permission.mysql;

public abstract class BasicMySql97PermissionTextTest extends BasicMySqlPermissionTextTest {

    protected BasicMySql97PermissionTextTest(int shardCount, int shardId){
        super("9.7", "9.7.1", shardCount, shardId);
    }
}
