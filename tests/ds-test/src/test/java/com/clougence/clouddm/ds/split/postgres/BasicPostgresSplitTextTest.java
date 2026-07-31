package com.clougence.clouddm.ds.split.postgres;

import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.ds.split.BasicSplitTextTest;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** PostgreSQL split fixtures are isolated by version directory. */
public abstract class BasicPostgresSplitTextTest extends BasicSplitTextTest {

    private final String          resourceDirectory;
    private final PostgresVersion version;
    private final TextResourceShard fixtureShard;

    protected BasicPostgresSplitTextTest(String directoryName, PostgresVersion version, int shardCount, int shardId){
        this.resourceDirectory = "split/postgres/" + directoryName;
        this.version = version;
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @Override
    protected final List<String> fixtureResources() {
        return fixtureShard.resourceFiles(path -> !path.contains("/reject/"));
    }

    @Override
    protected final SplitAnalysisSpi splitAnalysisSpi(Fixture fixture) {
        return new PgSplitAnalysisSpi(version);
    }

    @TestFactory
    public Stream<DynamicTest> rejectedScripts() {
        return rejectedDynamicTests(fixtureShard.resourceFiles(path -> path.contains("/reject/")), "postgres");
    }
}
