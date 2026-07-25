/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;

public class BehaviorDialectTextTest {

    @TestFactory
    public Stream<DynamicTest> behaviorScripts() {
        String datasourceFilter = System.getenv("BEHAVIOR_DATASOURCE");
        List<DynamicTest> tests = new ArrayList<>();
        for (String resourcePath : TextCaseSupport.resourceFiles("behavior")) {
            if (resourcePath.startsWith("behavior/mysql/") || resourcePath.endsWith("/README.md")) {
                continue;
            }

            String datasource = SqlTestSupport.datasourceFromPath(resourcePath);
            if (datasourceFilter != null && !datasource.equals(datasourceFilter)) {
                continue;
            }
            SqlEngineSpi engine = SqlTestSupport.sqlEngine(datasource);
            BehaviorAnalysisSpi spi = engine.behaviorAnalysisSpi(SqlParserParameters.empty());
            if (spi == null) {
                throw new IllegalStateException("No BehaviorAnalysisSpi for datasource: " + datasource);
            }

            for (BehaviorTextTest.TestCase testCase : BehaviorTextTest.loadCases(resourcePath)) {
                tests.add(DynamicTest.dynamicTest(testCase.displayName(),
                        () -> BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi)));
            }
        }
        return tests.stream();
    }
}
