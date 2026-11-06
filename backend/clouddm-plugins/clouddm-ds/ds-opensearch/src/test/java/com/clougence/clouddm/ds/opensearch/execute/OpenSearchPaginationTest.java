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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.es.EsBehaviorAnalysisSpi;
import com.clougence.sql.es.EsRequestParser;
import com.clougence.sql.es.RestProduct;

public class OpenSearchPaginationTest {
    private OpenSearchHttpStub         stub;
    private OpenSearchSession          session;
    private OpenSearchPagingFixture    peer;
    private RecordingOpenSearchResults results;
    private QueryResultConf            limits;
    private boolean                    masking;

    private void prepare(String version) throws Exception {
        stub = new OpenSearchHttpStub();
        stub.version = version;
        peer = new OpenSearchPagingFixture();
        stub.responder = peer;
        OpenSearchConfig config = new OpenSearchConfig();
        config.setRequestTimeoutMs(10000);
        session = stub.session(config, Map.of("requestTimeoutMs", "10000"));
        results = new RecordingOpenSearchResults();
        limits = new QueryResultConf();
        limits.setFetchPageSize(10);
    }

    @After
    public void close() {
        if (peer != null) {
            peer.release.countDown();
            peer.releaseClose.countDown();
        }
        if (session != null) {
            session.close();
        }
        if (stub != null) {
            stub.close();
        }
    }

    @Test
    public void oldVersionsUseOneScrollSnapshotAndKeepEveryCachedRow() throws Exception {
        for (String version : List.of("2.0.0", "2.4.1", "2.19.5", "3.0.0", "3.2.0")) {
            prepare(version);
            execute("GET /books/_search\n{\"size\":3000}");
            assertSnapshot(2505);
            assertFalse(peer.pit);
            assertEquals(4, peer.page);
            assertEquals(List.of("scroll-4"), peer.cleared);
            assertTrue(results.refreshOnProgress);
            assertEquals(10, results.silentRows.stream().filter(silent -> !silent).count());
            assertTrue(results.completions.contains("ResultSetRowsBuild:true"));
            assertTrue(stub.requests.stream().anyMatch(request -> request.startsWith("POST /books/_search?") && request.contains("scroll=1m")));
            assertFalse(stub.requests.stream().anyMatch(request -> request.contains("point_in_time")));
            close();
        }
    }

    @Test
    public void pitPreservesDefaultScoreOrderAndExactSearchAfterAcrossPages() throws Exception {
        prepare("3.3.0");
        execute("GET /books/_search?routing=a%2Bb&preference=_local\n{\"size\":3000}");
        assertSnapshot(2505);
        assertTrue(peer.pit);
        var first = peer.searchBodies.get(0);
        assertEquals("desc", first.path("sort").get(0).path("_score").asText());
        assertEquals("asc", first.path("sort").get(1).path("_shard_doc").asText());
        assertEquals(9007199254741992L, peer.searchBodies.get(1).path("search_after").get(1).longValue());
        assertEquals("pit-1", peer.searchBodies.get(1).path("pit").path("id").asText());
        assertEquals(List.of("pit-4"), peer.cleared);
        assertFalse(results.metadata.get(0).containsKey("sort"));
        assertTrue(stub.requests.stream()
            .anyMatch(request -> request.startsWith("POST /books/_search/point_in_time?") && request.contains("routing=a%2Bb")
                                 && request.contains("allow_partial_pit_creation=false")));
        assertFalse(stub.requests.stream().anyMatch(request -> request.startsWith("POST /_search?") && request.contains("routing=")));
    }

