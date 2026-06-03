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
package com.clougence.clouddm.ds.sqlserver.dialect;

import com.clougence.clouddm.dsfamily.language.completion.CompletionDialect;
import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.utils.StringUtils;

public class SqlServerDialect extends AbstractDialect implements CompletionDialect {

    public static SqlServerDialect INSTANCE = new SqlServerDialect();

    @Override
    public int maxArg() {
        return 2000;
    }

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/sqlserver.keywords";
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    public String fmtName(boolean useQualifier, String fmtString) {
        if (fmtString == null) {
            return null;
        }
        if (StringUtils.contains(fmtString, "]")) {
            return super.fmtName(true, fmtString.replace("]", "]]"));
        } else {
            return super.fmtName(useQualifier, fmtString);
        }
    }

    @Override
    public String leftQualifier() {
        return "[";
    }

    @Override
    public String rightQualifier() {
        return "]";
    }

    @Override
    public boolean isIdentifierChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$' || c == '[' || c == ']';
    }

    @Override
    public String unquoteIdentifier(String value) {
        String text = StringUtils.toString(value).trim();
        if (text.length() >= 2 && text.startsWith("[") && text.endsWith("]")) {
            return text.substring(1, text.length() - 1).replace("]]", "]");
        }
        return text;
    }
}
