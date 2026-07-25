package com.clougence.clouddm.ds.versioned;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.mysql.dsconf.MyConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlParserConfig.Feature;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;

public class MySqlVersionConfigurationTest {

    private static final String FIXTURE_DELIMITER =
            "------------------------------------------------------------------------------------------";

    @Test
    public void versionsAreOrderedAndLatestIsTheDefault() {
        Assertions.assertTrue(MySqlVersion.MYSQL_8_0.atLeast(MySqlVersion.MYSQL_5_7));
        Assertions.assertTrue(MySqlVersion.MYSQL_8_0.atMost(MySqlVersion.MYSQL_8_4));
        Assertions.assertTrue(MySqlVersion.MYSQL_8_0.between(MySqlVersion.MYSQL_5_7, MySqlVersion.MYSQL_8_4));
        Assertions.assertEquals(MySqlVersion.MYSQL_9_7, MySqlVersion.LATEST);
        Assertions.assertEquals(MySqlVersion.LATEST, new MyDslProvider(MySqlParserConfig.unknownSqlMode(null)).version());
        Assertions.assertEquals(90700, new MyDslProvider(MySqlParserConfig.unknownSqlMode(null)).exactVersion());
    }

    @Test
    public void parsesGrammarAndExactVersionsIndependently() {
        MyDslProvider provider = new MyDslProvider(MySqlParserConfig.unknownSqlMode("8.0.22-commercial"));
        Assertions.assertEquals(MySqlVersion.MYSQL_8_0, provider.version());
        Assertions.assertEquals(80022, provider.exactVersion());
        Assertions.assertEquals(80410, MySqlVersion.parseExactVersion("8.4.10-log"));
        Assertions.assertEquals(90701, MySqlVersion.parseExactVersion("9.7.1"));
        Assertions.assertEquals(100000, MySqlVersion.parseExactVersion("10.0.0"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> MySqlVersion.parseExactVersion("mysql-8.0.22"));
    }

    @Test
    public void engineKeepsItsExplicitParserVersion() {
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
        MyDslProvider provider = (MyDslProvider) engine.dslProvider(SqlParserParameters.ofVersion("5.7.44"));
        Assertions.assertEquals(MySqlVersion.MYSQL_5_7, provider.version());
        Assertions.assertEquals(50744, provider.exactVersion());

        SqlParserParameters overrideParameters = new SqlParserParameters(Map.of(
                SqlParserParameters.VERSION, "8.0.22",
                SqlParserParameters.GRAMMAR_VERSION, "5.7",
                SqlParserParameters.EXACT_VERSION, "50744"));
        MyDslProvider overrideProvider = (MyDslProvider) engine.dslProvider(overrideParameters);
        Assertions.assertEquals(MySqlVersion.MYSQL_5_7, overrideProvider.version());
        Assertions.assertEquals(50744, overrideProvider.exactVersion());
    }

    @Test
    public void engineUsesLatestWhenVersionIsNull() {
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
        MyDslProvider provider = (MyDslProvider) engine.dslProvider(SqlParserParameters.empty());
        Assertions.assertEquals(MySqlVersion.LATEST, provider.version());
        Assertions.assertEquals(MySqlVersion.LATEST.exactVersion(), provider.exactVersion());
    }

    @Test
    public void parserParameterCacheUsesSortedStringKey() {
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
        Map<String, String> values = new LinkedHashMap<>();
        values.put(SqlParserParameters.VERSION, "8.4.10");
        values.put(SqlParserParameters.GRAMMAR_VERSION, "8.4");
        values.put("cacheMarker", "first");
        Map<String, String> reorderedValues = new LinkedHashMap<>();
        reorderedValues.put(SqlParserParameters.GRAMMAR_VERSION, "8.4");
        reorderedValues.put("cacheMarker", "first");
        reorderedValues.put(SqlParserParameters.VERSION, "8.4.10");
        Map<String, String> differentValues = new LinkedHashMap<>(values);
        differentValues.put("cacheMarker", "second");

        MyDslProvider provider = (MyDslProvider) engine.dslProvider(new SqlParserParameters(values));
        MyDslProvider reorderedProvider = (MyDslProvider) engine.dslProvider(new SqlParserParameters(reorderedValues));
        MyDslProvider differentProvider = (MyDslProvider) engine.dslProvider(new SqlParserParameters(differentValues));

        Assertions.assertSame(provider, reorderedProvider);
        Assertions.assertNotSame(provider, differentProvider);
    }

    @Test
    public void engineMapsSqlModeParametersToParserProperties() {
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());

        MyDslProvider unknownProvider = (MyDslProvider) engine.dslProvider(
                SqlParserParameters.ofVersion("8.4.10"));
        Assertions.assertFalse(unknownProvider.config().isSqlModeKnown());

        MyDslProvider emptyProvider = (MyDslProvider) engine.dslProvider(parserParameters(""));
        Assertions.assertTrue(emptyProvider.config().isSqlModeKnown());
        Assertions.assertTrue(emptyProvider.config().features().isEmpty());

        String sqlMode = "ANSI,NO_BACKSLASH_ESCAPES";
        SqlParserParameters configuredParameters = parserParameters(sqlMode);
        Assertions.assertEquals(sqlMode, configuredParameters.get(SqlParserParameters.SQL_MODE));
        MyDslProvider configuredProvider = (MyDslProvider) engine.dslProvider(configuredParameters);
        Assertions.assertEquals(
                java.util.EnumSet.of(
                        Feature.ANSI_QUOTES,
                        Feature.PIPES_AS_CONCAT,
                        Feature.IGNORE_SPACE,
                        Feature.NO_BACKSLASH_ESCAPES),
                configuredProvider.config().features());
    }

