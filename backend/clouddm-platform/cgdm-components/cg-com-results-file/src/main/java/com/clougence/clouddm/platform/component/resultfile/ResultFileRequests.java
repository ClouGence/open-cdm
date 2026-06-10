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
package com.clougence.clouddm.platform.component.resultfile;

import java.sql.JDBCType;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;

import lombok.Getter;

public class ResultFileRequests {

    public static ResultFileRequest fromColumns(String queryBody, LinkedHashMap<String, JDBCType> columns) {
        return fromColumns(null, queryBody, columns, null);
    }

    public static ResultFileRequest fromColumns(String queryBody, LinkedHashMap<String, JDBCType> columns, Map<String, String> variables) {
        return fromColumns(null, queryBody, columns, variables);
    }

    public static ResultFileRequest fromColumns(String queryId, String queryBody, LinkedHashMap<String, JDBCType> columns, Map<String, String> variables) {
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Result columns must not be empty.");
        }

        QueryRequest query = new QueryRequest();
        query.setQueryId(StringUtils.isBlank(queryId) ? UUID.randomUUID().toString() : queryId);
        query.setQueryBody(queryBody);
        query.setRequestTime(new Date());
        query.setVariables(variables == null ? new LinkedHashMap<>() : new LinkedHashMap<>(variables));
        query.setResultConf(defaultResultConf());

        ColMetaData[] metaData = new ColMetaData[columns.size()];
        int index = 0;
        for (Map.Entry<String, JDBCType> entry : columns.entrySet()) {
            String column = entry.getKey();
            if (StringUtils.isBlank(column)) {
                throw new IllegalArgumentException("Result column name must not be blank.");
            }

            JDBCType jdbcType = entry.getValue() == null ? JDBCType.VARCHAR : entry.getValue();
            ColMetaData meta = new ColMetaData();
            meta.setColumn(column);
            meta.setIndex(index + 1);
            meta.setJdbcType(jdbcType);
            meta.setColumnType(jdbcType.getName());
            metaData[index++] = meta;
        }

        return new ResultFileRequest(query, metaData);
    }

    private static QueryResultConf defaultResultConf() {
        QueryResultConf conf = new QueryResultConf();
        conf.setReceiveMode(ReceiveMode.PAGINATED);
        conf.setDisplayChars(Integer.MAX_VALUE);
        conf.setDataFormat(WellKnowFormat.WKF_DATE10);
        conf.setTimeFormat(WellKnowFormat.WKF_TIME24_S9);
        conf.setDataTimeFormat(WellKnowFormat.WKF_DATE_TIME24_S9);
        conf.setTimeWithZoneFormat(WellKnowFormat.WKF_OFFSET_TIME24_S9);
        conf.setDataTimeWithZoneFormat(WellKnowFormat.WKF_OFFSET_DATE_TIME24_S9);
        return conf;
    }

    @Getter
    public static class ResultFileRequest {

        private final QueryRequest query;
        private final ColMetaData[] columns;

        private ResultFileRequest(QueryRequest query, ColMetaData[] columns){
            this.query = query;
            this.columns = columns;
        }
    }
}
