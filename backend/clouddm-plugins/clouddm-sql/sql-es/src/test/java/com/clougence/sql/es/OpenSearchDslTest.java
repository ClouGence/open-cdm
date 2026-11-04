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
package com.clougence.sql.es;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;

public class OpenSearchDslTest {
    @Test
    public void commonRequestsAndNativeManagerTimeoutAreSupported() {
        for (String version : List.of("2.0.0", "2.19.0", "3.0.0", "3.9.0")) {
            for (String text : List
                .of("GET /books/_search", "GET /books/_count", "PUT /books?cluster_manager_timeout=10s\n{}", "PUT /books?master_timeout=10s\n{}", "PUT /books/_mapping?cluster_manager_timeout=10s\n{\"properties\":{\"title\":{\"type\":\"text\"}}}")) {
                assertEquals("books", analyze(version, text).index().name());
            }
        }
        try {
            EsRequestPolicy.analyze(new EsRequestParser(RestProduct.ELASTICSEARCH.parameters("8.19.0")).parse("PUT /books?cluster_manager_timeout=10s\n{}"));
            fail();
        } catch (EsParseException expected) {
            assertEquals(EsSqlI18nKeys.ES_PARAMETER_OUT_OF_SCOPE, expected.getMessageKey());
        }
    }

    @Test
    public void foreignApisAndOutOfScopeQueriesAreRejected() {
        for (String text : List
            .of("GET /books/type/_search", "PUT /books?include_type_name=true\n{}", "GET /books/_knn_search\n{}", "GET /books/_search\n{\"knn\":{}}", "GET /books/_search\n{\"retriever\":{}}", "GET /books/_search\n{\"runtime_mappings\":{}}", "GET /books/_search\n{\"query\":{\"knn\":{\"v\":{\"vector\":[1],\"k\":1}}}}", "GET /books/_search\n{\"query\":{\"neural\":{}}}", "GET /books/_search\n{\"query\":{\"hybrid\":{}}}")) {
            rejected("3.9.0", text);
        }
        rejected("3.9.0", "GET /books/_search?search_pipeline=other");
        rejected("3.9.0", "GET /books/_search?filter_path=hits");
        rejected("3.9.0", "POST /books/_delete_by_query?wait_for_completion=false");
    }

    @Test
    public void nestedQueryPositionsUseTheSameProductPolicy() {
        for (String text : List
            .of("GET /books/_search\n{\"query\":{\"bool\":{\"filter\":[{\"knn\":{}}]}}}", "GET /books/_search\n{\"aggs\":{\"selected\":{\"filter\":{\"knn\":{}}}}}", "GET /books/_search\n{\"highlight\":{\"highlight_query\":{\"knn\":{}}}}", "GET /books/_search\n{\"aggs\":{\"sample\":{\"top_hits\":{\"knn\":{}}}}}")) {
            rejected("3.9.0", text);
        }
    }

    @Test
    public void vectorScriptPluginCannotBypassTheInitialQueryScope() {
        rejected("3.9.0", "GET /books/_search\n{\"query\":{\"script_score\":{\"query\":{\"match_all\":{}},\"script\":{\"lang\":\"knn\",\"source\":\"knn_score\",\"params\":{}}}}}");
        analyze("3.9.0", "GET /books/_search\n{\"query\":{\"script_score\":{\"query\":{\"match_all\":{}},\"script\":{\"lang\":\"painless\",\"source\":\"1\",\"params\":{\"knn\":1}}}}}");
    }

    @Test
    public void mappingDifferencesDoNotMatchOrdinaryFieldNames() {
        rejected("3.9.0", "PUT /books/_mapping\n{\"runtime\":{}}");
        rejected("3.9.0", "PUT /books\n{\"mappings\":{\"subobjects\":false}}");
        rejected("3.9.0", "PUT /books/_mapping\n{\"properties\":{\"obj\":{\"subobjects\":false}}}");
        rejected("3.9.0", "PUT /books/_mapping\n{\"dynamic_templates\":[{\"x\":{\"runtime\":{\"type\":\"keyword\"}}}]}");
        assertEquals(EsOperation.PUT_MAPPING, analyze("3.9.0", "PUT /books/_mapping\n{\"properties\":{\"runtime\":{\"type\":\"keyword\"},\"subobjects\":{\"type\":\"keyword\"}}}")
            .operation());
        assertEquals(EsOperation.REPLACE_DOCUMENT, analyze("3.9.0", "PUT /books/_doc/1\n{\"knn\":{},\"retriever\":{},\"runtime\":{},\"subobjects\":false}").operation());
        analyze("3.9.0", "GET /books/_search\n{\"query\":{\"term\":{\"knn\":\"retriever\"}},\"aggs\":{\"knn\":{\"terms\":{\"field\":\"runtime\"}}}}");
    }

