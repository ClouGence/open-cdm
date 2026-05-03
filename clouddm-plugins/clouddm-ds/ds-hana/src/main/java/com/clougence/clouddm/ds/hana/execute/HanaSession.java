package com.clougence.clouddm.ds.hana.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;

/**
 * @author mode 2022/3/28 19:25:30
 */
public class HanaSession extends DefaultRdbSession {

    public HanaSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new HanaHooks());
    }

    @Override
    protected void beforeQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.beforeQueryRequest(beginTime, query, builder);

        String queryBody = query.getQueryBody();
        if (queryBody.trim().endsWith(";")) {
            int index = queryBody.lastIndexOf(";");
            queryBody = queryBody.substring(0, index);
            query.setQueryBody(queryBody);
        }
    }

    // hana not support getLargeUpdateCount
    @Override
    public long getUpdateCount(Statement ps) throws SQLException {
        return ps.getUpdateCount();
    }
}
