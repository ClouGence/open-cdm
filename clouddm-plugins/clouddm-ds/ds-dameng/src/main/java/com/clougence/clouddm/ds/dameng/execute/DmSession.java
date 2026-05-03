package com.clougence.clouddm.ds.dameng.execute;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
public class DmSession extends DefaultRdbSession {

    public DmSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new DmHooks());
    }

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (query.isUseCallable()) {
            CallableStatement call = (CallableStatement) ps;
            if (CollectionUtils.isNotEmpty(query.getQueryArgs())) {
                List<QueryArg> inParams = query.getQueryArgs().stream().filter(item -> !item.isOutParam()).collect(Collectors.toList());
                for (QueryArg inParam : inParams) {
                    call.registerOutParameter(inParam.getIndex(), inParam.getJdbcType());
                }
            }
            return call.execute();
        } else {
            return super.executeStatement(ps, query, builder);
        }
    }
}