    @Test
    public void explicitSortIsPreservedAndOnlyInternalTieBreakerIsHidden() throws Exception {
        prepare("3.9.0");
        peer.sorted = true;
        limits.setReceiveMode(ReceiveMode.STREAM);
        execute("GET /books/_search\n{\"size\":1205,\"sort\":{\"rank\":\"asc\"}}");
        assertSnapshot(1205);
        assertEquals(2, peer.page);
        assertEquals(205, peer.searchBodies.get(1).path("size").asInt());
        assertEquals("asc", peer.searchBodies.get(0).path("sort").get(0).path("rank").asText());
        assertEquals("[142]", results.rows.get(1000).get("sort"));
        assertTrue(results.silentRows.stream().noneMatch(Boolean::booleanValue));
        assertEquals(List.of("pit-2"), peer.cleared);
    }

    @Test
    public void explicitStableKeyIsNotDuplicatedOrRemoved() throws Exception {
        prepare("3.9.0");
        peer.sorted = true;
        execute("GET /books/_search\n{\"size\":1001,\"sort\":{\"rank\":\"asc\",\"_shard_doc\":\"asc\"}}");
        assertEquals(2, peer.searchBodies.get(0).path("sort").size());
        assertEquals("[0,9007199254740993]", results.rows.get(0).get("sort"));
        assertSnapshot(1001);
    }

    @Test
    public void scrollTrimsFinalBatchToEffectiveRequestAndPlatformLimit() throws Exception {
        prepare("2.19.5");
        peer.sorted = true;
        limits.setFetchRecordCountLimit(1205);
        execute("GET /books/_search?size=2500\n{\"size\":4000,\"sort\":[{\"rank\":\"asc\"}]}");
        assertSnapshot(1205);
        assertEquals(2, peer.page);
        assertEquals("[142]", results.rows.get(1000).get("sort"));
        assertEquals(List.of("scroll-2"), peer.cleared);
        assertFalse(stub.requests.stream().anyMatch(request -> request.contains("size=2500")));
    }

    @Test
    public void unsafeShapesAndScrollTotalTrackingRetainNativeExecution() throws Exception {
        var parser = new EsRequestParser(RestProduct.OPENSEARCH.parameters("3.9.0"));
        for (String command : List
            .of("GET /books/_search?sort=rank:asc\n{\"size\":2000}", "GET /books/_search\n{\"size\":2000,\"sort\":\"_doc\"}", "GET /books/_search\n{\"size\":2000,\"from\":1}", "GET /books/_search\n{\"size\":2000,\"aggs\":{\"n\":{\"sum\":{\"field\":\"x\"}}}}", "GET /books/_search\n{\"size\":2000,\"search_after\":[1]}", "GET /books/_search\n{\"size\":2000,\"query\":{\"range\":{\"date\":{\"gte\":\"now-1d\"}}}}", "GET /books/_search\n{\"size\":2000,\"query\":{\"function_score\":{\"random_score\":{}}}}", "GET /books/_search\n{\"size\":2000,\"query\":{\"terms\":{\"tag\":{\"index\":\"books\",\"id\":\"1\",\"path\":\"tags\"}}}}")) {
            assertFalse(command, OpenSearchSearchCursor.supports(parser.parse(command), false));
            assertFalse(command, OpenSearchSearchCursor.supports(parser.parse(command), true));
        }
        var request = parser.parse("GET /books/_search\n{\"size\":2000,\"track_total_hits\":false}");
        assertFalse(OpenSearchSearchCursor.supports(request, false));
        assertTrue(OpenSearchSearchCursor.supports(request, true));
        assertTrue(OpenSearchSearchCursor.supports(parser.parse("GET /books/_search\n{\"size\":2000,\"track_total_hits\":true}"), false));
        prepare("3.9.0");
        stub.responder = exchange -> {
            var metadata = OpenSearchHttpStub.metadata(exchange);
            if (metadata != null) {
                return metadata;
            }
            return OpenSearchHttpStub.json(200, "{\"hits\":{\"hits\":[]},\"aggregations\":{\"n\":{\"value\":0}}}");
        };
        execute("GET /books/_search\n{\"size\":2000,\"aggs\":{\"n\":{\"sum\":{\"field\":\"x\"}}}}");
        assertFalse(stub.requests.stream().anyMatch(sent -> sent.contains("point_in_time") || sent.contains("scroll")));
        assertEquals(2, results.metadata.size());
    }

