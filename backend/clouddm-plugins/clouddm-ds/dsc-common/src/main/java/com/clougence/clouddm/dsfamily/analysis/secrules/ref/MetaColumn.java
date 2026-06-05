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
package com.clougence.clouddm.dsfamily.analysis.secrules.ref;

import lombok.Getter;
import lombok.Setter;

/**
 * defined for full migration. Every full task processor process one table unit
 *
 * @author wanshao create time is 2020/1/31
 **/
@Getter
@Setter
public class MetaColumn {

    private String  catalog;
    private String  schema;
    private String  table;
    private String  column;

    //private boolean real;
    //private boolean expression;
    private boolean all;

    private String  alias;

    public static MetaColumn buildAll(String catalog, String schema, String table) {
        MetaColumn col = new MetaColumn();
        col.catalog = catalog;
        col.schema = schema;
        col.table = table;
        col.column = null;
        col.all = true;
        return col;
    }

    public static MetaColumn buildCol(String catalog, String schema, String table, String colName, String asName) {
        MetaColumn col = new MetaColumn();
        col.catalog = catalog;
        col.schema = schema;
        col.table = table;
        col.column = colName;
        col.all = false;
        col.alias = asName;
        return col;
    }
}
