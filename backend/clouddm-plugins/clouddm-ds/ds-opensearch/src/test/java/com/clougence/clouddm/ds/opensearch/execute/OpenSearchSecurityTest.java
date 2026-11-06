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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.clougence.clouddm.ds.opensearch.definition.OpenSearchSecRulesSupportSpi;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.es.*;

public class OpenSearchSecurityTest {
    private OpenSearchHttpStub         stub;
    private OpenSearchSession          session;
    private OpenSearchConfig           config;
    private RecordingOpenSearchResults results;

    @Before
    public void prepare() throws Exception {
        stub = new OpenSearchHttpStub();
        config = new OpenSearchConfig();
        config.setRequestTimeoutMs(3000);
        session = stub.session(config, Map.of("requestTimeoutMs", "3000"));
        stub.responder = OpenSearchHttpStub::metadata;
        results = new RecordingOpenSearchResults();
    }

    @After
    public void close() {
        if (session != null) {
            session.close();
        }
        if (stub != null) {
            stub.close();
        }
    }

    @Test
    public void readOnlyBlocksEveryWriteOperationBeforeNetworkAndConfigCannotBeOverridden() {
        config.setReadOnly(true);
        session.setReadOnly(false);
        for (String command : List
            .of("POST /books/_doc\n{}", "PUT /books/_doc/1\n{}", "PUT /books/_create/1\n{}", "POST /books/_update/1\n{\"doc\":{}}", "DELETE /books/_doc/1", "POST /books/_update_by_query\n{}", "POST /books/_delete_by_query\n{}", "PUT /books\n{}", "DELETE /books", "PUT /books/_mapping\n{}", "PUT /books/_settings\n{}")) {
            reject(query(command, false), OpenSearchDsI18nKeys.OPENSEARCH_READ_ONLY);
        }
        assertTrue(stub.requests.isEmpty());
        assertTrue(session.isReadOnly());
    }

    @Test
    public void sessionReadOnlyCanBeEnabledAndDisabledWithoutTransactionChanges() {
        String command = "PUT /books/_doc/1\n{}";
        session.setReadOnly(true);
        assertTrue(session.isReadOnly());
        assertTrue(session.isAutoCommit());
        reject(query(command, false), OpenSearchDsI18nKeys.OPENSEARCH_READ_ONLY);
        assertTrue(stub.requests.isEmpty());

        session.setReadOnly(false);
        assertFalse(session.isReadOnly());
        stub.responder = exchange -> {
            var metadata = OpenSearchHttpStub.metadata(exchange);
            if (metadata != null) {
                return metadata;
            }
            return OpenSearchHttpStub.json(201, "{\"_index\":\"books\",\"_id\":\"1\",\"result\":\"created\"}");
        };
        results = new RecordingOpenSearchResults();
        session.executeQuery(query(command, false), results.builder());
        assertFalse(results.errorNotified);
        assertTrue(stub.requests.stream().anyMatch(request -> request.startsWith("PUT /books/_doc/1")));
    }

