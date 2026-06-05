/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.maxcompute.execute;

import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.aliyun.odps.jdbc.OdpsPreparedStatement;
import com.aliyun.odps.jdbc.OdpsStatement;
import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetMetaBuild;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbSession;
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
public class McSession extends DefaultRdbSession {

    private OdpsStatement currentStatement;

    public McSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new McHooks());
    }

    @Override
    protected boolean executeStatement(Statement ps, QueryRequest query, ResultBuilder builder) throws SQLException {
        this.currentStatement = (OdpsStatement) ps;
        if (query.isUseCallable()) {
            throw new UnsupportedOperationException("Unsupported Callable.");
        }
        globalTimer.newTimeout((timeout) -> {
            OdpsPreparedStatement preparedStatement = (OdpsPreparedStatement) ps;
            String logViewUrl = preparedStatement.getLogViewUrl();
            if (logViewUrl == null) {
                globalTimer.newTimeout(timeout.task(), 100, TimeUnit.MILLISECONDS);
            } else {
                ResultBuilder.ResultMessageBuild resultMessageBuild = builder.newMessage(query);
                resultMessageBuild.receiveMessage(MessageLevel.Info, "[LogViewURL] " + logViewUrl, false);
                resultMessageBuild.finishRecord(true);
            }
        }, 100, TimeUnit.MILLISECONDS);

        return super.executeStatement(ps, query, builder);
    }

    private void handleExplain(QueryRequest query, Statement ps, ResultBuilder builder) throws SQLException, IOException {
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
            String string = rs.getString("Info");
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

    @Override
    public String getCurrentQueryId() {
        if (this.currentStatement != null) {
            return currentStatement.getExecuteInstance().getId();
        } else {
            return "";
        }
    }

    @Override
    public void killCurrentQuery() throws Exception {
        if (this.currentStatement != null) {
            try {
                this.currentStatement.cancel();
            } finally {
                this.currentStatement = null;
            }
        }
    }

    //  not support getLargeUpdateCount
    @Override
    public long getUpdateCount(Statement ps) throws SQLException {
        return ps.getUpdateCount();
    }

    @Override
    protected void handleRs(QueryRequest query, boolean retVal, Statement ps, ResultBuilder builder, KillCurrentQueryAble killAble) throws SQLException, IOException {
        if (query.isUseExplain()) {
            this.handleExplain(query, ps, builder);
        } else {
            if (retVal && ps.getResultSet().getMetaData().getColumnCount() == 0) {
                super.handleRs(query, false, ps, builder, killAble);
            } else {
                super.handleRs(query, retVal, ps, builder, killAble);
            }
        }
    }
}
