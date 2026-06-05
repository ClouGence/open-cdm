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
package com.clougence.adapter.doris;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class DorisAttributeNames extends MyUmiAttributeNames {

    private DorisAttributeNames(String name){
        super(DsType.Doris.getShortName(), name);
    }

    protected DorisAttributeNames(String space, String name){
        super(space, name);
    }

    // table
    public static final UmiAttributeNames CREATE_TIME                = new DorisAttributeNames("ctime");
    public static final UmiAttributeNames UPDATE_TIME                = new DorisAttributeNames("utime");
    public static final UmiAttributeNames COLLATION                  = new DorisAttributeNames("coll");

    public static final UmiAttributeNames BUCKET_NUMBER              = new DorisAttributeNames("bn");
    public static final UmiAttributeNames PROPERTIES_CONFIG          = new DorisAttributeNames("conf");
    public static final UmiAttributeNames KEY_TYPE                   = new DorisAttributeNames("kt");
    public static final UmiAttributeNames DISTRIBUTED_BY_TYPE        = new DorisAttributeNames("dbt");
    public static final UmiAttributeNames DISTRIBUTED_BY_COLUMNS     = new DorisAttributeNames("dbc");
    public static final UmiAttributeNames PARTITION_EXPR             = new DorisAttributeNames("part");

    // column
    public static final UmiAttributeNames DATA_TYPE                  = new DorisAttributeNames("dt");
    public static final UmiAttributeNames COLUMN_TYPE                = new DorisAttributeNames("ct");
    public static final UmiAttributeNames DEFAULT_COLLATION_NAME     = new DorisAttributeNames("dfn");
    public static final UmiAttributeNames DEFAULT_CHARACTER_SET_NAME = new DorisAttributeNames("dcsn");
    public static final UmiAttributeNames CHARACTERS_MAX_LENGTH      = new DorisAttributeNames("clen");
    public static final UmiAttributeNames BYTES_MAX_LENGTH           = new DorisAttributeNames("blen");
    public static final UmiAttributeNames AGG_TYPE                   = new DorisAttributeNames("at");
    public static final UmiAttributeNames CURRENT_UPDATE_TYPE        = new DorisAttributeNames("cut");

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames STORAGE_TYPE               = new DorisAttributeNames("st");
    public static final UmiAttributeNames CONSTRAINT_TYPE            = new DorisAttributeNames("cont");
    public static final UmiAttributeNames SUB_PART                   = new DorisAttributeNames("sp");

    //for handler
    public static final UmiAttributeNames DORIS_ADD_OP_COLUMN        = new DorisAttributeNames("doris_add_op_column");
    public static final UmiAttributeNames DORIS_ADD_OP_COL_NAME      = new DorisAttributeNames("doris_add_op_col_name");
}
