package com.clougence.clouddm.ds.redis.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
public class RedisSession extends DefaultRdbSession {

    public RedisSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new RedisHooks());
    }
}
