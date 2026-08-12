/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.sdk.sql;

import java.util.Map;

/** Immutable SQL parser parameters. */
public record SqlParserParameters(Map<String, String> values) {

    public static final String VERSION = "version";
    public static final String GRAMMAR_VERSION = "grammarVersion";
    public static final String EXACT_VERSION = "exactVersion";
    public static final String SQL_MODE = "sqlMode";

    private static final SqlParserParameters EMPTY = new SqlParserParameters(Map.of());

    public SqlParserParameters{
        values = values == null || values.isEmpty() ? Map.of() : Map.copyOf(values);
    }

    public static SqlParserParameters empty() {
        return EMPTY;
    }

    public String get(String name) {
        return this.values.get(name);
    }

    public boolean contains(String name) {
        return this.values.containsKey(name);
    }

    public String version() {
        return this.get(VERSION);
    }

    public static SqlParserParameters nullToEmpty(SqlParserParameters parameters) {
        return parameters == null ? EMPTY : parameters;
    }
}
