package com.clougence.clouddm.ds.split.mysql;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.split.SplitTextTest;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySplitAnalysisSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

/** MySQL split fixtures are isolated by version directory. */
public abstract class MySqlSplitTextTest extends SplitTextTest {

    private final String resourceDirectory;
    private final String version;

    protected MySqlSplitTextTest(String directoryName, String version){
        this.resourceDirectory = "split/mysql/" + directoryName;
        this.version = version;
    }

    @Override
    protected final List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles(resourceDirectory, path -> {
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
        DslHelper.splitDsl(new MyDslProvider(MySqlParserConfig.unknownSqlMode(version)), rejectedSql);
    }

    @TestFactory
    public Stream<DynamicTest> rejectedScripts() {
        return rejectedDynamicTests(TextCaseSupport.resourceFiles(resourceDirectory, path -> {
            return path.contains("/reject/") && //
                   !path.contains("/mode/") &&  //
                   !isNestedExactVersion(path);
        }), "mysql");
    }

    private boolean isNestedExactVersion(String path) {
        return path.substring(resourceDirectory.length()).contains("/exact-");
    }
}
