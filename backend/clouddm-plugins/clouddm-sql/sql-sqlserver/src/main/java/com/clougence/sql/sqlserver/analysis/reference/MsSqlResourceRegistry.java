/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.PROCEDURE;
import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

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
        resources.register(PROCEDURE, 2008, 2022, true, "sys", "sp_getapplock");
        resources.register(PROCEDURE, 2008, 2022, true, "sys", "sp_releaseapplock");
        resources.register(PROCEDURE, 2016, 2022, true, "sys", "sp_set_session_context");
        for (String function : List.of("CONTEXT_INFO", "XACT_STATE", "ORIGINAL_LOGIN", "SUSER_SNAME", "USER_NAME",
                                       "DB_NAME", "DB_ID", "OBJECT_ID", "SESSIONPROPERTY")) {
            resources.register(FUNCTION, 2008, 2022, true, function);
        }
        resources.register(FUNCTION, 2016, 2022, true, "SESSION_CONTEXT");
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

    public boolean isSessionProcedure(String schema, String name, int version) {
        return resources.contains(PROCEDURE, version, schema, name);
    }

    public boolean isBuiltinFunction(String name, int version) {
        return resources.contains(FUNCTION, version, name);
    }

    private enum MsSqlResourceDialect implements ResourceRegistryDialect {
        INSTANCE;

        @Override
        public String normalizeIdentifier(String identifier) {
            String normalized = identifier == null ? "" : identifier.trim();
            if (isQuotedIdentifier(normalized)) {
                char quote = normalized.charAt(0);
                normalized = normalized.substring(1, normalized.length() - 1);
                if (quote == '[') {
                    normalized = normalized.replace("]]", "]");
                } else {
                    normalized = normalized.replace("\"\"", "\"");
                }
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
