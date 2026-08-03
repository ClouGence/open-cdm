package com.clougence.clouddm.ds.rules;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.TextCaseSupport.CaseBlock;
import com.clougence.clouddm.ds.TextTestCase;
import com.clougence.clouddm.ds.TextTestFramework;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sec.rules.domain.CheckerDomain;
import com.clougence.clouddm.sec.rules.domain.func.FuncLoggerUtils;
import com.clougence.clouddm.sec.rules.execute.DomainHelper;
import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.ValueObject;
import com.clougence.detectrule.lang.reflect.ReflectHelper;
import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.lang.reflect.TypeType;
import com.clougence.detectrule.parser.DetectRuleDslProvider;
import com.clougence.detectrule.runtime.DefaultDataTimeValueParser;
import com.clougence.detectrule.runtime.EngineOption;
import com.clougence.detectrule.runtime.v1.DetectRuleEngineV1;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.antlr.ThrowingListener;
import com.clougence.dslpaser.ast.StatementSet;

public final class RuleTextTest {

    static {
        ReflectHelper.addIgnoreField("com.clougence.clouddm.sdk.service.secrules.RuleDomain.children");
        ReflectHelper.addIgnoreField("com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbSelectDomain.selectColumns");
        ReflectHelper.addIgnoreField("com.clougence.clouddm.sdk.service.secrules.RuleDomain.splitScript");
        ReflectHelper.addIgnoreField("com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbSelectDomain.tableAlias");
    }

    @TestFactory
    public Stream<DynamicTest> ruleScripts() {
        return dynamicTests();
    }

    public static Stream<DynamicTest> dynamicTests() {
        return TextTestFramework.dynamicTests(SqlTestSupport.resourceFiles("rules"), RuleTextTest::loadCases, testCase -> {
            String datasource = SqlTestSupport.datasourceFromPath(testCase.resourcePath());
            return DynamicTest.dynamicTest(testCase.displayName(), () -> assertCase(testCase.resourcePath(), testCase, SqlTestSupport
                .dataSourceType(datasource), secDomainResolveSpi(datasource), SqlTestSupport.contextInfo(datasource)));
        });
    }

    static List<TestCase> loadCases(String resourcePath) {
        return TextCaseSupport.loadBlocks(resourcePath).stream().map(RuleTextTest::parseOneCase).toList();
    }

    static void assertCase(String resourcePath, TestCase testCase, DataSourceType dataSourceType, SecDomainResolveSpi resolveSpi, ContextInfo contextInfo) {
        try {
            List<RuleDomain> domains = resolveSpi.resolveDomain(dataSourceType, codeInfo(testCase.sql), contextInfo);
            boolean actual = runRuleScript(testCase.rule, DomainHelper.create(domains), testCase.vars);
            Assert.assertEquals(testCase.caseId(), testCase.expect, actual);
        } catch (Exception e) {
            AssertionError error = new AssertionError(testCase.caseId() + " unexpected exception: " + e.getClass().getName() + ": " + e.getMessage());
            error.initCause(e);
            throw error;
        }
    }

    private static SecDomainResolveSpi secDomainResolveSpi(String datasource) {
        String version = switch (datasource) {
            case "mysql", "mariadb", "por4my" -> "8.0.46";
            default -> null;
        };
        SqlParserParameters parameters = version == null ? SqlParserParameters.empty() : SqlParserParameters.ofVersion(version);
        SecDomainResolveSpi spi = SqlTestSupport.sqlEngine(datasource).secDomainResolveSpi(parameters);
        if (spi == null) {
            throw new IllegalStateException("No SecDomainResolveSpi for datasource: " + datasource);
        }
        return spi;
    }

