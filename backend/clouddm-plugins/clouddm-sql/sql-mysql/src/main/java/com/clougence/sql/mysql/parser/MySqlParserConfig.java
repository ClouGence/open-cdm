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

import java.util.*;

/** Immutable configuration for one MySQL lexer/parser lifecycle. */
public final class MySqlParserConfig {

    public enum Feature {
        ANSI_QUOTES,
        NO_BACKSLASH_ESCAPES,
        PIPES_AS_CONCAT,
        HIGH_NOT_PRECEDENCE,
        IGNORE_SPACE
    }

    private final MySqlVersion grammarVersion;
    private final boolean      mariaDb;
    private final int          exactVersion;
    private final boolean      sqlModeKnown;
    private final Set<Feature> features;

    private MySqlParserConfig(String version, String grammarVersion, String exactVersion, boolean sqlModeKnown, Set<Feature> features){
        this.mariaDb = resolveMariaDb(version, grammarVersion);
        this.grammarVersion = resolveGrammarVersion(version, grammarVersion, this.mariaDb);
        this.exactVersion = resolveExactVersion(version, exactVersion, this.mariaDb, this.grammarVersion);
        this.sqlModeKnown = sqlModeKnown;
        EnumSet<Feature> featureSet = features.isEmpty() ? EnumSet.noneOf(Feature.class) : EnumSet.copyOf(features);
        this.features = Collections.unmodifiableSet(featureSet);
    }

    private MySqlParserConfig(MySqlParserConfig source, Set<Feature> features){
        this.grammarVersion = source.grammarVersion;
        this.mariaDb = source.mariaDb;
        this.exactVersion = source.exactVersion;
        this.sqlModeKnown = source.sqlModeKnown;
        this.features = Collections.unmodifiableSet(features);
    }

    private static boolean resolveMariaDb(String version, String grammarVersion) {
        return "MariaDB".equalsIgnoreCase(grammarVersion) || version != null && version.toLowerCase(Locale.ROOT).contains("mariadb");
    }

    private static MySqlVersion resolveGrammarVersion(String version, String grammarVersion, boolean mariaDb) {
        // MariaDB shares the common grammar, not MySQL's release/removal timeline.
        if (mariaDb) {
            return MySqlVersion.MYSQL_8_0;
        }
        if (grammarVersion == null || grammarVersion.isBlank()) {
            return MySqlVersion.parse(version);
        }
        return MySqlVersion.parse(grammarVersion);
    }

    private static int resolveExactVersion(String version, String exactVersion, boolean mariaDb, MySqlVersion grammarVersion) {
        if (exactVersion != null && !exactVersion.isBlank()) {
            return MySqlVersion.parseExactVersionCode(exactVersion);
        }
        if (version == null || version.isBlank()) {
            if (mariaDb) {
                return 0;
            }
            return grammarVersion.exactVersion();
        }
        String serverVersion = version;
        if (mariaDb && serverVersion.startsWith("5.5.5-")) {
            serverVersion = serverVersion.substring("5.5.5-".length());
        }
        return MySqlVersion.parseExactVersion(serverVersion);
    }

    public static MySqlParserConfig unknownSqlMode(String version) {
        return new MySqlParserConfig(version, null, null, false, Set.of());
    }

    public static MySqlParserConfig knownSqlMode(String version, Set<Feature> features) {
        return new MySqlParserConfig(version, null, null, true, features);
    }

    public static MySqlParserConfig of(String version, String grammarVersion, String exactVersion, boolean sqlModeKnown, Set<Feature> features) {
        return new MySqlParserConfig(version, grammarVersion, exactVersion, sqlModeKnown, features);
    }

    MySqlParserConfig withFeature(Feature feature) {
        EnumSet<Feature> featureSet = features.isEmpty() ? EnumSet.noneOf(Feature.class) : EnumSet.copyOf(features);
        featureSet.add(feature);
        return new MySqlParserConfig(this, featureSet);
    }

    public MySqlVersion grammarVersion() {
        return grammarVersion;
    }

    public boolean isMariaDb() { return mariaDb; }

    public int exactVersion() {
        return exactVersion;
    }

    public boolean isSqlModeKnown() { return sqlModeKnown; }

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
        return mariaDb == other.mariaDb && exactVersion == other.exactVersion && sqlModeKnown == other.sqlModeKnown && grammarVersion == other.grammarVersion
               && features.equals(other.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mariaDb, grammarVersion, exactVersion, sqlModeKnown, features);
    }

    @Override
    public String toString() {
        return "MySqlParserConfig{mariaDb=" + mariaDb + ", grammarVersion=" + grammarVersion + ", exactVersion=" + exactVersion + ", sqlModeKnown=" + sqlModeKnown + ", features="
               + features + '}';
    }
}