    @Test
    public void allAnalysisSpisUseTheSameSqlModeParameters() {
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
        SqlParserParameters unknown = SqlParserParameters.ofVersion("8.4.10");
        SqlParserParameters ansiQuotes = parserParameters("ANSI_QUOTES");
        SqlParserParameters knownEmpty = parserParameters("");
        String sql = modeStatement(engine, ansiQuotes, 1);

        Assertions.assertEquals(1, engine.splitAnalysisSpi(unknown).splitScript(sql, null, 0, 0).size());
        Assertions.assertEquals(1, engine.splitAnalysisSpi(ansiQuotes).splitScript(sql, null, 0, 0).size());
        Assertions.assertThrows(AntlerSyntaxException.class,
                () -> engine.splitAnalysisSpi(knownEmpty).splitScript(sql, null, 0, 0));

        Map<UmiTypes, Object> levels = Map.of(
                UmiTypes.Catalog, "catalog1",
                UmiTypes.Schema, "schema1");
        List<StatementBehavior> unknownBehaviors = engine.behaviorAnalysisSpi(unknown)
                .analysisBehavior(sql, levels, 0, 0);
        List<StatementBehavior> ansiBehaviors = engine.behaviorAnalysisSpi(ansiQuotes)
                .analysisBehavior(sql, levels, 0, 0);
        Assertions.assertTrue(objects(unknownBehaviors).stream().anyMatch(object -> object.getResourcePath().endsWith("/table1/")));
        Assertions.assertTrue(objects(ansiBehaviors).stream().anyMatch(object -> object.getResourcePath().endsWith("/table1/")));
        Assertions.assertThrows(AntlerSyntaxException.class,
                () -> engine.behaviorAnalysisSpi(knownEmpty).analysisBehavior(sql, levels, 0, 0));

        CodeInfo codeInfo = CodeInfo.builder().query(sql).baseLine(0).baseColumn(0).build();
        ContextInfo contextInfo = ContextInfo.builder().deepParser(false).levelsParam(levels).build();
        Assertions.assertDoesNotThrow(() -> engine.secDomainResolveSpi(unknown)
                .resolveDomain(DataSourceType.MySQL, codeInfo, contextInfo));
        Assertions.assertDoesNotThrow(() -> engine.secDomainResolveSpi(ansiQuotes)
                .resolveDomain(DataSourceType.MySQL, codeInfo, contextInfo));
        Assertions.assertThrows(AntlerSyntaxException.class, () -> engine.secDomainResolveSpi(knownEmpty)
                .resolveDomain(DataSourceType.MySQL, codeInfo, contextInfo));

        Assertions.assertDoesNotThrow(() -> engine.selectColumnAnalysisSpi(unknown)
                .parseSelectColumn(sql, contextInfo));
        Assertions.assertDoesNotThrow(() -> engine.selectColumnAnalysisSpi(ansiQuotes)
                .parseSelectColumn(sql, contextInfo));
        Assertions.assertThrows(AntlerSyntaxException.class, () -> engine.selectColumnAnalysisSpi(knownEmpty)
                .parseSelectColumn(sql, contextInfo));

        QueryRequest request = new QueryRequest();
        request.setQueryBody(sql);
        RewriteContext rewriteContext = new RewriteContext();
        rewriteContext.setFetchLimit(10);
        Assertions.assertTrue(engine.rewriteSpi(unknown).rewriterQuery(request, rewriteContext).contains("LIMIT 10"));
        Assertions.assertTrue(engine.rewriteSpi(ansiQuotes).rewriterQuery(request, rewriteContext).contains("LIMIT 10"));
        Assertions.assertThrows(AntlerSyntaxException.class,
                () -> engine.rewriteSpi(knownEmpty).rewriterQuery(request, rewriteContext));
    }