    @Test
    public void missingRelatedIndexOrTamperedBehaviorCannotReachTheServer() {
        String command = "POST /books/_update_by_query\n{\"query\":{\"terms\":{\"tag\":{\"index\":\"lookup\",\"id\":\"1\",\"path\":\"tags\"}}}}";
        QueryRequest missing = query(command, false);
        missing.setResolvedResources(Map.of(TargetType.Table, List.of("books")));
        reject(missing, EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        QueryRequest tampered = query(command, false);
        tampered.getRelations().get(0).setTarget(List.of());
        reject(tampered, EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        assertTrue(stub.requests.isEmpty());
    }

    @Test
    public void relatedIndexIsCheckedWithTheDatabaseAccountBeforeSearch() {
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/_resolve/index/lookup")) {
                return OpenSearchHttpStub.json(403, "{\"error\":{\"reason\":\"secret-lookup\"}}");
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        reject(query("GET /books/_search\n{\"query\":{\"terms\":{\"tag\":{\"index\":\"lookup\",\"id\":\"1\",\"path\":\"tags\"}}}}", false), OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
        assertTrue(stub.requests.stream().anyMatch(r -> r.startsWith("GET /_resolve/index/lookup")));
        assertFalse(stub.requests.stream().anyMatch(r -> r.startsWith("GET /books/_search")));
        assertFalse(results.messages.toString().contains("secret-lookup"));
    }

    @Test
    public void databaseDenialsAreNotRetriedAndDoNotPoisonTheSession() {
        for (int status : List.of(401, 403)) {
            stub.requests.clear();
            stub.responder = exchange -> {
                OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
                if (meta != null) {
                    return meta;
                }
                return OpenSearchHttpStub.json(status, "{\"error\":{\"reason\":\"secret-document\"}}");
            };
            reject(query("PUT /books/_doc/1\n{}", true), OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
            assertEquals(1, stub.requests.stream().filter(r -> r.startsWith("PUT /books/_doc/1")).count());
            assertFalse(results.messages.toString().contains("secret-document"));
        }
        stub.responder = exchange -> {
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            return OpenSearchHttpStub.json(200, "{\"count\":0}");
        };
        session.executeQuery(query("GET /books/_count", false), results.builder());
        assertFalse(session.isExecuting());
    }

    @Test
    public void maskingRejectsUnsafeProjectionsAndMetadataBeforeAnyRequest() {
        for (String command : List
            .of("GET /books", "GET /books/_mapping", "GET /books/_settings", "GET /books/_alias", "GET /books/_search?stored_fields=secret", "GET /books/_doc/1?stored_fields=secret", "GET /books/_search\n{\"aggs\":{\"x\":{\"terms\":{\"field\":\"secret\"}}}}", "GET /books/_search\n{\"script_fields\":{\"copy\":{\"script\":{\"source\":\"1\"}}}}", "GET /books/_search\n{\"highlight\":{\"fields\":{\"secret\":{}}}}")) {
            reject(query(command, true), OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
        }
        assertTrue(stub.requests.isEmpty());
    }

    @Test
    public void unsafeNestedResponseFailsBeforeAnyRowsAndWriteMessagesDoNotEchoSource() {
        stub.responder = exchange -> {
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            return OpenSearchHttpStub.json(200, "{\"hits\":{\"hits\":[{\"_index\":\"books\",\"_id\":\"1\",\"_source\":{},\"inner_hits\":{\"secret\":\"value\"}}]}}");
        };
        reject(query("GET /books/_search", true), OpenSearchDsI18nKeys.OPENSEARCH_MASKING_UNSUPPORTED);
        assertTrue(results.rows.isEmpty());
        stub.responder = exchange -> {
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            return OpenSearchHttpStub.json(200, "{\"_index\":\"books\",\"result\":\"updated\",\"get\":{\"_source\":{\"secret\":\"value\"}}}");
        };
        session.executeQuery(query("POST /books/_update/1\n{\"doc\":{\"secret\":\"value\"},\"_source\":true}", true), results.builder());
        assertEquals(List.of(1L), results.counts);
        assertFalse(results.messages.toString().contains("secret"));
    }

    @Test
    public void createRejectsMatchingComposableAndLegacyTemplatesAndUnreadableMetadata() {
        for (String endpoint : List.of("/_index_template", "/_template")) {
            stub.requests.clear();
            stub.responder = exchange -> {
                if (exchange.getRequestURI().getPath().equals(endpoint)) {
                    if (endpoint.equals("/_index_template")) {
                        return OpenSearchHttpStub
                            .json(200, "{\"index_templates\":[{\"name\":\"t\",\"index_template\":{\"index_patterns\":[\"book*\"],\"composed_of\":[\"hidden-alias\"]}}]}");
                    }
                    return OpenSearchHttpStub.json(200, "{\"legacy\":{\"index_patterns\":[\"*oks\"],\"aliases\":{\"other\":{}}}}");
                }
                return OpenSearchHttpStub.metadata(exchange);
            };
            reject(query("PUT /books\n{}", false), OpenSearchDsI18nKeys.OPENSEARCH_TEMPLATE_UNSUPPORTED);
            assertFalse(stub.requests.stream().anyMatch(r -> r.startsWith("PUT /books")));
        }
        stub.responder = exchange -> OpenSearchHttpStub.json(403, "{}");
        reject(query("PUT /books\n{}", false), OpenSearchDsI18nKeys.OPENSEARCH_HTTP_ERROR);
    }

    @Test
    public void createWithoutMatchingTemplatesAndDottedPatternUsesLiteralMatching() {
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/_index_template")) {
                return OpenSearchHttpStub.json(200, "{\"index_templates\":[{\"index_template\":{\"index_patterns\":[\"b.oks\"]}}]}");
            }
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            return OpenSearchHttpStub.json(200, "{\"acknowledged\":true}");
        };
        session.executeQuery(query("PUT /books\n{}", false), results.builder());
        assertEquals(List.of(0L), results.counts);
        assertEquals(1, stub.requests.stream().filter(r -> r.startsWith("PUT /books")).count());
    }

    @Test
    public void enabledSystemPipelinesAndMalformedSettingsFailClosed() {
        for (String settings : List
            .of("{\"transient\":{},\"persistent\":{\"cluster.search.enabled_system_generated_factories\":[\"custom\"]},\"defaults\":{}}", "{\"transient\":{},\"persistent\":{},\"defaults\":{\"cluster.search.enabled_system_generated_factories\":[\"*\"]}}", "{}")) {
            stub.responder = exchange -> {
                if (exchange.getRequestURI().getPath().equals("/_cluster/settings")) {
                    return OpenSearchHttpStub.json(200, settings);
                }
                return OpenSearchHttpStub.metadata(exchange);
            };
            String key = OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED;
            if (settings.equals("{}")) {
                key = OpenSearchDsI18nKeys.OPENSEARCH_METADATA_INVALID;
            }
            reject(query("GET /books/_search", false), key);
        }
        assertFalse(stub.requests.stream().anyMatch(r -> r.startsWith("GET /books/_search")));
    }

    @Test
    public void emptyTransientSystemPipelineListOverridesPersistentValue() {
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/_cluster/settings")) {
                return OpenSearchHttpStub
                    .json(200, "{\"transient\":{\"cluster.search.enabled_system_generated_factories\":[]},\"persistent\":{\"cluster.search.enabled_system_generated_factories\":[\"custom\"]},\"defaults\":{}}");
            }
            OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
            if (meta != null) {
                return meta;
            }
            return OpenSearchHttpStub.json(200, "{\"hits\":{\"hits\":[]}}");
        };
        session.executeQuery(query("GET /books/_search", true), results.builder());
    }

    @Test
    public void modelBackedVectorMappingIsAnImplicitResource() {
        stub.responder = exchange -> {
            if (exchange.getRequestURI().getPath().equals("/books/_mapping")) {
                return OpenSearchHttpStub.json(200, "{\"books\":{\"mappings\":{\"properties\":{\"v\":{\"type\":\"knn_vector\",\"model_id\":\"model\"}}}}}");
            }
            return OpenSearchHttpStub.metadata(exchange);
        };
        reject(query("PUT /books/_doc/1\n{}", false), OpenSearchDsI18nKeys.OPENSEARCH_IMPLICIT_RESOURCE_UNSUPPORTED);
    }

    @Test
    public void olderVersionsDoNotRequireClusterSettingsPermission() {
        for (String version : List.of("2.0.0", "2.19.0", "3.0.0", "3.2.0")) {
            session.close();
            stub.version = version;
            session = stub.session(config, Map.of("requestTimeoutMs", "3000"));
            stub.responder = exchange -> {
                if (exchange.getRequestURI().getPath().equals("/_cluster/settings")) {
                    return OpenSearchHttpStub.json(403, "{}");
                }
                OpenSearchResponse meta = OpenSearchHttpStub.metadata(exchange);
                if (meta != null) {
                    return meta;
                }
                return OpenSearchHttpStub.json(200, "{\"hits\":{\"hits\":[]}}");
            };
            session.executeQuery(query("GET /books/_search", false), results.builder());
            assertFalse(stub.requests.stream().anyMatch(r -> r.startsWith("GET /_cluster/settings")));
        }
    }

    @Test
    public void securityRulesExposeIndexAndDocumentFieldRanges() {
        var spi = new OpenSearchSecRulesSupportSpi();
        assertTrue(spi.isSupport());
        assertEquals(List.of(TargetType.Table), spi.exactRangeForQuery());
        assertEquals(List.of(TargetType.Table, TargetType.Column), spi.exactRangeForSen());
        assertEquals(spi.exactRangeForSen(), spi.prefixRangeForSen());
        assertEquals(spi.exactRangeForSen(), spi.suffixRangeForSen());
        assertEquals(spi.exactRangeForSen(), spi.includeRangeForSen());
    }

    private QueryRequest query(String command, boolean masking) {
        var parameters = RestProduct.OPENSEARCH.parameters("3.9.0");
        QueryRequest query = new QueryRequest();
        query.setQueryId("security-test");
        query.setQueryBody(command);
        query.setResultConf(new QueryResultConf());
        query.setUsingValueProcess(masking);
        try (var behavior = new EsBehaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(command), Map.of(), 1, 0)) {
            query.setRelations(behavior.toList().get(0).getRelations());
        }
        var analysis = EsRequestPolicy.analyze(new EsRequestParser(parameters).parse(command), parameters);
        query.setResolvedResources(Map.of(TargetType.Table, analysis.indices()));
        return query;
    }

    private void reject(QueryRequest query, String key) {
        try {
            session.executeQuery(query, results.builder());
            fail(query.getQueryBody());
        } catch (ThirdPartyApiException expected) {
            assertEquals(key, expected.getMessageKey());
        }
        assertTrue(results.errorNotified);
        assertFalse(session.isExecuting());
        assertNull(session.getCurrentQueryId());
        assertTrue(results.completions.contains("ResultMessageBuild:false"));
    }
}
