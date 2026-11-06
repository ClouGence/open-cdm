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

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.ds.opensearch.metadata.OpenSearchFieldType;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.es.EsBehaviorAnalysisSpi;
import com.clougence.sql.es.EsSqlI18nKeys;
import com.clougence.sql.es.RestProduct;

public class OpenSearchExecutionTest {
    private OpenSearchHttpStub         stub;
    private OpenSearchSession          session;
    private OpenSearchConfig           config;
    private RecordingOpenSearchResults results;

    @Before
    public void createClient() throws Exception {
        stub = new OpenSearchHttpStub();
        config = new OpenSearchConfig();
        config.setRequestTimeoutMs(3000);
        session = stub.session(config, Map.of("requestTimeoutMs", "3000", "maxResponseBytes", "4096"));
        results = new RecordingOpenSearchResults();
    }

    @After
    public void closeClient() {
        if (session != null) {
            session.close();
        }
        if (stub != null) {
            stub.close();
        }
    }

    @Test
    public void metadataListsOnlyConcreteIndicesAndPreservesOpenSearchFieldTypes() {
        stub.responder = exchange -> switch (exchange.getRequestURI().getPath()) {
            case "/_resolve/index/*" -> OpenSearchHttpStub
                .json(200, "{\"indices\":[{\"name\":\"z-books\"},{\"name\":\"books\"},{\"name\":\".ds-logs\",\"data_stream\":\"logs\"}],\"aliases\":[{\"name\":\"alias\"}],\"data_streams\":[{\"name\":\"logs\"}]}");
            case "/books" -> OpenSearchHttpStub.json(200, """
                    {"books":{"aliases":{},"settings":{"index":{"number_of_shards":"1"}},"mappings":{
                      "properties":{"title":{"type":"text","fields":{"raw":{"type":"keyword"}}},
                        "obj":{"properties":{"id":{"type":"unsigned_long"}}},"flat":{"type":"flat_object"},
                        "vector":{"type":"knn_vector"},"unknown":{"type":"future_type"}},
                      "derived":{"derived_value":{"type":"long","script":{"source":"emit(1)"}}}}}}
                    """);
            default -> OpenSearchHttpStub.metadata(exchange);
        };
        var meta = session.getMetaService();
        var indices = meta.listLeaf(Map.of(), UmiTypes.Table, "*");
        assertEquals(List.of("books", "z-books"), indices.stream().map(element -> element.getObjName()).toList());
        assertEquals(1, meta.listLeaf(Map.of(), UmiTypes.Table, "z-*").size());
        RdbTable table = (RdbTable) meta.detailLeaf(Map.of(), UmiTypes.Table, "books");
        assertEquals(OpenSearchFieldType.UnsignedLong, table.getColumns().get("obj.id").getSqlType());
        assertEquals(OpenSearchFieldType.FlatObject, table.getColumns().get("flat").getSqlType());
        assertEquals(OpenSearchFieldType.KnnVector, table.getColumns().get("vector").getSqlType());
        assertEquals(OpenSearchFieldType.Unknown, table.getColumns().get("unknown").getSqlType());
        assertEquals(OpenSearchFieldType.Keyword, table.getColumns().get("title.raw").getSqlType());
        assertEquals(DsType.OpenSearch, table.getColumns().get("obj.id").getSqlType().getDsType());
        assertEquals("true", table.getColumns().get("derived_value").getAttribute("derived"));
        assertTrue(table.getAttribute("settings").toString().contains("number_of_shards"));
        assertFalse(session.isExecuting());
    }

