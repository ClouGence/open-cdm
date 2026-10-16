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
package com.clougence.sql.postgres.analysis.reference;

import com.clougence.sql.common.registry.ResourceRegistryDialect;

/** PostgreSQL identifier rules used by registered resources. */
public final class PgResourceDialect implements ResourceRegistryDialect {

    public static final PgResourceDialect INSTANCE = new PgResourceDialect();

    private PgResourceDialect(){
    }

    @Override
    public String normalizeIdentifier(String identifier) {
        String value = identifier == null ? "" : identifier.trim();
        if (isQuotedIdentifier(value)) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        char[] normalized = value.toCharArray();
        for (int i = 0; i < normalized.length; i++) {
            if (normalized[i] >= 'A' && normalized[i] <= 'Z') {
                normalized[i] += 'a' - 'A';
            }
        }
        return new String(normalized);
    }

    @Override
    public boolean isQuotedIdentifier(String identifier) {
        return identifier != null && identifier.length() >= 2 && identifier.charAt(0) == '"' && identifier.charAt(identifier.length() - 1) == '"';
    }
}
