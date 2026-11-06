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
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.dameng.sql.analysis.reference.DmResourceRegistry;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.ds.dameng.sql.parser.DmVersion;
import com.clougence.clouddm.ds.permission.PermissionTextTest;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.sql.common.registry.BuiltinFunctionCatalogLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

@Execution(ExecutionMode.CONCURRENT)
public final class Dameng8PermissionTextTest {

    private static final String RESOURCE_DIRECTORY = "permission/dameng/8";
    private static final String VERSION            = "8";

    @Test
    public void permissionFixtureCompleteness() {
        PermissionTextTest.assertCompleteResourceDirectory(RESOURCE_DIRECTORY);
    }

    @TestFactory
    public Stream<DynamicTest> builtInCatalogRetainsQualifiedNamesAndVersions() {
        DmResourceRegistry registry = DmResourceRegistry.instance();
        return BuiltinFunctionCatalogLoader.load(DmResourceRegistry.class, "/META-INF/clougence/dameng-built-in-functions.json")
            .stream()
            .map(function -> DynamicTest.dynamicTest(function.name(), () -> {
                String[] nameParts = function.nameParts().toArray(String[]::new);
                Assert.assertTrue(registry.isSystemFunction(DmVersion.DM_8, nameParts));
                Assert.assertFalse(registry.isSystemFunction(DmVersion.DM_7, nameParts));
            }));
    }

    @Test
    public void systemCatalogOnlyRegistersNonFunctionObjects() throws Exception {
        try (var input = DmResourceRegistry.class.getResourceAsStream("/META-INF/clougence/dameng-skip-permission-resources.json")) {
            var entries = new ObjectMapper().readTree(input).get("entries");
            for (var entry : entries) {
                Assert.assertNotEquals("FUNCTION", entry.get("type").asText());
            }
        }
    }

    @TestFactory
    public Stream<DynamicTest> permissionScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> {
            BehaviorAnalysisSpi analysisSpi = SqlTestSupport.sqlEngine("dameng").behaviorAnalysisSpi(//
                    new SqlParserParameters(Map.of(SqlParserParameters.VERSION, VERSION)));
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
