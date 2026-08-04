package com.clougence.clouddm.ds.split.postgres;

import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.ds.split.SplitTextTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** PostgreSQL split fixtures are isolated by version directory. */
public abstract class PostgresSplitTextTest extends SplitTextTest {

    private final String          resourceDirectory;
    private final PostgresVersion version;

    protected PostgresSplitTextTest(String directoryName, PostgresVersion version){
        this.resourceDirectory = "split/postgres/" + directoryName;
        this.version = version;
    }

    @Override
    protected final List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles(resourceDirectory, path -> !path.contains("/reject/"));
    }

    @Override
    protected final SplitAnalysisSpi splitAnalysisSpi(Fixture fixture) {
        return new PgSplitAnalysisSpi(version);
    }

    @TestFactory
    public Stream<DynamicTest> rejectedScripts() {
        return rejectedDynamicTests(TextCaseSupport.resourceFiles(resourceDirectory + "/reject"), "postgres");
    }
}
