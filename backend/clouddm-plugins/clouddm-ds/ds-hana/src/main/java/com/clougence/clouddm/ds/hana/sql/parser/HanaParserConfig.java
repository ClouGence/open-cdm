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
package com.clougence.clouddm.ds.hana.sql.parser;

import java.util.Objects;

/** Immutable configuration for one SAP HANA parser lifecycle. */
public final class HanaParserConfig {

    private final HanaVersion grammarVersion;

    private HanaParserConfig(String version, String grammarVersion){
        if (grammarVersion == null || grammarVersion.isBlank()) {
            this.grammarVersion = HanaVersion.parse(version);
        } else {
            this.grammarVersion = HanaVersion.parse(grammarVersion);
        }
    }

    public static HanaParserConfig of(String version, String grammarVersion) {
        return new HanaParserConfig(version, grammarVersion);
    }

    public HanaVersion grammarVersion() {
        return grammarVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HanaParserConfig other)) {
            return false;
        }
        return grammarVersion == other.grammarVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammarVersion);
    }

    @Override
    public String toString() {
        return "HanaParserConfig{grammarVersion=" + grammarVersion + '}';
    }
}
