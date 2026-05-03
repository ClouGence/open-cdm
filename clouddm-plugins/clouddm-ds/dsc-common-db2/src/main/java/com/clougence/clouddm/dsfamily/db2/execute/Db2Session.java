package com.clougence.clouddm.dsfamily.db2.execute;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.RandomUtils;

/**
 * @author bucketli 2022/3/28 19:25:30
 */
public class Db2Session extends DefaultRdbSession {

    public static Integer STMT_NO               = RandomUtils.nextInt(1000000, Integer.MAX_VALUE);
    private final String  QUERY_EXPLAIN_SQL     = "SELECT concat(OBJECT_SCHEMA ,concat('.',OBJECT_NAME)) as name,COLUMN_COUNT,ROW_COUNT,WIDTH,PAGES\n"
                                                  + "FROM SYSTOOLS.EXPLAIN_STATEMENT a\n"
                                                  + "         left join SYSTOOLS.EXPLAIN_OBJECT b on a.EXPLAIN_REQUESTER = b.EXPLAIN_REQUESTER\n"
                                                  + "and a.EXPLAIN_TIME = b.EXPLAIN_TIME and a.SOURCE_NAME = b.SOURCE_NAME and a.SOURCE_SCHEMA = b.SOURCE_SCHEMA\n"
                                                  + "and a.SOURCE_VERSION = b.SOURCE_VERSION and a.EXPLAIN_LEVEL = b.EXPLAIN_LEVEL and a.STMTNO = b.STMTNO\n"
                                                  + "and a.SECTNO = b.SECTNO WHERE a.QUERYNO = ?   AND a.EXPLAIN_LEVEL = 'P' WITH UR;";

    private final String  DELETE_EXPLAIN_RECODE = "DELETE FROM SYSTOOLS.EXPLAIN_INSTANCE I WHERE EXISTS (SELECT 1\n"
                                                  + " FROM SYSTOOLS.EXPLAIN_STATEMENT S  WHERE S.EXPLAIN_TIME = I.EXPLAIN_TIME\n"
                                                  + "  AND S.SOURCE_NAME = I.SOURCE_NAME AND S.SOURCE_SCHEMA = I.SOURCE_SCHEMA\n"
                                                  + "  AND S.SOURCE_VERSION = I.SOURCE_VERSION AND QUERYNO = ?)";

    public Db2Session(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject, Db2Hooks sessionHook){
        super(newSessionId, dsConfig, dsObject, sessionHook);
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

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        if (query.isUseExplain()) {
            try (PreparedStatement eps = (PreparedStatement) this.rdbHook().explainStatement(ps.getConnection(), query)) {
                eps.execute();
            }
            PreparedStatement preparedStatement = (PreparedStatement) ps;
            preparedStatement.setInt(1, STMT_NO);
            return preparedStatement.execute();
        }

        //
        if (query.isUseCallable()) {
            CallableStatement call = (CallableStatement) ps;
            if (CollectionUtils.isNotEmpty(query.getQueryArgs())) {
                List<QueryArg> inParams = query.getQueryArgs().stream().filter(item -> !item.isOutParam()).collect(Collectors.toList());
                for (QueryArg inParam : inParams) {
                    call.registerOutParameter(inParam.getIndex(), inParam.getJdbcType());
                }
            }
            return call.execute();
        }
        return super.executeStatement(ps, query, builder);
    }

    @Override
    protected void afterQueryRequest(long beginTime, QueryRequest query, ResultBuilder builder) throws SQLException {
        super.afterQueryRequest(beginTime, query, builder);
        if (query.isUseExplain()) {
            try (PreparedStatement dbStat = currentResource().prepareStatement(DELETE_EXPLAIN_RECODE)) {
                dbStat.setInt(1, STMT_NO);
                dbStat.execute();
            }
        }
    }

    @Override
    protected Statement createStatement(Connection conn, QueryRequest query) throws SQLException {
        if (query.isUseExplain()) {
            query.setUsingValueProcess(false);
            PreparedStatement stmt = conn.prepareStatement(QUERY_EXPLAIN_SQL, java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(200);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            return stmt;
        } else {
            return rdbHook().executeStatement(conn, query);
        }
    }

    @Override
    protected void doClose() {
        if (!this.isClose()) {
            this.setAutoCommit(true);
        }
        super.doClose();
    }
}
