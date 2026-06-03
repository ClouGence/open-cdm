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
package com.clougence.clouddm.dsfamily.mysql.dialect;

import com.clougence.clouddm.dsfamily.language.completion.CompletionDialect;
import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.utils.StringUtils;

/**
 * MySQL 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class MySqlDialect extends AbstractDialect implements CompletionDialect {

    public static MySqlDialect INSTANCE = new MySqlDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/mysql.keywords";
    }

    @Override
    protected String defaultQualifier() {
        return "`";
    }

    protected String fmtNameValue(boolean useQualifier, String fmtString) {
        return fmtString.replace("`", "``");
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''").replace("\\", "\\\\");
    }

    @Override
    public String fmtTableName(boolean useDelimited, String catalog, String schema, String table) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(schema)) {
            sqlBuilder.append(fmtName(useDelimited, schema));
            sqlBuilder.append(".");
        }
        sqlBuilder.append(fmtName(useDelimited, table));

        return sqlBuilder.toString();
    }

    @Override
    public boolean isIdentifierChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$' || c == '`';
    }

    @Override
    public String unquoteIdentifier(String value) {
        String text = StringUtils.toString(value).trim();
        if (text.length() >= 2 && text.startsWith("`") && text.endsWith("`")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
