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

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.ds.behavior.BehaviorTextTest;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;

@Execution(ExecutionMode.CONCURRENT)
public abstract class BasicDamengBehaviorTextTest {

    private final TextResourceShard fixtureShard;

    protected BasicDamengBehaviorTextTest(int shardCount, int shardId){
        this.fixtureShard = new TextResourceShard("behavior/dameng/8", shardCount, shardId);
    }

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

        return fixtureShard.resourceFiles().stream()
            .flatMap(resourcePath -> BehaviorTextTest.loadCases(resourcePath).stream()
                .map(testCase -> DynamicTest.dynamicTest(testCase.displayName(), () -> {
                    BehaviorTextTest.assertStrictCase(resourcePath, testCase, spi.get(), relation -> {
                        BehaviorObject object = relation.getSubject();
                        ObjectName name = object.getObjectName();
                        return name != null && registry.isPermissionExempt(relation.getAction(), object.getObjectType(),//
                                name.getCatalog(), name.getSchema(), name.getObjectName(), "8");
                    });
                })));
    }
}

final class DamengBehaviorShard1Test extends BasicDamengBehaviorTextTest { DamengBehaviorShard1Test(){ super(3, 0); } }
final class DamengBehaviorShard2Test extends BasicDamengBehaviorTextTest { DamengBehaviorShard2Test(){ super(3, 1); } }
final class DamengBehaviorShard3Test extends BasicDamengBehaviorTextTest { DamengBehaviorShard3Test(){ super(3, 2); } }
