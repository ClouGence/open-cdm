package com.clougence.clouddm.ds.secdomain;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.ds.goldendb.dialect.mysql.GoldenDBMySQLDialect;
import com.clougence.clouddm.ds.goldendb.dsconf.GoldenDBCompatibilityMode;
import com.clougence.clouddm.ds.goldendb.dsconf.mysql.GoldenDBMySQLConfig;
import com.clougence.clouddm.ds.goldendb.dsconf.oracle.GoldenDBOracleConfig;
import com.clougence.clouddm.ds.goldendb.execute.oracle.GoldenDBOracleMetaService;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SecDomainTextTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @TestFactory
    public Stream<DynamicTest> secDomainScripts() {
        return dynamicTests();
    }

    @Test
    public void goldenDbDriverAndKeywordContractsAreIndependent() {
        GoldenDBMySQLConfig config = new GoldenDBMySQLConfig();
        config.setClientTimeZone("Asia/Shanghai");

        Properties disabled = config.asDriverProperties();
        Assertions.assertEquals("DISABLED", disabled.getProperty("sslMode"));
        Assertions.assertEquals("Asia/Shanghai", disabled.getProperty("serverTimezone"));
        Assertions.assertEquals("com.goldendb.jdbc.log.NullLogger", disabled.getProperty("logger"));
        Assertions.assertEquals("mysql", disabled.getProperty(GoldenDBCompatibilityMode.EXPECTED_MODE_PROPERTY));

        GoldenDBOracleConfig oracleConfig = new GoldenDBOracleConfig();
        Assertions.assertEquals("oracle", oracleConfig.asDriverProperties().getProperty(GoldenDBCompatibilityMode.EXPECTED_MODE_PROPERTY));
        Assertions.assertEquals("12", new GoldenDBOracleMetaService(null).getSqlParserParameters().get(SqlParserParameters.VERSION));

        config.setSslMode(SslMode.TRUST);
        Assertions.assertEquals("DISABLED", config.asDriverProperties().getProperty("sslMode"));

        oracleConfig.setSslMode(SslMode.TRUST);
        Properties oracleProperties = oracleConfig.asDriverProperties();
        Assertions.assertEquals("false", oracleProperties.getProperty("useSSL"));
        Assertions.assertEquals("false", oracleProperties.getProperty("requireSSL"));

        Set<String> keywords = GoldenDBMySQLDialect.INSTANCE.keywords();
        Assertions.assertTrue(keywords.containsAll(Set.of("DISTRIBUTED", "SUBDISTRIBUTED", "DUPLICATE", "GLOBAL", "REMAIN", "HASH", "LIST")));
        Assertions.assertEquals("`distributed`", GoldenDBMySQLDialect.INSTANCE.fmtName(false, "distributed"));
    }

    public static Stream<DynamicTest> dynamicTests() {
        List<DynamicTest> tests = new ArrayList<>();
        for (Fixture fixture : fixtures()) {
            for (String resourcePath : resourceFiles(fixture)) {
                List<TestCase> cases = loadCases(resourcePath);
                tests.add(DynamicTest.dynamicTest(fixture.datasource() + " " + resourcePath, () -> {
                    DataSourceType dataSourceType = SqlTestSupport.dataSourceType(fixture.datasource());
                    SecDomainResolveSpi resolveSpi = secDomainResolveSpi(fixture.datasource());
                    ContextInfo contextInfo = SqlTestSupport.contextInfo(fixture.datasource());
                    Stream<Executable> validations = cases.stream().map(testCase -> {
                        return () -> assertCase(resourcePath, testCase, dataSourceType, resolveSpi, contextInfo);
                    });
                    Assertions.assertAll(resourcePath, validations);
                }));
            }
        }
        return tests.stream();
    }

    static List<String> listResourceFiles(String resourceDir) {
        return TextCaseSupport.resourceFiles(resourceDir, path -> !path.substring(resourceDir.length() + 1).startsWith("rules/"));
    }

    static List<TestCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(SecDomainTextTest::parseOneCase).toList();
    }

    static TestCase parseOneCase(CaseBlock block) {
        TestCase testCase = new TestCase(block);
        String body = block.body();

        int sqlIdx = body.indexOf("sql:");
        int expectIdx = body.indexOf("expect:");
        if (sqlIdx < 0 || expectIdx <= sqlIdx) {
            throw new IllegalArgumentException("Invalid sec domain test case: " + testCase.name());
        }

        String preSql = body.substring(0, sqlIdx);
        testCase.contextJson = readSection(preSql, "context:");
        testCase.sql = body.substring(sqlIdx + "sql:".length(), expectIdx).trim();
        testCase.expectJson = body.substring(expectIdx + "expect:".length()).trim();
        return testCase;
    }

    static List<String> verify(TestCase testCase, DataSourceType dataSourceType, SecDomainResolveSpi resolveSpi, ContextInfo contextInfo) {
        List<String> failures = new ArrayList<>();
        JsonNode expected;
        try {
            expected = OBJECT_MAPPER.readTree(testCase.expectJson);
        } catch (IOException e) {
            failures.add(prefix(testCase) + " invalid expect JSON: " + e.getMessage());
            return failures;
        }

        List<RuleDomain> domains;
        try (StringReader reader = new StringReader(testCase.sql);
                Stream<RuleDomain> stream = resolveSpi.resolveDomainStream(dataSourceType, reader, 1, 0, contextInfo(testCase, contextInfo))) {
            domains = flatten(stream.toList());
        } catch (Exception e) {
            if (expected.has("exception")) {
                assertExpectedException(testCase, expected.get("exception"), e, failures);
                return failures;
            }
            failures.add(prefix(testCase) + " unexpected exception: " + e.getMessage());
            return failures;
        }

        if (expected.has("exception")) {
            failures.add(prefix(testCase) + " expected exception=" + expected.get("exception").asText() + ", actual domains=" + summarize(domains));
            return failures;
        }

        if (expected.isArray()) {
            verifyDomainList(prefix(testCase) + ".domains", expected, domains, false, failures);
            return failures;
        }

        if (expected.has("size")) {
            assertEquals(failures, prefix(testCase) + ".size", expected.get("size"), domains.size());
        }

        JsonNode expectedDomains = expected.has("contains") ? expected.get("contains") : expected.get("domains");
        if (expectedDomains != null && expectedDomains.isArray()) {
            if (expected.has("contains")) {
                verifyContains(prefix(testCase), expectedDomains, domains, failures);
                return failures;
            }
            boolean allowExtra = expected.path("allowExtra").asBoolean(false);
            verifyDomainList(prefix(testCase) + ".domains", expectedDomains, domains, allowExtra, failures);
        }
        return failures;
    }

    static void assertCase(String resourcePath, TestCase testCase, DataSourceType dataSourceType, SecDomainResolveSpi resolveSpi, ContextInfo contextInfo) {
        List<String> failures = verify(testCase, dataSourceType, resolveSpi, contextInfo);
        if (!failures.isEmpty()) {
            Assert.fail(resourcePath + System.lineSeparator() + String.join(System.lineSeparator(), failures));
        }
    }

    private static List<Fixture> fixtures() {
        return List
            .of(fixture("adb", "secdomain/mysql"), fixture("clickhouse", "secdomain/clickhouse"), fixture("dameng", "secdomain/dameng"), fixture("db2", "secdomain/db2"), fixture("doris", "secdomain/doris"), fixture("gauss", "secdomain/postgres"), fixture("gauss_og", "secdomain/postgres"), fixture("goldendb", "secdomain/mysql"), fixture("greenplum", "secdomain/postgres"), fixture("hologres", "secdomain/postgres"), fixture("mariadb", "secdomain/mysql"), fixture("maxcompute", "secdomain/maxcompute"), fixture("mongodb", "secdomain/mongodb"), fixture("mysql", "secdomain/mysql"), fixture("ob4my", "secdomain/mysql"), fixture("ob4ora", "secdomain/oracle"), fixture("oracle", "secdomain/oracle"), fixture("por4my", "secdomain/mysql"), fixture("por4pg", "secdomain/postgres"), fixture("por4x", "secdomain/mysql/por4x_generated.txt"), fixture("postgres", "secdomain/postgres"), fixture("redis", "secdomain/redis"), fixture("selectdb", "secdomain/doris"), fixture("sql2003", "secdomain/sql2003"), fixture("sql92", "secdomain/sql92"), fixture("sql99", "secdomain/sql99"), fixture("sqlserver", "secdomain/sqlserver"), fixture("starrocks", "secdomain/starrocks"), fixture("tidb", "secdomain/mysql"));
    }

    private static List<String> resourceFiles(Fixture fixture) {
        String resourcePath = fixture.resourcePath();
        if (resourcePath.endsWith(".txt")) {
            return List.of(resourcePath);
        }
        List<String> resourceFiles = listResourceFiles(resourcePath);
        if ("secdomain/mysql".equals(resourcePath)) {
            return resourceFiles.stream()
                .filter(path -> !path.startsWith("secdomain/mysql/9.7/"))
                .filter(path -> !path.substring(path.lastIndexOf('/') + 1).startsWith("por4x_"))
                .toList();
        }
        if ("ob4ora".equals(fixture.datasource())) {
            return resourceFiles.stream().filter(path -> !path.endsWith("/function_procedure.txt")).toList();
        }
        return resourceFiles;
    }

    private static SecDomainResolveSpi secDomainResolveSpi(String datasource) {
        SecDomainResolveSpi spi = SqlTestSupport.sqlEngine(datasource).secDomainResolveSpi(SqlTestSupport.parserParameters(datasource));
        if (spi == null) {
            throw new IllegalStateException("No SecDomainResolveSpi for datasource: " + datasource);
        }
        return spi;
    }

    private static Fixture fixture(String datasource, String resourcePath) {
        return new Fixture(datasource, resourcePath);
    }

    private record Fixture(String datasource, String resourcePath) {
    }

    private static void assertExpectedException(TestCase testCase, JsonNode expected, Exception actual, List<String> failures) {
        String expectedName = expected.asText();
        Class<?> actualClass = actual.getClass();
        if (!Objects.equals(expectedName, actualClass.getSimpleName()) && !Objects.equals(expectedName, actualClass.getName())) {
            failures.add(prefix(testCase) + " exception: expected=" + expectedName + ", actual=" + actualClass.getName() + ": " + actual.getMessage());
        }
    }

    private static void verifyDomainList(String label, JsonNode expectedDomains, List<RuleDomain> domains, boolean allowExtra, List<String> failures) {
        if (!allowExtra && expectedDomains.size() != domains.size()) {
            failures.add(label + ".size: expected=" + expectedDomains.size() + ", actual=" + domains.size() + " " + summarize(domains));
            return;
        }
        if (expectedDomains.size() > domains.size()) {
            failures.add(label + ".size: expected at least=" + expectedDomains.size() + ", actual=" + domains.size());
            return;
        }
        for (int i = 0; i < expectedDomains.size(); i++) {
            verifyDomain(label + "[" + i + "]", expectedDomains.get(i), domains.get(i), failures);
        }
    }

    private static void verifyContains(String label, JsonNode expectedDomains, List<RuleDomain> domains, List<String> failures) {
        boolean[] used = new boolean[domains.size()];
        for (int i = 0; i < expectedDomains.size(); i++) {
            JsonNode expectedDomain = expectedDomains.get(i);
            int matched = findMatch(expectedDomain, domains, used);
            if (matched < 0) {
                failures.add(label + ".contains[" + i + "] not found: " + expectedDomain + " in " + summarize(domains) + ", closest mismatch: "
                             + closestMismatch(expectedDomain, domains, used));
            } else {
                used[matched] = true;
            }
        }
    }

    private static List<String> closestMismatch(JsonNode expectedDomain, List<RuleDomain> domains, boolean[] used) {
        List<String> closest = null;
        for (int i = 0; i < domains.size(); i++) {
            if (used[i]) {
                continue;
            }
            List<String> candidate = new ArrayList<>();
            verifyDomain("domain[" + i + "]", expectedDomain, domains.get(i), candidate);
            if (closest == null || candidate.size() < closest.size()) {
                closest = candidate;
            }
        }
        return closest == null ? Collections.emptyList() : closest;
    }

    private static int findMatch(JsonNode expectedDomain, List<RuleDomain> domains, boolean[] used) {
        for (int i = 0; i < domains.size(); i++) {
            if (used[i]) {
                continue;
            }
            List<String> failures = new ArrayList<>();
            verifyDomain("match", expectedDomain, domains.get(i), failures);
            if (failures.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static void verifyDomain(String prefix, JsonNode expected, RuleDomain actual, List<String> failures) {
        Iterator<Map.Entry<String, JsonNode>> fields = expected.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String name = entry.getKey();
            JsonNode expectedValue = entry.getValue();
            if ("children".equals(name)) {
                failures.add(prefix + ".children: use childrenSize on the parent and assert child domains at top level");
            } else if ("childrenSize".equals(name)) {
                assertEquals(failures, prefix + ".childrenSize", expectedValue, childSize(actual));
            } else if ("class".equals(name)) {
                assertEquals(failures, prefix + ".class", expectedValue, actual.getClass().getSimpleName());
            } else {
                try {
                    Object actualValue = readDomainProperty(actual, name);
                    assertEquals(failures, prefix + "." + name, expectedValue, actualValue);
                } catch (IllegalArgumentException e) {
                    failures.add(prefix + "." + name + ": " + e.getMessage());
                }
            }
        }
    }

    private static Object readDomainProperty(RuleDomain domain, String name) {
        return switch (name) {
            case "sqlTypes" -> domain.getSqlType() == null ? Collections.emptyList() : List.of(domain.getSqlType());
            case "sqlTargets" -> domain.getSqlTarget() == null ? Collections.emptyList() : List.of(domain.getSqlTarget());
            default -> readProperty(domain, name);
        };
    }

    private static Object readProperty(Object target, String name) {
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(target.getClass()).getPropertyDescriptors()) {
                if (Objects.equals(descriptor.getName(), name) && descriptor.getReadMethod() != null) {
                    return descriptor.getReadMethod().invoke(target);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to read property '" + name + "' from " + target.getClass().getName(), e);
        }
        throw new IllegalArgumentException("Property '" + name + "' not found on " + target.getClass().getName());
    }

    private static void assertEquals(List<String> failures, String label, JsonNode expected, Object actual) {
        if (expected.isNull()) {
            if (actual != null) {
                failures.add(label + ": expected=null, actual=" + actual);
            }
        } else if (expected.isTextual()) {
            String actualValue = actual instanceof Enum<?> ? ((Enum<?>) actual).name() : Objects.toString(actual, null);
            if (!Objects.equals(expected.asText(), actualValue)) {
                failures.add(label + ": expected=" + expected.asText() + ", actual=" + actualValue);
            }
        } else if (expected.isBoolean()) {
            if (!(actual instanceof Boolean) || expected.asBoolean() != (Boolean) actual) {
                failures.add(label + ": expected=" + expected.asBoolean() + ", actual=" + actual);
            }
        } else if (expected.isNumber()) {
            if (!(actual instanceof Number) || expected.asLong() != ((Number) actual).longValue()) {
                failures.add(label + ": expected=" + expected.asLong() + ", actual=" + actual);
            }
        } else if (expected.isArray()) {
            assertArrayEquals(failures, label, expected, actual);
        } else if (expected.isObject()) {
            if (actual == null) {
                failures.add(label + ": expected=" + expected + ", actual=null");
            } else if (actual instanceof Map<?, ?> actualMap) {
                verifyMap(label, expected, actualMap, failures);
            } else {
                verifyBean(label, expected, actual, failures);
            }
        } else {
            failures.add(label + ": unsupported expected value " + expected);
        }
    }

    private static void assertArrayEquals(List<String> failures, String label, JsonNode expected, Object actual) {
        if (!(actual instanceof Collection<?>)) {
            failures.add(label + ": expected collection=" + expected + ", actual=" + actual);
            return;
        }
        List<?> actualList = new ArrayList<>((Collection<?>) actual);
        if (expected.size() != actualList.size()) {
            failures.add(label + ".size: expected=" + expected.size() + ", actual=" + actualList.size() + " (" + actualList + ")");
            return;
        }
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(failures, label + "[" + i + "]", expected.get(i), actualList.get(i));
        }
    }

    private static void verifyBean(String prefix, JsonNode expected, Object actual, List<String> failures) {
        Iterator<Map.Entry<String, JsonNode>> fields = expected.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String name = entry.getKey();
            JsonNode expectedValue = entry.getValue();
            if ("class".equals(name)) {
                assertEquals(failures, prefix + ".class", expectedValue, actual.getClass().getSimpleName());
            } else {
                try {
                    Object actualValue = readProperty(actual, name);
                    assertEquals(failures, prefix + "." + name, expectedValue, actualValue);
                } catch (IllegalArgumentException e) {
                    failures.add(prefix + "." + name + ": " + e.getMessage());
                }
            }
        }
    }

    private static void verifyMap(String prefix, JsonNode expected, Map<?, ?> actual, List<String> failures) {
        Iterator<Map.Entry<String, JsonNode>> fields = expected.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            Object actualValue = actual.get(entry.getKey());
            assertEquals(failures, prefix + "." + entry.getKey(), entry.getValue(), actualValue);
        }
    }

    private static String prefix(TestCase testCase) {
        return "[" + testCase.name() + "]";
    }

    private static String summarize(List<RuleDomain> domains) {
        List<String> summary = new ArrayList<>();
        for (RuleDomain domain : domains) {
            summary.add(summarize(domain));
        }
        return summary.toString();
    }

    private static String summarize(RuleDomain domain) {
        List<String> values = new ArrayList<>();
        values.add("sqlType=" + domain.getSqlType());
        values.add("auditKind=" + domain.getAuditKind());
        int childrenSize = childSize(domain);
        if (childrenSize > 0) {
            values.add("childrenSize=" + childrenSize);
        }
        addIfPresent(values, domain, "catalog");
        addIfPresent(values, domain, "schema");
        addIfPresent(values, domain, "table");
        addIfPresent(values, domain, "column");
        addIfPresent(values, domain, "tableName");
        addIfPresent(values, domain, "name");
        addIfPresent(values, domain, "typeDesc");
        addIfPresent(values, domain, "newName");
        return domain.getClass().getSimpleName() + values;
    }

    private static List<RuleDomain> flatten(List<RuleDomain> domains) {
        List<RuleDomain> result = new ArrayList<>();
        appendDomains(result, domains);
        return result;
    }

    private static void appendDomains(List<RuleDomain> result, List<RuleDomain> domains) {
        if (domains == null) {
            return;
        }
        for (RuleDomain domain : domains) {
            result.add(domain);
            appendDomains(result, domain.getChildren());
        }
    }

    private static int childSize(RuleDomain domain) {
        List<RuleDomain> children = domain.getChildren();
        return children == null ? 0 : children.size();
    }

    private static void addIfPresent(List<String> values, RuleDomain domain, String name) {
        try {
            Object value = readProperty(domain, name);
            if (value != null) {
                values.add(name + "=" + value);
            }
        } catch (IllegalArgumentException ignored) {
            // Optional debug field for failure summaries.
        }
    }

    private static ContextInfo contextInfo(TestCase testCase, ContextInfo defaultContextInfo) {
        if (testCase.contextJson == null || testCase.contextJson.isBlank()) {
            return defaultContextInfo;
        }
        try {
            JsonNode context = OBJECT_MAPPER.readTree(testCase.contextJson);
            ContextInfo.ContextInfoBuilder builder = ContextInfo.builder();
            if (context.has("mcSchemaStyle")) {
                McConfig dataSourceConfig = new McConfig();
                dataSourceConfig.setSchemaStyle(context.get("mcSchemaStyle").asBoolean());
                builder.dataSourceConfig(dataSourceConfig);
            }
            return builder.build();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid context JSON: " + testCase.name(), e);
        }
    }

    private static String readSection(String text, String prefix) {
        int index = text.indexOf(prefix);
        if (index < 0) {
            return null;
        }
        return text.substring(index + prefix.length()).strip();
    }

    static class TestCase extends TextTestCase {

        TestCase(CaseBlock block){
            super(block);
        }

        String contextJson;
        String sql;
        String expectJson;

        String displayName(String fixtureName) {
            return fixtureName + "/" + caseId() + " " + summarizeSql(sql);
        }
    }

    private static String summarizeSql(String sql) {
        String summary = sql.replaceAll("\\s+", " ").strip();
        if (summary.length() <= 120) {
            return summary;
        }
        return summary.substring(0, 117) + "...";
    }
}
