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
package com.clougence.sql.mysql.analysis.reference;

import java.util.Locale;

import com.clougence.sql.common.registry.ResourceRegistryDialect;

/** Identifier rules used by the MySQL-owned resource registry. */
public final class MySqlResourceDialect implements ResourceRegistryDialect {

    public static final MySqlResourceDialect INSTANCE = new MySqlResourceDialect();

    private MySqlResourceDialect(){
    }

    @Override
    public String normalizeIdentifier(String identifier) {
        String normalized = identifier == null ? "" : identifier.trim();
        if (normalized.length() >= 2) {
            char quote = normalized.charAt(0);
            if ((quote == '`' || quote == '"') && normalized.charAt(normalized.length() - 1) == quote) {
                String delimiter = String.valueOf(quote);
                normalized = normalized.substring(1, normalized.length() - 1).replace(delimiter + delimiter, delimiter);
            }
        }
        return normalized.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean isQuotedIdentifier(String identifier) {
        if (identifier == null || identifier.length() < 2) {
            return false;
        }
        char quote = identifier.charAt(0);
        return (quote == '`' || quote == '"') && identifier.charAt(identifier.length() - 1) == quote;
    }
}
