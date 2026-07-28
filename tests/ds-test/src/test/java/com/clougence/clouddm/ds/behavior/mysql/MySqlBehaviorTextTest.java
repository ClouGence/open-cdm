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
package com.clougence.clouddm.ds.behavior.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.behavior.BehaviorTextTest;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.sysobj.MySysObjectRegistrySpi;

/** MySQL behavior fixtures are isolated by parser version. */
public abstract class MySqlBehaviorTextTest {

    private final String resourceDirectory;
    private final String version;

    protected MySqlBehaviorTextTest(String directoryName, String version){
        this.resourceDirectory = "behavior/mysql/" + directoryName;
        this.version = version;
    }

    @TestFactory
    public Stream<DynamicTest> behaviorScripts() {
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
        BehaviorAnalysisSpi spi = engine.behaviorAnalysisSpi(SqlParserParameters.ofVersion(version));
        MySysObjectRegistrySpi registry = new MySysObjectRegistrySpi();
        if (spi == null) {
            throw new IllegalStateException("No BehaviorAnalysisSpi for MySQL " + version);
        }

        List<DynamicTest> tests = new ArrayList<>();
        String fixtureFilter = System.getenv("BEHAVIOR_FIXTURE");
        String caseFilter = System.getenv("BEHAVIOR_CASE");
        for (String resourcePath : TextCaseSupport.resourceFiles(resourceDirectory)) {
            if (fixtureFilter != null && !resourcePath.contains(fixtureFilter)) {
                continue;
            }
            for (BehaviorTextTest.TestCase testCase : BehaviorTextTest.loadCases(resourcePath)) {
                if (caseFilter != null && !testCase.displayName().contains(caseFilter)) {
                    continue;
                }
                tests.add(DynamicTest.dynamicTest(testCase.displayName(),
                        () -> BehaviorTextTest.assertStrictCase(
                                resourcePath, testCase, spi, relation -> {
                                    BehaviorObject object = relation.getSubject();
                                    ObjectName name = object.getObjectName();
                                    return name != null && registry.isPermissionExempt(relation.getAction(), object.getObjectType(),//
                                            name.getCatalog(), name.getSchema(), name.getObjectName(), version);
                                })));
            }
        }
        return tests.stream();
    }
}
