package com.clougence.sql.mysql;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteContext;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.MySqlParserConfig.Feature;
import com.clougence.sql.mysql.parser.MySqlVersion;

public class MySqlVersionConfigurationTest {

    @Test
    public void versionsAreOrderedAndLatestIsTheDefault() {
        Assertions.assertTrue(MySqlVersion.ge(MySqlVersion.MYSQL_8_0, MySqlVersion.MYSQL_5_7));
        Assertions.assertTrue(MySqlVersion.le(MySqlVersion.MYSQL_8_0, MySqlVersion.MYSQL_8_4));
        Assertions.assertTrue(MySqlVersion.ge(MySqlVersion.MYSQL_8_0, MySqlVersion.MYSQL_5_7) && MySqlVersion.le(MySqlVersion.MYSQL_8_0, MySqlVersion.MYSQL_8_4));
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
        MySqlEngineSpi engine = new MySqlEngineSpi(null);
        MyDslProvider provider = (MyDslProvider) engine.dslProvider(new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "5.7.44")));
        Assertions.assertEquals(MySqlVersion.MYSQL_5_7, provider.version());
        Assertions.assertEquals(50744, provider.exactVersion());

        SqlParserParameters overrideParameters = new SqlParserParameters(
            Map.of(SqlParserParameters.VERSION, "8.0.22", SqlParserParameters.GRAMMAR_VERSION, "5.7", SqlParserParameters.EXACT_VERSION, "50744"));
        MyDslProvider overrideProvider = (MyDslProvider) engine.dslProvider(overrideParameters);
        Assertions.assertEquals(MySqlVersion.MYSQL_5_7, overrideProvider.version());
        Assertions.assertEquals(50744, overrideProvider.exactVersion());
    }

    @Test
    public void engineUsesLatestWhenVersionIsNull() {
        MySqlEngineSpi engine = new MySqlEngineSpi(null);
        SqlParserParameters parameters = SqlParserParameters.empty();
        MyDslProvider provider = (MyDslProvider) engine.dslProvider(parameters);
        Assertions.assertEquals(MySqlVersion.LATEST, provider.version());
        Assertions.assertEquals(MySqlVersion.LATEST.exactVersion(), provider.exactVersion());
        Assertions.assertSame(provider, engine.dslProvider(null));
        Assertions.assertSame(provider, engine.dslProvider(parameters));
        Assertions.assertSame(engine.splitAnalysisSpi(parameters), engine.splitAnalysisSpi(null));
        Assertions.assertSame(engine.behaviorAnalysisSpi(parameters), engine.behaviorAnalysisSpi(null));
        Assertions.assertSame(engine.lineageAnalysisSpi(parameters), engine.lineageAnalysisSpi(null));
        Assertions.assertSame(engine.secDomainResolveSpi(parameters), engine.secDomainResolveSpi(null));
        Assertions.assertSame(engine.rewriteSpi(parameters), engine.rewriteSpi(null));
    }

    @Test
    public void parserParameterCacheUsesSortedStringKey() {
        MySqlEngineSpi engine = new MySqlEngineSpi(null);
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
        MySqlEngineSpi engine = new MySqlEngineSpi(null);

        MyDslProvider unknownProvider = (MyDslProvider) engine.dslProvider(new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.4.10")));
        Assertions.assertFalse(unknownProvider.config().isSqlModeKnown());

        MyDslProvider emptyProvider = (MyDslProvider) engine.dslProvider(parserParameters(""));
        Assertions.assertTrue(emptyProvider.config().isSqlModeKnown());
        Assertions.assertTrue(emptyProvider.config().features().isEmpty());

        String sqlMode = "ANSI,NO_BACKSLASH_ESCAPES";
        SqlParserParameters configuredParameters = parserParameters(sqlMode);
        Assertions.assertEquals(sqlMode, configuredParameters.get(SqlParserParameters.SQL_MODE));
        MyDslProvider configuredProvider = (MyDslProvider) engine.dslProvider(configuredParameters);
        Assertions.assertEquals(java.util.EnumSet.of(Feature.ANSI_QUOTES, Feature.PIPES_AS_CONCAT, Feature.IGNORE_SPACE, Feature.NO_BACKSLASH_ESCAPES), configuredProvider.config()
            .features());
    }

    @Test
    public void allAnalysisSpisUseTheSameSqlModeParameters() {
        MySqlEngineSpi engine = new MySqlEngineSpi(metaService());
        SqlParserParameters ansiQuotes = parserParameters("ANSI_QUOTES");
        SqlParserParameters knownEmpty = parserParameters("");
        String sql = "SELECT * FROM \"table1\";";

        Map<UmiTypes, Object> levels = Map.of(UmiTypes.Catalog, "catalog1", UmiTypes.Schema, "schema1");
        Assertions.assertDoesNotThrow(() -> {
            try (StringReader reader = new StringReader(sql);
                    Stream<StatementBehavior> stream = engine.behaviorAnalysisSpi(ansiQuotes).analysisBehaviorStream(reader, levels, 0, 0)) {
                return stream.toList();
            }
        });
        Assertions.assertThrows(AntlerSyntaxException.class, () -> {
            try (StringReader reader = new StringReader(sql);
                    Stream<StatementBehavior> stream = engine.behaviorAnalysisSpi(knownEmpty).analysisBehaviorStream(reader, levels, 0, 0)) {
                stream.toList();
            }
        });

        Assertions.assertDoesNotThrow(() -> {
            try (StringReader reader = new StringReader(sql);
                    Stream<RuleDomain> stream = engine.secDomainResolveSpi(ansiQuotes).resolveDomainStream(DataSourceType.MySQL, reader, 0, 0, null)) {
                return stream.toList();
            }
        });
        Assertions.assertThrows(AntlerSyntaxException.class, () -> {
            try (StringReader reader = new StringReader(sql);
                    Stream<RuleDomain> stream = engine.secDomainResolveSpi(knownEmpty).resolveDomainStream(DataSourceType.MySQL, reader, 0, 0, null)) {
                stream.toList();
            }
        });

        var columnContextInfo = com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext.builder().levelsParam(levels).build();
        Assertions.assertDoesNotThrow(() -> {
            return engine.lineageAnalysisSpi(ansiQuotes).analyze(sql, columnContextInfo);
        });
        Assertions.assertThrows(AntlerSyntaxException.class, () -> {
            engine.lineageAnalysisSpi(knownEmpty).analyze(sql, columnContextInfo);
        });

        RewriteContext rewriteContext = new RewriteContext();
        rewriteContext.setFetchLimit(10);
        rewriteContext.setParameters(ansiQuotes);
        Assertions.assertTrue(engine.rewriteSpi(ansiQuotes).rewriteLimit(null, sql, rewriteContext).contains("LIMIT 10"));
        rewriteContext.setParameters(knownEmpty);
        Assertions.assertThrows(AntlerSyntaxException.class, () -> {
            engine.rewriteSpi(knownEmpty).rewriteLimit(null, sql, rewriteContext);
        });
    }

    @Test
    public void splitRetriesNoBackslashEscapesWhenSqlModeIsUnknown() {
        MySqlEngineSpi engine = new MySqlEngineSpi(null);
        String sql = "SELECT /*!50000 'trailing backslash\\' */;";

        try (var scripts = engine.splitAnalysisSpi(SqlParserParameters.ofVersion("8.0.46")).splitScriptStream(new StringReader(sql), null, 0, 0)) {
            var result = scripts.toList();
            Assertions.assertEquals(1, result.size());
            Assertions.assertEquals(sql, result.get(0).getScript());
        }
    }

    private static SqlParserParameters parserParameters(String sqlMode) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(SqlParserParameters.VERSION, "8.4.10");
        values.put(SqlParserParameters.SQL_MODE, sqlMode);
        return new SqlParserParameters(values);
    }

    private static MetaService metaService() {
        return new MetaService() {
            @Override
            public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
                MetaCol column = new MetaCol();
                column.setTable(tableName);
                column.setColumn("id");
                return List.of(column);
            }

            @Override
            public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
                return List.of();
            }
        };
    }

}
