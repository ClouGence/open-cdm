package com.clougence.clouddm.ds.permission.mysql;

public abstract class BasicMySql80PermissionTextTest extends BasicMySqlPermissionTextTest {

    protected BasicMySql80PermissionTextTest(int shardCount, int shardId){
        super("8.0", "8.0.40", shardCount, shardId);
    }
}
