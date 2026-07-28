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
package com.clougence.clouddm.ds.lineage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.ds.TextTestFramework;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Execution(ExecutionMode.CONCURRENT)
public abstract class LineageTextTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @TestFactory
    public Stream<DynamicTest> lineageScripts() {
        return dynamicTests();
    }

    protected Stream<DynamicTest> dynamicTests() {
        return TextTestFramework.dynamicTests(fixtureResources(), LineageTextTest::loadCases, testCase -> {
            String datasource = testCase.datasource == null ? datasource() : testCase.datasource;
            return DynamicTest.dynamicTest(testCase.displayName(datasource), () -> assertCase(testCase.resourcePath(), testCase, lineageAnalysisSpi(datasource)));
        });
    }

    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles("lineage/" + datasource());
    }

    protected abstract String datasource();

    static List<TestCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(LineageTextTest::parseOneCase).toList();
    }

    static TestCase parseOneCase(CaseBlock block) {
        TestCase testCase = new TestCase(block);
        String body = block.body();

        int sqlIdx = body.indexOf("sql:");
        int expectIdx = body.indexOf("expect:");
        if (sqlIdx < 0 || expectIdx <= sqlIdx) {
            throw new IllegalArgumentException("Invalid column lineage test case: " + testCase.name());
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

    List<String> verify(TestCase testCase, LineageAnalysisSpi analysisSpi) {
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

        List<LineageColumn> items;
        try {
            items = analysisSpi.analyze(testCase.sql, lineageContext(context));
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

        if (expected.isArray()) {
            assertOrderedLineage(prefix(testCase), expected, items, failures);
        } else if (expected.isObject()) {
            assertLineage(prefix(testCase), expected, columnLineage(items), failures);
        } else {
            failures.add(prefix(testCase) + " expected column lineage object or array");
        }
        return failures;
    }

    void assertCase(String resourcePath, TestCase testCase, LineageAnalysisSpi analysisSpi) {
        List<String> failures = verify(testCase, analysisSpi);
        if (!failures.isEmpty()) {
            Assert.fail(resourcePath + System.lineSeparator() + String.join(System.lineSeparator(), failures));
        }
    }

    private static LineageAnalysisSpi lineageAnalysisSpi(String datasource) {
        LineageAnalysisSpi spi = SqlTestSupport.sqlEngine(datasource).lineageAnalysisSpi(SqlParserParameters.empty());
        if (spi == null) {
            throw new IllegalStateException("No LineageAnalysisSpi for datasource: " + datasource);
        }
        return spi;
    }

    private void assertLineage(String label, JsonNode expected, Map<String, List<String>> actual, List<String> failures) {
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

    private void assertOrderedLineage(String label, JsonNode expected, List<LineageColumn> actual, List<String> failures) {
        if (expected.size() != actual.size()) {
            failures.add(label + ".size: expected=" + expected.size() + ", actual=" + actual.size() + " " + summarize(actual));
            return;
        }
        for (int i = 0; i < expected.size(); i++) {
            JsonNode expectedColumn = expected.get(i);
            if (!expectedColumn.isObject() || !expectedColumn.path("column").isTextual() || !expectedColumn.path("sources").isArray()) {
                failures.add(label + "[" + i + "]: expected {\"column\": string, \"sources\": array}");
                return;
            }
            LineageColumn actualColumn = actual.get(i);
            String expectedName = expectedColumn.path("column").asText();
            if (!Objects.equals(expectedName, actualColumn.column())) {
                failures.add(label + "[" + i + "].column: expected=" + expectedName + ", actual=" + actualColumn.column());
                return;
            }
            assertColumnPaths(label + "[" + i + "].sources", expectedColumn.path("sources"), columnPaths(actualColumn.sources()), failures);
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

    private static LineageContext lineageContext(JsonNode context) {
        LineageContext.LineageContextBuilder builder = LineageContext.builder().levelsParam(levels(context));
        if (context.has("mcSchemaStyle")) {
            McConfig dataSourceConfig = new McConfig();
            dataSourceConfig.setSchemaStyle(context.get("mcSchemaStyle").asBoolean());
            builder.dsConfig(dataSourceConfig);
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

    private String summarize(List<LineageColumn> items) {
        List<String> summary = new ArrayList<>();
        items.forEach(column -> summary.add(column.column() + "=" + columnPaths(column.sources())));
        return summary.toString();
    }

    private List<String> columnPaths(List<SourceName> sourceNames) {
        List<String> paths = new ArrayList<>();
        for (SourceName sourceName : sourceNames) {
            paths.add(sourcePath(sourceName));
        }
        return paths;
    }

    protected String sourcePath(SourceName sourceName) {
        return sourceName.toDsResPath();
    }

    private Map<String, List<String>> columnLineage(List<LineageColumn> items) {
        Map<String, List<String>> lineage = new LinkedHashMap<>();
        items.forEach(column -> lineage.computeIfAbsent(column.column(), ignored -> new ArrayList<>()).addAll(columnPaths(column.sources())));
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
