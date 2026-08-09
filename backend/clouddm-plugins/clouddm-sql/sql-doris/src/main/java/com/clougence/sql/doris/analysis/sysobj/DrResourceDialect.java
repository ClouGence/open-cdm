/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.doris.analysis.sysobj;

import java.util.Locale;

import com.clougence.sql.common.registry.ResourceRegistryDialect;

/** Identifier rules used by the Doris system-object registry. */
final class DrResourceDialect implements ResourceRegistryDialect {

    static final DrResourceDialect INSTANCE = new DrResourceDialect();

    private DrResourceDialect(){
    }

    @Override
    public String normalizeIdentifier(String identifier) {
        String value = identifier.strip();
        if (isQuotedIdentifier(value)) {
            value = value.substring(1, value.length() - 1);
        }
        return value.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean isQuotedIdentifier(String identifier) {
        if (identifier == null || identifier.length() < 2) {
            return false;
        }
        char first = identifier.charAt(0);
        char last = identifier.charAt(identifier.length() - 1);
        return first == '`' && last == '`' || first == '"' && last == '"';
    }
}
