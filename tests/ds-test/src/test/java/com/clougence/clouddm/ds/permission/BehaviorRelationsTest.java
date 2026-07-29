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
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;

public final class BehaviorRelationsTest {

    @Test
    public void permissionsComeFromBehaviorSemantics() {
        Assert.assertEquals(SecDataAuthKind.READ, flatten(BehaviorAction.READ, TargetType.Log).authKind());
        Assert.assertEquals(SecDataAuthKind.ADMIN, flatten(BehaviorAction.ADMIN, TargetType.Instance).authKind());
        Assert.assertNull(flatten(BehaviorAction.LOCK, TargetType.Table).authKind());
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
