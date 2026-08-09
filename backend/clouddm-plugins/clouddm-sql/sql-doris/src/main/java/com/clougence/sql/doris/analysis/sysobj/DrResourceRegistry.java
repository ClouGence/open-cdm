/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.doris.analysis.sysobj;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.clougence.sql.common.registry.DatabaseResource;
import com.clougence.sql.common.registry.DatabaseResourceXmlLoader;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.doris.parser.DorisVersion;

/** Doris-owned versioned database-resource facts. */
public final class DrResourceRegistry {

    private static final String                      RESOURCE             = "/META-INF/clougence/doris-database-resources.xml";
    private static final DrResourceRegistry          INSTANCE             = new DrResourceRegistry();

    private final VersionedResourceRegistry<Boolean> resources            = new VersionedResourceRegistry<>(DrResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Boolean> nameMatchedResources = new VersionedResourceRegistry<>(DrResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Boolean> aggregateFunctions   = new VersionedResourceRegistry<>(DrResourceDialect.INSTANCE);

    private DrResourceRegistry(){
        load();
    }

    public static DrResourceRegistry instance() {
        return INSTANCE;
    }

    public boolean isSystemFunction(String name, DorisVersion version) {
        return contains(FUNCTION, version, name);
    }

    public boolean isSystemFunction(String schema, String name, DorisVersion version) {
        return contains(FUNCTION, version, schema, name);
    }

    public boolean isSystemFunction(String catalog, String schema, String name, DorisVersion version) {
        return contains(FUNCTION, version, catalog, schema, name);
    }

    public boolean isAggregateFunction(String name, DorisVersion version) {
        return aggregateFunctions.contains(FUNCTION, major(version), name);
    }

    public boolean isSystemProcedure(String name, DorisVersion version) {
        return contains(RegisteredResourceType.PROCEDURE, version, name);
    }

    public boolean isSystemProcedure(String schema, String name, DorisVersion version) {
        return contains(RegisteredResourceType.PROCEDURE, version, schema, name);
    }

    public boolean isSystemProcedure(String catalog, String schema, String name, DorisVersion version) {
        return contains(RegisteredResourceType.PROCEDURE, version, catalog, schema, name);
    }

    public boolean isSystemRelation(String schema, String name, DorisVersion version) {
        return contains(RegisteredResourceType.TABLE, version, schema, name);
    }

    public boolean isSystemRelation(String catalog, String schema, String name, DorisVersion version) {
        return contains(RegisteredResourceType.TABLE, version, catalog, schema, name);
    }

    public boolean skipsPermission(RegisteredResourceType type, DorisVersion version, String... nameParts) {
        int major = major(version);
        Optional<Boolean> exact = resources.find(type, major, nameParts);
        if (exact.isPresent()) {
            return exact.get();
        }
        if (nameParts.length == 3) {
            Optional<Boolean> wildcardCatalog = resources.find(type, major, nameParts[1], nameParts[2]);
            if (wildcardCatalog.isPresent()) {
                return wildcardCatalog.get();
            }
        }
        return nameMatchedResources.find(type, major, nameParts[nameParts.length - 1]).orElse(false);
    }

    private void load() {
        Set<String> names = new HashSet<>();
        for (DatabaseResource resource : DatabaseResourceXmlLoader.load(DrResourceRegistry.class, RESOURCE)) {
            String[] nameParts = resource.registrationNameParts();
            String key = resource.type() + ":"
                         + Arrays.stream(nameParts).map(DrResourceDialect.INSTANCE::normalizeIdentifier).reduce((left, right) -> left + "." + right).orElseThrow();
            if (!names.add(key)) {
                throw invalid("duplicate resource " + String.join(".", nameParts));
            }
            VersionedResourceRegistry<Boolean> target = resource.isNameMatched() ? nameMatchedResources : resources;
            for (String versionName : resource.versions()) {
                int version = parserVersion(versionName);
                target.register(resource.type(), version, version, resource.skipPermission(), nameParts);
                if (resource.aggregate()) {
                    if (resource.type() != FUNCTION) {
                        throw invalid("aggregate flag is only valid for functions: " + resource.name());
                    }
                    aggregateFunctions.register(FUNCTION, version, version, true, resource.name());
                }
            }
        }
    }

    private boolean contains(RegisteredResourceType type, DorisVersion version, String... nameParts) {
        int major = major(version);
        if (resources.contains(type, major, nameParts)) {
            return true;
        }
        if (nameParts.length == 3 && resources.contains(type, major, nameParts[1], nameParts[2])) {
            return true;
        }
        return nameMatchedResources.contains(type, major, nameParts[nameParts.length - 1]);
    }

    private static int parserVersion(String versionName) {
        return switch (versionName) {
            case "2", "v2", "V2" -> DorisVersion.DORIS_2.major();
            case "3", "v3", "V3" -> DorisVersion.DORIS_3.major();
            case "4", "v4", "V4" -> DorisVersion.DORIS_4.major();
            default -> throw invalid("unsupported parser version " + versionName);
        };
    }

    private static int major(DorisVersion version) {
        return (version == null ? DorisVersion.LATEST : version).major();
    }

    private static IllegalStateException invalid(String message) {
        return new IllegalStateException("Invalid Doris database resource " + RESOURCE + ": " + message);
    }
}
