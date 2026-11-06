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
package com.clougence.clouddm.sec.rules.execute.sensitive;

import static org.junit.Assert.*;

import java.lang.reflect.Proxy;
import java.sql.JDBCType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;
import com.clougence.clouddm.sdk.service.secrules.SensitiveConfig;
import com.clougence.clouddm.sdk.service.secrules.SensitiveMode;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.function.EFunction;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenSearchDocumentMaskingTest {
    private SecValueProcessServiceProvider service;
    private CheckerRule                    rule;
    private QueryRequest                   query;
    private final Map<String, ColMetaData> metadata = new LinkedHashMap<>();

    @Before
    @SuppressWarnings("unchecked")
    public void prepare() {
        rule = new CheckerRule();
        rule.setRuleName("nested document fields");
        rule.setScriptType("DetectRules");
        rule.setScript("if @func.string.equalsIgnoreCase(@domain.column, 'people.secret') then return 'algorithm::FULL_MASK' else return 'algorithm::ORIGINAL' end");
        rule.setParameters(Map.of());
        rule.setRangeList(List.of());
        rule.setSenMode(SensitiveMode.Value);
        SensitiveConfig config = new SensitiveConfig();
        config.setDsId(7);
        config.setDsType(DataSourceType.OpenSearch);
        config.setSenRuleList(List.of(rule));
        ConfigService configs = (ConfigService) Proxy.newProxyInstance(ConfigService.class.getClassLoader(), new Class<?>[] { ConfigService.class }, (p, method, args) -> {
            return switch (method.getName()) {
                case "fetchSensitiveConfigByDs" -> config;
                case "fetchDsLevelDef" -> List.of(UmiTypes.Instance, UmiTypes.Table);
                default -> throw new AssertionError(method.getName());
            };
        });
        CacheService cache = (CacheService) Proxy.newProxyInstance(CacheService.class.getClassLoader(), new Class<?>[] { CacheService.class }, (p, method, args) -> {
            if (method.getName().equals("getObjectIfAbsent")) {
                return ((EFunction<String, Object, Exception>) args[args.length - 1]).apply((String) args[0]);
            }
            throw new AssertionError(method.getName());
        });
        service = new SecValueProcessServiceProvider(cache, configs);
        query = new QueryRequest();
        query.setDsId(7L);
        query.setDsType(DataSourceType.OpenSearch);
        query.setUsingValueProcess(true);
        query.setQueryBody("GET /books/_search");
        for (String name : List.of("_id", "_source")) {
            ColMetaData column = new ColMetaData();
            column.setTable("books");
            column.setColumn(name);
            column.setIndex(metadata.size());
            column.setJdbcType(JDBCType.VARCHAR);
            column.setColumnType("VARCHAR");
            if (name.equals("_source")) {
                column.setColumnType(ColMetaData.JSON_DOCUMENT);
            }
            metadata.put(name, column);
        }
    }

    @Test
    public void actualValueProcessorMasksNestedArrayAndDottedFieldsWithoutLosingPrecision() throws Exception {
        String source = "{\"people\":[{\"secret\":\"one\",\"public\":true},{\"secret\":\"two\"}],\"people.secret\":\"three\",\"large\":18446744073709551615,\"price\":1.2300,\"empty\":[],\"nil\":null}";
        List<String> processed = process(List.of("id", source));
        var json = new ObjectMapper().readTree(processed.get(1));
        assertEquals("******", json.at("/people/0/secret").asText());
        assertEquals("******", json.at("/people/1/secret").asText());
        assertEquals("******", json.path("people.secret").asText());
        assertTrue(json.at("/people/0/public").booleanValue());
        assertTrue(processed.get(1).contains("18446744073709551615"));
        assertTrue(processed.get(1).contains("1.2300"));
        assertTrue(json.path("nil").isNull());
        assertEquals("id", processed.get(0));
    }

    @Test
    public void rowMaskCoversDocumentAndMetadataAndRepeatedReadsUseTheSameRules() {
        rule.setSenMode(SensitiveMode.Row);
        List<String> original = List.of("id", "{\"people\":[{\"secret\":\"one\"}],\"public\":123}");
        List<String> first = process(original);
        assertEquals("******", first.get(0));
        assertFalse(first.get(1).contains("one"));
        assertFalse(first.get(1).contains("123"));
        assertEquals(first, process(original));
    }

    @Test
    public void malformedOrNonObjectDocumentFailsInsteadOfReturningRawData() {
        for (String source : List.of("{", "{} {}", "[\"secret\"]")) {
            try {
                process(List.of("id", source));
                fail(source);
            } catch (ThirdPartyApiException expected) {
                assertNotNull(expected);
            }
        }
    }

    @Test
    public void exemptQueryPreservesOriginalValues() {
        query.setUsingValueProcess(false);
        List<String> values = List.of("id", "{\"people\":[{\"secret\":\"one\"}]}");
        assertEquals(values, process(values));
    }

    private List<String> process(List<String> values) {
        Map<String, Object> flash = new HashMap<>();
        service.begin(query, metadata, flash);
        try {
            return service.processRow(query, metadata, values, flash);
        } finally {
            service.finish(query, flash);
            assertTrue(flash.isEmpty());
        }
    }
}
