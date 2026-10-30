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
package com.clougence.clouddm.ds.dameng.sql.analysis.reference;

import java.util.Locale;

import com.clougence.sql.common.registry.ResourceRegistryDialect;

/** Identifier rules used by the Dameng-owned resource registry. */
public final class DmResourceDialect implements ResourceRegistryDialect {

    public static final DmResourceDialect INSTANCE = new DmResourceDialect();

    private DmResourceDialect(){
    }

    @Override
    public String normalizeIdentifier(String identifier) {
        String normalized = identifier == null ? "" : identifier.trim();
        if (isQuotedIdentifier(normalized)) {
            normalized = normalized.substring(1, normalized.length() - 1).replace("\"\"", "\"");
        }
        return normalized.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean isQuotedIdentifier(String identifier) {
        return identifier != null && identifier.length() >= 2 && identifier.charAt(0) == '"' && identifier.charAt(identifier.length() - 1) == '"';
    }
}