    static TestCase parseOneCase(CaseBlock block) {
        TestCase testCase = new TestCase(block);
        List<String> lines = block.body().lines().toList();
        String currentKey = null;
        StringBuilder currentValue = new StringBuilder();

        for (String line : lines) {
            if (isFieldStart(line)) {
                flush(testCase, currentKey, currentValue);
                int split = line.indexOf(':');
                currentKey = line.substring(0, split).trim();
                currentValue.setLength(0);
                String inlineValue = line.substring(split + 1).trim();
                if (!inlineValue.isEmpty()) {
                    currentValue.append(inlineValue);
                }
                continue;
            }

            if (currentKey == null) {
                throw new IllegalArgumentException("Invalid rule test case: " + testCase.name());
            }
            if (!currentValue.isEmpty()) {
                currentValue.append('\n');
            }
            currentValue.append(line);
        }
        flush(testCase, currentKey, currentValue);
        testCase.validate();
        return testCase;
    }

    private static boolean isFieldStart(String line) {
        int split = line.indexOf(':');
        if (split <= 0) {
            return false;
        }
        String key = line.substring(0, split).trim();
        return Objects.equals(key, "rule") || Objects.equals(key, "expect") || Objects.equals(key, "vars") || Objects.equals(key, "sql");
    }

    private static void flush(TestCase testCase, String key, StringBuilder value) {
        if (key == null) {
            return;
        }
        String text = value.toString().strip();
        switch (key) {
            case "rule" -> testCase.rule = text;
            case "expect" -> testCase.expect = Boolean.parseBoolean(text);
            case "vars" -> testCase.vars = parseVars(text);
            case "sql" -> testCase.sql = text;
            default -> throw new IllegalArgumentException("Unsupported field: " + key);
        }
    }

    private static Map<String, String> parseVars(String text) {
        if (text.isBlank() || "{}".equals(text)) {
            return Map.of();
        }
        Map<String, String> vars = new HashMap<>();
        for (String line : text.lines().map(String::trim).filter(item -> !item.isEmpty()).toList()) {
            int split = line.indexOf(':');
            if (split <= 0) {
                throw new IllegalArgumentException("Invalid vars line: " + line);
            }
            vars.put(line.substring(0, split).trim(), line.substring(split + 1).trim());
        }
        return vars;
    }

    private static boolean runRuleScript(String scriptResource, List<CheckerDomain> domainData, Map<String, String> vars) throws IOException {
        for (CheckerDomain domain : domainData) {
            try (InputStream input = RuleTextTest.class.getClassLoader().getResourceAsStream(scriptResource)) {
                if (input == null) {
                    throw new IllegalArgumentException("Resource not found: " + scriptResource);
                }
                CharStream chars = CharStreams.fromStream(input);
                if (!runRuleScript(chars, domain, vars)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean runRuleScript(CharStream script, CheckerDomain domainData, Map<String, String> vars) {
        DslProvider provider = DetectRuleDslProvider.INSTANCE;
        Lexer lexer = provider.createLexer(script);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingListener.INSTANCE);

        Parser parser = provider.createParser(lexer);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingListener.INSTANCE);

        StatementSet statements = provider.doParser(lexer, parser);
        Type domainType = ReflectHelper.resolveDomain(domainData.getClass());

        EngineOption option = new EngineOption();
        option.setDataTimeValueParser(new DefaultDataTimeValueParser());
        DetectRuleEngineV1 visitor = new DetectRuleEngineV1(domainData, domainType, option);
        visitor.putVariables(vars == null ? Map.of() : vars);

        try {
            statements.accept(visitor);
        } finally {
            FuncLoggerUtils.outputLog.clear();
        }
        LangObject returnData = visitor.returnData(new ValueObject(true, TypeType.Boolean));
        return (boolean) returnData.unwrap();
    }

    private static CodeInfo codeInfo(String sql) {
        return CodeInfo.builder().query(sql).baseLine(1).baseColumn(0).build();
    }

    static class TestCase extends TextTestCase {

        TestCase(CaseBlock block){
            super(block);
        }

        String              rule;
        boolean             expect;
        Map<String, String> vars = Map.of();
        String              sql;

        String displayName() {
            return caseId() + " [" + rule + "] " + summarize(sql);
        }

        void validate() {
            if (name().isBlank()) {
                throw new IllegalArgumentException("Missing case name");
            }
            if (rule == null || rule.isBlank()) {
                throw new IllegalArgumentException("Missing rule: " + name());
            }
            if (sql == null || sql.isBlank()) {
                throw new IllegalArgumentException("Missing sql: " + name());
            }
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
