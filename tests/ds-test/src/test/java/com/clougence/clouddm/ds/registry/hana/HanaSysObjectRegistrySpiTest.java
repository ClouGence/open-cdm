/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.hana;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.hana.sql.HanaSqlEngineSpi;
import com.clougence.clouddm.ds.hana.sql.analysis.sysobj.HanaSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

import static org.junit.jupiter.api.Assertions.*;

public class HanaSysObjectRegistrySpiTest {

    private static final HanaSysObjectRegistrySpi REGISTRY = new HanaSysObjectRegistrySpi();

    @Test
    public void exposesTheHanaSqlEngineName() {
        assertEquals(HanaSqlEngineSpi.NAME, REGISTRY.name());
    }

    @Test
    public void exemptsRegisteredFunctionsForTheirConfiguredVersions() {
        assertTrue(REGISTRY.isPermissionExempt(BehaviorAction.CALL, TargetType.Function,
                null, null, "ABS", "1"));
        assertTrue(REGISTRY.isPermissionExempt(BehaviorAction.CALL, TargetType.Function,
                "SYS", "SQLSCRIPT_STRING", "FORMAT", "2.0"));
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.CALL, TargetType.Function,
                "SYS", "SQLSCRIPT_STRING", "FORMAT", "1.0"));
    }

    @Test
    public void requiresPermissionForSystemTablesAndViews() {
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.READ, TargetType.Table,
                null, "SYS", "USERS", "2"));
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.READ, TargetType.View,
                null, "TENANT#DI", "M_OBJECTS", "2"));
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.READ, TargetType.Materialized,
                null, "SYS", "USERS", "2"));
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.UPDATE, TargetType.View,
                null, "SYS", "USERS", "2"));
    }

    @Test
    public void doesNotExemptResourcesThatRequirePermission() {
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.CALL, TargetType.Procedure,
                null, null, "CHECK_CATALOG", "2"));
        assertFalse(REGISTRY.isPermissionExempt(BehaviorAction.CALL, TargetType.Function,
                "OTHER", "SQLSCRIPT_STRING", "FORMAT", "2"));
    }
}
