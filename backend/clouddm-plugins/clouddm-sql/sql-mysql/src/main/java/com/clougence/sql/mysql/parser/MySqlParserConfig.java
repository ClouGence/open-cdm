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
package com.clougence.sql.mysql.parser;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/** Immutable configuration for one MySQL lexer/parser lifecycle. */
public final class MySqlParserConfig {

    public enum Feature {
        ANSI_QUOTES,
        NO_BACKSLASH_ESCAPES,
        PIPES_AS_CONCAT,
        HIGH_NOT_PRECEDENCE,
        IGNORE_SPACE
    }

    private final MySqlVersion  grammarVersion;
    private final int           exactVersion;
    private final boolean       sqlModeKnown;
    private final Set<Feature>  features;

    private MySqlParserConfig(String version, String grammarVersion, String exactVersion, boolean sqlModeKnown, Set<Feature> features){
        this.grammarVersion = grammarVersion == null || grammarVersion.isBlank()
                ? MySqlVersion.parse(version)
                : MySqlVersion.parse(grammarVersion);
        if (exactVersion == null || exactVersion.isBlank()) {
            this.exactVersion = version == null || version.isBlank()
                    ? this.grammarVersion.exactVersion()
                    : MySqlVersion.parseExactVersion(version);
        } else {
            this.exactVersion = MySqlVersion.parseExactVersionCode(exactVersion);
        }
        this.sqlModeKnown = sqlModeKnown;
        EnumSet<Feature> featureSet = features.isEmpty() ? EnumSet.noneOf(Feature.class) : EnumSet.copyOf(features);
        this.features = Collections.unmodifiableSet(featureSet);
    }

    public static MySqlParserConfig unknownSqlMode(String version) {
        return new MySqlParserConfig(version, null, null, false, Set.of());
    }

    public static MySqlParserConfig knownSqlMode(String version, Set<Feature> features) {
        return new MySqlParserConfig(version, null, null, true, features);
    }

    public static MySqlParserConfig of(String version, String grammarVersion, String exactVersion,
            boolean sqlModeKnown, Set<Feature> features) {
        return new MySqlParserConfig(version, grammarVersion, exactVersion, sqlModeKnown, features);
    }

    public MySqlVersion grammarVersion() {
        return grammarVersion;
    }

    public int exactVersion() {
        return exactVersion;
    }

    public boolean isSqlModeKnown() {
        return sqlModeKnown;
    }

    public Set<Feature> features() {
        return features;
    }

    public boolean isEnabled(Feature feature) {
        return features.contains(feature);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MySqlParserConfig other)) {
            return false;
        }
        return exactVersion == other.exactVersion
                && sqlModeKnown == other.sqlModeKnown
                && grammarVersion == other.grammarVersion
                && features.equals(other.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammarVersion, exactVersion, sqlModeKnown, features);
    }

    @Override
    public String toString() {
        return "MySqlParserConfig{grammarVersion=" + grammarVersion + ", exactVersion=" + exactVersion
                + ", sqlModeKnown=" + sqlModeKnown + ", features=" + features + '}';
    }
}
