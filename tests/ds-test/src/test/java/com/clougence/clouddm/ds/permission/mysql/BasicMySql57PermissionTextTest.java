package com.clougence.clouddm.ds.permission.mysql;

public abstract class BasicMySql57PermissionTextTest extends BasicMySqlPermissionTextTest {

    protected BasicMySql57PermissionTextTest(int shardCount, int shardId){
        super("5.7", "5.7.44", shardCount, shardId);
    }
}
