package com.clougence.clouddm.ds.mariadb.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.clougence.drivers.DsObject;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class MarSession extends MySession {

    public MarSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new MarHooks(dsConfig.getVersion()));
    }
}
