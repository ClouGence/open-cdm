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
package com.clougence.clouddm.ds.permission.dameng;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.ds.permission.PermissionTextTest;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;

@Execution(ExecutionMode.CONCURRENT)
public final class Dameng8PermissionTextTest {

    private static final String RESOURCE_DIRECTORY = "permission/dameng/8";
    private static final String VERSION            = "8";

    @Test
    public void permissionFixtureCompleteness() {
        PermissionTextTest.assertCompleteResourceDirectory(RESOURCE_DIRECTORY);
    }

    @TestFactory
    public Stream<DynamicTest> permissionScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> {
            BehaviorAnalysisSpi analysisSpi = SqlTestSupport.sqlEngine("dameng").behaviorAnalysisSpi(//
                    SqlParserParameters.ofVersion(VERSION));
            if (analysisSpi == null) {
                throw new IllegalStateException("No BehaviorAnalysisSpi for Dameng " + VERSION);
            }
            return analysisSpi;
        });
        DmSysObjectRegistrySpi registry = new DmSysObjectRegistrySpi();
        List<DynamicTest> tests = new ArrayList<>();
        for (String resourcePath : TextCaseSupport.resourceFiles(RESOURCE_DIRECTORY)) {
            for (PermissionTextTest.TestCase testCase : PermissionTextTest.loadCases(resourcePath)) {
                tests.add(DynamicTest.dynamicTest(testCase.displayName(), () -> PermissionTextTest.assertStrictCase(//
                        resourcePath, testCase, spi.get(), registry, VERSION)));
            }
        }
        return tests.stream();
    }
}
