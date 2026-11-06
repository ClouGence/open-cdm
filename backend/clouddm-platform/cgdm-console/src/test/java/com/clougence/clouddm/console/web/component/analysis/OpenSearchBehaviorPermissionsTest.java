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
package com.clougence.clouddm.console.web.component.analysis;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;

public class OpenSearchBehaviorPermissionsTest {
    @Test
    public void indexActionsUseReadWriteOrDdlAndAllLookupsKeepIndependentReadChecks() {
        BehaviorObject subject = new BehaviorObject();
        subject.setObjectType(TargetType.Table);
        subject.setObjectName(new ObjectName(null, null, "books"));
        subject.setObjectPath("/1/7/books/");
        BehaviorObject lookup = new BehaviorObject();
        lookup.setObjectType(TargetType.Table);
        lookup.setObjectName(new ObjectName(null, null, "lookup"));
        lookup.setObjectPath("/1/7/lookup/");
        for (BehaviorAction action : List
            .of(BehaviorAction.READ, BehaviorAction.INSERT, BehaviorAction.REPLACE, BehaviorAction.UPDATE, BehaviorAction.DELETE, BehaviorAction.CREATE, BehaviorAction.ALTER, BehaviorAction.DROP)) {
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(subject);
            relation.setAction(action);
            relation.setTarget(List.of());
            SecDataAuthKind expected = SecDataAuthKind.WRITE;
            if (action == BehaviorAction.READ) {
                expected = SecDataAuthKind.READ;
                relation.setTarget(List.of(lookup));
            } else if (List.of(BehaviorAction.CREATE, BehaviorAction.ALTER, BehaviorAction.DROP).contains(action)) {
                expected = SecDataAuthKind.DDL;
            } else if (action == BehaviorAction.UPDATE || action == BehaviorAction.DELETE) {
                relation.setTarget(List.of(lookup));
            }
            var permissions = BehaviorRelations.flattenResourceIgnoringPermission(List.of(relation));
            assertEquals(expected, permissions.get(0).authKind());
            assertEquals("/1/7/books/", permissions.get(0).resource().getObjectPath());
            assertEquals(1 + relation.getTarget().size(), permissions.size());
            if (!relation.getTarget().isEmpty()) {
                assertEquals(SecDataAuthKind.READ, permissions.get(1).authKind());
                assertEquals(BehaviorAction.READ, permissions.get(1).action());
                assertEquals("/1/7/lookup/", permissions.get(1).resource().getObjectPath());
            }
        }
    }
}
