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
package com.clougence.adapter.oracle;

import com.clougence.schema.DsType;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.struts.UmiAttributeNames;

/**
 * @version : 2021-04-01
 * @author 赵永春 (zyc@hasor.net)
 */
public class OracleAttributeNames extends RdbAttributeNames {

    private OracleAttributeNames(String name){
        super(DsType.Oracle.getShortName(), name);
    }

    // schema
    public static final UmiAttributeNames CREATED                        = check(new OracleAttributeNames("cd"));
    public static final UmiAttributeNames OBJ_STATUS                     = check(new OracleAttributeNames("objs"));

    // common
    public static final UmiAttributeNames LAST_DDL_TIME                  = check(new OracleAttributeNames("ldt"));
    public static final UmiAttributeNames CREATE_TIME                    = check(new OracleAttributeNames("cti"));

    // table
    public static final UmiAttributeNames TEMPORARY                      = check(new OracleAttributeNames("tmp"));
    public static final UmiAttributeNames TABLESPACE                     = check(new OracleAttributeNames("ts"));
    public static final UmiAttributeNames READ_ONLY                      = check(new OracleAttributeNames("ro"));
    public static final UmiAttributeNames LOG_TABLE                      = check(new OracleAttributeNames("logt"));
    public static final UmiAttributeNames LOG_ROWIDS                     = check(new OracleAttributeNames("logr"));
    public static final UmiAttributeNames LOG_PK                         = check(new OracleAttributeNames("logpk"));
    public static final UmiAttributeNames LOG_SEQ                        = check(new OracleAttributeNames("logseq"));
    public static final UmiAttributeNames PARTITION_TABLE                = check(new OracleAttributeNames("pata"));

    public static final UmiAttributeNames CLUSTER_NAME                   = check(new OracleAttributeNames("clna"));
    public static final UmiAttributeNames PCT_FREE                       = check(new OracleAttributeNames("pctf"));
    public static final UmiAttributeNames PCT_USED                       = check(new OracleAttributeNames("pctu"));
    public static final UmiAttributeNames INI_TRANS                      = check(new OracleAttributeNames("init"));
    public static final UmiAttributeNames MAX_TRANS                      = check(new OracleAttributeNames("maxt"));
    public static final UmiAttributeNames INITIAL_EXTENT                 = check(new OracleAttributeNames("initex"));
    public static final UmiAttributeNames NEXT_EXTENT                    = check(new OracleAttributeNames("nextex"));
    public static final UmiAttributeNames MIN_EXTENTS                    = check(new OracleAttributeNames("minex"));
    public static final UmiAttributeNames MAX_EXTENTS                    = check(new OracleAttributeNames("maxex"));
    // column
    public static final UmiAttributeNames COLUMN_TYPE                    = check(new OracleAttributeNames("ct"));
    public static final UmiAttributeNames COLUMN_TYPE_OWNER              = check(new OracleAttributeNames("cto"));
    public static final UmiAttributeNames CHARACTER_SET_NAME             = check(new OracleAttributeNames("csn"));
    public static final UmiAttributeNames DATA_CHAR_LENGTH               = check(new OracleAttributeNames("clen"));
    public static final UmiAttributeNames DATA_BYTES_LENGTH              = check(new OracleAttributeNames("dlen"));
    public static final UmiAttributeNames HIDDEN                         = check(new OracleAttributeNames("hid"));
    public static final UmiAttributeNames VIRTUAL                        = check(new OracleAttributeNames("vir"));
    public static final UmiAttributeNames IDENTITY                       = check(new OracleAttributeNames("ide"));
    public static final UmiAttributeNames SENSITIVE                      = check(new OracleAttributeNames("sen"));
    public static final UmiAttributeNames CHAR_USED_TYPE                 = check(new OracleAttributeNames("cut"));
    public static final UmiAttributeNames MODIFY_NO_CONS                 = check(new OracleAttributeNames("mnc"));

    // PK\UK\FK\INDEX
    public static final UmiAttributeNames ENABLED                        = check(new OracleAttributeNames("enb"));
    public static final UmiAttributeNames VALIDATED                      = check(new OracleAttributeNames("val"));
    public static final UmiAttributeNames CONSTRAINT_TYPE                = check(new OracleAttributeNames("cont"));
    public static final UmiAttributeNames STORAGE_TYPE                   = check(new OracleAttributeNames("st"));
    public static final UmiAttributeNames UNIQUE_INDEX                   = check(new OracleAttributeNames("ui"));

