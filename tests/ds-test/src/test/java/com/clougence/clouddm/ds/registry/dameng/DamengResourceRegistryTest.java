/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.dameng;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.ds.dameng.sql.DmSqlPlugin;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmResourceRegistry;
import com.clougence.clouddm.ds.dameng.sql.analysis.sysobj.DmSysObjectRegistrySpi;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.sql.common.registry.DatabaseResourceXmlLoader;
import com.clougence.sql.common.registry.RegisteredResourceType;

public final class DamengResourceRegistryTest {

    private static final String XML_RESOURCES = "/META-INF/clougence/dameng-database-resources.xml";
    private final DmResourceRegistry resources = DmResourceRegistry.instance();

    @Test
    public void xmlCatalogIsAvailableAndLoadedDuringRegistryInitialization() {
        assertFalse(DatabaseResourceXmlLoader.load(DmResourceRegistry.class, XML_RESOURCES).isEmpty());
        assertNotNull(resources);
        assertTrue(resources.isSystemProcedure("SP_ADD_JOB_SCHEDULE", DmResourceRegistry.DM8));
    }

    @Test
    public void pluginRegistersTheSystemObjectSpiThatOwnsTheRegistry() {
        AtomicInteger registeredSpis = new AtomicInteger();
        AtomicReference<Object> lastSpi = new AtomicReference<>();
        DsPluginBinder binder = (DsPluginBinder) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { DsPluginBinder.class }, (proxy, method, arguments) -> {
                    if ("addGlobalSpi".equals(method.getName())) {
                        registeredSpis.incrementAndGet();
                        lastSpi.set(arguments[0]);
                    }
                    return null;
                });

        new DmSqlPlugin().loadPlugin(binder);

        assertEquals(2, registeredSpis.get());
        assertTrue(lastSpi.get() instanceof DmSysObjectRegistrySpi);
        assertTrue(DmResourceRegistry.instance().isBuiltInFunction("SUM", DmResourceRegistry.DM8));
    }

    @Test
    public void supportedVersionIsEnforced() {
        assertTrue(resources.isBuiltInFunction("SUM", DmResourceRegistry.DM8));
        assertFalse(resources.isBuiltInFunction("SUM", 7));
        assertFalse(resources.isBuiltInFunction("SUM", 9));
    }

    @Test
    public void nameScopeMatchesQualifiedFunctions() {
        assertTrue(resources.isSystemFunction("SUM", DmResourceRegistry.DM8));
        assertTrue(resources.isSystemFunction("APPLICATION_SCHEMA", "SUM", DmResourceRegistry.DM8));
        assertTrue(resources.isSystemFunction("APPLICATION_CATALOG", "APPLICATION_SCHEMA", "SUM", DmResourceRegistry.DM8));
    }

    @Test
    public void aggregateAndPermissionPropertiesAreRegistered() {
        assertTrue(resources.isBuiltInAggregateFunction("SUM", DmResourceRegistry.DM8));
        assertFalse(resources.isBuiltInAggregateFunction("ABS", DmResourceRegistry.DM8));
        assertTrue(resources.skipsPermission(RegisteredResourceType.FUNCTION, DmResourceRegistry.DM8,
                "APPLICATION_SCHEMA", "SUM"));
    }

    @Test
    public void exactScopeRequiresRegisteredNameParts() {
        assertTrue(resources.isSystemFunction("DBMS_BINARY", "BINARY_GET_BIGINT", DmResourceRegistry.DM8));
        assertFalse(resources.isSystemFunction("BINARY_GET_BIGINT", DmResourceRegistry.DM8));
        assertFalse(resources.isSystemFunction("APPLICATION_SCHEMA", "BINARY_GET_BIGINT", DmResourceRegistry.DM8));
    }

    @Test
    public void systemTypesUseExactMatching() {
        assertTrue(resources.isSystemType("DBMS_AQ", "DEQUEUE_OPTIONS_T", DmResourceRegistry.DM8));
        assertFalse(resources.isSystemType("DEQUEUE_OPTIONS_T", DmResourceRegistry.DM8));
        assertFalse(resources.isSystemType("APPLICATION_SCHEMA", "DEQUEUE_OPTIONS_T", DmResourceRegistry.DM8));
    }

    @Test
    public void expandedAliasesFollowCatalogAndSchemaScope() {
        assertTrue(resources.isSystemView("SYS", "ALL_OBJECTS", DmResourceRegistry.DM8));
        assertTrue(resources.isSystemView("ALL_OBJECTS", DmResourceRegistry.DM8));
        assertTrue(resources.isSystemView("APPLICATION_SCHEMA", "ALL_OBJECTS", DmResourceRegistry.DM8));
    }
}
