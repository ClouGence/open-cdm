package com.clougence.clouddm.ds.mongodb.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/2/17 17:03:20
 */
@Slf4j
public class MongoSession extends DefaultRdbSession {

    public MongoSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new MongoHooks());
    }

}