    @Test
    public void shardDocSortIsGatedInBothUrlAndJson() {
        for (String version : List.of("2.19.0", "3.2.0")) {
            rejected(version, "GET /books/_search?sort=_shard_doc:asc");
            rejected(version, "GET /books/_search\n{\"sort\":[\"_shard_doc\"]}");
            rejected(version, "GET /books/_search\n{\"sort\":[{\"_shard_doc\":\"asc\"}]}");
        }
        analyze("3.3.0", "GET /books/_search\n{\"sort\":[{\"_shard_doc\":\"asc\"}]}");
    }

    @Test
    public void enginePinsProductForAllVersionAwareEntryPoints() {
        var engine = new OpenSearchSqlEngineSpi();
        var spoofed = RestProduct.ELASTICSEARCH.parameters("3.9.0");
        String text = "PUT /books?cluster_manager_timeout=10s\n{}";
        try (var split = engine.splitAnalysisSpi(spoofed).splitScriptStream(new StringReader(text), List.of(), 1, 0)) {
            assertEquals(1, split.count());
        }
        try (var analyzed = engine.behaviorAnalysisSpi(spoofed).analysisBehaviorStream(new StringReader(text), Map.of(), 1, 0)) {
            assertEquals(1, analyzed.count());
        }
        try (var domains = engine.secDomainResolveSpi(spoofed).resolveDomainStream(DataSourceType.OpenSearch, new StringReader(text), 1, 0, ContextInfo.builder().build())) {
            assertEquals(DataSourceType.OpenSearch, domains.toList().get(0).getDsType());
        }
        assertEquals(OpenSearchSqlEngineSpi.NAME, engine.dslProvider(spoofed).getDslName()[0]);
        RewriteContext context = new RewriteContext();
        context.setFetchLimit(5);
        assertTrue(engine.rewriteSpi(spoofed).rewriteLimit("q", "GET /books/_search", context).contains("5"));
        try {
            engine.dslProvider(RestProduct.ELASTICSEARCH.parameters("8.19.0"));
            fail();
        } catch (ThirdPartyApiException expected) {
            assertEquals(EsSqlI18nKeys.OPENSEARCH_SERVER_VERSION_INVALID, expected.getMessageKey());
        }
    }

    @Test
    public void resourceAnalysisAndRewritingKeepProductContext() {
        var parameters = RestProduct.OPENSEARCH.parameters("3.9.0");
        String text = "GET /books/_search\n{\"query\":{\"terms\":{\"tag\":{\"index\":\"lookup\",\"id\":\"1\",\"path\":\"tags\"}}}}";
        assertEquals(List.of("books", "lookup"), analyze("3.9.0", text).indices());
        try (var behavior = new EsBehaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(text), Map.of(), 1, 0)) {
            assertEquals("lookup", behavior.toList().get(0).getRelations().get(0).getTarget().get(0).getObjectName().getObjectName());
        }
        RewriteContext context = new RewriteContext();
        context.setFetchLimit(10);
        try {
            new EsRewriteSpi(parameters).rewriteLimit("q", "GET /books/_search\n{\"knn\":{}}", context);
            fail();
        } catch (EsParseException expected) {
            assertEquals(EsSqlI18nKeys.OPENSEARCH_FEATURE_UNSUPPORTED, expected.getMessageKey());
        }
    }

    private static EsRequestAnalysis analyze(String version, String text) {
        var parameters = RestProduct.OPENSEARCH.parameters(version);
        return EsRequestPolicy.analyze(new EsRequestParser(parameters).parse(text), parameters);
    }

    private static void rejected(String version, String text) {
        try {
            analyze(version, text);
            fail(text);
        } catch (EsParseException expected) {
            assertNotNull(expected.getRange());
        }
    }
}
