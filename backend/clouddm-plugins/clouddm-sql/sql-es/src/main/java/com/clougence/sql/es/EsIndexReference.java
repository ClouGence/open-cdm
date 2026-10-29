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
package com.clougence.sql.es;

public record EsIndexReference(String name, EsSourceRange range) {
    public EsIndexReference{
        // ES names cannot contain path selectors or permission-path separators.
        if (name.isBlank() || name.equals(".") || name.equals("..") || name.startsWith("_") || name.startsWith("-") || name.startsWith("+")
            || name.codePoints().anyMatch(c -> Character.isWhitespace(c) || Character.isISOControl(c) || "\\/*?\"<>| ,#:%".indexOf(c) >= 0)) {
            throw new EsParseException(range, EsSqlI18nKeys.ES_EXACT_INDEX_REQUIRED);
        }
    }
}
