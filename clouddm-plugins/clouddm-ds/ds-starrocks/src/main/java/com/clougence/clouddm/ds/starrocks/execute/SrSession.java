package com.clougence.clouddm.ds.starrocks.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

/**
 * @author Ekko 2022/11/03 16:48
 */
public class SrSession extends DefaultRdbSession {

    public SrSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new SrHooks(dsConfig.getVersion()));
    }
}
