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
package com.clougence.clouddm.ds.version.doris;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.sql.doris.DrSqlEngineSpi;
import com.clougence.sql.doris.parser.DorisParserConfig;
import com.clougence.sql.doris.parser.DorisVersion;
import com.clougence.sql.doris.parser.DrDslProvider;
import com.clougence.sql.doris.parser.antlr.DorisLexer;
import com.clougence.sql.doris.parser.antlr.DorisParser;

public class DorisVersionConfigurationTest {

    private static final String BITMAP_INDEX = "CREATE INDEX idx ON t (c) USING BITMAP;";
    private static final String ANN_INDEX    = "CREATE INDEX idx ON t (c) USING ANN;";

    @Test
    public void versionsAreOrderedAndLatestIsTheDefault() {
        Assertions.assertTrue(DorisVersion.DORIS_3.atLeast(DorisVersion.DORIS_2));
        Assertions.assertTrue(DorisVersion.DORIS_3.atMost(DorisVersion.DORIS_4));
        Assertions.assertEquals(DorisVersion.DORIS_4, DorisVersion.LATEST);
        Assertions.assertEquals(DorisVersion.LATEST, DorisParserConfig.fromParameters(SqlParserParameters.empty()).version());
        Assertions.assertEquals(DorisVersion.LATEST, DorisParserConfig.fromParameters(null).version());
    }

    @Test
    public void parserParametersAreOptionalAcrossEveryEngineCapability() {
        DrSqlEngineSpi engine = new DrSqlEngineSpi(null);
        SqlParserParameters parameters = SqlParserParameters.empty();
        DrDslProvider defaultProvider = (DrDslProvider) engine.dslProvider(parameters);

        Assertions.assertEquals(DorisVersion.LATEST, defaultProvider.config().version());
        Assertions.assertSame(defaultProvider, engine.dslProvider(null));
        Assertions.assertSame(defaultProvider, engine.dslProvider(parameters));
        Assertions.assertSame(engine.splitAnalysisSpi(parameters), engine.splitAnalysisSpi(null));
        Assertions.assertSame(engine.behaviorAnalysisSpi(parameters), engine.behaviorAnalysisSpi(null));
        Assertions.assertSame(engine.lineageAnalysisSpi(parameters), engine.lineageAnalysisSpi(null));
        Assertions.assertSame(engine.secDomainResolveSpi(parameters), engine.secDomainResolveSpi(null));
        Assertions.assertSame(engine.rewriteSpi(parameters), engine.rewriteSpi(null));
    }

    @Test
    public void versionParameterSelectsTheMajorGrammarFamily() {
        Assertions.assertEquals(DorisVersion.DORIS_2, config("2.1.11").version());
        Assertions.assertEquals(DorisVersion.DORIS_3, config("v3.1.4-rc02").version());
        Assertions.assertEquals(DorisVersion.DORIS_3, config("Apache Doris version doris-3.1.4-rc02-abcd").version());
        Assertions.assertEquals(DorisVersion.DORIS_4, config("4.1.3").version());
        Assertions.assertEquals(DorisVersion.DORIS_4, config("5.0.0").version());
    }

