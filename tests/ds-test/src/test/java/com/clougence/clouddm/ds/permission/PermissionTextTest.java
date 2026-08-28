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
package com.clougence.clouddm.ds.permission;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Stream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.clougence.clouddm.console.web.component.analysis.BehaviorRelations;
import com.clougence.clouddm.console.web.component.analysis.BehaviorRequest;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.ds.behavior.BehaviorCodeLine;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;
import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Script-driven verification of SQL behavior-to-permission output.
 *
 * <p>The Java code only executes the contract. SQL inputs and expected permission requests belong
 * in {@code src/test/resources/permission}.</p>
 */
public final class PermissionTextTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern      RESOURCE_TEXT = Pattern.compile("^([A-Za-z][A-Za-z0-9]*)\\(([1-9][0-9]*:[0-9]+~[1-9][0-9]*:[0-9]+)\\) (/.*)$");

    private PermissionTextTest(){
    }

    public static List<TestCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(PermissionTextTest::parseCase).toList();
    }

    public static void assertCompleteResourceDirectory(String resourceDirectory) {
        Set<SecDataAuthKind> actual = EnumSet.noneOf(SecDataAuthKind.class);
        List<String> resourcePaths = TextCaseSupport.resourceFiles(resourceDirectory);
        Assert.assertFalse(resourceDirectory + " must contain permission fixtures", resourcePaths.isEmpty());
        for (String resourcePath : resourcePaths) {
            for (TestCase testCase : loadCases(resourcePath)) {
                try {
                    actual.addAll(parseExpectedResources(testCase.expectJson).keySet());
                } catch (IOException e) {
                    Assert.fail(resourcePath + System.lineSeparator() + prefix(testCase) + " invalid expect JSON: " + e.getMessage());
                }
            }
        }
        Assert.assertEquals(resourceDirectory + " must cover every SecDataAuthKind", EnumSet.allOf(SecDataAuthKind.class), actual);
    }

    public static void assertStrictCase(String resourcePath, TestCase testCase, BehaviorAnalysisSpi spi,//
                                        SysObjectRegistrySpi registry, String databaseVersion) {
        List<String> failures = new ArrayList<>();
        Map<SecDataAuthKind, List<String>> expected;
        try {
            expected = parseExpectedResources(testCase.expectJson);
        } catch (IOException e) {
            Assert.fail(resourcePath + System.lineSeparator() + prefix(testCase) + " invalid expect JSON: " + e.getMessage());
            return;
        }

        List<StatementBehavior> statements;
        try (StringReader reader = new StringReader(testCase.sql);
                Stream<StatementBehavior> stream = spi.analysisBehaviorStream(reader, testCase.levels, testCase.baseLine, testCase.baseColumn)) {
            statements = stream.toList();
        } catch (Exception e) {
            Assert.fail(resourcePath + System.lineSeparator() + prefix(testCase) + " unexpected exception: " + e);
            return;
        }
        if (statements == null) {
            Assert.fail(resourcePath + System.lineSeparator() + prefix(testCase) + " analysisBehavior must not return null");
            return;
        }
        List<BehaviorRequest> actual = new ArrayList<>();
        for (StatementBehavior statement : statements) {
            actual.addAll(BehaviorRelations.flattenResource(registry, databaseVersion, statement.getRelations()));
        }
        for (Map.Entry<SecDataAuthKind, List<String>> entry : expected.entrySet()) {
            List<BehaviorRequest> actualForKind = actual.stream().filter(request -> request.authKind() == entry.getKey()).toList();
            verifyResources(prefix(testCase) + "." + entry.getKey(), entry.getValue(), actualForKind, failures);
        }
        if (!failures.isEmpty()) {
            Assert.fail(resourcePath + System.lineSeparator() + String.join(System.lineSeparator(), failures) + System.lineSeparator() + prefix(testCase) + ".actual: "
                        + summarize(actual));
        }
    }

    private static TestCase parseCase(CaseBlock block) {
        TestCase testCase = new TestCase(block);
        String body = block.body();
        int sqlIndex = body.indexOf("sql:");
        int levelsIndex = body.indexOf("levels:");
        int baseIndex = body.indexOf("base:");
        int expectIndex = body.indexOf("expect:");
        if (sqlIndex < 0 || levelsIndex <= sqlIndex || expectIndex <= levelsIndex) {
            throw new IllegalArgumentException("Invalid permission test case: " + testCase.name());
        }
        testCase.sql = body.substring(sqlIndex + "sql:".length(), levelsIndex).trim();
        int levelsEnd = baseIndex > levelsIndex && baseIndex < expectIndex ? baseIndex : expectIndex;
        testCase.levels = parseLevels(body.substring(levelsIndex + "levels:".length(), levelsEnd).trim(), testCase.name());
        if (levelsEnd == baseIndex) {
            parseBase(body.substring(baseIndex + "base:".length(), expectIndex).trim(), testCase);
        }
        testCase.expectJson = body.substring(expectIndex + "expect:".length()).trim();
        return testCase;
    }

    private static Map<UmiTypes, Object> parseLevels(String value, String caseName) {
        String[] parts = value.strip().replaceFirst("^/", "").split("/", -1);
        if (parts.length != 4 || Arrays.stream(parts).anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Levels must be '/<environment>/<datasourceId>/<catalog>/<schema>' in " + caseName);
        }
        return Map.of(UmiTypes.Instance, parts[0] + "/" + parts[1], UmiTypes.Catalog, parts[2], UmiTypes.Schema, parts[3]);
    }

    private static void parseBase(String value, TestCase testCase) {
        String[] parts = value.split(":", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Base must be '<line>:<column>' in " + testCase.name());
        }
        testCase.baseLine = Integer.parseInt(parts[0]);
        testCase.baseColumn = Integer.parseInt(parts[1]);
    }

    private static Map<SecDataAuthKind, List<String>> parseExpectedResources(String json) throws IOException {
        JsonNode root = OBJECT_MAPPER.readTree(json);
        if (root == null || !root.isObject()) {
            throw new IOException("expect must be an auth-kind object");
        }
        Map<SecDataAuthKind, List<String>> expected = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            SecDataAuthKind authKind;
            try {
                authKind = SecDataAuthKind.valueOf(field.getKey());
            } catch (IllegalArgumentException e) {
                throw new IOException("unknown SecDataAuthKind: " + field.getKey());
            }
            if (!field.getValue().isArray()) {
                throw new IOException(authKind + " resources must be an array");
            }
            List<String> resources = new ArrayList<>();
            for (JsonNode resource : field.getValue()) {
                if (!resource.isTextual()) {
                    throw new IOException(authKind + " resources must be strings");
                }
                resources.add(resource.asText());
            }
            expected.put(authKind, resources);
        }
        return expected;
    }

    private static void verifyResources(String label, List<String> expected, List<BehaviorRequest> actual, List<String> failures) {
        if (expected.size() != actual.size()) {
            failures.add(label + ".size: expected=" + expected.size() + ", actual=" + actual.size() + " " + summarize(actual));
            return;
        }
        for (int i = 0; i < expected.size(); i++) {
            verifyResource(label + "[" + i + "]", expected.get(i), actual.get(i).resource(), failures);
        }
    }

    private static void verifyResource(String label, String expected, BehaviorObject actual, List<String> failures) {
        Matcher matcher = RESOURCE_TEXT.matcher(expected);
        if (!matcher.matches()) {
            failures.add(label + " has invalid format: " + expected);
            return;
        }
        if (actual == null) {
            failures.add(label + " actual resource is null");
            return;
        }
        if (!Objects.equals(matcher.group(1), enumName(actual.getObjectType()))) {
            failures.add(label + ".targetType: expected=" + matcher.group(1) + ", actual=" + enumName(actual.getObjectType()));
        }
        BehaviorCodeLine.Range range;
        try {
            range = BehaviorCodeLine.parse(matcher.group(2));
        } catch (IllegalArgumentException e) {
            failures.add(label + ".codeLine: " + e.getMessage());
            return;
        }
        assertInt(label + ".startLine", range.startLine(), actual.getStartLine(), failures);
        assertInt(label + ".startColumn", range.startColumn(), actual.getStartColumn(), failures);
        assertInt(label + ".endLine", range.endLine(), actual.getEndLine(), failures);
        assertInt(label + ".endColumn", range.endColumn(), actual.getEndColumn(), failures);
        if (!Objects.equals(matcher.group(3), actual.getObjectPath())) {
            failures.add(label + ".objectPath: expected=" + matcher.group(3) + ", actual=" + actual.getObjectPath());
        }
    }

    private static String enumName(Enum<?> value) {
        return value == null ? null : value.name();
    }

    private static void assertInt(String label, int expected, int actual, List<String> failures) {
        if (expected != actual) {
            failures.add(label + ": expected=" + expected + ", actual=" + actual);
        }
    }

    private static String summarize(List<BehaviorRequest> requests) {
        return requests.stream().map(request -> {
            BehaviorObject resource = request.resource();
            return "{resource=\"" + resource.getObjectType() + "(" + resource.getStartLine() + ":" + resource.getStartColumn() + "~" + //
                   resource.getEndLine() + ":" + resource.getEndColumn() + ") " + resource.getObjectPath() + "\", action=\"" + //
                   request.action() + "\", authKind=" + request.authKind() + "}";
        }).toList().toString();
    }

    private static String prefix(TestCase testCase) {
        return "[" + testCase.name() + "]";
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

        public String displayName() {
            String summary = sql.replaceAll("\\s+", " ").strip();
            return caseId() + " " + (summary.length() > 120 ? summary.substring(0, 117) + "..." : summary);
        }
    }
}
