package com.clougence.clouddm.ds.db2i.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.db2.execute.Db2Session;
import com.clougence.drivers.DsObject;

/**
 * @author mode 2022/3/28 19:25:30
 */
public class Db2ForiSession extends Db2Session {

    public Db2ForiSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new Db2ForiHooks());
    }
}
