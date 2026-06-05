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
package com.clougence.clouddm.ds.oceanbase.dialect.ob4ora;

import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;

/**
 * @author chunlin create time is 2024/8/27
 */
public class ObForOracleDialect extends OracleDialect {

    public static ObForOracleDialect INSTANCE = new ObForOracleDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/obfororacle.keywords";
    }
}
