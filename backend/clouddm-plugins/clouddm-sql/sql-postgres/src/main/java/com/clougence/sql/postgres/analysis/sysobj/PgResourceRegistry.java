/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.sysobj;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;
import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;
import static com.clougence.sql.common.registry.RegisteredResourceType.TABLE;
import static com.clougence.sql.common.registry.RegisteredResourceType.TYPE;

import com.clougence.sql.common.registry.DatabaseResource;
import com.clougence.sql.common.registry.DatabaseResourceXmlLoader;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** PostgreSQL-owned core, bundled-extension and mainstream-extension object facts. */
public final class PgResourceRegistry {

    private static final String                      RESOURCE           = "/META-INF/clougence/postgres-database-resources.xml";
    private static final PgResourceRegistry          INSTANCE           = new PgResourceRegistry();

    private final VersionedResourceRegistry<Boolean> resources          = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Boolean> extensionFunctions = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);
    private final VersionedResourceRegistry<Boolean> extensionRelations = new VersionedResourceRegistry<>(PgResourceDialect.INSTANCE);

    private PgResourceRegistry(){
        register();
    }

    public static PgResourceRegistry instance() {
        return INSTANCE;
    }

    public boolean isSystemFunction(String name, PostgresVersion version) {
        return contains(FUNCTION, major(version), name);
    }

    public boolean isSystemFunction(String schema, String name, PostgresVersion version) {
        int exactVersion = major(version);
        return contains(FUNCTION, exactVersion, schema, name);
    }

    public boolean isSystemFunction(String catalog, String schema, String name, PostgresVersion version) {
        return contains(FUNCTION, major(version), catalog, schema, name);
    }

    public boolean isSystemProcedure(String schema, String name, PostgresVersion version) {
        return contains(PROCEDURE, major(version), schema, name);
    }

    public boolean isSystemProcedure(String name, PostgresVersion version) {
        return contains(PROCEDURE, major(version), name);
    }

    public boolean isSystemProcedure(String catalog, String schema, String name, PostgresVersion version) {
        return contains(PROCEDURE, major(version), catalog, schema, name);
    }

    public boolean isSystemRelation(String schema, String name, PostgresVersion version) {
        return contains(TABLE, major(version), schema, name);
    }

    public boolean isSystemRelation(String name, PostgresVersion version) {
        return contains(TABLE, major(version), name);
    }

    public boolean isSystemRelation(String catalog, String schema, String name, PostgresVersion version) {
        return contains(TABLE, major(version), catalog, schema, name);
    }

    public boolean isSystemType(String schema, String name, PostgresVersion version) {
        return contains(TYPE, major(version), schema, name);
    }

    public boolean isSystemType(String name, PostgresVersion version) {
        return contains(TYPE, major(version), name);
    }

    public boolean isSystemType(String catalog, String schema, String name, PostgresVersion version) {
        return contains(TYPE, major(version), catalog, schema, name);
    }

    public boolean skipsPermission(RegisteredResourceType type, PostgresVersion version, String... nameParts) {
        int exactVersion = major(version);
        java.util.Optional<Boolean> exact = resources.find(type, exactVersion, nameParts);
        if (exact.isPresent()) {
            return exact.get();
        }
        if (type == FUNCTION) {
            return extensionFunctions.find(FUNCTION, exactVersion, nameParts[nameParts.length - 1]).orElse(false);
        }
        if (type == TABLE) {
            return extensionRelations.find(TABLE, exactVersion, nameParts[nameParts.length - 1]).orElse(false);
        }
        return resources.find(type, exactVersion, nameParts[nameParts.length - 1]).orElse(false);
    }

    private boolean contains(RegisteredResourceType type, int exactVersion, String... nameParts) {
        if (resources.contains(type, exactVersion, nameParts) || resources.contains(type, exactVersion, nameParts[nameParts.length - 1])) {
            return true;
        }
        if (type == FUNCTION) {
            return extensionFunctions.contains(FUNCTION, exactVersion, nameParts[nameParts.length - 1]);
        }
        return type == TABLE && extensionRelations.contains(TABLE, exactVersion, nameParts[nameParts.length - 1]);
    }

    private void register() {
        for (DatabaseResource entry : DatabaseResourceXmlLoader.load(PgResourceRegistry.class, RESOURCE)) {
            String[] name = entry.registrationNameParts();
            for (String configuredVersion : entry.versions()) {
                PostgresVersion version = parseVersion(configuredVersion);
                int major = version.major();
                resources.register(entry.type(), major, major, entry.skipPermission(), name);
                if (entry.environment() != null && entry.isNameMatched()) {
                    if (entry.type() == FUNCTION) {
                        extensionFunctions.register(FUNCTION, major, major, entry.skipPermission(), entry.name());
                    } else if (entry.type() == TABLE) {
                        extensionRelations.register(TABLE, major, major, entry.skipPermission(), entry.name());
                    }
                }
            }
        }
    }

    private static PostgresVersion parseVersion(String configuredVersion) {
        PostgresVersion version = PostgresVersion.parse(configuredVersion);
        if (!Integer.toString(version.major()).equals(configuredVersion)) {
            throw new IllegalStateException("Unsupported PostgreSQL database resource version: " + configuredVersion);
        }
        return version;
    }

    private static int major(PostgresVersion version) {
        return (version == null ? PostgresVersion.LATEST : version).major();
    }
}