    @Test
    public void routedDuplicateIdsWithinAndAcrossPagesKeepEveryDocument() throws Exception {
        for (String version : List.of("2.19.5", "3.9.0")) {
            prepare(version);
            peer.routedDuplicateIds = true;
            execute("GET /books/_search\n{\"size\":3000}");
            assertEquals(2505, results.rows.size());
            assertTrue(results.metadata.get(0).containsKey("_routing"));
            for (int i = 0; i < results.rows.size(); i++) {
                var row = results.rows.get(i);
                assertEquals("doc-" + ((i + 1) / 2), row.get("_id"));
                assertEquals("route-" + (i % 2), row.get("_routing"));
                assertEquals(i, peer.mapper.readTree((String) row.get("_source")).path("id").intValue());
            }
            assertEquals(2505, results.metrics.get(results.metrics.size() - 1).intValue());
            assertEquals(List.of(peer.latestId), peer.cleared);
            assertTrue(results.completions.contains("ResultSetRowsBuild:true"));
            assertFalse(results.errorNotified);
            assertFalse(session.isExecuting());
            close();
        }
    }

    @Test
    public void routedPagesStillRejectUnmaskedRoutingWhenMaskingIsRequired() throws Exception {
        for (String version : List.of("2.19.5", "3.9.0")) {
            prepare(version);
            peer.routedDuplicateIds = true;
            masking = true;
            rejected(OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
            assertTrue(results.rows.isEmpty());
            assertEquals(List.of(peer.latestId), peer.cleared);
            assertTrue(results.errorNotified);
            assertFalse(session.isExecuting());
            close();
        }
    }

    @Test
    public void repeatedPitSortValuesWithinAndAcrossPagesStillFail() throws Exception {
        for (int repeatedAt : List.of(1, 1000)) {
            prepare("3.9.0");
            peer.repeatSortAt = repeatedAt;
            rejected(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_NOT_ADVANCING);
            assertEquals(repeatedAt / 1000 * 1000, results.rows.size());
            assertEquals(List.of(peer.latestId), peer.cleared);
            assertTrue(results.errorNotified);
            assertFalse(session.isExecuting());
            close();
        }
    }

    @Test
    public void prematureScrollEndStillFailsInsteadOfSilentlyDroppingRows() throws Exception {
        prepare("2.19.5");
        peer.emptyPage = 2;
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_INCOMPLETE);
        assertEquals(1000, results.rows.size());
        assertEquals(List.of("scroll-2"), peer.cleared);
    }

    @Test
    public void pageFailureIsNotRetriedAndCleanupFailureIsSuppressed() throws Exception {
        prepare("3.9.0");
        peer.failPage = 2;
        peer.closeFailure = true;
        ThirdPartyApiException failure = rejected(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        assertEquals(1, failure.getSuppressed().length);
        assertEquals(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_CLOSE_FAILED, ((ThirdPartyApiException) failure.getSuppressed()[0]).getMessageKey());
        assertEquals(2, peer.page);
        assertEquals(List.of("pit-1"), peer.cleared);
        assertTrue(results.errorNotified);
        assertTrue(results.completions.contains("ResultSetRowsBuild:false"));
        assertFalse(session.isExecuting());
    }

    @Test
    public void successfulRowsDoNotHideFailedCleanup() throws Exception {
        prepare("2.19.5");
        peer.closeFailure = true;
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_CLOSE_FAILED);
        assertEquals(2505, results.rows.size());
        assertTrue(results.completions.contains("ResultSetRowsBuild:false"));
        assertFalse(results.completions.contains("ResultSetRowsBuild:true"));
    }

