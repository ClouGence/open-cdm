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
package com.clougence.clouddm.ds.behavior.dameng;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.behavior.BehaviorTextTest;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;

@Execution(ExecutionMode.CONCURRENT)
public final class DamengBehaviorTextTest {

    @TestFactory
    public Stream<DynamicTest> behaviorScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> {
            BehaviorAnalysisSpi analysisSpi = SqlTestSupport.sqlEngine("dameng").behaviorAnalysisSpi(SqlParserParameters.ofVersion("8"));
            if (analysisSpi == null) {
                throw new IllegalStateException("No BehaviorAnalysisSpi for Dameng 8");
            }
            return analysisSpi;
        });
        DmSysObjectRegistrySpi registry = new DmSysObjectRegistrySpi();

        List<DynamicTest> tests = new ArrayList<>();
        for (String resourcePath : TextCaseSupport.resourceFiles("behavior/dameng")) {
            for (BehaviorTextTest.TestCase testCase : BehaviorTextTest.loadCases(resourcePath)) {
                tests.add(DynamicTest.dynamicTest(testCase.displayName(), () -> {
                    BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi.get(), relation -> {
                        BehaviorObject object = relation.getSubject();
                        ObjectName name = object.getObjectName();
                        return name != null && registry.isPermissionExempt(relation.getAction(), object.getObjectType(),//
                                name.getCatalog(), name.getSchema(), name.getObjectName(), "8");
                    });
                }));
            }
        }
        return tests.stream();
    }
}
