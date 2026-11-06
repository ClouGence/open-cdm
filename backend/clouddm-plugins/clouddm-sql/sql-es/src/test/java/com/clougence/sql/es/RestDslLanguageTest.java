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

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.language.format.FormatRequest;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;

public class RestDslLanguageTest {
    @Test
    public void completionUsesProductAndVersion() {
        assertTrue(complete(RestProduct.ELASTICSEARCH, "8.19.0", "GET /books/_kn").contains("_knn_search"));
        assertFalse(complete(RestProduct.ELASTICSEARCH, "9.0.0", "GET /books/_kn").contains("_knn_search"));
        assertFalse(complete(RestProduct.OPENSEARCH, "3.9.0", "GET /books/_kn").contains("_knn_search"));
        assertFalse(complete(RestProduct.ELASTICSEARCH, "8.3.0", "GET /books/_search\n{\"kn").contains("knn"));
        assertTrue(complete(RestProduct.ELASTICSEARCH, "8.4.0", "GET /books/_search\n{\"kn").contains("knn"));
        assertFalse(complete(RestProduct.OPENSEARCH, "3.9.0", "GET /books/_search\n{\"kn").contains("knn"));
        assertTrue(complete(RestProduct.OPENSEARCH, "2.0.0", "PUT /books/_mapping?cl").contains("cluster_manager_timeout"));
        assertFalse(complete(RestProduct.ELASTICSEARCH, "8.19.0", "PUT /books/_mapping?cl").contains("cluster_manager_timeout"));
    }

    @Test
    public void validationPinsProductDespiteCallerSuppliedIdentity() {
        var language = new RestDslLanguageSpi(null, RestProduct.OPENSEARCH);
        ValidateRequest request = new ValidateRequest();
        request.setRequestId("validate");
        request.setRequestVersion(11);
        request.setSqlParameters(RestProduct.ELASTICSEARCH.parameters("3.9.0").values());
        request.setSqlText("PUT /books?cluster_manager_timeout=10s\n{}");
        assertTrue(language.validate(request).getDiagnostics().isEmpty());
        request.setSqlText("GET /books/_search\n{\"knn\":{}}");
        var result = language.validate(request);
        assertEquals(1, result.getDiagnostics().size());
        assertEquals("validate", result.getRequestId());
        assertEquals(11, result.getRequestVersion());
        assertNotNull(result.getDiagnostics().get(0).getRange());
    }

    @Test
    public void validationDoesNotMistakeLiteralNamesForCapabilities() {
        var language = new RestDslLanguageSpi(null, RestProduct.OPENSEARCH);
        ValidateRequest request = new ValidateRequest();
        request.setSqlParameters(RestProduct.OPENSEARCH.parameters("3.9.0").values());
        request.setSqlText("PUT /books/_doc/1\n{\"knn\":{},\"runtime\":{},\"retriever\":{}}");
        assertTrue(language.validate(request).getDiagnostics().isEmpty());
    }

    @Test
    public void editorOperationsRemainSyntaxOnlyAndShareTheSameGrammar() {
        String text = "# 😀\nGET /books/_search\n{\"knn\":{},\"number\":9007199254740993}\nGET /books/_count";
        for (RestProduct product : RestProduct.values()) {
            var language = new RestDslLanguageSpi(null, product);
            FormatRequest format = new FormatRequest();
            format.setSqlText(text);
            String formatted = language.format(format).getSqlText();
            assertTrue(formatted.contains("9007199254740993"));
            assertTrue(formatted.startsWith("# 😀"));
            SplitRequest split = new SplitRequest();
            split.setSqlText(formatted);
            split.setBasicCodeLine(4);
            split.setBasicCodeColumn(0);
            var statements = language.split(split).getStatements();
            assertEquals(2, statements.size());
            assertTrue(statements.get(1).getSql().startsWith("GET /books/_count"));
        }
    }

    private static List<String> complete(RestProduct product, String version, String text) {
        var language = new RestDslLanguageSpi(null, product);
        CompletionRequest request = new CompletionRequest();
        request.setSqlText(text);
        request.setSqlParameters(product.parameters(version).values());
        request.setCursorLineNumber((int) text.chars().filter(c -> c == '\n').count() + 1);
        request.setCursorColNumber(text.length() - text.lastIndexOf('\n') - 1);
        return language.complete(request).getItems().stream().map(CompletionItem::getLabel).toList();
    }
}
