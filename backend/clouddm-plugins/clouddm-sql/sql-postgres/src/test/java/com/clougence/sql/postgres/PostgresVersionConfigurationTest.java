package com.clougence.sql.postgres;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.sql.postgres.analysis.behavior.PgBehaviorAnalysisSpi;
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
        Assertions.assertEquals(PostgresVersion.LATEST, PostgresVersion.parse(null));
        Assertions.assertEquals(PostgresVersion.LATEST, PostgresVersion.parse(""));
        Assertions.assertEquals(PostgresVersion.LATEST, PostgresVersion.parse("unknown"));
    }

    @Test
    public void enginePassesExplicitVersionToEveryComponent() {
        PgSqlEngineSpi engine = new PgSqlEngineSpi(null);
        SqlParserParameters parameters = SqlParserParameters.ofVersion("16.4 (Debian)");

        Assertions.assertEquals(PostgresVersion.POSTGRES_16, ((PgDslProvider) engine.dslProvider(parameters)).version());
        Assertions.assertEquals(PostgresVersion.POSTGRES_16, ((PgSplitAnalysisSpi) engine.splitAnalysisSpi(parameters)).version());
        Assertions.assertEquals(PostgresVersion.POSTGRES_16, ((PgSecDomainResolveSpi) engine.secDomainResolveSpi(parameters)).version());
        Assertions.assertEquals(PostgresVersion.POSTGRES_16, ((PgBehaviorAnalysisSpi) engine.behaviorAnalysisSpi(parameters)).version());
        Assertions.assertSame(LineageAnalysisSpi.EMPTY, engine.lineageAnalysisSpi(parameters));
        Assertions.assertEquals(PostgresVersion.POSTGRES_16, ((PgRewriteSpi) engine.rewriteSpi(parameters)).version());
    }

    @Test
    public void engineUsesLatestWhenVersionCannotBeObtained() {
        PgSqlEngineSpi engine = new PgSqlEngineSpi(null);
        SqlParserParameters parameters = SqlParserParameters.empty();

        PgDslProvider provider = (PgDslProvider) engine.dslProvider(parameters);
        Assertions.assertEquals(PostgresVersion.LATEST, provider.version());
        Assertions.assertSame(provider, engine.dslProvider(null));
        Assertions.assertSame(provider, engine.dslProvider(parameters));

        Assertions.assertEquals(PostgresVersion.LATEST, ((PgSplitAnalysisSpi) engine.splitAnalysisSpi(parameters)).version());
        Assertions.assertEquals(PostgresVersion.LATEST, ((PgSecDomainResolveSpi) engine.secDomainResolveSpi(parameters)).version());
        Assertions.assertEquals(PostgresVersion.LATEST, ((PgBehaviorAnalysisSpi) engine.behaviorAnalysisSpi(parameters)).version());
        Assertions.assertSame(LineageAnalysisSpi.EMPTY, engine.lineageAnalysisSpi(parameters));
        Assertions.assertEquals(PostgresVersion.LATEST, ((PgRewriteSpi) engine.rewriteSpi(parameters)).version());
        Assertions.assertEquals(PostgresVersion.LATEST, ((PgDslProvider) engine.dslProvider(SqlParserParameters.ofVersion("unsupported"))).version());
    }
}
