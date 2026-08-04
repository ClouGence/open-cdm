/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.permission;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.console.web.component.analysis.BehaviorRelations;
import com.clougence.clouddm.console.web.component.analysis.BehaviorRequest;
import com.clougence.clouddm.sdk.security.auth.SecDataAuthKind;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;

public final class BehaviorRelationsTest {

    @Test
    public void permissionsComeFromBehaviorSemantics() {
        Assert.assertEquals(SecDataAuthKind.READ, flatten(BehaviorAction.READ, TargetType.Log).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.CHECKPOINT, TargetType.Instance).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.ANALYZE, TargetType.Table).authKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, flatten(BehaviorAction.UNSAFE, TargetType.Instance).authKind());
        Assert.assertNull(flatten(BehaviorAction.LOCK, TargetType.Table).authKind());
    }

    @Test
    public void managementObjectLifecycleUsesManagementPermission() {
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.CREATE, TargetType.Link).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.ALTER, TargetType.Job).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.DROP, TargetType.Publication).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.CREATE, TargetType.RowAccessPolicy).authKind());
    }

    @Test
    public void operationalBehaviorUsesMaintenancePermission() {
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.IMPORT, TargetType.Table).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.EXPORT, TargetType.File).authKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, flatten(BehaviorAction.CREATE, TargetType.ResourceGroup).authKind());
    }

    @Test
    public void grantsAndConfigurationUseManagementPermission() {
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.GRANT, TargetType.UserOrRole).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.REVOKE, TargetType.Table).authKind());
        Assert.assertEquals(SecDataAuthKind.MANAGE, flatten(BehaviorAction.CONFIGURE, TargetType.ConfigKey).authKind());
    }

    @Test
    public void programmableObjectDefinitionsRequireDdlPermission() {
        Assert.assertEquals(SecDataAuthKind.DDL, flatten(BehaviorAction.CREATE, TargetType.Procedure).authKind());
        Assert.assertEquals(SecDataAuthKind.DDL, flatten(BehaviorAction.ALTER, TargetType.Function).authKind());
        Assert.assertEquals(SecDataAuthKind.DDL, flatten(BehaviorAction.DROP, TargetType.Trigger).authKind());
    }

    @Test
    public void programmableObjectExecutionRequiresProgramPermission() {
        Assert.assertEquals(SecDataAuthKind.PROGRAM, flatten(BehaviorAction.CALL, TargetType.Procedure).authKind());
        Assert.assertEquals(SecDataAuthKind.PROGRAM, flatten(BehaviorAction.CALL, TargetType.Function).authKind());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void legacyOtherStatementKindsUseExplicitPermissions() {
        Assert.assertEquals(SecDataAuthKind.PROGRAM, RuleQueryType.BLOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.PROGRAM, RuleQueryType.PROGRAM_CONTROL.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.READ, RuleQueryType.QUERY_LOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.SESSION_LOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.ANALYZE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.OPTIMIZE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.CHECK_TABLE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.TRANSACTION.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.PREPARE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.EXECUTE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.DEALLOCATE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.PROGRAM, RuleQueryType.SQL_BLOCK.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.EXPORT.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.MAINTAIN, RuleQueryType.SYNC.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.UNSAFE.getAuthKind());
        Assert.assertEquals(SecDataAuthKind.UNSAFE, RuleQueryType.UNKNOWN.getAuthKind());
    }

    @Test
    public void namedRegistryCanExemptBehaviorResource() {
        BehaviorRelation relation = relation(BehaviorAction.READ, TargetType.Function);
        relation.getSubject().setObjectName(new ObjectName(null, null, "BUILT_IN"));
        SysObjectRegistrySpi registry = (action, targetType, catalog, schema, objectName, dbVersion) -> {
            return action == BehaviorAction.READ && targetType == TargetType.Function && "BUILT_IN".equals(objectName);
        };

        List<BehaviorRequest> requests = BehaviorRelations.flattenResource(registry, "8", List.of(relation));

        Assert.assertEquals(1, requests.size());
        Assert.assertNull(requests.get(0).authKind());
    }

    private static BehaviorRequest flatten(BehaviorAction action, TargetType targetType) {
        List<BehaviorRequest> requests = BehaviorRelations.flattenResource(null, null, List.of(relation(action, targetType)));
        Assert.assertEquals(1, requests.size());
        return requests.get(0);
    }

    private static BehaviorRelation relation(BehaviorAction action, TargetType targetType) {
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(targetType);
        object.setObjectPath("/test/1/");

        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(object);
        return relation;
    }
}
