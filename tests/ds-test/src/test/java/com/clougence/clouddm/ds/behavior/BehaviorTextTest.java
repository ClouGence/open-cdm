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
package com.clougence.clouddm.ds.behavior;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class BehaviorTextTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern      OBJECT_TEXT   = Pattern.compile("^([A-Za-z][A-Za-z0-9]*)\\(([1-9][0-9]*:[0-9]+~[1-9][0-9]*:[0-9]+)\\) (/.*)$");

    private BehaviorTextTest(){
    }

    public static List<TestCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(BehaviorTextTest::parseOneCase).toList();
    }

    private static TestCase parseOneCase(CaseBlock block) {
        TestCase testCase = new TestCase(block);
        String body = block.body();
        int sqlIdx = body.indexOf("sql:");
        int levelsIdx = body.indexOf("levels:");
        int baseIdx = body.indexOf("base:");
        int expectIdx = body.indexOf("expect:");
        if (sqlIdx < 0 || levelsIdx <= sqlIdx || expectIdx <= levelsIdx) {
            throw new IllegalArgumentException("Invalid behavior test case: " + testCase.name());
        }
        testCase.sql = body.substring(sqlIdx + "sql:".length(), levelsIdx).trim();
        int levelsEnd = baseIdx > levelsIdx && baseIdx < expectIdx ? baseIdx : expectIdx;
        testCase.levels = parseLevels(body.substring(levelsIdx + "levels:".length(), levelsEnd).trim(), testCase.name());
        if (levelsEnd == baseIdx) {
            parseBase(body.substring(baseIdx + "base:".length(), expectIdx).trim(), testCase);
        }
        testCase.expectJson = body.substring(expectIdx + "expect:".length()).trim();
        return testCase;
    }

    private static void parseBase(String value, TestCase testCase) {
        String[] parts = value.split(":", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Base must be '<line>:<column>' in " + testCase.name());
        }
        try {
            testCase.baseLine = Integer.parseInt(parts[0]);
            testCase.baseColumn = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid base in " + testCase.name() + ": " + value, e);
        }
        if (testCase.baseLine < 1 || testCase.baseColumn < 0) {
            throw new IllegalArgumentException("Base must use a 1-based line and 0-based column in " + testCase.name());
        }
    }

    private static Map<UmiTypes, Object> parseLevels(String levelsPath, String caseName) {
        String normalized = levelsPath.strip();
        if (!normalized.startsWith("/")) {
            throw new IllegalArgumentException("Invalid levels path in " + caseName + ": " + levelsPath);
        }
        String[] parts = normalized.substring(1).split("/", -1);
        if (parts.length != 4 || Arrays.stream(parts).anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Levels path must be '/<environment>/<datasourceId>/<catalog>/<schema>' in " + caseName + ": " + levelsPath);
        }
        return Map.of(UmiTypes.Instance, parts[0] + "/" + parts[1], UmiTypes.Catalog, parts[2], UmiTypes.Schema, parts[3]);
    }

    public static void assertStrictCase(String resourcePath, TestCase testCase, BehaviorAnalysisSpi spi, PermissionVerifier permissionVerifier) {
        List<String> failures = verifyStrict(testCase, spi, testCase.baseLine, testCase.baseColumn, permissionVerifier);
        if (!failures.isEmpty()) {
            Assert.fail(resourcePath + System.lineSeparator() + String.join(System.lineSeparator(), failures));
        }
    }

    public static List<String> verifyStrict(TestCase testCase, BehaviorAnalysisSpi spi, int baseLine, int baseColumn, PermissionVerifier permissionVerifier) {
        List<String> failures = new ArrayList<>();
        List<ExpectedStatement> expected;
        try {
            expected = parseExpectedStatements(testCase.expectJson);
        } catch (IOException e) {
            failures.add(prefix(testCase) + " invalid expect JSON: " + e.getMessage());
            return failures;
        }

        List<StatementBehavior> actual;
        try {
            actual = spi.analysisBehavior(testCase.sql, testCase.levels, baseLine, baseColumn);
        } catch (Exception e) {
            failures.add(prefix(testCase) + " unexpected exception: " + e.getClass().getName() + ": " + e.getMessage());
            return failures;
        }
        if (actual == null) {
            failures.add(prefix(testCase) + " analysisBehavior must not return null");
            return failures;
        }
        if (expected.size() != actual.size()) {
            failures.add(prefix(testCase) + ".size: expected=" + expected.size() + ", actual=" + actual.size());
            return failures;
        }
        for (int i = 0; i < expected.size(); i++) {
            verifyStatement(prefix(testCase) + "[" + i + "]", expected.get(i), actual.get(i), permissionVerifier, failures);
        }
        return failures;
    }

    private static List<ExpectedStatement> parseExpectedStatements(String expectJson) throws IOException {
        List<ExpectedStatement> statements = new ArrayList<>();
        try (JsonParser parser = OBJECT_MAPPER.createParser(expectJson)) {
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                throw new IOException("expect must be an ordered statement object");
            }
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                if (parser.currentToken() != JsonToken.FIELD_NAME) {
                    throw new IOException("statement type must be an object field");
                }
                String statementType = parser.currentName();
                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IOException(statementType + " relations must be an array");
                }
                JsonNode relations = OBJECT_MAPPER.readTree(parser);
                statements.add(new ExpectedStatement(statementType, relations));
            }
            if (parser.nextToken() != null) {
                throw new IOException("unexpected content after expect object");
            }
        }
        return statements;
    }

    private static void verifyStatement(String label, ExpectedStatement expected, StatementBehavior actual, PermissionVerifier permissionVerifier, List<String> failures) {
        assertEnum(label + ".statementType", expected.statementType(), actual.getStatementType(), failures);
        JsonNode expectedRelations = expected.relations();
        List<BehaviorRelation> actualRelations = actual.getRelations();
        if (expectedRelations == null || !expectedRelations.isArray()) {
            failures.add(label + ".relations must be an array");
            return;
        }
        if (actualRelations == null) {
            failures.add(label + ".relations must not be null");
            return;
        }
        if (expectedRelations.size() != actualRelations.size()) {
            failures.add(label + ".relations.size: expected=" + expectedRelations.size() + ", actual=" + actualRelations.size() + " " + summarizeRelations(actualRelations));
            return;
        }
        for (int i = 0; i < expectedRelations.size(); i++) {
            verifyRelation(label + ".relations[" + i + "]", expectedRelations.get(i), actualRelations.get(i), permissionVerifier, failures);
        }
    }

    private static void verifyRelation(String label, JsonNode expected, BehaviorRelation actual, PermissionVerifier permissionVerifier, List<String> failures) {
        if (expected == null || !expected.isObject()) {
            failures.add(label + " must be an object");
            return;
        }
        List<String> fields = fieldNames(expected);
        boolean hasTarget = expected.has("target");
        List<String> expectedFields = new ArrayList<>(List.of("subject", "action"));
        if (expected.has("permission")) {
            expectedFields.add("permission");
        }
        if (hasTarget) {
            expectedFields.add("target");
        }
        if (!fields.equals(expectedFields)) {
            failures.add(label + " fields must be [subject, action], optionally followed by target" + ", actual=" + fields);
            return;
        }
        assertEnum(label + ".action", expected.get("action"), actual.getAction(), failures);
        verifyObjectText(label + ".subject", expected.get("subject"), actual.getSubject(), failures);
        verifyPermission(label, expected.get("permission"), actual, permissionVerifier, failures);
        if (!hasTarget) {
            if (actual.getTarget() == null || !actual.getTarget().isEmpty()) {
                failures.add(label + ".target: omitted target requires an empty list, actual=" + actual.getTarget());
            }
            return;
        }

        JsonNode expectedTarget = expected.get("target");
        List<BehaviorObject> actualTarget = actual.getTarget();
        if (expectedTarget != null && expectedTarget.isTextual()) {
            if (actualTarget == null || actualTarget.size() != 1) {
                failures.add(label + ".target.size: expected=1, actual=" + (actualTarget == null ? "null" : actualTarget.size() + " " + summarize(actualTarget)));
                return;
            }
            verifyObjectText(label + ".target", expectedTarget, actualTarget.get(0), failures);
            return;
        }
        if (expectedTarget == null || !expectedTarget.isArray() || expectedTarget.size() < 2) {
            failures.add(label + ".target must be a string for one object or an array for multiple objects");
            return;
        }
        if (actualTarget == null || expectedTarget.size() != actualTarget.size()) {
            String msg = ", actual=" + (actualTarget == null ? "null" : actualTarget.size() + " " + summarize(actualTarget));
            failures.add(label + ".target.size: expected=" + expectedTarget.size() + msg);
            return;
        }
        for (int i = 0; i < expectedTarget.size(); i++) {
            verifyObjectText(label + ".target[" + i + "]", expectedTarget.get(i), actualTarget.get(i), failures);
        }
    }

    private static void verifyPermission(String label, JsonNode expected, BehaviorRelation actual, PermissionVerifier permissionVerifier, List<String> failures) {
        if (expected == null) {
            return;
        }
        if (!expected.isTextual() || (!"EXEMPT".equals(expected.asText()) && !"REQUIRED".equals(expected.asText()))) {
            failures.add(label + ".permission must be EXEMPT or REQUIRED");
            return;
        }
        if (permissionVerifier == null) {
            failures.add(label + ".permission requires a datasource permission verifier");
            return;
        }
        boolean expectedExempt = "EXEMPT".equals(expected.asText());
        boolean actualExempt = permissionVerifier.isPermissionExempt(actual);
        if (expectedExempt != actualExempt) {
            failures.add(label + ".permission: expected=" + expected.asText() + ", actual=" + (actualExempt ? "EXEMPT" : "REQUIRED"));
        }
    }

    private static void verifyObjectText(String label, JsonNode expected, BehaviorObject actual, List<String> failures) {
        JsonNode value = expected;
        if (expected != null && expected.isObject()) {
            List<String> fields = fieldNames(expected);
            if (!fields.equals(List.of("value", "catalog", "schema", "objectName"))) {
                failures.add(label + " object fields must be [value, catalog, schema, objectName], actual=" + fields);
                return;
            }
            value = expected.get("value");
        }
        if (value == null || !value.isTextual()) {
            failures.add(label + " must use '<TargetType>(<codeLine>) <resourcePath>'");
            return;
        }
        Matcher matcher = OBJECT_TEXT.matcher(value.asText());
        if (!matcher.matches()) {
            failures.add(label + " must match '<TargetType>(<codeLine>) <resourcePath>', actual=" + value.asText());
            return;
        }
        if (actual == null) {
            failures.add(label + ": actual BehaviorObject is null");
            return;
        }
        String actualType = actual.getObjectType() == null ? null : actual.getObjectType().name();
        if (!Objects.equals(matcher.group(1), actualType)) {
            failures.add(label + ".targetType: expected=" + matcher.group(1) + ", actual=" + actualType);
        }
        verifyCodeLine(label, matcher.group(2), actual, failures);
        if (!Objects.equals(matcher.group(3), actual.getObjectPath())) {
            failures.add(label + ".objectPath: expected=" + matcher.group(3) + ", actual=" + actual.getObjectPath());
        }
        if (expected.isObject()) {
            verifyObjectName(label, expected, actual.getObjectName(), failures);
        }
    }

    private static void verifyObjectName(String label, JsonNode expected, ObjectName actual, List<String> failures) {
        if (actual == null) {
            failures.add(label + ".objectName: actual ObjectName is null");
            return;
        }
        assertNullableText(label + ".catalog", expected.get("catalog"), actual.getCatalog(), failures);
        assertNullableText(label + ".schema", expected.get("schema"), actual.getSchema(), failures);
        assertNullableText(label + ".objectName", expected.get("objectName"), actual.getObjectName(), failures);
    }

    private static void assertNullableText(String label, JsonNode expected, String actual, List<String> failures) {
        String expectedValue = expected == null || expected.isNull() ? null : expected.asText();
        if (!Objects.equals(expectedValue, actual)) {
            failures.add(label + ": expected=" + expectedValue + ", actual=" + actual);
        }
    }

    private static void verifyCodeLine(String label, String codeLine, BehaviorObject actual, List<String> failures) {
        try {
            BehaviorCodeLine.Range range = BehaviorCodeLine.parse(codeLine);
            assertInt(label + ".startLine", range.startLine(), actual.getStartLine(), failures);
            assertInt(label + ".startColumn", range.startColumn(), actual.getStartColumn(), failures);
            assertInt(label + ".endLine", range.endLine(), actual.getEndLine(), failures);
            assertInt(label + ".endColumn", range.endColumn(), actual.getEndColumn(), failures);
        } catch (IllegalArgumentException e) {
            failures.add(label + ".codeLine: " + e.getMessage());
        }
    }

    private static List<String> fieldNames(JsonNode node) {
        List<String> fields = new ArrayList<>();
        Iterator<String> iterator = node.fieldNames();
        iterator.forEachRemaining(fields::add);
        return fields;
    }

    private static void assertEnum(String label, String expectedValue, Enum<?> actual, List<String> failures) {
        String actualValue = actual == null ? null : actual.name();
        if (!Objects.equals(expectedValue, actualValue)) {
            failures.add(label + ": expected=" + expectedValue + ", actual=" + actualValue);
        }
    }

    private static void assertEnum(String label, JsonNode expected, Enum<?> actual, List<String> failures) {
        String expectedValue = expected != null && expected.isTextual() ? expected.asText() : null;
        assertEnum(label, expectedValue, actual, failures);
    }

    private static void assertInt(String label, int expected, int actual, List<String> failures) {
        if (expected != actual) {
            failures.add(label + ": expected=" + expected + ", actual=" + actual);
        }
    }

    private static String prefix(TestCase testCase) {
        return "[" + testCase.name() + "]";
    }

    private static List<String> summarize(List<BehaviorObject> objects) {
        return objects.stream().map(object -> object.getObjectType() + ":" + object.getObjectPath()).toList();
    }

    private static List<String> summarizeRelations(List<BehaviorRelation> relations) {
        return relations.stream().map(relation -> {
            return relation.getAction() +                          //
                   "(" + relation.getSubject().getObjectType() +   //
                   ":" + relation.getSubject().getStartLine() +    //
                   ":" + relation.getSubject().getStartColumn() +  //
                   "~" + relation.getSubject().getEndLine() +      //
                   ":" + relation.getSubject().getEndColumn() +    //
                   ":" + relation.getSubject().getObjectPath() +   //
                   ")";
        }).toList();
    }

    private record ExpectedStatement(String statementType, JsonNode relations) {
    }

    @FunctionalInterface
    public interface PermissionVerifier {

        boolean isPermissionExempt(BehaviorRelation relation);
    }

    public static final class TestCase extends TextTestCase {

        private String                sql;
        private Map<UmiTypes, Object> levels;
        private String                expectJson;
        private int                   baseLine = 1;
        private int                   baseColumn;

        private TestCase(CaseBlock block){
            super(block);
        }

        public String sql() {
            return sql;
        }

        public String displayName() {
            String summary = sql.replaceAll("\\s+", " ").strip();
            if (summary.length() > 120) {
                summary = summary.substring(0, 117) + "...";
            }
            return caseId() + " " + summary;
        }
    }
}
