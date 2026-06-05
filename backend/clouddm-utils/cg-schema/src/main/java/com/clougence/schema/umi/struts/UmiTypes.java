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
package com.clougence.schema.umi.struts;

import com.clougence.utils.StringUtils;

/**
 * 类型
 * @version : 2021-05-10
 * @author 赵永春 (zyc@hasor.net)
 */
public enum UmiTypes {

    Instance("INSTANCE"),

    // rdb
    Catalog("CATALOG"),
    ExternalCatalog("EXTERNAL_CATALOG"),
    Schema("SCHEMA"),
    ExternalSchema("EXTERNAL_SCHEMA"),
    Table("TABLE"),
    View("VIEW"),
    Materialized("MATERIALIZED"),
    Sequence("SEQUENCE"),
    Synonym("SYNONYM"),
    Column("COLUMN"),
    Function("FUNC"),
    Procedure("PROC"),
    Index("Index"),
    Trigger("TRIGGER"),
    Param("PARAM"),
    PARTITION("PARTITION"),
    ROLE("ROLE"),
    USER("USER"),
    TABLESPACE("TABLESPACE"),
    ExternalTable("EXTERNAL_TABLE"),
    DBLink("DBLINK"),
    Job("JOB"),
    ScheduleJob("SCHEDULE_JOB"),

    // mq
    Endpoint("ENDPOINT"),
    Topic("TOPIC"),

    // key/value
    Key("KEY"),
    Field("FIELD"),;

    private final String typeName;

    UmiTypes(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static UmiTypes valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (UmiTypes rdbUmiTypes : UmiTypes.values()) {
            if (StringUtils.equalsIgnoreCase(rdbUmiTypes.typeName, code)) {
                return rdbUmiTypes;
            }
        }
        return null;
    }
}
