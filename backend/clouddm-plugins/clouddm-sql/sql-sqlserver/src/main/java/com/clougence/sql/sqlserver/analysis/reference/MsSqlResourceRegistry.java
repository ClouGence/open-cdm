/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;

import java.util.List;
import java.util.Locale;

import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedResourceRegistry;

/**
 * SQL Server-owned system resources that do not represent user-defined authorization objects.
 */
public final class MsSqlResourceRegistry {

    private static final int                         VERSION   = 0;
    private static final MsSqlResourceRegistry       INSTANCE  = new MsSqlResourceRegistry();
    private final VersionedResourceRegistry<Boolean> resources = new VersionedResourceRegistry<>(MsSqlResourceDialect.INSTANCE);

    private MsSqlResourceRegistry(){
        resources.register(PROCEDURE, VERSION, VERSION, true, "sys", "sp_cdc_enable_db");
        resources.register(PROCEDURE, VERSION, VERSION, true, "sys", "sp_cdc_disable_db");
    }

    public static MsSqlResourceRegistry instance() {
        return INSTANCE;
    }

    public boolean isSystemProcedure(List<String> nameParts) {
        if (nameParts == null || nameParts.isEmpty()) {
            return false;
        }
        return resources.contains(PROCEDURE, VERSION, nameParts.toArray(String[]::new));
    }

    private enum MsSqlResourceDialect implements ResourceRegistryDialect {
        INSTANCE;

        @Override
        public String normalizeIdentifier(String identifier) {
            String normalized = identifier == null ? "" : identifier.trim();
            if (isQuotedIdentifier(normalized)) {
                normalized = normalized.substring(1, normalized.length() - 1);
            }
            return normalized.toLowerCase(Locale.ROOT);
        }

        @Override
        public boolean isQuotedIdentifier(String identifier) {
            if (identifier == null || identifier.length() < 2) {
                return false;
            }
            char first = identifier.charAt(0);
            char last = identifier.charAt(identifier.length() - 1);
            return first == '[' && last == ']' || first == '"' && last == '"';
        }
    }
}
