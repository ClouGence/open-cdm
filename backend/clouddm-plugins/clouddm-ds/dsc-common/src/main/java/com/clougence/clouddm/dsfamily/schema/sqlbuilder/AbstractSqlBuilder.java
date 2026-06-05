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
package com.clougence.clouddm.dsfamily.schema.sqlbuilder;

import com.clougence.clouddm.dsfamily.schema.dialect.DefaultDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;

public abstract class AbstractSqlBuilder {

    public Dialect getDialect() { return DefaultDialect.DEFAULT; }

    protected String fmtTable(boolean useDelimited, String catalog, String schema, String table) {
        catalog = getDialect().fmtName(useDelimited, catalog);
        schema = getDialect().fmtName(useDelimited, schema);
        table = getDialect().fmtName(useDelimited, table);
        return getDialect().fmtTableName(useDelimited, catalog, schema, table);
    }

    protected String fmtIndex(boolean useDelimited, String schema, String index) {
        return fmtTable(useDelimited, null, schema, index);
    }

    protected String fmtName(boolean useDelimited, String column) {
        return getDialect().fmtName(useDelimited, column);
    }

    protected static String safeToString(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    protected static Integer safeToInteger(Object obj) {
        return (obj == null) ? null : (Integer) ConverterUtils.convert(Integer.class, obj);
    }

    protected static String safeIdxName(String prefix, String tableName, String idxName, boolean rewrite) {
        if (!rewrite) {
            return idxName;
        }

        if (idxName.startsWith(prefix)) {
            idxName = idxName.substring(prefix.length());
        }
        String tmpName = prefix + "_" + idxName + "_for_" + tableName;
        tmpName = tmpName.replaceAll("[_]{2,}", "_");
        String hashName = String.valueOf(StringUtils.hashString(tmpName));

        if (tmpName.length() >= 18) {
            tmpName = tmpName.substring(0, 18);
        }

        return tmpName.endsWith("_") ? (tmpName + hashName) : (tmpName + "_" + hashName);
    }
}
