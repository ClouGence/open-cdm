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
package com.clougence.clouddm.console.web.service.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;

public class ResourceActionConverterTest {

    private final ResourceActionConverter converter = new ResourceActionConverter();

    @Test
    public void convertReturnsEmptyForMissingBehavior() {
        assertTrue(this.converter.convert(null, "/instance/schema/").isEmpty());
        assertTrue(this.converter.convert(List.of(), "/instance/schema/").isEmpty());

        StatementBehavior behavior = new StatementBehavior();
        behavior.setRelations(null);
        assertTrue(this.converter.convert(List.of(behavior), "/instance/schema/").isEmpty());
    }

    @Test
    public void renameExpandsToDropAndCreate() {
        StatementBehavior behavior = behavior(SecQueryType.RENAME_TABLE, relation(BehaviorAction.RENAME, //
                object(TargetType.Table, "/instance/schema/source/"), object(TargetType.Table, "/instance/schema/target/")));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(2, actions.size());
        assertAction(actions.get(0), BehaviorAction.DROP, "/instance/schema/source/");
        assertAction(actions.get(1), BehaviorAction.CREATE, "/instance/schema/target/");
    }

    @Test
    public void createAsSelectKeepsCreateAndReadActions() {
        StatementBehavior behavior = behavior(SecQueryType.CREATE_TABLE, relation(BehaviorAction.CREATE, //
                object(TargetType.Table, "/instance/schema/target/"), object(TargetType.Table, "/instance/schema/source/")));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(2, actions.size());
        assertAction(actions.get(0), BehaviorAction.CREATE, "/instance/schema/target/");
        assertAction(actions.get(1), BehaviorAction.READ, "/instance/schema/source/");
    }

    @Test
    public void tableOwnedObjectAlsoAltersCarrierTable() {
        StatementBehavior behavior = behavior(SecQueryType.ADD_INDEX, relation(BehaviorAction.CREATE, //
                object(TargetType.Index, "/instance/schema/table/index/"), object(TargetType.Table, "/instance/schema/table/")));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(2, actions.size());
        assertAction(actions.get(0), BehaviorAction.CREATE, "/instance/schema/table/index/");
        assertAction(actions.get(1), BehaviorAction.ALTER, "/instance/schema/table/");
    }

    @Test
    public void distinctKeepsDifferentActionsOnSameResource() {
        BehaviorObject table = object(TargetType.Table, "/instance/schema/table/");
        StatementBehavior behavior = behavior(SecQueryType.MERGE, //
                relation(BehaviorAction.READ, table), relation(BehaviorAction.UPDATE, table));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(2, actions.size());
        assertAction(actions.get(0), BehaviorAction.READ, "/instance/schema/table/");
        assertAction(actions.get(1), BehaviorAction.UPDATE, "/instance/schema/table/");
    }

    @Test
    public void subjectUsesStatementAuthorizationDomain() {
        StatementBehavior behavior = behavior(SecQueryType.CREATE_USER, relation(BehaviorAction.CREATE, //
                object(TargetType.User, "/instance/user/")));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(1, actions.size());
        assertEquals(SecDataAuthKind.ADMIN, actions.get(0).getAuthKind());
    }

    @Test
    public void nestedManagementBehaviorKeepsActionAuthorizationDomain() {
        StatementBehavior behavior = behavior(SecQueryType.SELECT, relation(BehaviorAction.ADMIN, //
                object(TargetType.Procedure, "/instance/system/routine/")));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(1, actions.size());
        assertEquals(SecDataAuthKind.ADMIN, actions.get(0).getAuthKind());
    }

    @Test
    public void sessionLifetimeObjectSkipsPersistentResourcePermission() {
        BehaviorObject temporaryTables = object(TargetType.Table, "/instance/schema/");
        StatementBehavior behavior = behavior(SecQueryType.DROP_TABLE, relation(BehaviorAction.DROP, temporaryTables));

        List<ResourceAction> actions = this.converter.convert(List.of(behavior), "/instance/schema/");

        assertEquals(1, actions.size());
        assertTrue(actions.get(0).isSkipPermission());
    }

    @Test
    public void sameActionInDifferentStatementsIsNotCollapsed() {
        BehaviorObject table = object(TargetType.Table, "/instance/schema/table/");
        StatementBehavior first = behavior(SecQueryType.SELECT, relation(BehaviorAction.READ, table));
        StatementBehavior second = behavior(SecQueryType.SELECT, relation(BehaviorAction.READ, table));

        List<ResourceAction> actions = this.converter.convert(List.of(first, second), "/instance/schema/");

        assertEquals(2, actions.size());
    }

    private static StatementBehavior behavior(SecQueryType statementType, BehaviorRelation... relations) {
        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(statementType);
        behavior.getRelations().addAll(List.of(relations));
        return behavior;
    }

    private static BehaviorRelation relation(BehaviorAction action, BehaviorObject subject, BehaviorObject... targets) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(subject);
        relation.getTarget().addAll(List.of(targets));
        return relation;
    }

    private static BehaviorObject object(TargetType type, String path) {
        BehaviorObject object = new BehaviorObject();
        object.setTargetType(type);
        object.setResourcePath(path);
        return object;
    }

    private static void assertAction(ResourceAction action, BehaviorAction expectedAction, String expectedPath) {
        assertEquals(expectedAction, action.getAction());
        assertEquals(expectedPath, action.toDsResPath().getResPath());
    }
}