    @Test
    public void aliasesAndDefaultPipelinesAreRejectedBeforeDispatch() {
        stub.responder = exchange -> OpenSearchHttpStub.json(200, "{\"indices\":[],\"aliases\":[{\"name\":\"books\"}],\"data_streams\":[]}");
        rejected("GET /books/_search", EsSqlI18nKeys.ES_INDEX_NOT_CONCRETE);
        assertEquals(1, stub.requests.size());
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/books/_settings")) {
                return OpenSearchHttpStub.json(200, "{\"books\":{\"settings\":{\"index\":{\"search\":{\"default_pipeline\":\"filter\"}}}}}");
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        rejected("GET /books/_search", OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
        assertFalse(stub.requests.stream().anyMatch(request -> request.startsWith("GET /books/_search")));
        assertTrue(results.errorNotified);
    }

    @Test
    public void rawBodyAndEncodedParametersReachTheServerExactlyOnce() {
        respond(200, "{\"_index\":\"books\",\"result\":\"updated\"}");
        execute("PUT /books/_doc/a%20b?routing=a%2Bb%20c\n{\"amount\":18446744073709551615,\"price\":1.2300}");
        assertTrue(stub.requests.contains("PUT /books/_doc/a%20b?routing=a%2Bb+c\n{\"amount\":18446744073709551615,\"price\":1.2300}"));
        respond(200, "{\"deleted\":2}");
        execute("POST /books/_delete_by_query?conflicts=proceed");
        assertTrue(stub.requests.contains("POST /books/_delete_by_query?conflicts=proceed\n"));
        assertEquals(List.of(1L, 2L), results.counts);
        assertFalse(session.isExecuting());
        assertNull(session.getCurrentQueryId());
    }

    @Test
    public void lostWriteResponseIsNotReplayedAndTheSessionRecovers() {
        stub.responder = OpenSearchHttpStub::metadata;
        rejected("POST /books/_doc\n{}", OpenSearchDsI18nKeys.OPENSEARCH_QUERY_FAILED);
        assertEquals(1, stub.requests.stream().filter(request -> request.startsWith("POST /books/_doc\n")).count());
        assertTrue(results.errorNotified);
        assertFalse(session.isExecuting());
        respond(200, "{\"count\":0}");
        execute("GET /books/_count");
        assertEquals(1, results.rows.size());
    }

    @Test
    public void responseSizeAndMalformedJsonFailWithoutLeavingTheSessionBusy() {
        respond(200, "{\"data\":\"" + "x".repeat(5000) + "\"}");
        rejected("GET /books/_search", OpenSearchDsI18nKeys.OPENSEARCH_RESPONSE_SIZE_LIMIT);
        respond(200, "{} {}");
        rejected("GET /books/_count", OpenSearchDsI18nKeys.OPENSEARCH_QUERY_FAILED);
        respond(200, "{\"count\":0}");
        execute("GET /books/_count");
        assertFalse(session.isExecuting());
    }

    @Test
    public void cancelFreesSingleConnectionAndBlocksConcurrentExecution() throws Exception {
        CountDownLatch arrived = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        stub.responder = exchange -> {
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            arrived.countDown();
            try {
                release.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return OpenSearchHttpStub.json(200, "{\"count\":0}");
        };
        var worker = Executors.newSingleThreadExecutor();
        try {
            var running = worker.submit(() -> rejected("GET /books/_count", OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED));
            assertTrue(arrived.await(3, TimeUnit.SECONDS));
            assertTrue(session.isExecuting());
            assertEquals("unit-query", session.getCurrentQueryId());
            rejected("GET /books/_count", OpenSearchDsI18nKeys.OPENSEARCH_SESSION_BUSY);
            session.cancel();
            running.get(2, TimeUnit.SECONDS);
            respond(200, "{\"count\":0}");
            execute("GET /books/_count");
            assertFalse(session.isExecuting());
            assertNull(session.getCurrentQueryId());
        } finally {
            release.countDown();
            worker.shutdownNow();
        }
    }

    @Test
    public void overallDeadlineCoversPreflightAndLaterRequests() {
        config.setRequestTimeoutMs(150);
        stub.responder = exchange -> {
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        rejected("GET /books/_count", OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_TIMEOUT);
        assertFalse(stub.requests.stream().anyMatch(request -> request.startsWith("GET /books/_count")));
        assertFalse(session.isExecuting());
        config.setRequestTimeoutMs(3000);
        respond(200, "{\"count\":0}");
        execute("GET /books/_count");
    }

    @Test
    public void closeDuringPreflightPreventsTheMutation() throws Exception {
        CountDownLatch arrived = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        stub.responder = exchange -> {
            arrived.countDown();
            try {
                release.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        var worker = Executors.newSingleThreadExecutor();
        try {
            var running = worker.submit(() -> rejected("PUT /books/_doc/1\n{}", OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED));
            assertTrue(arrived.await(3, TimeUnit.SECONDS));
            session.close();
            running.get(2, TimeUnit.SECONDS);
            assertEquals(1, stub.requests.size());
            assertFalse(session.isExecuting());
        } finally {
            release.countDown();
            worker.shutdownNow();
        }
    }

    @Test
    public void missingIndexHeadAndConflictHaveDistinctTerminalStates() {
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().startsWith("/_resolve")) {
                return OpenSearchHttpStub.json(404, "{\"error\":{\"type\":\"index_not_found_exception\"}}");
            }
            return OpenSearchHttpStub.json(404, "{}");
        };
        execute("HEAD /books");
        assertFalse(results.errorNotified);
        respond(409, "{\"error\":{\"type\":\"version_conflict_engine_exception\"}}");
        rejected("PUT /books/_create/1\n{}", OpenSearchDsI18nKeys.OPENSEARCH_VERSION_CONFLICT);
        assertTrue(results.errorNotified);
        assertTrue(results.counts.isEmpty());
        assertFalse(session.isExecuting());
    }

    @Test
    public void jsonPrecisionAndNonJsonHttpErrorsKeepTheirOriginalMeaning() {
        respond(200, "{\"amount\":18446744073709551615,\"price\":1.2300}");
        execute("GET /books/_source/1");
        assertEquals("{\"amount\":18446744073709551615,\"price\":1.2300}", results.rows.get(0).get("_source"));
        respond(403, "Forbidden");
        rejected("GET /books/_count", OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        assertTrue(results.errorNotified);
        assertFalse(session.isExecuting());
    }

    @Test
    public void partialMutationKeepsAffectedCountAndNotifiesAuditFailure() {
        respond(409, "{\"updated\":2,\"deleted\":3,\"noops\":1,\"version_conflicts\":1,\"failures\":[{\"status\":409}]}");
        rejected("POST /books/_update_by_query", OpenSearchDsI18nKeys.OPENSEARCH_VERSION_CONFLICT);
        assertEquals(List.of(5L), results.counts);
        assertTrue(results.completions.contains("ResultCountBuild:false"));
        assertTrue(results.errorNotified);
        assertFalse(session.isExecuting());
    }

    @Test
    public void changedIndexScopeAndImplicitWriteResourcesAreBlocked() {
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/books/_mapping")) {
                return OpenSearchHttpStub.json(200, "{\"other\":{\"mappings\":{}}}");
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        rejected("PUT /books/_doc/1\n{}", OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID);
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/books/_settings")) {
                return OpenSearchHttpStub.json(200, "{\"books\":{\"settings\":{\"index\":{\"final_pipeline\":\"enrich\"}}}}");
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        rejected("PUT /books/_doc/1\n{}", OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
        rejected("PUT /books/_mapping\n{\"properties\":{\"text\":{\"type\":\"semantic\"}}}", OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
        assertFalse(stub.requests.stream().anyMatch(request -> request.startsWith("PUT ")));
    }

    private void respond(int status, String body) {
        stub.responder = exchange -> {
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            return OpenSearchHttpStub.json(status, body);
        };
    }

    private void rejected(String command, String key) {
        try {
            execute(command);
            fail(command);
        } catch (ThirdPartyApiException e) {
            assertEquals(key, e.getMessageKey());
        }
    }

    private void execute(String command) {
        QueryRequest query = new QueryRequest();
        query.setQueryId("unit-query");
        query.setQueryBody(command);
        query.setResultConf(new QueryResultConf());
        try (var analysis = new EsBehaviorAnalysisSpi(RestProduct.OPENSEARCH.parameters("3.9.0")).analysisBehaviorStream(new StringReader(command), Map.of(), 1, 0)) {
            query.setRelations(analysis.toList().get(0).getRelations());
        }
        query.setResolvedResources(Map.of(TargetType.Table, List.of("books")));
        session.executeQuery(query, results.builder());
    }
}
