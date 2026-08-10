/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.postgres;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.postgres.analysis.sysobj.PgSysObjectRegistrySpi;

public class PostgresSysObjectRegistrySpiTest {

    private final PgSysObjectRegistrySpi resources = new PgSysObjectRegistrySpi();

    @Test
    public void permissionResourcesCoverEverySupportedMajorVersion() {
        for (int version = 12; version <= 18; version++) {
            String databaseVersion = Integer.toString(version);
            assertTrue(resources.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, "application", "public", "abs", databaseVersion));
            assertFalse(resources.isPermissionExempt(BehaviorAction.READ, TargetType.Table, "application", "pg_catalog", "pg_class", databaseVersion));
            assertFalse(resources.isPermissionExempt(BehaviorAction.READ, TargetType.View, "application", "public", "spatial_ref_sys", databaseVersion));
            assertFalse(resources.isPermissionExempt(BehaviorAction.READ, TargetType.Materialized, "application", "public", "spatial_ref_sys", databaseVersion));
            assertTrue(resources.isPermissionExempt(BehaviorAction.READ, TargetType.Type, "application", "public", "vector", databaseVersion));
        }
    }

    @Test
    public void actionTypeAndVersionMustMatchTheCatalog() {
        assertFalse(resources.isPermissionExempt(BehaviorAction.READ, TargetType.Function, null, null, "abs", "18"));
        assertFalse(resources.isPermissionExempt(BehaviorAction.CALL, TargetType.Procedure, null, null, "unknown_procedure", "18"));
        assertFalse(resources.isPermissionExempt(BehaviorAction.READ, TargetType.Table, null, "public", "pg_class", "18"));
        assertFalse(resources.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", "11"));
        assertFalse(resources.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "abs", "19"));
    }
}
