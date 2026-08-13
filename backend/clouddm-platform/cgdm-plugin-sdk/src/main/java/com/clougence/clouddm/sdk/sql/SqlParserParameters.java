/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.sdk.sql;

import java.util.LinkedHashMap;
import java.util.Map;

/** SQL parser parameters. */
public record SqlParserParameters(Map<String, String> values) {

    public static final String VERSION = "version";
    public static final String GRAMMAR_VERSION = "grammarVersion";
    public static final String EXACT_VERSION = "exactVersion";
    public static final String SQL_MODE = "sqlMode";
    public static final String EXPECT_PLAN = "expectPlan";

    public SqlParserParameters{
        values = values == null ? Map.of() : Map.copyOf(values);
    }

    public static SqlParserParameters empty() {
        return new SqlParserParameters(Map.of());
    }

    public String get(String name) {
        return this.values.get(name);
    }

    public boolean contains(String name) {
        return this.values.containsKey(name);
    }

    public SqlParserParameters put(String name, String value) {
        Map<String, String> merged = new LinkedHashMap<>(this.values);
        merged.put(name, value);
        return new SqlParserParameters(merged);
    }

    public SqlParserParameters putAll(Map<String, String> values) {
        Map<String, String> merged = new LinkedHashMap<>(this.values);
        merged.putAll(values);
        return new SqlParserParameters(merged);
    }

    public String version() {
        return this.get(VERSION);
    }

    public static SqlParserParameters nullToEmpty(SqlParserParameters parameters) {
        return parameters == null ? empty() : parameters;
    }
}
