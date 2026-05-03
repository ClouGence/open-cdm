package com.clougence.clouddm.ds.oceanbase.execute.obformysql;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.clougence.drivers.DsObject;

/**
 * @author Ekko 2022/11/03 16:48
 */
public class ObSession extends MySession {

    public ObSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new ObHooks(dsConfig.getVersion()));
    }
}
