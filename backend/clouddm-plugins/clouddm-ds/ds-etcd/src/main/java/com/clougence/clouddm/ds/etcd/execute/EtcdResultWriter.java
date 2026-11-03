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
package com.clougence.clouddm.ds.etcd.execute;

import java.sql.JDBCType;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;

public class EtcdResultWriter {

    private final QueryRequest  query;
    private final ResultBuilder builder;

    public EtcdResultWriter(QueryRequest query, ResultBuilder builder){
        this.query = query;
        this.builder = builder;
    }

    public void rows(String[] columns, java.util.List<Map<String, Object>> data) throws Exception {
        long start = System.currentTimeMillis();
        Map<String, ResultColMeta> metadata = new LinkedHashMap<>();
        for (String column : columns) {
            ColMetaData meta = new ColMetaData();
            meta.setColumn(column);
            meta.setIndex(metadata.size());
            meta.setJdbcType(JDBCType.VARCHAR);
            meta.setColumnType(JDBCType.VARCHAR.getName());
            metadata.put(column, new ResultColMeta(meta, AbstractColReader.STRING_VALUE_FETCHER));
        }
        var meta = this.builder.newResultMeta(this.query, this.builder.newResultId(this.query));
        ResultSetRowsBuild rows = meta.receiveMeta(metadata);
        meta.finishRecord(true);
        int count = 0;
        for (Map<String, Object> row : data) {
            rows.receiveRow(false, row);
            count++;
        }
        rows.collectMetric(count);
        rows.collectCost(System.currentTimeMillis() - start);
        rows.flushData();
        rows.finishRecord(true);
    }
}
