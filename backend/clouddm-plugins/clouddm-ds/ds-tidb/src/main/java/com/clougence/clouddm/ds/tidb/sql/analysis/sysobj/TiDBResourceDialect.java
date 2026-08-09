/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.sysobj;

import java.util.Locale;

import com.clougence.sql.common.registry.ResourceRegistryDialect;

final class TiDBResourceDialect implements ResourceRegistryDialect {

    static final TiDBResourceDialect INSTANCE = new TiDBResourceDialect();

    private TiDBResourceDialect(){
    }

    @Override
    public String normalizeIdentifier(String identifier) {
        String normalized = identifier.strip();
        if (isQuotedIdentifier(normalized)) {
            normalized = normalized.substring(1, normalized.length() - 1);
        }
        return normalized.toUpperCase(Locale.ROOT);
    }

    @Override
    public boolean isQuotedIdentifier(String identifier) {
        if (identifier == null || identifier.length() < 2) {
            return false;
        }
        char first = identifier.charAt(0);
        char last = identifier.charAt(identifier.length() - 1);
        return (first == '`' && last == '`') || (first == '"' && last == '"');
    }
}
