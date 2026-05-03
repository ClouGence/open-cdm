package com.clougence.clouddm.ds.ads.execute.ads4my;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetMetaBuild;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.rdb.KillCurrentQueryAble;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.drivers.DsObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2022/3/28 19:25:30
 */
@Slf4j
public class AdsMySqlSession extends MySession {

    public AdsMySqlSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new AdsMyHooks(dsConfig.getVersion()));
    }

    @Override
    protected void handleRs(QueryRequest query, boolean retVal, Statement ps, ResultBuilder builder, KillCurrentQueryAble killAble) throws SQLException, IOException {
        if (query.isUseExplain()) {
            this.handleExplain(query, ps, builder);
        } else {
            super.handleRs(query, retVal, ps, builder, killAble);
        }
    }

    private long handleExplain(QueryRequest query, Statement ps, ResultBuilder builder) throws SQLException, IOException {
        // fetch meta
        String resultId = builder.newResultId(query);
        ResultSetMetaBuild mb = builder.newResultMeta(query, resultId);
        ColMetaData cm = createStrColMeta(null, null, "Query Plan", 0);
        ValueFetcher cf = AbstractColReader.STRING_VALUE_FETCHER;
        ResultSetRowsBuild rb = mb.receiveMeta(CollectionUtils.asMap("Query Plan", new ResultColMeta(cm, cf)));
        mb.finishRecord(true);

        // fetch rows
        long fetchTimeMs = System.currentTimeMillis();
        try (ResultSet rs = ps.getResultSet()) {

            rs.next();
            String string = rs.getString("Query Plan");
            for (String str : StringUtils.split(string, "\n")) {
                Map<String, Object> valMap = CollectionUtils.asMap("Query Plan", str);
                rb.receiveRow(false, valMap);
            }

            rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
            rb.finishRecord(true);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            rb.collectCost(System.currentTimeMillis() - fetchTimeMs);
            rb.finishRecord(false, e.getMessage(), e);
            throw e;
        }

        return -1;
    }

    private ColMetaData createStrColMeta(String schema, String table, String column, int index) {
        ColMetaData colMetaData = new ColMetaData();
        colMetaData.setCatalog("");
        colMetaData.setSchema(schema);
        colMetaData.setTable(table);
        colMetaData.setColumn(column);
        colMetaData.setColumnType(JDBCType.VARCHAR.getName().toLowerCase());
        colMetaData.setJdbcType(JDBCType.VARCHAR);
        colMetaData.setIndex(index);
        return colMetaData;
    }
}
