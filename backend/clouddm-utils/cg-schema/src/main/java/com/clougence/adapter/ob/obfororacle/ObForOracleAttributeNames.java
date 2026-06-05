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
package com.clougence.adapter.ob.obfororacle;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class ObForOracleAttributeNames extends RdbAttributeNames {

    private ObForOracleAttributeNames(String name){
        super(DsType.ObForOracle.getShortName(), name);
    }

    // schema
    public static final UmiAttributeNames CREATED            = check(new ObForOracleAttributeNames("cd"));

    // table
    public static final UmiAttributeNames TEMPORARY          = check(new ObForOracleAttributeNames("tmp"));
    public static final UmiAttributeNames TABLESPACE         = check(new ObForOracleAttributeNames("ts"));
    public static final UmiAttributeNames READ_ONLY          = check(new ObForOracleAttributeNames("ro"));
    public static final UmiAttributeNames LOG_TABLE          = check(new ObForOracleAttributeNames("logt"));
    public static final UmiAttributeNames LOG_ROWIDS         = check(new ObForOracleAttributeNames("logr"));
    public static final UmiAttributeNames LOG_PK             = check(new ObForOracleAttributeNames("logpk"));
    public static final UmiAttributeNames LOG_SEQ            = check(new ObForOracleAttributeNames("logseq"));

    // column
    public static final UmiAttributeNames COLUMN_TYPE        = check(new ObForOracleAttributeNames("ct"));
    public static final UmiAttributeNames COLUMN_TYPE_OWNER  = check(new ObForOracleAttributeNames("cto"));
    public static final UmiAttributeNames CHARACTER_SET_NAME = check(new ObForOracleAttributeNames("csn"));
    public static final UmiAttributeNames DATA_CHAR_LENGTH   = check(new ObForOracleAttributeNames("clen"));
    public static final UmiAttributeNames DATA_BYTES_LENGTH  = check(new ObForOracleAttributeNames("dlen"));
    public static final UmiAttributeNames HIDDEN             = check(new ObForOracleAttributeNames("hid"));
    public static final UmiAttributeNames VIRTUAL            = check(new ObForOracleAttributeNames("vir"));
    public static final UmiAttributeNames IDENTITY           = check(new ObForOracleAttributeNames("ide"));
    public static final UmiAttributeNames SENSITIVE          = check(new ObForOracleAttributeNames("sen"));
    public static final UmiAttributeNames CHAR_USED_TYPE     = check(new ObForOracleAttributeNames("cut"));
    public static final UmiAttributeNames MODIFY_NO_CONS     = check(new ObForOracleAttributeNames("mnc"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames ENABLED            = check(new ObForOracleAttributeNames("enb"));
    public static final UmiAttributeNames VALIDATED          = check(new ObForOracleAttributeNames("val"));
    public static final UmiAttributeNames CONSTRAINT_TYPE    = check(new ObForOracleAttributeNames("cont"));
    public static final UmiAttributeNames STORAGE_TYPE       = check(new ObForOracleAttributeNames("st"));
}
