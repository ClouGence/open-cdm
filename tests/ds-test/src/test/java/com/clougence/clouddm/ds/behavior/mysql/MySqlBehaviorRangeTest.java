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

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.SqlTestSupport;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.MySqlEngineSpi;

public class MySqlBehaviorRangeTest {

    @Test
    public void shouldApplyDocumentOffsetAndKeepEndExclusiveRanges() {
        String sql = "SELECT db1.f_score(t.score)\nFROM db1.t1 t;";
        MySqlEngineSpi engine = new MySqlEngineSpi(SqlTestSupport.metaService());

        List<StatementBehavior> statements = engine.behaviorAnalysisSpi(
                        SqlParserParameters.ofVersion("8.0.46"))
                .analysisBehavior(sql, Map.of(
                        UmiTypes.Instance, "test/1",
                        UmiTypes.Catalog, "catalog1",
                        UmiTypes.Schema, "schema1"), 10, 5);

        Assertions.assertEquals(1, statements.size());
        List<BehaviorRelation> relations = statements.get(0).getRelations();
        Assertions.assertEquals(2, relations.size());
        assertObject(relation(relations, BehaviorAction.CALL).getSubject(),
                TargetType.Function, 10, 12, 10, 23,
                "/test/1/catalog1/db1/f_score/");
        assertObject(relation(relations, BehaviorAction.READ).getSubject(),
                TargetType.Table, 11, 5, 11, 11,
                "/test/1/catalog1/db1/t1/");
    }

    private static BehaviorRelation relation(List<BehaviorRelation> relations, BehaviorAction action) {
        return relations.stream()
                .filter(relation -> relation.getAction() == action)
                .findFirst()
                .orElseThrow();
    }

    private static void assertObject(BehaviorObject object, TargetType targetType,
            int startLine, int startColumn, int endLine, int endColumn, String resourcePath) {
        Assertions.assertEquals(targetType, object.getTargetType());
        Assertions.assertEquals(startLine, object.getStartLine());
        Assertions.assertEquals(startColumn, object.getStartColumn());
        Assertions.assertEquals(endLine, object.getEndLine());
        Assertions.assertEquals(endColumn, object.getEndColumn());
        Assertions.assertEquals(resourcePath, object.getResourcePath());
    }
}
