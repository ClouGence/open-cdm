/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.tidb;

import com.clougence.clouddm.ds.tidb.sql.analysis.sysobj.TiDBResourceRegistry;
import com.clougence.clouddm.ds.tidb.sql.analysis.sysobj.TiDBSysObjectRegistrySpi;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TiDBResourceRegistryTest {

    private static final TiDBResourceRegistry REGISTRY = TiDBResourceRegistry.instance();

    @Test
    void loadsOnlyUnifiedResource() {
        assertNotNull(TiDBResourceRegistry.class.getResource("/META-INF/clougence/tidb-database-resources.xml"));
        assertNull(TiDBResourceRegistry.class.getResource("/META-INF/clougence/tidb-database-resources.json"));
        assertNull(TiDBResourceRegistry.class.getResource("/META-INF/clougence/tidb-built-in-functions.json"));
        assertNull(TiDBResourceRegistry.class.getResource("/META-INF/clougence/tidb-system-resources.json"));
    }

    @Test
    void recognizesCoreFunctionsAcrossParserMajors() {
        for (TiDBVersion version : TiDBVersion.values()) {
            assertFalse(REGISTRY.isUserDefinedFunction("ABS", false, version), version.name());
        }
    }

    @Test
    void classifiesAggregatesByVersion() {
        for (TiDBVersion version : TiDBVersion.values()) {
            assertTrue(REGISTRY.isBuiltInAggregateFunction("SUM", version.major()), version.name());
        }
        assertFalse(REGISTRY.isBuiltInAggregateFunction("MAX_COUNT", TiDBVersion.TIDB_8.major()));
        assertTrue(REGISTRY.isBuiltInAggregateFunction("MAX_COUNT", TiDBVersion.TIDB_9.major()));
    }

    @Test
    void appliesFunctionIntroductionVersions() {
        assertFunctionIntroduced("JSON_STORAGE_FREE", TiDBVersion.TIDB_5, TiDBVersion.TIDB_6);
        assertFunctionIntroduced("CURRENT_RESOURCE_GROUP", TiDBVersion.TIDB_6, TiDBVersion.TIDB_7);
        assertFunctionIntroduced("VEC_AS_TEXT", TiDBVersion.TIDB_7, TiDBVersion.TIDB_8);
        assertFunctionIntroduced("EMBED_TEXT", TiDBVersion.TIDB_8, TiDBVersion.TIDB_9);
    }

    @Test
    void appliesSystemViewIntroductionVersions() {
        assertViewIntroduced("CLUSTER_MEMORY_USAGE", TiDBVersion.TIDB_5, TiDBVersion.TIDB_6);
        assertViewIntroduced("CHECK_CONSTRAINTS", TiDBVersion.TIDB_6, TiDBVersion.TIDB_7);
        assertViewIntroduced("CLUSTER_TIDB_INDEX_USAGE", TiDBVersion.TIDB_7, TiDBVersion.TIDB_8);
        assertViewIntroduced("CLUSTER_TIDB_PLAN_CACHE", TiDBVersion.TIDB_8, TiDBVersion.TIDB_9);
    }

    @Test
    void sysObjectSpiUsesVersionedXmlPermissionFacts() {
        TiDBSysObjectRegistrySpi spi = new TiDBSysObjectRegistrySpi();
        for (TiDBVersion version : TiDBVersion.values()) {
            assertTrue(spi.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, "catalog", "application", "ABS", Integer.toString(version.major())));
            assertFalse(spi.isPermissionExempt(BehaviorAction.READ, TargetType.View, "catalog", "INFORMATION_SCHEMA", "TABLES", Integer.toString(version.major())));
        }
        assertFalse(spi.isPermissionExempt(BehaviorAction.READ, TargetType.Function, null, null, "ABS", "9"));
        assertFalse(spi.isPermissionExempt(BehaviorAction.READ, TargetType.View, null, "application", "TABLES", "9"));
        assertFalse(spi.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ABS", "10"));
        assertTrue(spi.isPermissionExempt(BehaviorAction.CALL, TargetType.Function, null, null, "ABS", "5.7.25-TiDB-v8.5.7"));
    }

    private static void assertFunctionIntroduced(String name, TiDBVersion before, TiDBVersion introduced) {
        assertTrue(REGISTRY.isUserDefinedFunction(name, false, before));
        assertFalse(REGISTRY.isUserDefinedFunction(name, false, introduced));
    }

    private static void assertViewIntroduced(String name, TiDBVersion before, TiDBVersion introduced) {
        assertFalse(REGISTRY.isMetadataTable("INFORMATION_SCHEMA", name, before));
        assertTrue(REGISTRY.isMetadataTable("INFORMATION_SCHEMA", name, introduced));
    }
}
