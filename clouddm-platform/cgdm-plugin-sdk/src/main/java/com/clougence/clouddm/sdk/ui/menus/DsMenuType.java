package com.clougence.clouddm.sdk.ui.menus;

import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

/**
 * @author mode create time is 2020/4/13
 **/
public enum DsMenuType {

    Unknown("UNKNOWN"),
    Env("ENV"),
    Instance(UmiTypes.Instance.getTypeName()),

    // RDB
    RdbCatalog(UmiTypes.Catalog.getTypeName()),
    RdbExternalCatalog(UmiTypes.ExternalCatalog.getTypeName()),
    RdbSchema(UmiTypes.Schema.getTypeName()),
    RdbExternalSchema(UmiTypes.ExternalSchema.getTypeName()),
    RdbTable(UmiTypes.Table.getTypeName()),
    RdbView(UmiTypes.View.getTypeName()),
    RDBProcedure(UmiTypes.Procedure.getTypeName()),
    RDBFunction(UmiTypes.Function.getTypeName()),
    RDBTrigger(UmiTypes.Trigger.getTypeName()),
    RdbMaterialized(UmiTypes.Materialized.getTypeName()),
    RdbSequence(UmiTypes.Sequence.getTypeName()),
    RdbSynonym(UmiTypes.Synonym.getTypeName()),
    RdbColumn(UmiTypes.Column.getTypeName()),
    RdbRole(UmiTypes.ROLE.getTypeName()),
    RdbUser(UmiTypes.USER.getTypeName()),
    RdbTablespace(UmiTypes.TABLESPACE.getTypeName()),
    RdbExternalTable(UmiTypes.ExternalTable.getTypeName()),
    RdbDBLink(UmiTypes.DBLink.getTypeName()),
    RdbJob(UmiTypes.Job.getTypeName()),
    RdbScheduleJob(UmiTypes.ScheduleJob.getTypeName()),
    RdbParam("RDB_PARAM"),
    RdbParamGroup("RDB_PARAM_GROUP"),
    RdbReturns("RDB_RETURNS"),
    RdbColumnGroup("RDB_COLUMN_GROUP"),
    RdbPrimary("RDB_PRIMARY"),
    RdbPrimaryGroup("RDB_PRIMARY_GROUP"),
    RdbIndex("RDB_INDEX"),
    RdbIndexGroup("RDB_INDEX_GROUP"),
    RdbPartition("RDB_PARTITION"),
    RdbPartitionGroup("RDB_PARTITION_GROUP"),
    RdbConstraint("RDB_CONSTRAINT"),
    RdbConstraintGroup("RDB_CONSTRAINT_GROUP"),
    RdbForeign("RDB_FOREIGN"),
    RdbForeignGroup("RDB_FOREIGN_GROUP"),

    //
    Endpoint(UmiTypes.Endpoint.getTypeName()),
    Topic(UmiTypes.Topic.getTypeName()),
    Key(UmiTypes.Key.getTypeName()),;

    private final String typeName;

    DsMenuType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DsMenuType valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return Unknown;
        }
        for (DsMenuType typeEnum : DsMenuType.values()) {
            if (StringUtils.equalsIgnoreCase(typeEnum.typeName, code)) {
                return typeEnum;
            }
        }
        return Unknown;
    }
}
