/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.hana;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.hana.sql.analysis.sysobj.HanaResourceRegistry;
import com.clougence.clouddm.ds.hana.sql.parser.HanaVersion;
import com.clougence.sql.common.registry.RegisteredResourceType;

import static org.junit.jupiter.api.Assertions.*;

public class HanaResourceRegistryTest {

    private static final HanaResourceRegistry REGISTRY = HanaResourceRegistry.instance();

    @Test
    public void resourcesRespectParserMajorVersion() {
        assertTrue(REGISTRY.isSystemFunction("_SYS_AFL.AFLPAL:KMEANS", HanaVersion.HANA_1));
        assertFalse(REGISTRY.isSystemFunction("_SYS_AFL.AFLPAL:KMEANS", HanaVersion.HANA_2));

        assertFalse(REGISTRY.isSystemProcedure(List.of("CHECK_CATALOG"), HanaVersion.HANA_1));
        assertTrue(REGISTRY.isSystemProcedure(List.of("CHECK_CATALOG"), HanaVersion.HANA_2));
    }

    @Test
    public void catalogAndSchemaAttributesDefineTheRegisteredPath() {
        assertTrue(REGISTRY.isSystemView("SYS", "USERS", HanaVersion.HANA_2));
        assertTrue(REGISTRY.isSystemView("APP", "USERS", HanaVersion.HANA_2));

        assertTrue(REGISTRY.isSystemFunction("SYS.SQLSCRIPT_STRING:FORMAT", HanaVersion.HANA_2));
        assertFalse(REGISTRY.isSystemFunction("APP.SQLSCRIPT_STRING:FORMAT", HanaVersion.HANA_2));
        assertTrue(REGISTRY.isSystemFunction("_SYS_AFL.AFLPAL:KMEANS", HanaVersion.HANA_1));
        assertFalse(REGISTRY.isSystemFunction("OTHER.AFLPAL:KMEANS", HanaVersion.HANA_1));
    }

    @Test
    public void nameScopeMatchesAnySchema() {
        assertTrue(REGISTRY.isSystemFunction("ABS", HanaVersion.HANA_2));
        assertTrue(REGISTRY.isSystemFunction("APP.ABS", HanaVersion.HANA_2));
        assertTrue(REGISTRY.isAggregateFunction("APP.SUM", HanaVersion.HANA_2));
    }

    @Test
    public void schemaWildcardOnlyMatchesHdiContainerSchemas() {
        assertTrue(REGISTRY.isSystemView("TENANT#DI", "M_OBJECTS", HanaVersion.HANA_2));
        assertFalse(REGISTRY.isSystemView("TENANT", "M_OBJECTS", HanaVersion.HANA_2));
    }

    @Test
    public void wildcardCatalogAndSchemaMatchQualifiedAndUnqualifiedResources() {
        assertTrue(REGISTRY.isSystemView("", "USERS", HanaVersion.HANA_2));
        assertTrue(REGISTRY.isSystemView("APP", "USERS", HanaVersion.HANA_2));
    }

    @Test
    public void permissionExemptionComesFromTheMatchedResource() {
        assertTrue(REGISTRY.shouldSkipPermissionCheck(
                RegisteredResourceType.FUNCTION, List.of("APP", "ABS"), HanaVersion.HANA_2));
        assertFalse(REGISTRY.shouldSkipPermissionCheck(
                RegisteredResourceType.TABLE, List.of("SYS", "USERS"), HanaVersion.HANA_2));
        assertFalse(REGISTRY.shouldSkipPermissionCheck(
                RegisteredResourceType.PROCEDURE,
                List.of("SYS", "AFLLANG_WRAPPER_PROCEDURE_CREATE"),
                HanaVersion.HANA_2));
        assertFalse(REGISTRY.shouldSkipPermissionCheck(
                RegisteredResourceType.PROCEDURE, List.of("CHECK_CATALOG"), HanaVersion.HANA_2));
    }
}
