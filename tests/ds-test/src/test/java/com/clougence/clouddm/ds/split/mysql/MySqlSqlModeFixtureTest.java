package com.clougence.clouddm.ds.split.mysql;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.split.SplitTextTest;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySplitAnalysisSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlParserConfig.Feature;
import com.clougence.sql.mysql.parser.antlr.MySqlLexer;
import com.clougence.sql.mysql.parser.antlr.MySqlParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Verifies mode-sensitive tokens and AST shapes from versioned fixtures. SQL
 * remains exclusively in the resource directories.
 */
public class MySqlSqlModeFixtureTest extends SplitTextTest {

    private static final String FIXTURE_DELIMITER =
            "------------------------------------------------------------------------------------------";
    private static final ObjectMapper JSON = new ObjectMapper();

    @Override
    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles("split/mysql",
                path -> path.contains("/mode/")
                        && path.endsWith(".txt")
                        && !path.contains("/reject/")
                        && !path.contains("/unknown/"));
    }

    @Override
    protected SplitAnalysisSpi splitAnalysisSpi(Fixture fixture) {
        String version = exactVersion(fixture.resourcePath());
        if (version == null) {
            version = segmentAfter(fixture.resourcePath(), "split/mysql/");
        }
        try {
            MyDslProvider provider = new MyDslProvider(parserConfig(version, fixture.resourcePath()));
            return new MySplitAnalysisSpi(provider);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load parser properties: " + fixture.resourcePath(), e);
        }
    }

    @Test
    public void parserPropertySidecarsMatchFixtures() {
        Set<String> expectedConfigs = TextCaseSupport.resourceFiles("split/mysql",
                path -> path.contains("/mode/") && path.endsWith(".txt")).stream()
                .map(path -> path.substring(0, path.length() - ".txt".length()) + ".json")
                .collect(Collectors.toSet());
        Set<String> actualConfigs = Set.copyOf(TextCaseSupport.resourceFiles("split/mysql", ".json",
                path -> path.contains("/mode/")));
        Assertions.assertEquals(expectedConfigs, actualConfigs);
    }

    @TestFactory
    public Stream<DynamicTest> modeSensitiveTokensAndTrees() {
        return TextCaseSupport.resourceFiles("split/mysql", path -> path.contains("/mode/")
                && path.endsWith(".txt") && !path.contains("/reject/")).stream()
                .map(path -> DynamicTest.dynamicTest(path, () -> verifyFixture(path)));
    }

    @TestFactory
    public Stream<DynamicTest> modeSensitiveRejections() {
        return rejectedDynamicTests(TextCaseSupport.resourceFiles("split/mysql",
                path -> path.contains("/mode/") && path.endsWith(".txt") && path.contains("/reject/")), "mysql");
    }

    @Override
    protected void splitRejectedCase(String resourcePath, String datasource, int splitIndex) throws Exception {
        String version = exactVersion(resourcePath);
        if (version == null) {
            version = segmentAfter(resourcePath, "split/mysql/");
        }
        MyDslProvider provider = new MyDslProvider(parserConfig(version, resourcePath));
        DslHelper.splitDsl(provider, rejectedScript(resourcePath, splitIndex));
    }

    private static void verifyFixture(String resourcePath) throws Exception {
        String modeName = segmentAfter(resourcePath, "/mode/");
        String version = exactVersion(resourcePath);
        if (version == null) {
            version = segmentAfter(resourcePath, "split/mysql/");
        }
        MyDslProvider provider = new MyDslProvider(parserConfig(version, resourcePath));
        String input = fixtureInput(resourcePath);

        ParseEvidence evidence = evidence(provider, input);
        Assertions.assertFalse(evidence.trees().isEmpty(), resourcePath);

        if (resourcePath.contains("/dcl_mysql_mode_configuration_")) {
            return;
        }

        if (resourcePath.contains("/ddl_mysql_mode_routine_isolation_")) {
            assertToken(evidence, MySqlLexer.DOUBLE_QUOTE_STRING_LITERAL, resourcePath);
            assertNoToken(evidence, MySqlLexer.DOUBLE_QUOTE_ID, resourcePath);
            return;
        }

        if (resourcePath.contains("/ddl_mysql_mode_function_identifier_")) {
            assertTokenCount(evidence, "BIT_AND", MySqlLexer.ID, 1, resourcePath);
            assertNoToken(evidence, MySqlLexer.BIT_AND, resourcePath);
            return;
        }

        if (resourcePath.contains("/ddl_mysql_mode_identifier_")) {
            assertTokenCount(evidence, "`COUNT`", MySqlLexer.REVERSE_QUOTE_ID, 1, resourcePath);
            assertTokenCount(evidence, "COUNT", MySqlLexer.ID, 1, resourcePath);
            assertNoToken(evidence, MySqlLexer.COUNT, resourcePath);
            return;
        }

        switch (modeName) {
            case "known-empty" -> {
                assertToken(evidence, MySqlLexer.DOUBLE_QUOTE_STRING_LITERAL, resourcePath);
                assertToken(evidence, MySqlLexer.PIPES_LOGICAL_OR, resourcePath);
                assertToken(evidence, MySqlLexer.ID, resourcePath);
                assertTokenCount(evidence, "PI", MySqlLexer.PI, 1, resourcePath);
                assertTokenCount(evidence, "PI", MySqlLexer.ID, 3, resourcePath);
                Assertions.assertFalse(hasTree(evidence, MySqlParser.HighNotExpressionContext.class), resourcePath);
            }
            case "unknown" -> {
                assertToken(evidence, MySqlLexer.DOUBLE_QUOTE_AMBIGUOUS, resourcePath);
                assertToken(evidence, MySqlLexer.PIPES_AMBIGUOUS, resourcePath);
                assertTokenCount(evidence, "PI", MySqlLexer.PI, 4, resourcePath);
                assertTokenCount(evidence, "PI", MySqlLexer.ID, 0, resourcePath);
                assertNoToken(evidence, MySqlLexer.DOUBLE_QUOTE_ID, resourcePath);
                assertNoToken(evidence, MySqlLexer.DOUBLE_QUOTE_STRING_LITERAL, resourcePath);
                assertNoToken(evidence, MySqlLexer.PIPES_CONCAT, resourcePath);
                assertNoToken(evidence, MySqlLexer.PIPES_LOGICAL_OR, resourcePath);
            }
            case "ansi-quotes" -> {
                assertToken(evidence, MySqlLexer.DOUBLE_QUOTE_ID, resourcePath);
                assertNoToken(evidence, MySqlLexer.DOUBLE_QUOTE_STRING_LITERAL, resourcePath);
            }
            case "no-backslash-escapes" -> assertToken(evidence, MySqlLexer.STRING_LITERAL, resourcePath);
            case "pipes-as-concat" -> {
                assertToken(evidence, MySqlLexer.PIPES_CONCAT, resourcePath);
                assertNoToken(evidence, MySqlLexer.PIPES_LOGICAL_OR, resourcePath);
                Assertions.assertTrue(hasTree(evidence, MySqlParser.PipesConcatExpressionAtomContext.class),
                        resourcePath);
            }
            case "high-not-precedence" -> Assertions.assertTrue(
                    hasTree(evidence, MySqlParser.HighNotExpressionContext.class), resourcePath);
            case "pipes-as-concat-high-not-precedence" -> {
                assertToken(evidence, MySqlLexer.PIPES_CONCAT, resourcePath);
                assertNoToken(evidence, MySqlLexer.PIPES_LOGICAL_OR, resourcePath);
                Assertions.assertTrue(hasTree(evidence, MySqlParser.PipesConcatExpressionAtomContext.class),
                        resourcePath);
                Assertions.assertTrue(hasTree(evidence, MySqlParser.HighNotExpressionContext.class), resourcePath);
            }
            case "ignore-space" -> {
                assertToken(evidence, MySqlLexer.COUNT, resourcePath);
                assertTokenCount(evidence, "PI", MySqlLexer.PI, 2, resourcePath);
                assertTokenCount(evidence, "PI", MySqlLexer.ID, 2, resourcePath);
                Assertions.assertTrue(hasTree(evidence, MySqlParser.AggregateFunctionContext.class), resourcePath);
            }
            case "ansi" -> {
                assertToken(evidence, MySqlLexer.DOUBLE_QUOTE_ID, resourcePath);
                assertToken(evidence, MySqlLexer.PIPES_CONCAT, resourcePath);
                assertToken(evidence, MySqlLexer.COUNT, resourcePath);
            }
            default -> throw new IllegalArgumentException("Unknown sql_mode fixture directory: " + resourcePath);
        }
    }

    private static ParseEvidence evidence(MyDslProvider provider, String input) {
        Lexer lexer = provider.createLexer(CharStreams.fromString(input));
        List<TokenEvidence> tokens = lexer.getAllTokens().stream()
                .map(token -> new TokenEvidence(token.getText(), token.getType()))
                .toList();
        List<ParseTree> trees = DslHelper.splitDsl(provider, input).stream()
                .map(AstSplitScript::getAstTree)
                .toList();
        return new ParseEvidence(tokens, trees);
    }

    private static boolean hasTree(ParseEvidence evidence, Class<? extends ParseTree> type) {
        return evidence.trees().stream().anyMatch(tree -> hasTree(tree, type));
    }

    private static boolean hasTree(ParseTree tree, Class<? extends ParseTree> type) {
        if (type.isInstance(tree)) {
            return true;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (hasTree(tree.getChild(i), type)) {
                return true;
            }
        }
        return false;
    }

    private static void assertToken(ParseEvidence evidence, int tokenType, String resourcePath) {
        Assertions.assertTrue(evidence.tokens().stream().anyMatch(token -> token.type() == tokenType),
                () -> "missing token " + MySqlLexer.VOCABULARY.getSymbolicName(tokenType) + ": " + resourcePath);
    }

    private static void assertNoToken(ParseEvidence evidence, int tokenType, String resourcePath) {
        Assertions.assertFalse(evidence.tokens().stream().anyMatch(token -> token.type() == tokenType),
                () -> "unexpected token " + MySqlLexer.VOCABULARY.getSymbolicName(tokenType) + ": " + resourcePath);
    }

    private static void assertTokenCount(ParseEvidence evidence, String text, int tokenType, long expected,
            String resourcePath) {
        long actual = evidence.tokens().stream()
                .filter(token -> token.type() == tokenType && text.equalsIgnoreCase(token.text()))
                .count();
        Assertions.assertEquals(expected, actual,
                () -> "unexpected " + text + " token count for "
                        + MySqlLexer.VOCABULARY.getSymbolicName(tokenType) + ": " + resourcePath);
    }

    private static String fixtureInput(String resourcePath) {
        String content = TextCaseSupport.readResource(resourcePath);
        int delimiter = content.indexOf(FIXTURE_DELIMITER);
        Assertions.assertTrue(delimiter >= 0, "invalid fixture: " + resourcePath);
        return content.substring(0, delimiter);
    }

    private static MySqlParserConfig parserConfig(String version, String resourcePath) throws Exception {
        String configPath = resourcePath.substring(0, resourcePath.length() - ".txt".length()) + ".json";
        JsonNode config = JSON.readTree(TextCaseSupport.readResource(configPath));
        Assertions.assertTrue(config.has("known") && config.get("known").isBoolean(),
                "parser property config requires boolean known: " + configPath);
        Assertions.assertTrue(config.has("features") && config.get("features").isArray(),
                "parser property config requires feature array: " + configPath);
        boolean known = config.path("known").asBoolean(false);
        EnumSet<Feature> features = EnumSet.noneOf(Feature.class);
        for (JsonNode feature : config.path("features")) {
            features.add(Feature.valueOf(feature.asText()));
        }
        Assertions.assertTrue(known || features.isEmpty(),
                "unknown parser properties cannot enable features: " + configPath);
        return known ? MySqlParserConfig.knownSqlMode(version, features) : MySqlParserConfig.unknownSqlMode(version);
    }

    private static String segmentAfter(String path, String marker) {
        int start = path.indexOf(marker);
        if (start < 0) {
            throw new IllegalArgumentException("Missing path marker " + marker + ": " + path);
        }
        start += marker.length();
        int end = path.indexOf('/', start);
        return end < 0 ? path.substring(start) : path.substring(start, end);
    }

    private static String exactVersion(String resourcePath) {
        for (String segment : resourcePath.split("/")) {
            if (segment.startsWith("exact-")) {
                return segment.substring("exact-".length());
            }
        }
        return null;
    }

    private record ParseEvidence(List<TokenEvidence> tokens, List<ParseTree> trees) {
    }

    private record TokenEvidence(String text, int type) {
    }
}
