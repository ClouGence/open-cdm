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
package com.clougence.clouddm.ds.tidb.sql.parser;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;

public record TiDBParserConfig(TiDBVersion version, boolean sqlModeKnown, Set<TiDBParserFeature> features) {

    static final int GRAMMAR_COMPATIBILITY_VERSION = 80046;

    public TiDBParserConfig{
        version = version == null ? TiDBVersion.LATEST : version;
        if (features == null || features.isEmpty()) {
            features = Collections.emptySet();
        } else {
            features = Collections.unmodifiableSet(EnumSet.copyOf(features));
        }
    }

    public static TiDBParserConfig unknownSqlMode(String version) {
        return new TiDBParserConfig(TiDBVersion.parse(version), false, Collections.emptySet());
    }

    public static TiDBParserConfig fromParameters(SqlParserParameters parameters) {
        SqlParserParameters parserParameters = SqlParserParameters.nullToEmpty(parameters);
        String grammarVersion = parserParameters.get(SqlParserParameters.GRAMMAR_VERSION);
        String selectedVersion = grammarVersion == null || grammarVersion.isBlank() ? parserParameters.version() : grammarVersion;
        if (!parserParameters.contains(SqlParserParameters.SQL_MODE)) {
            return unknownSqlMode(selectedVersion);
        }
        return new TiDBParserConfig(TiDBVersion.parse(selectedVersion), true, sqlModeFeatures(parserParameters.get(SqlParserParameters.SQL_MODE)));
    }

    private static EnumSet<TiDBParserFeature> sqlModeFeatures(String sqlMode) {
        EnumSet<TiDBParserFeature> features = EnumSet.noneOf(TiDBParserFeature.class);
        if (sqlMode == null || sqlMode.isBlank()) {
            return features;
        }
        StringTokenizer names = new StringTokenizer(sqlMode, ",");
        while (names.hasMoreTokens()) {
            String normalized = names.nextToken().trim().toUpperCase(Locale.ROOT);
            if (switch (normalized) {
                case "ANSI", "DB2", "MAXDB", "MSSQL", "ORACLE", "POSTGRESQL" -> true;
                default -> false;
            }) {
                features.add(TiDBParserFeature.ANSI_QUOTES);
                features.add(TiDBParserFeature.PIPES_AS_CONCAT);
                features.add(TiDBParserFeature.IGNORE_SPACE);
                continue;
            }
            try {
                features.add(TiDBParserFeature.valueOf(normalized));
            } catch (IllegalArgumentException ignored) {
                // SQL modes unrelated to lexical or grammar behavior do not affect parsing.
            }
        }
        return features;
    }

    public boolean isEnabled(TiDBParserFeature feature) {
        return features.contains(feature);
    }

    public int exactVersion() {
        return GRAMMAR_COMPATIBILITY_VERSION;
    }

    public boolean atLeast(int major, int minor) {
        return exactVersion() >= versionCode(major, minor);
    }

    public boolean atMost(int major, int minor) {
        return exactVersion() / 100 <= versionCode(major, minor) / 100;
    }

    public boolean between(int minMajor, int minMinor, int maxMajor, int maxMinor) {
        return atLeast(minMajor, minMinor) && atMost(maxMajor, maxMinor);
    }

    public TiDBParserConfig withFeature(TiDBParserFeature feature) {
        EnumSet<TiDBParserFeature> copy = EnumSet.noneOf(TiDBParserFeature.class);
        copy.addAll(features);
        copy.add(feature);
        return new TiDBParserConfig(version, sqlModeKnown, copy);
    }

    private static int versionCode(int major, int minor) {
        return major * 10000 + minor * 100;
    }
}
