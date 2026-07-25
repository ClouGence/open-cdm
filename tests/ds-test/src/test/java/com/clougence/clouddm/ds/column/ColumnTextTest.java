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
package com.clougence.clouddm.ds.column;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.ds.TextTestFramework;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectItem;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ColumnTextTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @TestFactory
    public Stream<DynamicTest> columnScripts() {
        return dynamicTests();
    }

    public static Stream<DynamicTest> dynamicTests() {
        return TextTestFramework.dynamicTests(listResourceFiles("column"), ColumnTextTest::loadCases, testCase -> {
            String datasource = testCase.datasource == null ? SqlTestSupport.datasourceFromPath(testCase.resourcePath()) : testCase.datasource;
            return DynamicTest.dynamicTest(testCase.displayName(datasource), () -> assertCase(testCase.resourcePath(), testCase, selectColumnAnalysisSpi(datasource)));
        });
    }

    static List<String> listResourceFiles(String resourceDir) {
        return TextCaseSupport.resourceFiles(resourceDir);
    }

    static List<TestCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(ColumnTextTest::parseOneCase).toList();
    }

    static TestCase parseOneCase(CaseBlock block) {
        TestCase testCase = new TestCase(block);
        String body = block.body();

        int sqlIdx = body.indexOf("sql:");
        int expectIdx = body.indexOf("expect:");
        if (sqlIdx < 0 || expectIdx <= sqlIdx) {
            throw new IllegalArgumentException("Invalid column test case: " + testCase.name());
        }

        String preSql = body.substring(0, sqlIdx);
        testCase.datasource = readOptionalLine(preSql, "datasource:");
        testCase.contextJson = readSection(preSql, "context:");
        testCase.sql = body.substring(sqlIdx + "sql:".length(), expectIdx).trim();
        testCase.expectJson = body.substring(expectIdx + "expect:".length()).trim();
        return testCase;
    }

    private static String readOptionalLine(String text, String prefix) {
        return TextCaseSupport.readOptionalLine(text, prefix);
    }

    private static String readSection(String text, String prefix) {
        int index = text.indexOf(prefix);
        if (index < 0) {
            return null;
        }
        return text.substring(index + prefix.length()).strip();
    }

    static List<String> verify(TestCase testCase, SelectColumnAnalysisSpi analysisSpi) {
        List<String> failures = new ArrayList<>();
        JsonNode expected;
        try {
            expected = OBJECT_MAPPER.readTree(testCase.expectJson);
        } catch (IOException e) {
            failures.add(prefix(testCase) + " invalid expect JSON: " + e.getMessage());
            return failures;
        }
        JsonNode context;
        try {
            context = testCase.contextJson == null || testCase.contextJson.isBlank() ? OBJECT_MAPPER.createObjectNode() : OBJECT_MAPPER.readTree(testCase.contextJson);
        } catch (IOException e) {
            failures.add(prefix(testCase) + " invalid context JSON: " + e.getMessage());
            return failures;
        }

        List<SelectItem> items;
        try {
            items = analysisSpi.parseSelectColumn(testCase.sql, contextInfo(context));
        } catch (Exception e) {
            if (expected.has("exception")) {
                assertExpectedException(testCase, expected.get("exception"), e, failures);
                return failures;
            }
            failures.add(prefix(testCase) + " unexpected exception: " + e.getClass().getName() + ": " + e.getMessage());
            return failures;
        }

        if (expected.has("exception")) {
            failures.add(prefix(testCase) + " expected exception=" + expected.get("exception").asText() + ", actual items=" + summarize(items));
            return failures;
        }

        if (!expected.isObject()) {
            failures.add(prefix(testCase) + " expected column lineage object");
            return failures;
        }
        assertLineage(prefix(testCase), expected, columnLineage(items), failures);
        return failures;
    }

    static void assertCase(String resourcePath, TestCase testCase, SelectColumnAnalysisSpi analysisSpi) {
        List<String> failures = verify(testCase, analysisSpi);
        if (!failures.isEmpty()) {
            Assert.fail(resourcePath + System.lineSeparator() + String.join(System.lineSeparator(), failures));
        }
    }

    private static SelectColumnAnalysisSpi selectColumnAnalysisSpi(String datasource) {
        SelectColumnAnalysisSpi spi = SqlTestSupport.sqlEngine(datasource).selectColumnAnalysisSpi(SqlParserParameters.empty());
        if (spi == null) {
            throw new IllegalStateException("No SelectColumnAnalysisSpi for datasource: " + datasource);
        }
        return spi;
    }

    private static void assertLineage(String label, JsonNode expected, Map<String, List<String>> actual, List<String> failures) {
        if (expected.size() != actual.size()) {
            failures.add(label + ".size: expected=" + expected.size() + ", actual=" + actual.size() + " " + actual);
            return;
        }
        Iterator<Map.Entry<String, JsonNode>> expectedFields = expected.fields();
        Iterator<Map.Entry<String, List<String>>> actualFields = actual.entrySet().iterator();
        while (expectedFields.hasNext() && actualFields.hasNext()) {
            Map.Entry<String, JsonNode> expectedField = expectedFields.next();
            Map.Entry<String, List<String>> actualField = actualFields.next();
            String expectedName = expectedField.getKey();
            if (!Objects.equals(expectedName, actualField.getKey())) {
                failures.add(label + ".column: expected=" + expectedName + ", actual=" + actualField.getKey());
                return;
            }
            assertColumnPaths(label + "." + expectedName, expectedField.getValue(), actualField.getValue(), failures);
        }
    }

    private static void assertColumnPaths(String label, JsonNode expected, List<String> actual, List<String> failures) {
        List<String> expectedPaths = new ArrayList<>();
        for (JsonNode node : expected) {
            if (!node.isTextual()) {
                failures.add(label + ": expected column path string, actual=" + node);
                return;
            }
            expectedPaths.add(node.asText());
        }
        if (expectedPaths.size() != actual.size()) {
            failures.add(label + ".size: expected=" + expectedPaths.size() + ", actual=" + actual.size() + " " + actual);
            return;
        }
        for (int i = 0; i < expectedPaths.size(); i++) {
            if (!Objects.equals(expectedPaths.get(i), actual.get(i))) {
                failures.add(label + "[" + i + "]: expected=" + expectedPaths.get(i) + ", actual=" + actual.get(i));
            }
        }
    }

    private static void assertExpectedException(TestCase testCase, JsonNode expected, Exception actual, List<String> failures) {
        String expectedName = expected.asText();
        Class<?> actualClass = actual.getClass();
        if (!Objects.equals(expectedName, actualClass.getSimpleName()) && !Objects.equals(expectedName, actualClass.getName())) {
            failures.add(prefix(testCase) + " exception: expected=" + expectedName + ", actual=" + actualClass.getName() + ": " + actual.getMessage());
        }
    }

    private static ContextInfo contextInfo(JsonNode context) {
        ContextInfo.ContextInfoBuilder builder = ContextInfo.builder().levelsParam(levels(context));
        if (context.has("mcSchemaStyle")) {
            McConfig dataSourceConfig = new McConfig();
            dataSourceConfig.setSchemaStyle(context.get("mcSchemaStyle").asBoolean());
            builder.dataSourceConfig(dataSourceConfig);
        }
        return builder.build();
    }

    private static Map<UmiTypes, Object> levels(JsonNode context) {
        Map<UmiTypes, Object> levels = new HashMap<>();
        levels.put(UmiTypes.Schema, "schema1");
        levels.put(UmiTypes.Catalog, "catalog1");
        JsonNode node = context.get("levels");
        if (node == null || !node.isObject()) {
            return levels;
        }
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (StringUtils.isNotBlank(entry.getKey()) && entry.getValue().isValueNode()) {
                levels.put(UmiTypes.valueOf(entry.getKey()), entry.getValue().asText());
            }
        }
        return levels;
    }

    private static String prefix(TestCase testCase) {
        return "[" + testCase.name() + "]";
    }

    private static String summarize(List<SelectItem> items) {
        List<String> summary = new ArrayList<>();
        for (SelectItem item : items) {
            summary.add(item.getItemAlias() + "=" + columnPaths(item));
        }
        return summary.toString();
    }

    private static List<String> columnPaths(SelectItem item) {
        List<String> paths = new ArrayList<>();
        for (RealColumn column : item.getColumns()) {
            paths.add(column.toDsResPath());
        }
        return paths;
    }

    private static Map<String, List<String>> columnLineage(List<SelectItem> items) {
        Map<String, List<String>> lineage = new LinkedHashMap<>();
        for (SelectItem item : items) {
            lineage.computeIfAbsent(item.getItemAlias(), ignored -> new ArrayList<>()).addAll(columnPaths(item));
        }
        return lineage;
    }

    static class TestCase extends TextTestCase {

        TestCase(CaseBlock block){
            super(block);
        }

        String datasource;
        String contextJson;
        String sql;
        String expectJson;

        String displayName(String datasource) {
            return caseId() + " [" + datasource + "] " + summarize(sql);
        }
    }

    private static String summarize(String sql) {
        String text = sql.replaceAll("\\s+", " ").strip();
        if (text.length() <= 120) {
            return text;
        }
        return text.substring(0, 117) + "...";
    }
}
