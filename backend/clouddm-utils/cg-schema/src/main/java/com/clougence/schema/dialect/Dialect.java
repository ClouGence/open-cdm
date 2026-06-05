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
package com.clougence.schema.dialect;

import java.util.Set;

/**
 * Dialect
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface Dialect {

    default int maxArg() {
        return Integer.MAX_VALUE;
    }

    /** Cannot be used as a key for column names. when column name is key words, Generate SQL using Qualifier warp it. */
    Set<String> keywords();

    String leftQualifier();

    String rightQualifier();

    String fmtName(boolean useDelimited, String name);

    String fmtValue(String value);

    String fmtComment(String str);

    String fmtTableName(boolean useDelimited, String catalog, String schema, String table);
}
