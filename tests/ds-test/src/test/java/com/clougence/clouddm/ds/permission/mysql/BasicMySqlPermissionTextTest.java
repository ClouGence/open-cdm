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
package com.clougence.clouddm.ds.permission.mysql;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.ds.permission.PermissionTextTest;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.sysobj.MySysObjectRegistrySpi;

/** MySQL permission fixtures are isolated by parser version. */
@Execution(ExecutionMode.CONCURRENT)
public abstract class BasicMySqlPermissionTextTest {

    private final String resourceDirectory;
    private final String version;
    private final TextResourceShard fixtureShard;

    protected BasicMySqlPermissionTextTest(String directoryName, String version, int shardCount, int shardId){
        this.resourceDirectory = "permission/mysql/" + directoryName;
        this.version = version;
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    @Test
    public void permissionFixtureCompleteness() {
        PermissionTextTest.assertCompleteResourceDirectory(resourceDirectory);
    }

    @TestFactory
    public Stream<DynamicTest> permissionScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> {
            MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());
            BehaviorAnalysisSpi analysisSpi = engine.behaviorAnalysisSpi(SqlParserParameters.ofVersion(version));
            if (analysisSpi == null) {
                throw new IllegalStateException("No BehaviorAnalysisSpi for MySQL " + version);
            }
            return analysisSpi;
        });
        MySysObjectRegistrySpi registry = new MySysObjectRegistrySpi();
        return fixtureShard.resourceFiles().stream()
            .flatMap(resourcePath -> PermissionTextTest.loadCases(resourcePath).stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(), () -> PermissionTextTest.assertStrictCase(//
                        resourcePath, testCase, spi.get(), registry, version))));
    }
}
