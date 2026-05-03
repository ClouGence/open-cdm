package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

/**
 * @author Ekko 2022/11/03 16:48
 */
public class ChSession extends DefaultRdbSession {

    public ChSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new ChHooks(dsConfig));
    }

    @Override
    public long getUpdateCount(Statement ps) throws SQLException {
        int updateCount = ps.getUpdateCount();
        if (updateCount == 0) {
            return 1;
        }
        // updateCount = -1
        return updateCount;
    }
}
