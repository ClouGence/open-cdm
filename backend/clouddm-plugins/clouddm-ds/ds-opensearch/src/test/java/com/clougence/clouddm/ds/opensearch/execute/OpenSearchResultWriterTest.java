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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.sql.es.EsRequestParser;
import com.clougence.sql.es.EsRequestPolicy;
import com.clougence.sql.es.RestProduct;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class OpenSearchResultWriterTest {
    private final RecordingOpenSearchResults results = new RecordingOpenSearchResults();
    private final QueryResultConf            limits  = new QueryResultConf();
    private boolean                          masking;

    @Test
    public void emptySearchAndMissingDocumentsHaveCompletedEmptyTables() throws Exception {
        write("GET /books/_search", 200, "{\"hits\":{\"total\":0,\"hits\":[]}}");
        write("GET /books/_doc/1", 404, "{\"_index\":\"books\",\"_id\":\"1\",\"found\":false}");
        write("GET /books/_source/1", 404, "{\"error\":{\"type\":\"resource_not_found_exception\"}}");
        assertTrue(results.rows.isEmpty());
        assertEquals(3, results.metadata.size());
        assertEquals(3, results.completions.stream().filter("ResultSetRowsBuild:true"::equals).count());
        assertTrue(results.metadata.stream().allMatch(columns -> columns.containsKey("_source")));
    }

    @Test
    public void sourceAndIndexNamesAreNotInterpretedAsResponseStatus() throws Exception {
        write("GET /books/_source/1", 200, "{\"error\":\"business data\",\"timed_out\":true}");
        write("GET /error/_mapping", 200, "{\"error\":{\"mappings\":{}}}");
        assertEquals(2, results.rows.size());
        assertTrue(results.rows.get(0).get("_source").toString().contains("business data"));
    }

    @Test
    public void emptyAggregationsAndExactNumericCountArePreserved() throws Exception {
        write("GET /books/_search", 200, "{\"hits\":{\"hits\":[]},\"aggregations\":{\"amount\":{\"value\":null},\"groups\":{\"buckets\":[]}}}");
        assertEquals(2, results.metadata.size());
        assertEquals(2, results.rows.size());
        write("GET /books/_count", 200, "{\"count\":9007199254740993}");
        assertEquals(new BigDecimal("9007199254740993"), results.rows.get(2).get("count"));
    }

    @Test
    public void documentMutationCountsIncludeNoopAndNotFound() throws Exception {
        write("POST /books/_doc\n{}", 201, "{\"_index\":\"books\",\"result\":\"created\"}");
        write("PUT /books/_doc/1\n{}", 200, "{\"_index\":\"books\",\"result\":\"updated\"}");
        write("POST /books/_update/1\n{\"doc\":{}}", 200, "{\"_index\":\"books\",\"result\":\"noop\"}");
        write("POST /books/_update/1\n{\"script\":{\"source\":\"ctx.op='delete'\"}}", 200, "{\"_index\":\"books\",\"result\":\"deleted\"}");
        write("DELETE /books/_doc/1", 200, "{\"_index\":\"books\",\"result\":\"deleted\"}");
        write("DELETE /books/_doc/1", 404, "{\"_index\":\"books\",\"result\":\"not_found\"}");
        assertEquals(List.of(1L, 1L, 0L, 1L, 1L, 0L), results.counts);
    }

    @Test
    public void queryMutationCountsExcludeNoopsAndIncludeScriptDeletes() throws Exception {
        write("POST /books/_update_by_query", 200, "{\"updated\":3,\"deleted\":2,\"noops\":4,\"total\":9}");
        write("POST /books/_delete_by_query", 200, "{\"deleted\":0}");
        assertEquals(List.of(5L, 0L), results.counts);
    }

    @Test
    public void partialWritesRetainCountsAndFailTheirResultRecords() throws Exception {
        rejected("POST /books/_update_by_query?conflicts=proceed", 200, "{\"updated\":2,\"deleted\":1,\"version_conflicts\":3}", OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        rejected("POST /books/_delete_by_query", 500, "{\"deleted\":4,\"failures\":[{\"status\":500}]}", OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        rejected("POST /books/_delete_by_query", 408, "{\"deleted\":1,\"timed_out\":true}", OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_TIMEOUT);
        rejected("PUT /books/_doc/1\n{}", 200, "{\"_index\":\"books\",\"result\":\"updated\",\"_shards\":{\"failed\":1}}", OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        assertEquals(List.of(3L, 4L, 1L, 1L), results.counts);
        assertEquals(4, results.completions.stream().filter("ResultCountBuild:false"::equals).count());
        assertFalse(results.completions.contains("ResultCountBuild:true"));
    }

    @Test
    public void conflictsAndHttpErrorsNeverReportSuccess() throws Exception {
        rejected("PUT /books/_create/1\n{}", 409, "{\"error\":{\"type\":\"version_conflict_engine_exception\"}}", OpenSearchDsI18nKeys.OPENSEARCH_VERSION_CONFLICT);
        rejected("GET /books/_doc/1", 404, "{\"error\":{\"type\":\"index_not_found_exception\"}}", OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        rejected("GET /books/_count", 500, "{\"count\":0}", OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        rejected("HEAD /books/_doc/1", 403, "", OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        assertTrue(results.counts.isEmpty());
        assertTrue(results.completions.isEmpty());
    }

    @Test
    public void headAndIndexAcknowledgementsRemainDistinctFromDocumentCounts() throws Exception {
        write("HEAD /books", 200, "");
        write("HEAD /books/_doc/1", 404, "");
        write("PUT /books\n{}", 200, "{\"acknowledged\":true,\"shards_acknowledged\":true}");
        write("PUT /books/_mapping\n{\"properties\":{}}", 200, "{\"acknowledged\":true}");
        write("PUT /books/_settings\n{\"number_of_replicas\":0}", 200, "{\"acknowledged\":true}");
        rejected("DELETE /books", 200, "{\"acknowledged\":false}", OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        assertEquals(List.of(0L, 0L, 0L, 0L), results.counts);
        assertEquals(3, results.completions.stream().filter("ResultCountBuild:true"::equals).count());
    }

    @Test
    public void malformedAndPartialResponsesCannotBecomeEmptySuccesses() throws Exception {
        rejected("GET /books/_search", 200, "{\"hits\":{\"hits\":[]},\"_shards\":{\"failed\":1}}", OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        rejected("GET /books/_search", 200, "{\"hits\":{\"hits\":[{\"_index\":\"other\"}]}}", OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        rejected("GET /books/_count", 200, "{\"count\":-1}", OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        rejected("POST /books/_update_by_query", 200, "{\"updated\":1}", OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        rejected("POST /books/_update_by_query", 200, "{\"updated\":9223372036854775807,\"deleted\":1}", OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_INVALID);
        assertTrue(results.rows.isEmpty());
        assertTrue(results.counts.isEmpty());
    }

    @Test
    public void rowAndByteLimitsAndMaskingAreAppliedBeforeSuccessfulCompletion() throws Exception {
        limits.setFetchRecordCountLimit(1);
        write("GET /books/_search", 200, "{\"hits\":{\"hits\":[{\"_index\":\"books\",\"_source\":{}},{\"_index\":\"books\",\"_source\":{}}]}}");
        assertEquals(1, results.rows.size());
        limits.setFetchResultSetBytesLimit(1);
        rejected("GET /books/_source/1", 200, "{\"name\":\"abc\"}", OpenSearchDsI18nKeys.OPENSEARCH_RESULT_SIZE_LIMIT);
        assertTrue(results.completions.contains("ResultSetRowsBuild:false"));
        limits.setFetchResultSetBytesLimit(0);
        masking = true;
        rejected("GET /books/_search", 200, "{\"hits\":{\"hits\":[]},\"aggregations\":{}}", OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
    }

    private void rejected(String command, int status, String body, String key) throws Exception {
        try {
            write(command, status, body);
            fail(command);
        } catch (ThirdPartyApiException e) {
            assertEquals(key, e.getMessageKey());
        }
    }

    private void write(String command, int status, String body) throws Exception {
        QueryRequest query = new QueryRequest();
        query.setQueryBody(command);
        query.setResultConf(limits);
        query.setUsingValueProcess(masking);
        var parameters = RestProduct.OPENSEARCH.parameters("3.9.0");
        var request = new EsRequestParser(parameters).parse(command);
        var operation = EsRequestPolicy.analyze(request, parameters).operation();
        var mapper = JsonMapper.builder().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).build();
        var response = new OpenSearchJsonResponse(status, mapper.readTree(body));
        new OpenSearchResultWriter(query, results.builder(), new OpenSearchExecutionContext(5000, 0), System.currentTimeMillis()).write(request, operation, response);
    }
}
