/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.registry.doris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.doris.DrSqlPlugin;
import com.clougence.sql.doris.analysis.sysobj.DrResourceRegistry;
import com.clougence.sql.doris.analysis.sysobj.DrSysObjectRegistrySpi;
import com.clougence.sql.doris.parser.DorisVersion;

public class DorisResourceRegistryTest {

    @Test
    public void databaseResourceXmlRespectsDorisVersionScopes() {
        DrResourceRegistry resources = DrResourceRegistry.instance();

        assertTrue(resources.isSystemFunction("any", DorisVersion.DORIS_2));
        assertTrue(resources.isAggregateFunction("`ANY`", DorisVersion.DORIS_4));
        assertFalse(resources.isSystemFunction("AI_AGG", DorisVersion.DORIS_2));
        assertTrue(resources.isSystemFunction("AI_AGG", DorisVersion.DORIS_4));
        assertTrue(resources.isAggregateFunction("AI_AGG", DorisVersion.DORIS_4));
        assertFalse(resources.isSystemRelation(//
                "information_schema", "authentication_integrations", DorisVersion.DORIS_3));
        assertTrue(resources.isSystemRelation(//
                "information_schema", "authentication_integrations", DorisVersion.DORIS_4));
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

        new DrSqlPlugin().loadPlugin(binder);

        assertEquals(2, registeredSpis.get());
        assertTrue(lastSpi.get() instanceof DrSysObjectRegistrySpi);
    }

    @Test
    public void permissionSpiRespectsObjectScopeActionAndVersion() {
        DrSysObjectRegistrySpi registry = new DrSysObjectRegistrySpi();

        assertTrue(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function,
                "APPLICATION_CATALOG", "APPLICATION_SCHEMA", "BOOL_AND", "4"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Function,
                "APPLICATION_CATALOG", "APPLICATION_SCHEMA", "BOOL_AND", "3"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.CALL, TargetType.Procedure,
                null, "APPLICATION_SCHEMA", "EXECUTE_STMT", "2.1"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.READ, TargetType.Table,
                "INTERNAL", "information_schema", "TABLES", "3"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.READ, TargetType.Materialized,
                "INTERNAL", "information_schema", "TABLES", "3"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.READ, TargetType.Table,
                "INTERNAL", "APPLICATION_SCHEMA", "TABLES", "3"));
        assertFalse(registry.isPermissionExempt(BehaviorAction.ALTER, TargetType.Table,
                "INTERNAL", "information_schema", "TABLES", "3"));
    }

    @Test
    public void permissionPropertyComesFromTheXmlCatalog() {
        DrResourceRegistry resources = DrResourceRegistry.instance();

        assertTrue(resources.skipsPermission(RegisteredResourceType.FUNCTION, DorisVersion.DORIS_4,
                "APPLICATION_SCHEMA", "BOOL_AND"));
        assertFalse(resources.skipsPermission(RegisteredResourceType.TABLE, DorisVersion.DORIS_3,
                "INTERNAL", "information_schema", "TABLES"));
    }
}
