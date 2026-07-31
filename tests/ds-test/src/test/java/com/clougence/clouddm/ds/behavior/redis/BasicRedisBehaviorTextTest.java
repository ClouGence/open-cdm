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
package com.clougence.clouddm.ds.behavior.redis;

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

@Execution(ExecutionMode.CONCURRENT)
public abstract class BasicRedisBehaviorTextTest {

    private final TextResourceShard fixtureShard;

    protected BasicRedisBehaviorTextTest(int shardCount, int shardId){
        this.fixtureShard = new TextResourceShard("behavior/redis", shardCount, shardId);
    }

    @TestFactory
    public Stream<DynamicTest> behaviorScripts() {
        ThreadLocal<BehaviorAnalysisSpi> spi = ThreadLocal.withInitial(() -> SqlTestSupport.sqlEngine("redis").behaviorAnalysisSpi(SqlParserParameters.empty()));
        return fixtureShard.resourceFiles().stream()
            .flatMap(resourcePath -> BehaviorTextTest.loadCases(resourcePath).stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(), () -> {
                    BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi.get(), null);
                })));
    }
}

final class RedisBehaviorShard1Test extends BasicRedisBehaviorTextTest { RedisBehaviorShard1Test(){ super(3, 0); } }
final class RedisBehaviorShard2Test extends BasicRedisBehaviorTextTest { RedisBehaviorShard2Test(){ super(3, 1); } }
final class RedisBehaviorShard3Test extends BasicRedisBehaviorTextTest { RedisBehaviorShard3Test(){ super(3, 2); } }
