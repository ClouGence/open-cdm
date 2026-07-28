/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;
import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;

import java.util.HashSet;
import java.util.Set;

import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

/**
 * Dameng-owned registered resource facts shared by SQL analysis implementations.
 */
public final class DmResourceRegistry {

    public static final int                          DM8                         = 8;
    private static final String                      BUILT_IN_FUNCTIONS_RESOURCE = "/META-INF/clougence/dameng-built-in-functions.json";
    private static final String                      SYSTEM_RESOURCES            = "/META-INF/clougence/dameng-skip-permission-resources.json";
    private static final DmResourceRegistry          INSTANCE                    = new DmResourceRegistry(DmResourceDialect.INSTANCE);

    private final ResourceRegistryDialect            dialect;
    private final VersionedResourceRegistry<Boolean> resources;

    public static DmResourceRegistry instance() {
        return INSTANCE;
    }

    DmResourceRegistry(ResourceRegistryDialect dialect){
        this.dialect = dialect;
        this.resources = new VersionedResourceRegistry<>(dialect);
        registerBuiltInFunctions();
        registerSystemResources();
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified) {
        return isUserDefinedFunction(functionName, qualified, DM8);
    }

    public boolean isUserDefinedFunction(String functionName, boolean qualified, int exactVersion) {
        return qualified || !resources.contains(FUNCTION, exactVersion, functionName);
    }

    public boolean isBuiltInFunction(String functionName) {
        return isBuiltInFunction(functionName, DM8);
    }

    public boolean isBuiltInFunction(String functionName, int exactVersion) {
        return resources.contains(FUNCTION, exactVersion, functionName);
    }

    public boolean isSystemProcedure(String procedureName) {
        return isSystemProcedure(procedureName, DM8);
    }

    public boolean isSystemProcedure(String procedureName, int exactVersion) {
        return resources.contains(PROCEDURE, exactVersion, procedureName);
    }

    private void registerBuiltInFunctions() {
        Set<String> names = new HashSet<>();
        for (DmRegistryResourceLoader.Entry entry : DmRegistryResourceLoader.load(DmResourceRegistry.class, BUILT_IN_FUNCTIONS_RESOURCE)) {
            if (entry.type() != FUNCTION || entry.nameParts().size() != 1) {
                throw new IllegalStateException("Dameng function resource requires one FUNCTION name part: " + entry);
            }
            String name = entry.nameParts().get(0);
            if (!names.add(dialect.normalizeIdentifier(name))) {
                throw new IllegalStateException("Duplicate Dameng function resource: " + name);
            }
            entry.versions().forEach(version -> resources.register(FUNCTION, version, version, true, name));
        }
    }

    private void registerSystemResources() {
        Set<String> names = new HashSet<>();
        for (DmRegistryResourceLoader.Entry entry : DmRegistryResourceLoader.load(DmResourceRegistry.class, SYSTEM_RESOURCES)) {
            if (entry.type() != PROCEDURE || entry.nameParts().size() != 1) {
                throw new IllegalStateException("Dameng system resource requires one PROCEDURE name part: " + entry);
            }
            String name = entry.nameParts().get(0);
            if (!names.add(dialect.normalizeIdentifier(name))) {
                throw new IllegalStateException("Duplicate Dameng system resource: " + name);
            }
            entry.versions().forEach(version -> resources.register(PROCEDURE, version, version, true, name));
        }
    }
}
