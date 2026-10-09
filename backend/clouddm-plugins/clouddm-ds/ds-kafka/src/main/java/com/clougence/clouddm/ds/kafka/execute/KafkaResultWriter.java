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
package com.clougence.clouddm.ds.kafka.execute;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.kafka.i18n.KafkaDsI18nKeys;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.utils.i18n.I18nUtils;

public class KafkaResultWriter {
    private final QueryRequest  query;
    private final ResultBuilder builder;
    private final I18nUtils     i18n = I18nUtils.initI18n(KafkaDsI18nKeys.class);

    public KafkaResultWriter(QueryRequest query, ResultBuilder builder){
        this.query = query;
        this.builder = builder;
    }

    public void table(List<KafkaResultColumn> columns, List<Object[]> data, Runnable checkActive) throws Exception {
        table(columns, data.iterator(), checkActive);
        long limit = query.getResultConf().getFetchRecordCountLimit();
        if (limit > 0 && data.size() > limit) {
            truncated();
        }
    }

    public void table(List<KafkaResultColumn> columns, Iterator<Object[]> data, Runnable checkActive) throws Exception {
        long start = System.currentTimeMillis();
        Map<String, ResultColMeta> metadata = new LinkedHashMap<>();
        for (KafkaResultColumn column : columns) {
            ColMetaData meta = new ColMetaData();
            meta.setColumn(column.columnName());
            meta.setIndex(metadata.size());
            meta.setJdbcType(column.getJdbcType());
            meta.setColumnType(column.getJdbcType().getName());
            metadata.put(meta.getColumn(), new ResultColMeta(meta, column.fetcher()));
        }
        var meta = builder.newResultMeta(query, builder.newResultId(query));
        ResultSetRowsBuild rows = meta.receiveMeta(metadata);
        try {
            meta.finishRecord(true);
            writeRows(rows, columns, data, checkActive, start);
        } catch (Exception e) {
            rows.collectCost(System.currentTimeMillis() - start);
            rows.finishRecord(false, e.getMessage(), e);
            throw e;
        }
    }

    private void writeRows(ResultSetRowsBuild rows, List<KafkaResultColumn> columns, Iterator<Object[]> data, Runnable checkActive, long start) throws Exception {
        QueryResultConf limits = query.getResultConf();
        int count = 0;
        int pageCount = 0;
        boolean silent = false;
        boolean truncated = false;
        // Check the row bound before advancing the source: hasNext() may block in Consumer.poll().
        while (limits.getFetchRecordCountLimit() <= 0 || count < limits.getFetchRecordCountLimit()) {
            checkActive.run();
            if (!data.hasNext()) {
                break;
            }
            Object[] values = data.next();
            Map<String, Object> row = new LinkedHashMap<>();
            for (int index = 0; index < columns.size(); index++) {
                row.put(columns.get(index).columnName(), values[index]);
            }
            rows.receiveRow(silent, row);
            if (rows.fetcherOverflow()) {
                truncated = true;
                break;
            }
            count++;
            pageCount++;

            if (limits.getFetchResultSetBytesLimit() > 0 && rows.dataSize() >= limits.getFetchResultSetBytesLimit()) {
                truncated = true;
                break;
            }

            if (limits.getReceiveMode() == ReceiveMode.STREAM || (!silent && limits.getFetchPageSize() > 0 && pageCount >= limits.getFetchPageSize())) {
                rows.collectMetric(count);
                rows.collectCost(System.currentTimeMillis() - start);
                rows.flushData();
                rows.finishAndContinue();
                pageCount = 0;
                silent = limits.getReceiveMode() == ReceiveMode.PAGINATED;
            }
        }
        checkActive.run();
        rows.collectMetric(count);
        rows.collectCost(System.currentTimeMillis() - start);
        rows.flushData();
        if (silent) {
            var update = rows.newRowCountUpdate();
            update.collectMetric(count);
            update.collectCost(System.currentTimeMillis() - start);
            update.finishRecord(true);
            rows.finishAndSilent(true);
        } else {
            rows.finishRecord(true);
        }
        if (truncated) {
            truncated();
        }
    }

    private void truncated() {
        var message = builder.newMessage(query);
        message.receiveMessage(MessageLevel.Warn, i18n.getMessage(KafkaDsI18nKeys.KAFKA_RESULT_TRUNCATED), false);
        message.finishRecord(true);
    }
}