    @Test
    public void partialPitCreationReleasesReturnedIdWithoutStartingSearch() throws Exception {
        prepare("3.9.0");
        peer.partialOpen = true;
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_PARTIAL_RESPONSE);
        assertEquals(0, peer.page);
        assertEquals(List.of("pit-open"), peer.cleared);
    }

    @Test
    public void deniedPitDoesNotFallbackToAnotherSnapshot() throws Exception {
        prepare("3.9.0");
        stub.responder = exchange -> {
            var metadata = OpenSearchHttpStub.metadata(exchange);
            if (metadata != null) {
                return metadata;
            }
            return OpenSearchHttpStub.json(403, "{\"error\":{\"type\":\"security_exception\"}}");
        };
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        assertEquals(1, stub.requests.stream().filter(request -> request.startsWith("POST ")).count());
    }

    @Test
    public void cumulativeOutputLimitAppliesAcrossPagesAndReleasesCursor() throws Exception {
        prepare("3.9.0");
        peer.payload = "x".repeat(500);
        limits.setFetchResultSetBytesLimit(900000);
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_RESULT_SIZE_LIMIT);
        assertTrue(results.rows.size() > 1000);
        assertTrue(results.rows.size() < 2000);
        assertEquals(List.of("pit-2"), peer.cleared);
        assertTrue(results.completions.contains("ResultSetRowsBuild:false"));
    }

    @Test
    public void outputFailureAndCancellationBothReleaseTheLatestCursor() throws Exception {
        prepare("3.9.0");
        results.onRow = row -> {
            throw new IllegalStateException("output unavailable");
        };
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_QUERY_FAILED);
        assertEquals(List.of("pit-1"), peer.cleared);
        close();
        prepare("2.19.5");
        results.onRow = row -> session.cancel();
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
        assertEquals(List.of("scroll-1"), peer.cleared);
        assertFalse(session.isExecuting());
    }

    @Test
    public void closeDuringNextPageStillClearsKnownCursorBeforeClosingTransport() throws Exception {
        prepare("3.9.0");
        peer.blockPage = 2;
        var worker = Executors.newSingleThreadExecutor();
        try {
            var running = worker.submit(() -> rejected(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED));
            assertTrue(peer.arrived.await(3, TimeUnit.SECONDS));
            session.close();
            running.get(3, TimeUnit.SECONDS);
            assertEquals(List.of("pit-1"), peer.cleared);
            assertFalse(session.isExecuting());
        } finally {
            peer.release.countDown();
            worker.shutdownNow();
        }
    }

    @Test
    public void repeatedCancelDoesNotAbortIndependentCleanup() throws Exception {
        prepare("2.19.5");
        peer.blockPage = 2;
        peer.blockClose = true;
        var worker = Executors.newSingleThreadExecutor();
        try {
            var running = worker.submit(() -> rejected(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED));
            assertTrue(peer.arrived.await(3, TimeUnit.SECONDS));
            session.cancel();
            assertTrue(peer.closing.await(3, TimeUnit.SECONDS));
            session.cancel();
            peer.releaseClose.countDown();
            ThirdPartyApiException failure = running.get(3, TimeUnit.SECONDS);
            assertEquals(0, failure.getSuppressed().length);
            assertEquals(List.of("scroll-1"), peer.cleared);
            assertFalse(session.isExecuting());
        } finally {
            peer.release.countDown();
            peer.releaseClose.countDown();
            worker.shutdownNow();
        }
    }

    @Test
    public void completedCursorResponsesRetainIdsWhenCancelOrCloseRacesDecoding() throws Exception {
        for (String version : List.of("2.19.5", "3.9.0")) {
            for (boolean closeSession : List.of(false, true)) {
                prepare(version);
                var mapperField = OpenSearchClient.class.getDeclaredField("mapper");
                mapperField.setAccessible(true);
                mapperField.set(stub.client(), new OpenSearchResponseHookMapper(response -> {
                    if (response.has("pit_id") || response.has("_scroll_id")) {
                        if (closeSession) {
                            session.close();
                        } else {
                            session.cancel();
                        }
                    }
                }));
                rejected(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED);
                if (peer.pit) {
                    assertEquals(List.of("pit-open"), peer.cleared);
                } else {
                    assertEquals(List.of("scroll-1"), peer.cleared);
                }
                assertTrue(results.rows.isEmpty());
                assertFalse(session.isExecuting());
                close();
            }
        }
    }

    @Test
    public void queryDeadlineDoesNotPreventCleanupOrSubsequentRequests() throws Exception {
        prepare("3.9.0");
        peer.blockPage = 2;
        limits.setQueryTimeoutSec(1);
        rejected(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_TIMEOUT);
        assertEquals(List.of("pit-1"), peer.cleared);
        assertFalse(session.isExecuting());
        peer.release.countDown();
        stub.responder = exchange -> {
            var metadata = OpenSearchHttpStub.metadata(exchange);
            if (metadata != null) {
                return metadata;
            }
            return OpenSearchHttpStub.json(200, "{\"count\":0}");
        };
        execute("GET /books/_count");
    }

    @Test
    public void blockedOutputCannotKeepClosedClientResourcesAliveIndefinitely() throws Exception {
        prepare("3.9.0");
        var entered = new java.util.concurrent.CountDownLatch(1);
        var resume = new java.util.concurrent.CountDownLatch(1);
        var deadlinesField = OpenSearchClient.class.getDeclaredField("deadlines");
        deadlinesField.setAccessible(true);
        var deadlines = (java.util.concurrent.ScheduledExecutorService) deadlinesField.get(stub.client());
        results.onRow = row -> {
            entered.countDown();
            try {
                resume.await(8, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        var worker = Executors.newSingleThreadExecutor();
        try {
            var running = worker.submit(() -> rejected(OpenSearchDsI18nKeys.OPENSEARCH_REQUEST_CANCELLED));
            assertTrue(entered.await(3, TimeUnit.SECONDS));
            session.close();
            assertTrue(deadlines.awaitTermination(7, TimeUnit.SECONDS));
            resume.countDown();
            ThirdPartyApiException failure = running.get(2, TimeUnit.SECONDS);
            assertEquals(1, failure.getSuppressed().length);
            assertEquals(OpenSearchDsI18nKeys.OPENSEARCH_CURSOR_CLOSE_FAILED, ((ThirdPartyApiException) failure.getSuppressed()[0]).getMessageKey());
            assertFalse(session.isExecuting());
        } finally {
            resume.countDown();
            worker.shutdownNow();
        }
    }

    private void assertSnapshot(int expected) {
        assertEquals(expected, results.rows.size());
        assertEquals(IntStream.range(0, expected).mapToObj(i -> "doc-" + i).toList(), results.rows.stream().map(row -> row.get("_id")).toList());
        assertEquals(1, results.metadata.size());
        assertEquals(ColMetaData.JSON_DOCUMENT, results.metadata.get(0).get("_source").getMeta().getColumnType());
        assertEquals(expected, results.metrics.get(results.metrics.size() - 1).intValue());
        assertTrue(results.flushes > 0);
        assertFalse(results.errorNotified);
        assertFalse(session.isExecuting());
    }

    private ThirdPartyApiException rejected(String key) {
        try {
            execute("GET /books/_search\n{\"size\":3000}");
            throw new AssertionError("Expected " + key);
        } catch (ThirdPartyApiException e) {
            assertEquals(key, e.getMessageKey());
            return e;
        }
    }

    private void execute(String command) {
        QueryRequest query = new QueryRequest();
        query.setQueryId("page-test");
        query.setQueryBody(command);
        query.setResultConf(limits);
        query.setUsingValueProcess(masking);
        try (var analysis = new EsBehaviorAnalysisSpi(RestProduct.OPENSEARCH.parameters(stub.version)).analysisBehaviorStream(new StringReader(command), Map.of(), 1, 0)) {
            query.setRelations(analysis.toList().get(0).getRelations());
        }
        query.setResolvedResources(Map.of(TargetType.Table, List.of("books")));
        session.executeQuery(query, results.builder());
    }
}