    @Test
    public void grammarVersionCanOverrideTheDatabaseVersion() {
        SqlParserParameters parameters = new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "4.1.3", SqlParserParameters.GRAMMAR_VERSION, "3.1"));
        DrSqlEngineSpi engine = new DrSqlEngineSpi(null);
        DrDslProvider provider = (DrDslProvider) engine.dslProvider(parameters);

        Assertions.assertEquals(DorisVersion.DORIS_3, provider.config().version());
        Assertions.assertDoesNotThrow(() -> split(engine, parameters, BITMAP_INDEX));
        Assertions.assertThrows(AntlerSyntaxException.class, () -> split(engine, parameters, ANN_INDEX));
    }

    @Test
    public void configuredVersionActivatesTheMatchingIndexSyntax() {
        DrSqlEngineSpi engine = new DrSqlEngineSpi(null);
        SqlParserParameters doris2 = SqlParserParameters.ofVersion("2.1.11");
        SqlParserParameters doris3 = SqlParserParameters.ofVersion("3.1.4");
        SqlParserParameters doris4 = SqlParserParameters.ofVersion("4.1.3");

        Assertions.assertDoesNotThrow(() -> split(engine, doris2, BITMAP_INDEX));
        Assertions.assertDoesNotThrow(() -> split(engine, doris3, BITMAP_INDEX));
        Assertions.assertThrows(AntlerSyntaxException.class, () -> split(engine, doris4, BITMAP_INDEX));

        Assertions.assertThrows(AntlerSyntaxException.class, () -> split(engine, doris2, ANN_INDEX));
        Assertions.assertThrows(AntlerSyntaxException.class, () -> split(engine, doris3, ANN_INDEX));
        Assertions.assertDoesNotThrow(() -> split(engine, doris4, ANN_INDEX));
    }

    @Test
    public void providerAppliesTheSameImmutableConfigToLexerAndParser() {
        DorisParserConfig config = config("3.1.4");
        DrDslProvider provider = new DrDslProvider(config);
        DorisLexer lexer = (DorisLexer) provider.createLexer(CharStreams.fromString(BITMAP_INDEX));
        DorisParser parser = (DorisParser) provider.createParser(lexer);

        Assertions.assertSame(config, lexer.config());
        Assertions.assertSame(config, parser.config());
    }

    @Test
    public void everyParserBackedSpiIsCachedByDorisConfiguration() {
        DrSqlEngineSpi engine = new DrSqlEngineSpi(null);
        SqlParserParameters doris3 = SqlParserParameters.ofVersion("3.1.4");
        SqlParserParameters anotherDoris3 = SqlParserParameters.ofVersion("3.0.8");
        SqlParserParameters doris4 = SqlParserParameters.ofVersion("4.1.3");

        Assertions.assertSame(engine.dslProvider(doris3), engine.dslProvider(anotherDoris3));
        Assertions.assertNotSame(engine.dslProvider(doris3), engine.dslProvider(doris4));
        Assertions.assertSame(engine.splitAnalysisSpi(doris3), engine.splitAnalysisSpi(anotherDoris3));
        Assertions.assertNotSame(engine.splitAnalysisSpi(doris3), engine.splitAnalysisSpi(doris4));
        Assertions.assertSame(engine.secDomainResolveSpi(doris3), engine.secDomainResolveSpi(anotherDoris3));
        Assertions.assertNotSame(engine.secDomainResolveSpi(doris3), engine.secDomainResolveSpi(doris4));
        Assertions.assertSame(engine.behaviorAnalysisSpi(doris3), engine.behaviorAnalysisSpi(anotherDoris3));
        Assertions.assertNotSame(engine.behaviorAnalysisSpi(doris3), engine.behaviorAnalysisSpi(doris4));
        Assertions.assertSame(engine.lineageAnalysisSpi(doris3), engine.lineageAnalysisSpi(anotherDoris3));
        Assertions.assertNotSame(engine.lineageAnalysisSpi(doris3), engine.lineageAnalysisSpi(doris4));
        Assertions.assertSame(engine.rewriteSpi(doris3), engine.rewriteSpi(anotherDoris3));
        Assertions.assertNotSame(engine.rewriteSpi(doris3), engine.rewriteSpi(doris4));
    }

    @Test
    public void analysisSpisUseTheirConfiguredParserProvider() {
        DrSqlEngineSpi engine = new DrSqlEngineSpi(null);
        SqlParserParameters doris3 = SqlParserParameters.ofVersion("3.1.4");
        SqlParserParameters doris4 = SqlParserParameters.ofVersion("4.1.3");

        Assertions.assertDoesNotThrow(() -> analyzeBehavior(engine, doris3, BITMAP_INDEX));
        Assertions.assertThrows(AntlerSyntaxException.class, () -> analyzeBehavior(engine, doris4, BITMAP_INDEX));
    }

    private static DorisParserConfig config(String version) {
        return DorisParserConfig.fromParameters(SqlParserParameters.ofVersion(version));
    }

    private static List<SplitScript> split(DrSqlEngineSpi engine, SqlParserParameters parameters, String sql) {
        try (var scripts = engine.splitAnalysisSpi(parameters).splitScriptStream(new StringReader(sql), null, 0, 0)) {
            return scripts.toList();
        }
    }

    private static void analyzeBehavior(DrSqlEngineSpi engine, SqlParserParameters parameters, String sql) {
        try (var behaviors = engine.behaviorAnalysisSpi(parameters).analysisBehaviorStream(new StringReader(sql), Map.of(), 0, 0)) {
            behaviors.toList();
        }
    }
}
