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
package com.clougence.clouddm.ds.opensearch.execute;

import java.nio.charset.StandardCharsets;
import java.sql.JDBCType;
import java.util.*;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.sql.es.EsOperation;
import com.clougence.sql.es.EsRequest;
import com.clougence.utils.i18n.I18nUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

final class OpenSearchResultWriter {
    private static final I18nUtils           I18N = I18nUtils.initI18n(OpenSearchDsI18nKeys.class);
    private final QueryRequest               query;
    private final ResultBuilder              builder;
    private final OpenSearchExecutionContext execution;
    private final long                       start;
    private long                             bytes;
    private int                              count;
    private int                              pageCount;
    private boolean                          silent;
    private String                           index;

    OpenSearchResultWriter(QueryRequest query, ResultBuilder builder, OpenSearchExecutionContext execution, long start){
        this.query = query;
        this.builder = builder;
        this.execution = execution;
        this.start = start;
    }

    void write(EsRequest request, EsOperation operation, OpenSearchJsonResponse response) throws Exception {
        execution.checkActive();
        index = request.segments().get(0).value();
        JsonNode body = response.body();
        if (request.method().equals("HEAD")) {
            if (response.status() != 200 && response.status() != 404) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
            }
            message(MessageLevel.Info, OpenSearchDsI18nKeys.OPENSEARCH_EXISTS_RESULT, response.status() == 200);
            return;
        }
        if (body == null || !body.isObject()) {
            if (response.status() >= 300) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
            }
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        }
        // _source is user data: its fields must not be interpreted as response status.
        boolean sourceOnly = request.segments().size() == 3 && request.segments().get(1).value().equals("_source");
        if (operation.getAction() != BehaviorAction.READ) {
            writeResult(operation, body, response.status());
            return;
        }
        if (sourceOnly && response.status() == 404 && body.path("error").path("type").asText().equals("resource_not_found_exception")) {
            table(List.of(), "_source");
            return;
        }
        boolean missingDocument = operation == EsOperation.READ_DOCUMENT && !sourceOnly && response.status() == 404 && body.path("found").isBoolean()
                                  && !body.path("found").booleanValue() && index.equals(body.path("_index").asText()) && !body.has("error");
        if (response.status() < 200 || (response.status() >= 300 && !missingDocument)) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, response.status());
        }
        if (!sourceOnly && operation != EsOperation.READ_INDEX) {
            checkStatus(body, response.status());
        }
        switch (operation) {
            case SEARCH -> search(body, request.segments().get(0).value());
            case READ_DOCUMENT -> {
                List<JsonNode> documents = new ArrayList<>();
                if (sourceOnly && response.status() == 200) {
                    documents.add(JsonNodeFactory.instance.objectNode().set("_source", body));
                } else if (body.path("found").asBoolean()) {
                    if (!body.path("_index").asText().equals(request.segments().get(0).value())) {
                        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                    }
                    documents.add(body);
                } else if (!body.path("found").isBoolean() || !index.equals(body.path("_index").asText())) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
                table(prepareDocuments(documents), "_source");
            }
            case COUNT -> {
                if (!body.path("count").isIntegralNumber() || !body.path("count").canConvertToLong() || body.path("count").longValue() < 0) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
                table(List.of(JsonNodeFactory.instance.objectNode().set("count", body.get("count"))), "count");
            }
            case READ_INDEX -> {
                if (query.isUsingValueProcess()) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
                }
                if (body.size() != 1 || !body.has(request.segments().get(0).value())) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
                table(List.of(JsonNodeFactory.instance.objectNode().set("response", body)), "response");
            }
            default -> throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_OPERATION_UNSUPPORTED);
        }
    }

    static void checkStatus(JsonNode body, int status) {
        if (body == null || !body.isObject()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        }
        if (body.has("error")) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, status);
        }
        if (body.path("timed_out").asBoolean()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_TIMEOUT);
        }
        if (body.path("version_conflicts").asLong() > 0 || body.path("terminated_early").asBoolean() || body.path("_shards").path("failed").asLong() > 0
            || !body.path("failures").isEmpty() || (body.has("acknowledged") && !body.path("acknowledged").asBoolean())
            || (body.has("shards_acknowledged") && !body.path("shards_acknowledged").asBoolean())) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        }
    }

    private void search(JsonNode body, String index) throws Exception {
        JsonNode hits = body.path("hits").path("hits");
        if (!hits.isArray()) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        }
        List<JsonNode> documents = new ArrayList<>();
        for (JsonNode hit : hits) {
            execution.checkActive();
            if (!hit.path("_index").asText().equals(index)) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
            }
            documents.add(hit);
        }
        JsonNode aggregates = body.get("aggregations");
        if (query.isUsingValueProcess() && aggregates != null) {
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
        }
        table(prepareDocuments(documents), "_source");
        if (aggregates != null) {
            if (!aggregates.isObject()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
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
        message(MessageLevel.Info, OpenSearchDsI18nKeys.OPENSEARCH_SEARCH_RESULT, totalText, documents.size(), body.path("took").asLong());
    }

    private void writeResult(EsOperation operation, JsonNode body, int status) {
        // A document delete can return 404 with result=not_found; index-not-found and conflicts remain failures.
        boolean missingDocument = operation == EsOperation.DELETE_DOCUMENT && status == 404 && body.path("result").asText().equals("not_found") && !body.has("error");
        boolean byQuery = operation == EsOperation.UPDATE_BY_QUERY || operation == EsOperation.DELETE_BY_QUERY;
        if (body.has("error") || (!byQuery && (status < 200 || (status >= 300 && !missingDocument)))) {
            if (status == 409) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_VERSION_CONFLICT);
            }
            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, status);
        }
        long affected = 0;
        switch (operation) {
            case INSERT_DOCUMENT, REPLACE_DOCUMENT, UPDATE_DOCUMENT, DELETE_DOCUMENT -> {
                if (!index.equals(body.path("_index").asText())) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
                String result = body.path("result").asText();
                boolean valid = switch (operation) {
                    case INSERT_DOCUMENT -> result.equals("created");
                    case REPLACE_DOCUMENT -> Set.of("created", "updated").contains(result);
                    case UPDATE_DOCUMENT -> Set.of("created", "updated", "deleted", "noop").contains(result);
                    case DELETE_DOCUMENT -> Set.of("deleted", "not_found").contains(result);
                    default -> false;
                };
                if (!valid) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
                if (Set.of("created", "updated", "deleted").contains(result)) {
                    affected = 1;
                }
            }
            case UPDATE_BY_QUERY, DELETE_BY_QUERY -> {
                List<String> counters = List.of("deleted");
                if (operation == EsOperation.UPDATE_BY_QUERY) {
                    counters = List.of("updated", "deleted");
                }
                for (String name : counters) {
                    JsonNode value = body.path(name);
                    if (!value.isIntegralNumber() || !value.canConvertToLong() || value.longValue() < 0 || Long.MAX_VALUE - affected < value.longValue()) {
                        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                    }
                    affected += value.longValue();
                }
            }
            default -> {
                if (!body.path("acknowledged").isBoolean()) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
                }
            }
        }
        ThirdPartyApiException failure = null;
        try {
            // By-query returns its completed counters even when a failed batch changes HTTP status.
            if (status == 409) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_VERSION_CONFLICT);
            }
            checkStatus(body, status);
            if (status < 200 || (status >= 300 && !missingDocument)) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR, status);
            }
        } catch (ThirdPartyApiException e) {
            failure = e;
        }
        // Keep the completed mutation count even when later batches, shards or acknowledgements fail.
        var result = builder.newResultCount(query);
        result.receiveUpdateCount(affected);
        result.collectCost(System.currentTimeMillis() - start);
        if (failure != null) {
            String detail = I18N.getMessage(failure.getMessageKey(), failure.getMessageArgs());
            result.finishRecord(false, detail, failure);
            throw failure;
        }
        execution.checkActive();
        result.finishRecord(true);
        if (query.isUsingValueProcess()) {
            message(MessageLevel.Info, OpenSearchDsI18nKeys.OPENSEARCH_EXECUTION_RESULT, operation.name() + ": " + affected);
        } else {
            message(MessageLevel.Info, OpenSearchDsI18nKeys.OPENSEARCH_EXECUTION_RESULT, body.toString());
        }
    }

    private void table(List<JsonNode> data, String emptyColumn) throws Exception {
        bytes = 0;
        count = 0;
        pageCount = 0;
        silent = false;
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
            appendRows(rows, columns, selected);
            finishRows(rows);
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
            message(MessageLevel.Warn, OpenSearchDsI18nKeys.OPENSEARCH_RESULT_TRUNCATED, count);
        }
    }

    private Map<String, ResultColMeta> columns(List<JsonNode> data, String emptyColumn) {
        Map<String, JDBCType> types = new LinkedHashMap<>();
        for (JsonNode row : data) {
            execution.checkActive();
            if (!row.isObject()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
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
            column.setTable(index);
            column.setColumn(entry.getKey());
            column.setIndex(columns.size());
            column.setJdbcType(type);
            if (entry.getKey().equals("_source")) {
                column.setColumnType(ColMetaData.JSON_DOCUMENT);
            } else {
                column.setColumnType(type.getName());
            }
            columns.put(entry.getKey(), new ResultColMeta(column, fetcher));
        }
        return columns;
    }

    private void appendRows(ResultSetRowsBuild rows, Map<String, ResultColMeta> columns, List<JsonNode> data) throws Exception {
        QueryResultConf limits = query.getResultConf();
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
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESULT_SIZE_LIMIT, limits.getFetchResultSetBytesLimit());
            }
            rows.receiveRow(silent, row);
            count++;
            pageCount++;
            rows.collectMetric(count);
            if (limits.getFetchResultSetBytesLimit() > 0 && rows.dataSize() > limits.getFetchResultSetBytesLimit()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESULT_SIZE_LIMIT, limits.getFetchResultSetBytesLimit());
            }
            if (limits.getReceiveMode() == ReceiveMode.STREAM || (!silent && limits.getFetchPageSize() > 0 && pageCount >= limits.getFetchPageSize())) {
                rows.collectCost(System.currentTimeMillis() - start);
                rows.flushData();
                rows.finishAndContinue();
                pageCount = 0;
                silent = limits.getReceiveMode() == ReceiveMode.PAGINATED;
            }
        }
    }

    private void finishRows(ResultSetRowsBuild rows) {
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

    private List<JsonNode> prepareDocuments(List<JsonNode> documents) {
        if (!query.isUsingValueProcess()) {
            return documents;
        }
        List<JsonNode> processed = new ArrayList<>();
        Set<String> allowed = Set.of("_index", "_id", "_type", "_version", "_seq_no", "_primary_term", "_score", "_source", "found", "sort", "_ignored");
        for (JsonNode document : documents) {
            for (var field : document.properties()) {
                if (!allowed.contains(field.getKey())) {
                    throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
                }
            }
            JsonNode source = document.get("_source");
            if (source != null && !source.isNull() && !source.isObject()) {
                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
            }
            ObjectNode copy = document.deepCopy();
            // Sort values may duplicate sensitive fields.
            copy.remove("sort");
            processed.add(copy);
        }
        return processed;
    }

    void writeContinuous(EsRequest request, OpenSearchClient client, long limit, boolean pit) throws Exception {
        index = request.segments().get(0).value();
        ObjectNode shape = JsonNodeFactory.instance.objectNode();
        for (String name : List.of("_index", "_id", "_type", "_routing", "_source", "_ignored", "sort")) {
            shape.put(name, "");
        }
        for (String name : List.of("_score", "_version", "_seq_no", "_primary_term")) {
            shape.put(name, 0);
        }
        // All pages share one schema and one result cache, used by both page display and export.
        var meta = builder.newResultMeta(query, builder.newResultId(query));
        meta.setRefreshOnProgress(true);
        ResultSetRowsBuild rows = null;
        try {
            try (OpenSearchSearchCursor cursor = new OpenSearchSearchCursor(client, request, execution, query.getResultConf().getFetchResultSetBytesLimit(), pit)) {
                if (query.isUsingValueProcess() || !cursor.hasVisibleSort()) {
                    shape.remove("sort");
                }
                Map<String, ResultColMeta> columns = columns(List.of(shape), "_source");
                rows = meta.receiveMeta(columns);
                meta.finishRecord(true);
                long total = -1;
                boolean first = true;
                while (count < limit) {
                    JsonNode response = cursor.nextPage(limit - count);
                    if (first) {
                        JsonNode hitsTotal = response.path("hits").path("total");
                        if (hitsTotal.isIntegralNumber() && hitsTotal.canConvertToLong()) {
                            total = hitsTotal.longValue();
                        } else if (hitsTotal.path("relation").asText().equals("eq") && hitsTotal.path("value").isIntegralNumber() && hitsTotal.path("value").canConvertToLong()) {
                            total = hitsTotal.path("value").longValue();
                        }
                        first = false;
                    }
                    if (response.has("aggregations")) {
                        throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CONTINUOUS_RESPONSE_UNSUPPORTED);
                    }
                    JsonNode hits = response.path("hits").path("hits");
                    if (hits.isEmpty()) {
                        if (total >= 0 && count < Math.min(limit, total)) {
                            throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_INCOMPLETE);
                        }
                        break;
                    }
                    List<JsonNode> page = new ArrayList<>();
                    for (JsonNode hit : hits) {
                        for (var field : hit.properties()) {
                            if (!columns.containsKey(field.getKey()) && !(query.isUsingValueProcess() && field.getKey().equals("sort"))) {
                                throw ThirdPartyApiException.as().with(OpenSearchDsI18nKeys.OPENSEARCH_CONTINUOUS_RESPONSE_UNSUPPORTED);
                            }
                        }
                        if (page.size() < limit - count) {
                            page.add(hit);
                        }
                    }
                    appendRows(rows, columns, prepareDocuments(page));
                    rows.flushData();
                    var progress = rows.newRowCountUpdate();
                    progress.collectMetric(count);
                    progress.collectCost(System.currentTimeMillis() - start);
                    progress.finishRecord(true);
                }
            }
            finishRows(rows);
        } catch (Exception e) {
            if (rows == null) {
                meta.finishRecord(false, e.getMessage(), e);
            } else {
                rows.collectCost(System.currentTimeMillis() - start);
                rows.finishRecord(false, e.getMessage(), e);
            }
            throw e;
        }
        message(MessageLevel.Info, OpenSearchDsI18nKeys.OPENSEARCH_CONTINUOUS_RESULT, count);
    }

    private void message(MessageLevel level, String key, Object... args) {
        execution.checkActive();
        var message = builder.newMessage(query);
        message.receiveMessage(level, I18N.getMessage(key, args), false);
        message.collectCost(System.currentTimeMillis() - start);
        message.finishRecord(true);
    }
}
