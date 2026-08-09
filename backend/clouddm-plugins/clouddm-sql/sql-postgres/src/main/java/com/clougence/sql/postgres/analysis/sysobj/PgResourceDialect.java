/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.sysobj;

import java.util.Locale;

import com.clougence.sql.common.registry.ResourceRegistryDialect;

enum PgResourceDialect implements ResourceRegistryDialect {
    INSTANCE;

    @Override
    public String normalizeIdentifier(String identifier) {
        String value = identifier == null ? "" : identifier.strip();
        if (isQuotedIdentifier(value)) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean isQuotedIdentifier(String identifier) {
        String value = identifier == null ? "" : identifier.strip();
        return value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"';
    }
}
