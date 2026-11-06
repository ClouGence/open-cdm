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

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;

public class EsDslRegressionTest {
    @Test
    public void versionBoundariesKeepExistingEsRules() {
        assertFalse(EsServerVersion.parse("7.10.2").capabilities().pitShardDoc());
        assertTrue(EsServerVersion.parse("7.12.0").capabilities().pitShardDoc());
        assertTrue(EsServerVersion.parse("7.17.0").capabilities().mappingTypes());
        assertFalse(EsServerVersion.parse("8.0.0").capabilities().mappingTypes());
        assertEquals(Integer.valueOf(8), EsServerVersion.parse("9.0.0").capabilities().compatibleApiMajor());
        for (String version : List.of("2.19.0", "3.3.0", "7.9.3", "10.0.0", "invalid")) {
            try {
                EsServerVersion.parse(version);
                fail(version);
            } catch (ThirdPartyApiException expected) {
                assertNotNull(expected.getMessageKey());
            }
        }
        parser("7.17.0").parse("GET /books/type/_search");
        rejected("8.0.0", "GET /books/type/_search", EsSqlI18nKeys.ES_VERSION_FEATURE_UNSUPPORTED);
        rejected("8.3.0", "GET /books/_search\n{\"knn\":{}}", EsSqlI18nKeys.ES_VERSION_FEATURE_UNSUPPORTED);
        parser("8.4.0").parse("GET /books/_search\n{\"knn\":{}}");
        rejected("8.13.0", "GET /books/_search\n{\"retriever\":{}}", EsSqlI18nKeys.ES_VERSION_FEATURE_UNSUPPORTED);
        parser("8.14.0").parse("GET /books/_search\n{\"retriever\":{}}");
        rejected("9.0.0", "GET /books/_knn_search", EsSqlI18nKeys.ES_VERSION_FEATURE_UNSUPPORTED);
    }

    @Test
    public void documentFieldsAreNotInterpretedAsDslFeatures() {
        String text = "PUT /books/_doc/1\n{\"knn\":{},\"retriever\":{},\"index\":\"secret\",\"query\":{\"terms\":{\"x\":{\"index\":\"other\"}}}}";
        EsRequestAnalysis analysis = EsRequestPolicy.analyze(parser("7.10.2").parse(text));
        assertEquals(EsOperation.REPLACE_DOCUMENT, analysis.operation());
        assertEquals(List.of("books"), analysis.indices());
    }

    @Test
    public void nestedLookupResourcesRemainVisibleToAnalysis() {
        String text = "GET /books/_search\n{\"query\":{\"bool\":{\"filter\":[{\"terms\":{\"tag\":{\"index\":\"lookup\",\"id\":\"1\",\"path\":\"tags\"}}}]}}}";
        EsRequest request = parser("8.19.0").parse(text);
        assertEquals(List.of("books", "lookup"), EsRequestPolicy.analyze(request).indices());
        assertFalse(EsSearchPagination.supports(request));
        try (var stream = new EsBehaviorAnalysisSpi(parameters("8.19.0")).analysisBehaviorStream(new StringReader(text), Map.of(), 1, 0)) {
            var relation = stream.toList().get(0).getRelations().get(0);
            assertEquals("books", relation.getSubject().getObjectName().getObjectName());
            assertEquals("lookup", relation.getTarget().get(0).getObjectName().getObjectName());
        }
    }

    @Test
    public void executionScopeRejectsUnsafeRoutesAndParameters() {
        for (String text : List.of("GET /_search", "GET /books*/_search", "GET /books%2Fother/_search", "POST /books/_bulk\n{}")) {
            try {
                parser("8.19.0").parse(text);
                fail(text);
            } catch (EsParseException expected) {
                assertNotNull(expected.getRange());
            }
        }
        for (String text : List.of("GET /books/_search?filter_path=hits", "POST /books/_delete_by_query?wait_for_completion=false", "GET /books/_search?size=1&size=2")) {
            try {
                EsRequestPolicy.analyze(parser("8.19.0").parse(text));
                fail(text);
            } catch (EsParseException expected) {
                assertNotNull(expected.getMessageKey());
            }
        }
    }

    @Test
    public void editorFormattingPreservesNumbersAndStatementBoundaries() {
        String text = "# 😀\nPUT /books/_doc/1\n{\"number\":9007199254740993,\"text\":\"GET /other/_search\"}\nGET /books/_count";
        String formatted = EsEditorParser.format(text);
        assertTrue(formatted.contains("9007199254740993"));
        assertTrue(formatted.startsWith("# 😀\n"));
        var statements = EsEditorParser.parse(formatted);
        assertEquals(2, statements.size());
        assertEquals("GET", statements.get(1).method());
        assertEquals("/books/_count", statements.get(1).path());
        try (var stream = new EsSplitAnalysisSpi(parameters("8.19.0")).splitScriptStream(new StringReader(text), List.of(), 4, 2)) {
            var split = stream.toList();
            assertEquals(2, split.size());
            // The streaming split range includes the leading comment retained in the script.
            assertEquals(4, split.get(0).getBodyStartCodeLine());
            assertTrue(split.get(0).getScript().startsWith("# 😀"));
        }
    }

    @Test
    public void rewriteLimitKeepsUnicodeAndQueryLiterals() {
        String text = "GET /books/_search\n{\"query\":{\"term\":{\"name\":\"😀\"}},\"size\":100}";
        RewriteContext context = new RewriteContext();
        context.setFetchLimit(20);
        String rewritten = new EsRewriteSpi(parameters("8.19.0")).rewriteLimit("query", text, context);
        assertEquals(text.replace("100", "20"), rewritten);
        assertEquals(20, EsSearchLimit.resolve(parser("8.19.0").parse(rewritten)).size());
    }

    private static SqlParserParameters parameters(String version) {
        return new SqlParserParameters(Map.of(SqlParserParameters.VERSION, version));
    }

    private static EsRequestParser parser(String version) {
        return new EsRequestParser(parameters(version));
    }

    private static void rejected(String version, String text, String key) {
        try {
            parser(version).parse(text);
            fail(text);
        } catch (EsParseException expected) {
            assertEquals(key, expected.getMessageKey());
        }
    }
}
