package com.clougence.clouddm.ds.split.postgres;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.sql.postgres.PgSqlEngineSpi;
import com.clougence.sql.postgres.analysis.lineage.PgLineageAnalysisSpi;
import com.clougence.sql.postgres.analysis.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.editor.rewrite.PgRewriteSpi;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class PostgresVersionConfigurationTest {

    @Test
    public void versionsAreOrderedAndLatestIsTheDefault() {
        Assertions.assertTrue(PostgresVersion.POSTGRES_16.atLeast(PostgresVersion.POSTGRES_15));
        Assertions.assertTrue(PostgresVersion.POSTGRES_16.atMost(PostgresVersion.POSTGRES_17));
        Assertions.assertTrue(PostgresVersion.POSTGRES_16.between(PostgresVersion.POSTGRES_15, PostgresVersion.POSTGRES_17));
        Assertions.assertEquals(PostgresVersion.POSTGRES_18, PostgresVersion.LATEST);
        Assertions.assertEquals(PostgresVersion.LATEST, new PgDslProvider(PostgresVersion.LATEST).version());
    }

    @Test
    public void engineKeepsItsExplicitParserVersionAcrossComponents() {
        String versionStr = "16";
        PostgresVersion version = PostgresVersion.POSTGRES_16;
        PgSqlEngineSpi engine = new PgSqlEngineSpi(SqlTestSupport.metaService());
        SqlParserParameters parameters = SqlParserParameters.ofVersion(versionStr);

        Assertions.assertEquals(version, ((PgDslProvider) engine.dslProvider(parameters)).version());
        Assertions.assertEquals(version, ((PgSplitAnalysisSpi) engine.splitAnalysisSpi(parameters)).version());
        Assertions.assertEquals(version, ((PgSecDomainResolveSpi) engine.secDomainResolveSpi(parameters)).version());
        Assertions.assertNotNull(engine.behaviorAnalysisSpi(parameters));
        Assertions.assertEquals(version, ((PgLineageAnalysisSpi) engine.lineageAnalysisSpi(parameters)).version());
        Assertions.assertEquals(version, ((PgRewriteSpi) engine.rewriteSpi(parameters)).version());
    }

    @Test
    public void parserParameterCacheUsesSortedStringKey() {
        PgSqlEngineSpi engine = new PgSqlEngineSpi(SqlTestSupport.metaService());
        Map<String, String> values = new LinkedHashMap<>();
        values.put(SqlParserParameters.VERSION, "16");
        values.put("cacheMarker", "first");
        Map<String, String> reorderedValues = new LinkedHashMap<>();
        reorderedValues.put("cacheMarker", "first");
        reorderedValues.put(SqlParserParameters.VERSION, "16");
        Map<String, String> differentValues = new LinkedHashMap<>(values);
        differentValues.put("cacheMarker", "second");

        PgDslProvider provider = (PgDslProvider) engine.dslProvider(new SqlParserParameters(values));
        PgDslProvider reorderedProvider = (PgDslProvider) engine.dslProvider(new SqlParserParameters(reorderedValues));
        PgDslProvider differentProvider = (PgDslProvider) engine.dslProvider(new SqlParserParameters(differentValues));

        Assertions.assertSame(provider, reorderedProvider);
        Assertions.assertNotSame(provider, differentProvider);
    }
}
