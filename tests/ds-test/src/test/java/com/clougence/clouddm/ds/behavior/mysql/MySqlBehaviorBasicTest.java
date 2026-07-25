package com.clougence.clouddm.ds.behavior.mysql;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.behavior.BehaviorTextTest;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.sql.mysql.MySqlEngineSpi;

public final class MySqlBehaviorBasicTest {

    @Test
    public void basicRelations() {
        String resourcePath = "behavior/mysql/8.0/basic.txt";
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
        BehaviorAnalysisSpi spi = engine.behaviorAnalysisSpi(SqlParserParameters.ofVersion("8.0.46"));
        for (BehaviorTextTest.TestCase testCase : BehaviorTextTest.loadCases(resourcePath)) {
            BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi);
        }
    }
}
