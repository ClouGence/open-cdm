package com.clougence.clouddm.ds.split.mysql;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.ds.split.BasicSplitTextTest;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySplitAnalysisSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

/** MySQL split fixtures are isolated by version directory. */
public abstract class BasicMySqlSplitTextTest extends BasicSplitTextTest {

    private final String resourceDirectory;
    private final String version;
    private final TextResourceShard fixtureShard;

    protected BasicMySqlSplitTextTest(String directoryName, String version, int shardCount, int shardId){
        this.resourceDirectory = "split/mysql/" + directoryName;
        this.version = version;
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @Override
    protected final List<String> fixtureResources() {
        return fixtureShard.resourceFiles(path -> {
            return !path.contains("/reject/") &&//
                   !path.contains("/mode/") &&  //
                   !isNestedExactVersion(path);
        });
    }

    @Override
    protected final SplitAnalysisSpi splitAnalysisSpi(Fixture fixture) {
        return new MySplitAnalysisSpi(new MyDslProvider(MySqlParserConfig.unknownSqlMode(version)));
    }

    @Override
    protected final boolean verifyAllTypes() {
        return true;
    }

    @Override
    protected final void splitRejectedCase(String resourcePath, String datasource, String rejectedSql) throws Exception {
        try (StringReader reader = new StringReader(rejectedSql)) {
            DslHelper.splitDsl(new MyDslProvider(MySqlParserConfig.unknownSqlMode(version)), reader);
        }
    }

    @TestFactory
    public Stream<DynamicTest> rejectedScripts() {
        List<String> resources = fixtureShard.resourceFiles(path -> {
            return path.contains("/reject/") && //
                   !path.contains("/mode/") &&  //
                   !isNestedExactVersion(path);
        });
        return rejectedDynamicTests(resources, "mysql");
    }

    private boolean isNestedExactVersion(String path) {
        return path.substring(resourceDirectory.length()).contains("/exact-");
    }
}