    private static List<BehaviorObject> objects(List<StatementBehavior> behaviors) {
        return behaviors.stream()
                .flatMap(behavior -> behavior.getRelations().stream())
                .flatMap(relation -> java.util.stream.Stream.concat(java.util.stream.Stream.of(relation.getSubject()), relation.getTarget().stream()))
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    @Test
    public void mysqlDatasourceConfigDoesNotDeclareSqlMode() {
        Assertions.assertThrows(NoSuchFieldException.class, () -> MyConfig.class.getDeclaredField("sqlMode"));
    }

    @Test
    public void sessionParserParametersPreserveUnknownAndKnownEmptyJsonSemantics() {
        SessionContextDTO legacy = JsonUtils.toObj("{\"sessionId\":\"legacy\"}", SessionContextDTO.class);
        Assertions.assertNotNull(legacy);
        SqlParserParameters legacyParameters = new SqlParserParameters(legacy.getSqlParameters());
        Assertions.assertTrue(legacyParameters.values().isEmpty());
        Assertions.assertFalse(legacyParameters.contains(SqlParserParameters.SQL_MODE));

        Map<String, String> values = new LinkedHashMap<>();
        values.put(SqlParserParameters.VERSION, "8.4.10");
        values.put(SqlParserParameters.SQL_MODE, "");
        SessionContextDTO current = new SessionContextDTO();
        current.setSqlParameters(values);

        SessionContextDTO restored = JsonUtils.toObj(JsonUtils.toJson(current), SessionContextDTO.class);
        SqlParserParameters restoredParameters = new SqlParserParameters(restored.getSqlParameters());
        Assertions.assertEquals("8.4.10", restoredParameters.version());
        Assertions.assertTrue(restored.getSqlParameters().containsKey(SqlParserParameters.SQL_MODE));
        Assertions.assertEquals("", restored.getSqlParameters().get(SqlParserParameters.SQL_MODE));
    }

    private static SqlParserParameters parserParameters(String sqlMode) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(SqlParserParameters.VERSION, "8.4.10");
        values.put(SqlParserParameters.SQL_MODE, sqlMode);
        return new SqlParserParameters(values);
    }

    private static String modeStatement(MySqlEngineSpi engine, SqlParserParameters parameters, int index) {
        String fixture = TextCaseSupport.readResource(
                "split/mysql/8.4/mode/ansi-quotes/dql_mysql_mode_0.txt");
        String input = fixture.substring(0, fixture.indexOf(FIXTURE_DELIMITER));
        return DslHelper.splitDsl(engine.dslProvider(parameters), input).get(index).getScript();
    }

}
