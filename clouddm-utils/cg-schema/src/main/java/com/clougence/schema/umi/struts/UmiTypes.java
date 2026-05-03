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