    //trigger
    public static final UmiAttributeNames NEW_ALIAS                      = check(new OracleAttributeNames("newal"));
    public static final UmiAttributeNames OLD_ALIAS                      = check(new OracleAttributeNames("oldal"));
    public static final UmiAttributeNames CONDITION                      = check(new OracleAttributeNames("cond"));
    public static final UmiAttributeNames TRIGGER_GRANULARITY            = check(new OracleAttributeNames("trig"));

    // view
    public static final UmiAttributeNames FORCE_CREATE_VIEW              = check(new OracleAttributeNames("fcv"));
    public static final UmiAttributeNames VIEW_TEXT_LENGTH               = check(new OracleAttributeNames("vtl"));
    public static final UmiAttributeNames VIEW_READ_ONLY                 = check(new OracleAttributeNames("vro"));

    public static final UmiAttributeNames MVIEW_REFRESH_DATE             = check(new OracleAttributeNames("mvrd"));
    public static final UmiAttributeNames MVIEW_REFRESH_END_TIME         = check(new OracleAttributeNames("mvret"));

    // job
    public static final UmiAttributeNames JOB_LAST_EXEC                  = check(new OracleAttributeNames("joble"));
    public static final UmiAttributeNames JOB_NEXT_EXEC                  = check(new OracleAttributeNames("jobne"));

    // schedule job
    public static final UmiAttributeNames SCHEDULE_JOB_START_DATE        = check(new OracleAttributeNames("sjsd"));
    public static final UmiAttributeNames SCHEDULE_JOB_END_DATE          = check(new OracleAttributeNames("sjed"));
    public static final UmiAttributeNames SCHEDULE_JOB_REPEAT_INTERVAL   = check(new OracleAttributeNames("sjri"));
    public static final UmiAttributeNames SCHEDULE_JOB_AUTO_DROP         = check(new OracleAttributeNames("sjad"));
    public static final UmiAttributeNames SCHEDULE_JOB_TYPE              = check(new OracleAttributeNames("sjt"));

    // proc
    public static final UmiAttributeNames PROCEDURE_AGGREGATE            = check(new OracleAttributeNames("proa"));
    public static final UmiAttributeNames PROCEDURE_PIPELINED            = check(new OracleAttributeNames("propi"));
    public static final UmiAttributeNames PROCEDURE_PARALLEL             = check(new OracleAttributeNames("propa"));
    public static final UmiAttributeNames PROCEDURE_INTERFACE            = check(new OracleAttributeNames("proin"));
    public static final UmiAttributeNames PROCEDURE_DETERMINISTIC        = check(new OracleAttributeNames("prode"));

    // sequence
    public static final UmiAttributeNames SEQUENCE_CYCLE_FLAG            = check(new OracleAttributeNames("scf"));
    public static final UmiAttributeNames SEQUENCE_CACHE_SIZE            = check(new OracleAttributeNames("scs"));
    public static final UmiAttributeNames SEQUENCE_LAST_NUMBER           = check(new OracleAttributeNames("sln"));
    public static final UmiAttributeNames SEQUENCE_SESSION_FLAG          = check(new OracleAttributeNames("ssf"));
    public static final UmiAttributeNames SEQUENCE_KEEP_VALUE            = check(new OracleAttributeNames("skv"));

    // user
    public static final UmiAttributeNames USER_ID                        = check(new OracleAttributeNames("ui"));
    public static final UmiAttributeNames USER_OR_ROLE_COMMON            = check(new OracleAttributeNames("uorc"));
    public static final UmiAttributeNames USER_OR_ROLE_ORACLE_MAINTAINED = check(new OracleAttributeNames("uorom"));
    public static final UmiAttributeNames ROLE_AUTHENTICATION_TYPE       = check(new OracleAttributeNames("rat"));

    //
    public static final UmiAttributeNames SYNONYM_TABLE_DBLINK           = check(new OracleAttributeNames("sydb"));

}
