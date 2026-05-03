package com.clougence.clouddm.ds.oceanbase.execute.obforora;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.oracle.execute.OraSession;
import com.clougence.drivers.DsObject;

public class ObForOraSession extends OraSession {

    public ObForOraSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new ObForOraHooks(dsConfig));
    }
}
