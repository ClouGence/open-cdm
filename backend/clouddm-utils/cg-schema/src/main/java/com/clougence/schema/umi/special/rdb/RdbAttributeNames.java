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
package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.umi.struts.AbstractAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * Keys。
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public class RdbAttributeNames extends AbstractAttributeNames {

    private RdbAttributeNames(String name){
        super("rdb", name);
    }

    protected RdbAttributeNames(String space, String name){
        super("rdb" + space, name);
    }

    // database object id
    public static final UmiAttributeNames RDB_OBJ_ID                 = check(new RdbAttributeNames("roi"));
    public static final UmiAttributeNames RDB_OBJ_CREATE_TIME        = check(new RdbAttributeNames("roct"));

    // level
    public static final UmiAttributeNames CATALOG                    = check(new RdbAttributeNames("cat"));
    public static final UmiAttributeNames SCHEMA                     = check(new RdbAttributeNames("sch"));
    public static final UmiAttributeNames TABLE                      = check(new RdbAttributeNames("tab"));
    public static final UmiAttributeNames COLUMN                     = check(new RdbAttributeNames("col"));

    // column
    public static final UmiAttributeNames NULLABLE                   = check(new RdbAttributeNames("nul"));
    public static final UmiAttributeNames JDBC_TYPE                  = check(new RdbAttributeNames("jt"));
    public static final UmiAttributeNames COMMENT                    = check(new RdbAttributeNames("com"));
    public static final UmiAttributeNames INDEX                      = check(new RdbAttributeNames("i"));
    public static final UmiAttributeNames DATETIME_PRECISION         = check(new RdbAttributeNames("dp"));
    public static final UmiAttributeNames NUMERIC_PRECISION          = check(new RdbAttributeNames("np"));
    public static final UmiAttributeNames NUMERIC_SCALE              = check(new RdbAttributeNames("ns"));
    public static final UmiAttributeNames DEFAULT                    = check(new RdbAttributeNames("def"));
    public static final UmiAttributeNames DEFAULT_VALUE_OF_TIME_TYPE = check(new RdbAttributeNames("deft"));
    public static final UmiAttributeNames AUTO_INCREMENT             = check(new RdbAttributeNames("auto"));

    // column / pk
    public static final UmiAttributeNames GENERATED                  = check(new RdbAttributeNames("gen"));

    // index
    public static final UmiAttributeNames INDEX_TYPE                 = check(new RdbAttributeNames("it"));
    public static final UmiAttributeNames INDEX_OWNER                = check(new RdbAttributeNames("itowm"));
    public static final UmiAttributeNames INDEX_CONTAINS_EXPRESSION  = check(new RdbAttributeNames("ice"));

    // procedure / function
    public static final UmiAttributeNames FUNC_RETURN_TYPE           = check(new RdbAttributeNames("fret"));

    // trigger
    public static final UmiAttributeNames TRIGGER_TABLE              = check(new RdbAttributeNames("tb"));

    // sequence
    public static final UmiAttributeNames SEQUENCE_TYPE              = check(new RdbAttributeNames("seqt"));

    // number no scale and precision
    public static final UmiAttributeNames NUMBER_NO_SCALE_PREC       = check(new RdbAttributeNames("numnosp"));

    // partition
    public static final UmiAttributeNames SYNC_PARTITION             = check(new RdbAttributeNames("syncpart"));

    // llm vector
    public static final UmiAttributeNames LLM_VECTOR_TYPE            = check(new RdbAttributeNames("llmvectp"));
    public static final UmiAttributeNames LLM_VECTOR_DIM             = check(new RdbAttributeNames("llmvecdim"));
    public static final UmiAttributeNames LLM_EXTRA_FIELDS           = check(new RdbAttributeNames("llmextflds"));

    // obj valid,disabled
    public static final UmiAttributeNames OBJ_UI_TIPS                = check(new RdbAttributeNames("tips"));
    public static final UmiAttributeNames OBJ_INVALID                = check(new RdbAttributeNames("invalid"));
    public static final UmiAttributeNames OBJ_DISABLED               = check(new RdbAttributeNames("disabled"));
    public static final UmiAttributeNames OBJ_ENABLED                = check(new RdbAttributeNames("enabled"));
}
