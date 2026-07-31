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

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.ds.behavior.BehaviorTextTest;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.sysobj.MySysObjectRegistrySpi;

/** MySQL behavior fixtures are isolated by parser version. */
@Execution(ExecutionMode.CONCURRENT)
public abstract class BasicMySqlBehaviorTextTest {

    private final String            resourceDirectory;
    private final String            version;
    private final TextResourceShard fixtureShard;

    protected BasicMySqlBehaviorTextTest(String directoryName, String version, int shardCount, int shardId){
        this.resourceDirectory = "behavior/mysql/" + directoryName;
        this.version = version;
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @TestFactory
    public Stream<DynamicTest> behaviorScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> {
            MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
            BehaviorAnalysisSpi analysisSpi = engine.behaviorAnalysisSpi(SqlParserParameters.ofVersion(version));
            if (analysisSpi == null) {
                throw new IllegalStateException("No BehaviorAnalysisSpi for MySQL " + version);
            }
            return analysisSpi;
        });
        MySysObjectRegistrySpi registry = new MySysObjectRegistrySpi();

        String fixtureFilter = System.getenv("BEHAVIOR_FIXTURE");
        String caseFilter = System.getenv("BEHAVIOR_CASE");
        return fixtureShard.resourceFiles()
            .stream()
            .filter(resourcePath -> fixtureFilter == null || resourcePath.contains(fixtureFilter))
            .flatMap(resourcePath -> BehaviorTextTest.loadCases(resourcePath)
                .stream()
                .filter(testCase -> caseFilter == null || testCase.displayName().contains(caseFilter))
                .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(), () -> BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi.get(), relation -> {
                    BehaviorObject object = relation.getSubject();
                    ObjectName name = object.getObjectName();
                    return name != null && registry.isPermissionExempt(relation.getAction(), object.getObjectType(),//
                            name.getCatalog(), name.getSchema(), name.getObjectName(), version);
                }))));
    }
}
