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
package com.clougence.clouddm.ds.es.execute;

import java.nio.charset.StandardCharsets;
import java.sql.JDBCType;
import java.util.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsOperation;
import com.clougence.sql.es.EsRequest;
import com.clougence.utils.i18n.I18nUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

final class EsResultWriter {
    private static final I18nUtils   I18N = I18nUtils.initI18n(EsDsI18nKeys.class);
    private final QueryRequest       query;
    private final ResultBuilder      builder;
    private final EsExecutionContext execution;
    private final long               start;

    EsResultWriter(QueryRequest query, ResultBuilder builder, EsExecutionContext execution, long start){
        this.query = query;
        this.builder = builder;
        this.execution = execution;
        this.start = start;
    }

    void write(EsRequest request, EsOperation operation, EsResponse response) throws Exception {
        execution.checkActive();
        JsonNode body = response.body();
        if (request.method().equals("HEAD")) {
            message(MessageLevel.Info, EsDsI18nKeys.ES_EXISTS_RESULT, response.status() == 200);
            return;
        }
        if (body == null || !body.isObject()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
        }
        // _source is user data: its fields must not be interpreted as response status.
        boolean sourceOnly = request.segments().size() == 3 && request.segments().get(1).value().equals("_source");
        if ((!sourceOnly && operation != EsOperation.READ_INDEX) || response.status() >= 400) {
            checkStatus(body, response.status());
        }
        switch (operation) {
            case SEARCH, KNN_SEARCH -> search(body, request.segments().get(0).value());
            case READ_DOCUMENT -> {
                List<JsonNode> documents = new ArrayList<>();
                if (sourceOnly && response.status() == 200) {
                    documents.add(JsonNodeFactory.instance.objectNode().set("_source", body));
                } else if (body.path("found").asBoolean()) {
                    if (!body.path("_index").asText().equals(request.segments().get(0).value())) {
                        throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                    }
                    documents.add(body);
                } else if (!body.has("found")) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                }
                table(documents, "_source");
            }
            case COUNT -> {
                if (!body.path("count").isIntegralNumber()) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                }
                table(List.of(JsonNodeFactory.instance.objectNode().set("count", body.get("count"))), "count");
            }
            case READ_INDEX -> {
                if (body.size() != 1 || !body.has(request.segments().get(0).value())) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                }
                table(List.of(JsonNodeFactory.instance.objectNode().set("response", body)), "response");
            }
            default -> writeResult(operation, body);
        }
    }

    private void checkStatus(JsonNode body, int status) {
        if (body.has("error")) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_HTTP_ERROR, status);
        }
        if (body.path("timed_out").asBoolean()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_REQUEST_TIMEOUT);
        }
        if (body.path("_shards").path("failed").asLong() > 0 || !body.path("failures").isEmpty() || (body.has("acknowledged") && !body.path("acknowledged").asBoolean())
            || (body.has("shards_acknowledged") && !body.path("shards_acknowledged").asBoolean())) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_PARTIAL_RESPONSE);
        }
    }

    private void search(JsonNode body, String index) throws Exception {
        JsonNode hits = body.path("hits").path("hits");
        if (!hits.isArray()) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
        }
        List<JsonNode> documents = new ArrayList<>();
        for (JsonNode hit : hits) {
            execution.checkActive();
            if (!hit.path("_index").asText().equals(index)) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
            documents.add(hit);
        }
        table(documents, "_source");
        JsonNode aggregates = body.get("aggregations");
        if (aggregates != null) {
            if (!aggregates.isObject()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
            List<JsonNode> rows = new ArrayList<>();
            for (var entry : aggregates.properties()) {
                ObjectNode row = JsonNodeFactory.instance.objectNode();
                row.put("aggregation", entry.getKey());
                row.set("value", entry.getValue());
                rows.add(row);
            }
            table(rows, "value");
        }
        JsonNode total = body.path("hits").path("total");
        String totalText = "?";
        if (total.isIntegralNumber()) {
            totalText = total.asText();
        } else if (total.path("value").isIntegralNumber()) {
            totalText = total.path("value").asText();
            if (total.path("relation").asText().equals("gte")) {
                totalText = ">=" + totalText;
            }
        }
        message(MessageLevel.Info, EsDsI18nKeys.ES_SEARCH_RESULT, totalText, documents.size(), body.path("took").asLong());
    }

    private void writeResult(EsOperation operation, JsonNode body) {
        long count = 0;
        switch (operation) {
            case INSERT_DOCUMENT, REPLACE_DOCUMENT, UPDATE_DOCUMENT, DELETE_DOCUMENT -> {
                String result = body.path("result").asText();
                if (Set.of("created", "updated", "deleted").contains(result)) {
                    count = 1;
                } else if (!Set.of("noop", "not_found").contains(result)) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                }
            }
            case UPDATE_BY_QUERY, DELETE_BY_QUERY -> {
                String counter = "updated";
                if (operation == EsOperation.DELETE_BY_QUERY) {
                    counter = "deleted";
                }
                if (!body.path(counter).isIntegralNumber() || !body.path(counter).canConvertToLong()) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                }
                count = body.path(counter).longValue();
            }
            default -> {
                if (!body.has("acknowledged")) {
                    throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
                }
            }
        }
        // Preserve native result/version/conflict details without guessing additional affected rows.
        message(MessageLevel.Info, EsDsI18nKeys.ES_EXECUTION_RESULT, body.toString());
        execution.checkActive();
        var result = builder.newResultCount(query);
        result.receiveUpdateCount(count);
        result.collectCost(System.currentTimeMillis() - start);
        result.finishRecord(true);
    }

    private void table(List<JsonNode> data, String emptyColumn) throws Exception {
        QueryResultConf limits = query.getResultConf();
        int count = data.size();
        if (limits.getFetchRecordCountLimit() > 0) {
            count = (int) Math.min(count, limits.getFetchRecordCountLimit());
        }
        List<JsonNode> selected = data.subList(0, count);
        Map<String, ResultColMeta> columns = columns(selected, emptyColumn);
        var meta = builder.newResultMeta(query, builder.newResultId(query));
        ResultSetRowsBuild rows = null;
        try {
            rows = meta.receiveMeta(columns);
            meta.finishRecord(true);
            writeRows(rows, columns, selected);
        } catch (Exception e) {
            if (rows == null) {
                meta.finishRecord(false, e.getMessage(), e);
            } else {
                rows.collectCost(System.currentTimeMillis() - start);
                rows.finishRecord(false, e.getMessage(), e);
            }
            throw e;
        }
        if (count < data.size()) {
            message(MessageLevel.Warn, EsDsI18nKeys.ES_RESULT_TRUNCATED, count);
        }
    }

    private Map<String, ResultColMeta> columns(List<JsonNode> data, String emptyColumn) {
        Map<String, JDBCType> types = new LinkedHashMap<>();
        for (JsonNode row : data) {
            execution.checkActive();
            if (!row.isObject()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESPONSE_INVALID);
            }
            for (var field : row.properties()) {
                JDBCType type = null;
                if (!field.getValue().isNull()) {
                    type = JDBCType.VARCHAR;
                    if (field.getValue().isNumber()) {
                        type = JDBCType.DECIMAL;
                    } else if (field.getValue().isBoolean()) {
                        type = JDBCType.BOOLEAN;
                    }
                }
                JDBCType previous = types.get(field.getKey());
                if (previous == null) {
                    types.put(field.getKey(), type);
                } else if (type != null && previous != type) {
                    types.put(field.getKey(), JDBCType.VARCHAR);
                }
            }
        }
        if (types.isEmpty()) {
            types.put(emptyColumn, JDBCType.VARCHAR);
        }
        Map<String, ResultColMeta> columns = new LinkedHashMap<>();
        for (var entry : types.entrySet()) {
            JDBCType type = entry.getValue();
            if (type == null) {
                type = JDBCType.VARCHAR;
            }
            ValueFetcher fetcher = AbstractColReader.STRING_VALUE_FETCHER;
            if (type == JDBCType.DECIMAL) {
                fetcher = AbstractColReader.BIGDECIMAL_VALUE_FETCHER;
            } else if (type == JDBCType.BOOLEAN) {
                fetcher = AbstractColReader.BOOLEAN_VALUE_FETCHER;
            }
            ColMetaData column = new ColMetaData();
            column.setColumn(entry.getKey());
            column.setIndex(columns.size());
            column.setJdbcType(type);
            column.setColumnType(type.getName());
            columns.put(entry.getKey(), new ResultColMeta(column, fetcher));
        }
        return columns;
    }

    private void writeRows(ResultSetRowsBuild rows, Map<String, ResultColMeta> columns, List<JsonNode> data) throws Exception {
        QueryResultConf limits = query.getResultConf();
        long bytes = 0;
        int count = 0;
        int pageCount = 0;
        boolean silent = false;
        for (JsonNode document : data) {
            execution.checkActive();
            Map<String, Object> row = new LinkedHashMap<>();
            for (var entry : columns.entrySet()) {
                JsonNode node = document.get(entry.getKey());
                Object value = null;
                if (node != null && !node.isNull()) {
                    if (entry.getValue().getMeta().getJdbcType() == JDBCType.DECIMAL) {
                        value = node.decimalValue();
                    } else if (entry.getValue().getMeta().getJdbcType() == JDBCType.BOOLEAN) {
                        value = node.booleanValue();
                    } else if (node.isTextual()) {
                        value = node.textValue();
                    } else {
                        value = node.toString();
                    }
                    bytes += value.toString().getBytes(StandardCharsets.UTF_8).length;
                }
                row.put(entry.getKey(), value);
            }
            if (limits.getFetchResultSetBytesLimit() > 0 && bytes > limits.getFetchResultSetBytesLimit()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESULT_SIZE_LIMIT, limits.getFetchResultSetBytesLimit());
            }
            rows.receiveRow(silent, row);
            count++;
            pageCount++;
            rows.collectMetric(count);
            if (limits.getFetchResultSetBytesLimit() > 0 && rows.dataSize() > limits.getFetchResultSetBytesLimit()) {
                throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_RESULT_SIZE_LIMIT, limits.getFetchResultSetBytesLimit());
            }
            if (limits.getReceiveMode() == ReceiveMode.STREAM || (!silent && limits.getFetchPageSize() > 0 && pageCount >= limits.getFetchPageSize())) {
                rows.collectCost(System.currentTimeMillis() - start);
                rows.flushData();
                rows.finishAndContinue();
                pageCount = 0;
                silent = limits.getReceiveMode() == ReceiveMode.PAGINATED;
            }
        }
        execution.checkActive();
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
    }

    private void message(MessageLevel level, String key, Object... args) {
        execution.checkActive();
        var message = builder.newMessage(query);
        message.receiveMessage(level, I18N.getMessage(key, args), false);
        message.collectCost(System.currentTimeMillis() - start);
        message.finishRecord(true);
    }
}
