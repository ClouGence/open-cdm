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
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;

public class OpenSearchPermissionAnalysisTest {
    @Test
    public void nestedResourcePositionsIncludeEveryLookupAndDefaultShapeIndex() {
        for (String version : List.of("2.0.0", "2.19.0", "3.0.0", "3.9.0")) {
            var parameters = RestProduct.OPENSEARCH.parameters(version);
            String command = """
                    POST /books/_update_by_query
                    {"query":{"bool":{"filter":[
                      {"terms":{"tag":{"index":"lookup","id":"1","path":"tags"}}},
                      {"geo_shape":{"location":{"indexed_shape":{"id":"2"}}}},
                      {"more_like_this":{"like":[{"_index":"examples","_id":"3"}],"unlike":[{"_id":"4"}]}}
                    ]}}}
                    """;
            var analysis = EsRequestPolicy.analyze(new EsRequestParser(parameters).parse(command), parameters);
            assertEquals(List.of("books", "lookup", "shapes", "examples"), analysis.indices());
            try (var stream = new EsBehaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(command), Map.of(), 1, 0)) {
                var relation = stream.toList().get(0).getRelations().get(0);
                assertEquals(BehaviorAction.UPDATE, relation.getAction());
                assertEquals(List.of("lookup", "shapes", "examples", "books"), relation.getTarget().stream().map(t -> t.getObjectName().getObjectName()).toList());
            }
            try (var stream = new EsSecDomainResolveSpi(parameters)
                .resolveDomainStream(DataSourceType.OpenSearch, new StringReader(command), 1, 0, ContextInfo.builder().build())) {
                var domain = (EsRequestDomain) stream.toList().get(0);
                assertEquals(analysis.indices(), domain.getIndices());
                assertEquals("UPDATE", domain.getSqlType().name());
            }
        }
    }

    @Test
    public void nestedSortHighlightAndAggregationFiltersAlsoCarryReadPermissions() {
        String lookup = "{\"terms\":{\"tag\":{\"index\":\"lookup\",\"id\":\"1\",\"path\":\"tags\"}}}";
        for (String body : List
            .of("{\"sort\":[{\"name\":{\"nested\":{\"path\":\"children\",\"filter\":" + lookup
                + "}}}]}", "{\"highlight\":{\"fields\":{\"name\":{\"highlight_query\":" + lookup
                           + "}}}}", "{\"aggs\":{\"a\":{\"filters\":{\"filters\":{\"match\":" + lookup + "}}}}}", "{\"rescore\":{\"query\":{\"rescore_query\":" + lookup + "}}}")) {
            var parameters = RestProduct.OPENSEARCH.parameters("3.9.0");
            var analysis = EsRequestPolicy.analyze(new EsRequestParser(parameters).parse("GET /books/_search\n" + body), parameters);
            assertEquals(List.of("books", "lookup"), analysis.indices());
        }
    }

    @Test
    public void encodedStoredPluginAndMultiResourceRequestsAreNotExecutable() {
        for (String text : List
            .of("GET /books,other/_search", "GET /books*/_search", "GET /remote:books/_search", "POST /_bulk\n{}", "GET /books/_search\n{\"query\":{\"wrapper\":{\"query\":\"e30=\"}}}", "GET /books/_search\n{\"query\":{\"percolate\":{\"field\":\"query\",\"index\":\"other\",\"id\":\"1\"}}}", "GET /books/_search\n{\"query\":{\"script\":{\"script\":{\"id\":\"saved\"}}}}", "POST /books/_doc?pipeline=enrich\n{}", "GET /books/_search\n{\"search_pipeline\":{}}")) {
            var parameters = RestProduct.OPENSEARCH.parameters("3.9.0");
            try {
                EsRequestPolicy.analyze(new EsRequestParser(parameters).parse(text), parameters);
                fail(text);
            } catch (EsParseException expected) {
                assertNotNull(expected.getMessageKey());
            }
        }
    }
}
