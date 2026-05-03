package com.clougence.clouddm.ds.doris.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.clougence.drivers.DsObject;

/**
 * @author Ekko 2022/11/03 16:48
 */
public class DrSession extends MySession {

    public DrSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new DrHooks(dsConfig.getVersion()));
    }
}
